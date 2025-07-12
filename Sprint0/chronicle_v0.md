<img src="../commons/resources/.referenceDocProf/organizzazioneSprint.png">

# Chronicle 0

## Goal
L’obiettivo dello sprint0 è analizzare i requisiti forniti dal committente riguardo al TemaFinale25 per chiarire eventuali dubbi ed eliminare ambiguità. Successivamente questi verranno formalizzati attraverso l’uso di componenti software per poi procedere con una prima definizione dell’architettura logica del sistema costituita da macrocomponenti (evidenziabili dai bounded context individuati) e le varie interazioni tra questi. Essendo un sistema complesso andremo ad utilizzare macrocomponenti sia già precedentemente sviluppate che sviluppabili per le nostre esigenze con annesso piano di testing affinché si possano isolare problemi o errori.
Infine sarà necessario sviluppare un piano di lavoro per definire deadline e i successivi sprint.

## Requisiti
I requisiti richiesti dal committente si possono consultare al seguente link:
[Requisiti del committente](../commons/requirements/requirements.md)

## Vocabolario
| Termine          | Descrizione |
|------------------|-------------|
| **Compagnia**             | Compagnia di trasporto marittimo che rappresenta il committente del lavoro. |
| **Hold (stiva)**          | Area della nave in cui verranno effettuate le varie operazioni di carico. |
| **Cargorobot**            | Nome attribuito al Differential Drive Robot (DDR) impiegato dal committente per il trasporto dei contenitori nella stiva. |
| **Products (prodotti)**   | Merce che il robot deve caricare sulla nave. |
| **Container (contenitore)** | Dove viene collocato il prodotto prima di essere caricato nella nave. |
| **Productservice**        | Servizio (software) che si occupa di registrare i prodotti e restituire le varie informazioni al cargoservice (es. PID). |
| **PID**                   | Identificatore univoco (Numero naturale > 0) assegnato ad ogni prodotto nel database. |
| **IO-Port**               | Porta d'ingresso/uscita alla stiva, punto di arrivo per i contenitori prima del carico. |
| **Slot**                  | Posizioni di stoccaggio all’interno della hold area contenenti i vari container dei prodotti, hanno una capacità massima. Sono 5 di cui una è permanentemente occupata. |
| **Sonar**                 | Componente concreto (Hardware) dato dal committente con opportuno software per il funzionamento e posto sulla IO-Port che individua la presenza di un ostacolo, nel nostro caso per individuare un product container. |
| **D**                     | Distanza che rileva il sonar alla presenza di un contenitore. |
| **DFREE**                 | Distanza massima per cui il sistema è in grado di funzionare correttamente. |
| **Cargoservice**          | Sistema software da sviluppare. |
| **MaxLoad**               | Peso (in kg) massimo che può trasportare la nave. |
| **Richiesta**             | Volontà di caricare un prodotto sulla nave. |
| **HOME**                  | Posizione di partenza/ritorno del cargorobot dopo ogni operazione. |
| **Web-gui**               | Interfaccia web grafica dove è possibile mostrare lo stato della hold e delle operazioni. |
| **Led**                   | Componente concreto (Hardware) dato dal committente con opportuno software per il funzionamento e in grado di emettere luce nel caso il sonar misuri una distanza maggiore rispetto quella massima di rilevazione. |


## Software fornito dal committente

## Macrocomponenti del sistema
Per individuare le macrocomponenti del sistema è giusto esaminare e schematizzare i bounded context precedentemente individuati.

| Bounded Context            | Macrocomponente     | Responsabilità Principale      |
|----------------------------|---------------------|--------------------------------|
| Gestione Carico            | cargoservice        | Si occupa della logica di business legata al processo di carico, come validazione, assegnazione dello slot, avanzamento del processo di carico |
| Slot Management            | slotmanagement      | Gestisce lo stato fisico della stiva, come tenere traccia degli slot disponibili e occupati, quale prodotto si trova in quale slot, fornire informazioni sull'occupazione della stiva |
| Interazione fisica/sensori | leddevice, sonardevice, cargorobot | Si occupa dell'interfacciamento con i dispositivi fisici, leggendone dati (come nel caso del sonar) o attivandoli per determinare anomalie (come nel caso del led) o determinare una determinata posizione (come nel caso del cargorobot) |
| Visualizzazione Stiva      | cargoservicestatusgui | Gestisce l'interfaccia utente (GUI) per mostrare lo stato corrente della stiva in modo dinamico interrogando i dati rilevanti |
| Product Management         | productservice      | Gestisce il ciclo di vita dei prodotti che possono essere caricati in mare, in particolare fornirà alla gestione del carico il PID del prodotto e attributi rilevanti come il peso |


## Numero di nodi computazionali

## Modello

### Perché usare QAK
[Documentazione](../commons/resources/qakDoc)

### Architettura iniziale

## Piano di testing

## Piano di lavoro
