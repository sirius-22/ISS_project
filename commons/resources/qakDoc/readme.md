# QAK

### ✅ **Perché usare Qak per i microservizi**

**Qak** (quasi-actor in Kotlin) è un framework basato sul modello ad attori, orientato alla progettazione e implementazione di **sistemi distribuiti e reattivi**. I microservizi realizzati in Qak sono modellati come **attori autonomi**, con comportamento definito da un **automa a stati finiti** (FSM) che reagisce ai messaggi ricevuti.

---

### 📌 **Principali vantaggi di Qak per microservizi**

1. **Message-driven e concorrente**
   Gli attori elaborano messaggi in modo asincrono, sfruttando **canali Kotlin**. Questo evita il blocco dei thread e consente maggiore **scalabilità** e **reattività**.

2. **Modellazione eseguibile e DSL**
   Il linguaggio Qak permette di definire il sistema in forma **modellata** e **eseguibile**, facilitando:

   * analisi;
   * test;
   * generazione automatica del codice;
   * comprensione dell’interazione tra servizi.

3. **Supporto integrato per protocolli distribuiti**
   Ogni attore è anche una:

   * **risorsa CoAP** (per IoT);
   * **entità MQTT** (per eventi distribuiti);
     rendendo Qak adatto a sistemi **eterogenei**.

4. **FSM (Finite State Machine)**
   Gli attori Qak possono operare come FSM, offrendo una gestione **esplicita e controllata degli stati**. Perfetto per workflow, protocolli, robotica.

---

### 📐 **Abstraction gap ridotto**

Con Qak, il **gap tra modello concettuale e codice eseguibile** è ridotto drasticamente.
In sistemi tradizionali, l’**abstraction gap** è alto: i modelli (diagrammi UML, diagrammi di sequenza, ecc.) non corrispondono al codice effettivo.

Con Qak:

* il modello **è il codice**;
* si parla di **modello eseguibile**, sviluppato con una DSL (Domain Specific Language);
* si facilita l'**allineamento tra progetto e implementazione**, riducendo bug e tempo di sviluppo.

---

### ⚔️ Vantaggi rispetto a POJO e approcci tradizionali

| **Aspetto**                   | **Qak Actor**                                                 | **POJO tradizionale**                                        |
| ----------------------------- | ------------------------------------------------------------- | ------------------------------------------------------------ |
| **Natura del componente**     | 🟢 **Attivo**: ha un proprio flusso di controllo              | 🔴 **Passivo**: esegue solo se chiamato da altri             |
| **Concorrenza**               | Gestita da coroutine e canali (message-driven, non bloccante) | Richiede gestione manuale (thread, lock, sincronizzazione)   |
| **Comunicazione**             | Asincrona via messaggi (dispatch/request/event)               | Invocazione diretta, spesso sincrona e accoppiata            |
| **Stato e comportamento**     | Modellato come **FSM** (chiaro, deterministico, reattivo)     | Sparso nel codice, spesso implicito                          |
| **Distribuzione**             | Supporto integrato per CoAP, MQTT, TCP                        | Richiede configurazione e librerie esterne                   |
| **Progettazione**             | DSL + **modello eseguibile**                                  | Modello separato dal codice, soggetto a disallineamenti      |
| **Abstraction gap**           | 🔽 Ridotto: il modello è il codice                            | 🔼 Alto: necessaria traduzione tra modello e implementazione |
| **Sviluppo e prototipazione** | Rapido con Qak factory + Eclipse plugin                       | Manuale, lento, maggior rischio di errori                    |

---

