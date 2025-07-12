# Perchè Scegliere qak 


### ✅ **Vantaggi degli Attori rispetto ai POJO**

1. **Incapsulamento dello stato interno**

   * Gli attori **non espongono direttamente il loro stato interno**. L’unico modo per interagire con un attore è tramite l’invio di messaggi, evitando accessi diretti e quindi errori legati alla mutabilità o visibilità dello stato.

2. **Thread safety garantita**

   * Il **modello ad attori è intrinsecamente thread-safe**, poiché ogni attore processa i messaggi **uno alla volta**, evitando race condition senza bisogno di `synchronized`, `volatile`, o altre primitive di sincronizzazione.

3. **Protezione da concorrenza implicita**

   * Gli attori **gestiscono la concorrenza in modo implicito**, rendendo più semplice scrivere codice concorrente senza introdurre errori difficili da tracciare.

4. **Maggiore scalabilità**

   * Il modello degli attori è progettato per essere **scalabile su più core e nodi** in modo efficiente, sfruttando modelli di scheduling e mailbox non bloccanti.

5. **Facile distribuzione su rete**

   * Framework come Akka permettono di **distribuire gli attori su più nodi** della rete in modo trasparente, mentre un POJO vive all’interno di un singolo processo.

6. **Tolleranza ai guasti integrata**

   * Gli attori possono essere **supervisionati e gestiti in caso di errore** secondo una gerarchia, permettendo il recovery automatico o l’isolamento degli errori (es. actor supervision trees in Akka).

7. **Maggiore chiarezza nell’architettura**

   * Il modello a messaggi favorisce una **separazione più chiara delle responsabilità** tra componenti e promuove un design orientato ai comportamenti (es. `receive` cambia dinamicamente con `context.become()`).

8. **Supporto per comportamenti asincroni naturali**

   * L’interazione tramite messaggi promuove un **modello asincrono e reattivo**, più adatto a sistemi real-time, distribuiti o orientati a eventi.

9. **Nessuna condivisione dello stato**

   * Gli attori **non condividono memoria**, riducendo drasticamente i problemi tipici della programmazione concorrente come deadlock o livelock.

