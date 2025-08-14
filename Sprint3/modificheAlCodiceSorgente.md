#  Refactoring e Correzione del Sistema `basicrobot24` e `planner23`

## Indice
1. [Introduzione e Obiettivi](#1-introduzione-e-obiettivi)
2. [Panoramica dell'Architettura](#2-panoramica-dellarchitettura)
3. [Principali Modifiche e Motivazioni](#3-principali-modifiche-e-motivazioni)
    - [3.1. Robustezza contro le Incoerenze di Stato (Collisioni e Allarmi)](#31-robustezza-contro-le-incoerenze-di-stato-collisioni-e-allarmi)
    - [3.2. Prevenzione delle Race Condition](#32-prevenzione-delle-race-condition)
    - [3.3. Gestione Sicura dei Messaggi (Sanificazione Anti-Prolog)](#33-gestione-sicura-dei-messaggi-sanificazione-anti-prolog)
    - [3.4. Miglioramento della Logica di Pianificazione (`planner23`)](#34-miglioramento-della-logica-di-pianificazione-planner23)
    - [3.5. Correzione di Bug Latenti e Gestione degli Errori](#35-correzione-di-bug-latenti-e-gestione-degli-errori)
4. [Riepilogo dei Benefici](#4-riepilogo-dei-benefici)

---

### 1. Introduzione e Obiettivi

Questo documento descrive le modifiche apportate al sistema `basicrobot24`, un servizio intermediario per il controllo di un robot virtuale o fisico. La versione originale del codice, sebbene funzionale in scenari ideali, presentava diverse criticità che potevano portare a **incoerenze tra lo stato interno percepito dal sistema e lo stato fisico del robot**. Tali discrepanze si manifestavano principalmente in seguito a eventi imprevisti come collisioni con ostacoli o allarmi asincroni, causando fallimenti a catena e comportamenti imprevedibili.

L'obiettivo primario di questo refactoring è stato quello di **irrobustire il sistema `basicrobot24` a livello architetturale**, risolvendo i bug alla radice. Questo approccio è stato scelto per mantenere l'attore di alto livello (`cargorobot`) semplice e focalizzato sulla sua logica di business, **evitando di appesantirlo con controlli e logiche di recupero complesse** che appartengono, per responsabilità, al servizio sottostante.

Il risultato è un sistema più affidabile, resiliente e la cui coerenza interna è garantita anche in condizioni avverse.

### 2. Panoramica dell'Architettura

L'architettura del sistema è basata su attori QAK con responsabilità ben definite:
- **`engager`**: Gestisce l'acquisizione e il rilascio del controllo del robot.
- **`basicrobot`**: Funge da driver a basso livello, traducendo comandi logici in movimenti fisici.
- **`planexec`**: Esegue sequenze di movimenti (piani) passo-passo.
- **`robotpos`**: È il "cartografo" del sistema. Mantiene la mappa e la posizione corrente del robot, generando i piani per raggiungere le destinazioni.

Le modifiche si sono concentrate principalmente su `basicrobot`, `planexec` e `robotpos` per migliorare la loro comunicazione e la gestione degli stati di errore.

### 3. Principali Modifiche e Motivazioni

#### 3.1. Robustezza contro le Incoerenze di Stato (Collisioni e Allarmi)

**Problema Originale:**
Il sistema non distingueva correttamente tra un fallimento logico (es. un comando non valido) e un fallimento fisico come una collisione. Una collisione veniva trattata come un passo fallito, portando `robotpos` a credere che il robot non si fosse mosso. Questo creava una **discrepanza di una cella tra la posizione reale e quella sulla mappa**, un errore che si propagava a tutte le pianificazioni successive.

**Soluzione Implementata:**
È stato introdotto un nuovo paradigma di comunicazione per descrivere con maggiore precisione il risultato di un'azione:
- **`stepdone`**: Il passo è stato completato con successo.
- **`stepfailed`**: Il passo è fallito per una causa logica (es. mancanza di permessi) e il robot non si è mosso.
- **`stepcollided`**: Il passo **è stato completato**, ma si è concluso contro un ostacolo.

**Motivazione:**
Questa distinzione è fondamentale. Ora, quando `planexec` riceve un `stepcollided`, sa che il movimento è avvenuto e aggiorna correttamente il `PathDone`. Di conseguenza, `robotpos` aggiorna la sua mappa con la posizione corretta, **eliminando alla radice la causa principale di incoerenza dello stato**.

#### 3.2. Prevenzione delle Race Condition

**Problema Originale:**
Eventi asincroni come gli `alarm` potevano arrivare mentre un piano era in esecuzione. La logica originale non era in grado di gestire correttamente le risposte tardive relative a un passo già in corso, portando a una corruzione dello stato del piano e a decisioni basate su informazioni obsolete.

**Soluzione Implementata:**
È stato introdotto un meccanismo transazionale basato su un **ID di piano univoco (`PlanId`)**:
1.  **Generazione:** `robotpos` assegna un `PlanId` progressivo a ogni nuovo piano (`doplan`).
2.  **Propagazione:** `planexec` riceve l'ID e lo "attacca" a tutte le sue richieste di basso livello (`step`, `stepback`).
3.  **Validazione:** `basicrobot` include l'ID nelle sue risposte (`stepdone`, `stepfailed`, etc.).
4.  **Filtraggio:** `planexec` ora scarta qualsiasi risposta il cui `PlanId` non corrisponda a quello del piano che sta attualmente eseguendo, ignorando i messaggi tardivi e obsoleti.
5.  **Gestione Allarmi:** Quando un allarme interrompe un piano, il `PlanId` corrente viene "avvelenato" (azzerato), garantendo che tutte le risposte successive relative a quel piano vengano scartate.

**Motivazione:**
Questo meccanismo rende l'esecuzione dei piani **atomica e immune alle race condition**. L'attore `planexec` opera solo sulla base di informazioni pertinenti al compito corrente, aumentando drasticamente l'affidabilità del sistema in ambienti concorrenti.

#### 3.3. Gestione Sicura dei Messaggi 

**Problema Originale:**
Il parser Prolog sottostante al runtime di QAK è molto sensibile alla sintassi dei messaggi. Un messaggio con un argomento vuoto, come `doplanfailed(, w)`, poteva essere interpretato erroneamente come `doplanfailed con argomenti ',' e 'w'`, causando uno sfasamento degli argomenti e crash imprevedibili. Questo accadeva spesso quando un piano falliva senza aver completato alcuna mossa.

**Soluzione Implementata:**
È stata adottata una politica di **sanificazione degli argomenti** in tutti gli attori che generano risposte:
- Se un percorso (come `PathDone`) è vuoto, viene sostituito con un placeholder atomico e non ambiguo (la stringa `"n"`).
- Se il percorso rimanente (`PathTodo`) è vuoto, viene sostituito con il placeholder `"none"`.

**Motivazione:**
Questa accortezza garantisce che nessun messaggio venga mai inviato con argomenti vuoti. Si previene così alla radice una classe intera di errori di parsing del runtime, rendendo la comunicazione tra attori **strutturalmente robusta** e compatibile con le rigidità del motore Prolog.

#### 3.4. Miglioramento della Logica di Pianificazione (`planner23`)

**Problema Originale:**
Il planner `AIMA` sottostante era limitato. Utilizzava una ricerca `BreadthFirstSearch` che non teneva conto dei costi delle azioni e non considerava la retromarcia come una mossa possibile, generando piani sub-ottimali e talvolta illogici (es. due rotazioni invece di una marcia indietro).

**Soluzione Implementata:**
Il `planner23` è stato potenziato:
1.  **Algoritmo di Ricerca:** Sostituzione di `BreadthFirstSearch` con `UniformCostSearch`, che permette di trovare il percorso a costo minimo.
2.  **Costo delle Azioni:** Introdotta una funzione di costo che assegna un peso diverso alle azioni (es. la retromarcia ha un costo leggermente superiore a una sequenza di rotazioni e avanzamento, per essere usata solo quando conveniente).
3.  **Abilitazione della Retromarcia:** La funzione che genera le azioni possibili (`actions`) ora include la retromarcia (`s`) come mossa valida, a patto che non ci siano ostacoli alle spalle del robot.
4.  **Passo Indietro (`stepback`):** È stata implementata la funzionalità di passo all'indietro a tutti i livelli, dal supporto fisico (`robotSupport`) fino all'attore `basicrobot`.

**Motivazione:**
Queste modifiche rendono il robot più "intelligente". Ora può generare **piani di movimento più efficienti e realistici**, sfruttando la retromarcia per manovre complesse e ottimizzando i percorsi in base al costo.

#### 3.5. Correzione di Bug Latenti e Gestione degli Errori

**Problema Originale:**
Il codice presentava diverse "cattive pratiche" che contribuivano all'instabilità:
- **Doppio Aggiornamento Mappa:** In caso di fallimento, `robotpos` aggiornava la mappa due volte, corrompendo la posizione.
- **Logica di Recupero Pericolosa:** Il `basicrobot` tentava un "auto-recupero" (passo indietro dopo un passo avanti fallito) che poteva portare a loop infiniti.
- **Fallimenti Silenziosi:** Alcuni comandi falliti non producevano alcuna risposta, rendendo il debug difficile.

**Soluzioni Implementate:**
- **Rimozione Doppio Aggiornamento:** La logica in `robotpos` è stata semplificata per garantire un singolo e corretto aggiornamento della mappa dopo un fallimento.
- **Logica di Recupero Rimossa:** Il `basicrobot` ora si limita a riportare il fallimento, lasciando la decisione su come procedere a un livello superiore.
- **Comunicazione Esplicita:** Tutte le operazioni ora forniscono una risposta chiara, sia in caso di successo che di fallimento.

### 4. Riepilogo dei Benefici

Le modifiche apportate hanno trasformato `basicrobot24` da un sistema funzionante ma fragile a una piattaforma **robusta, affidabile e coerente**. I principali vantaggi sono:
- **Resilienza:** Il sistema ora gestisce correttamente collisioni, allarmi e messaggi tardivi senza perdere la coerenza dello stato.
- **Affidabilità:** Le race condition e gli errori di parsing sono stati eliminati, garantendo che il robot esegua i comandi in modo prevedibile.
- **Efficienza:** Il nuovo planner genera percorsi più intelligenti e ottimali.
- **Mantenibilità:** L'architettura è più pulita e la gestione degli errori è esplicita, semplificando il debug e le future evoluzioni.
- **Disaccoppiamento:** L'intelligenza è stata confinata dove deve essere (nel servizio `basicrobot24`), permettendo al client (`cargorobot`) di rimanere semplice e focalizzato sui suoi obiettivi.
