# Sprint 1

## Architettura iniziale dello sprint

## Obiettivi

Come concordato nello sprint precedente, l’obiettivo di questo sprint è lo sviluppo dei componenti ```cargoservice``` e ```cargorobot```, in modo da soddisfare i requisiti a essi associati. Prima di procedere con l’implementazione, verrà effettuata un’attenta analisi del problema e una fase di progettazione di ciascun componente. In particolare i requisiti su cui ci concentreremo in questo sprint sono:

> The software system:
>
> 1\. **Receives a request to load** on the cargo a product container already registered in the ${\color{blue}\text{productservice}}$.
>
>   The request is rejected when:
>
>   a. the product weight is evaluated too high, since the ship can carry a maximum load of
>     ${\color{blue}\texttt{MaxLoad > 0 kg}}$
>
>   b. the hold is already full, i.e., the
>     ${\color{lightblue}\texttt{4 slots}}$ are already occupied.
>
>   If the request is accepted, the ${\color{blue}\text{cargoservice}}$ associates a slot to the product
>   ${\color{blue}\texttt{PID}}$ and returns the name of the reserved slot.
>   Afterwards, it waits for the product container to be delivered to the ${\color{blue}\text{ioport}}$.
>   In the meantime, other requests are not elaborated.
>
> 3\. Ensures that the product container is placed by the ${\color{blue}\text{cargorobot}}$ within its reserved slot.
>   At the end of the operation:
>
>   * the ${\color{blue}\text{cargorobot}}$ should return to its ${\color{brown}\text{HOME}}$ location;
>   * the ${\color{blue}\text{cargoservice}}$ can process another *load-request*
>
> 5\. ${\color{brown}\text{Interrupts}}$ any activity and turns on a LED if the ${\color{blue}\text{sonar sensor}}$ measures a distance
>   $D > D_{FREE}$ for at least \$3\$ seconds (possibly a sonar failure).
>   The service continues its activities as soon as the sonar measures a distance $D \leq D_{FREE}$

Abbiamo deciso di mantenere i requisiti originali in inglese per non correre il rischio di alterarne il significato, tuttavia è necessario specificare che i punti 1 e 5 verranno sviluppati parzialmente appoggiandoci a componenti mock per ```slotmanagement```, ```sonardevice``` e ```leddevice``` che verranno implementati in seguito. 

## Analisi del problema
### Cargoservice
Cargoservice è il componente principale del sistema e avrà il compito di interagire con ogni altra componente affinchè le operazioni da eseguire seguano il giusto ordine e flusso. Essendo, dunque, un componente reattivo e proattivo lo andremo a considerare come attore.
Flusso di operazioni di cargoservice:
- inizializzazione: comunica a cargorobot la mappa della stiva (vedere la considerazione nella sezione successiva)
**Requisito 1**
- riceve una request per il carico di un prodotto (```loadrequest```)
- nel caso in cui ci siano richieste successive vengono accodate finchè la richiesta presa in carico non è stata gestita (come di seguito specificato)
- legge il PID dal corpo della ```loadrequest```
- interroga slot management per sapere se c'è uno slot libero e quale
    - se risposta positiva, slotmanagement manda il nome dello slot libero e si va avanti
    - se risposta negativa, si rifiuta la request (```loadrejected```)
- interroghiamo productservice sull'esistenza di questo PID
  - se risposta negativa, ovvero errore, messaggio di errore
  - se risposta positiva il productservice ci vengono mandati i dati del prodotto, in particolare il peso *
- viene interrogato slotmanagement sul peso totale in hold
- ricevuta risposta controllo che l'aggiunta del peso del nuovo prodotto rimanga entro i limiti di MaxLoad
  - se il controllo va a buon fine viene accettata la richiesta (```loadaccepted(SLOT)```)
  - se il controllo fallisce la request è rifiutata, ovvero viene propagato un messaggio di errore (```loadrejected```)
- una volta accettata la richiesta, attende di essere notificato della presenza del container
- invia a cargorobot l'istruzione di caricare il container, fornendogli il nome dello slot assegnato
- verifica la corretta esecuzione della richiesta (riceve richiesta di rilascio, vedi in seguito)
- attende il completamento della richiesta (riceve da cargorobot la comunicazione di raggiungimento della HOME)
- Da questo momento cargoservice può gestire una nuova richiesta
 
* nel caso in cui ci fosse in futuro l'opportunità di dover chiedere altre informazioni del prodotto e non solo il peso, rispetta il principio aperto/chiuso

Per quanto riguarda la verifica della corretta esecuzione della richiesta, ci siamo chieste quale fosse il componente più adatto al compito. La prima idea è stata quella di richiedere, a fine richiesta, a slotmanagement quale fosse il PID del prodotto caricato nello slot prescelto, in modo da verificare che coincida con quello appena caricato. Questo richiederebbe che slotmanagement riceva da cargorobot l'esito delle operazioni di carico e usi questo per aggiornare la sua rappresentazione della stiva.
In alternativa, abbiamo pensato di confrontare lo slot assegnato al prodotto con quello in cui si trova il robot alla richiesta di rilascio prodotto. 

Decidiamo quindi di definire una nuova richiesta da cargorobot a cargoservice:
```
Request loadcontainer : loadcontainer(SLOT)
```

In questo modo possiamo anche gestire l'errore in cui il robot porta il container allo slot sbagliato. ${\color{red}\text{Sarà necessario richiedere al committente come comportarsi in tale situazione.}}$

Dovendo gestire diverse situazioni di fallimento, abbiamo ritenuto opportuno l'introduzione di diverse cause di messaggio rifiutato, in particolare:
- ```too_heavy```
- ```no_slots```
- ```no_PID```

**Requisito 5**
- gestione interrupt attraverso evento
- gestione evento di ripristino

Sarà necessario aggiungere al modello due messaggi per gestire questi eventi:
```

```

Poiché gli eventi andrebbero propagati a cargorobot, decidiamo di fare una subscribe dell'intero contesto ```ctx_cargoservice```.

### Cargorobot
Cargorobot è il componente che si occupa di portare un container nello slot specificato dal cargoservice. Essendo anch'esso un componente reattivo e proattivo lo andremo a considerare come attore, analogamente al cargoservice.

A questo punto si presenta la necessità di stabilire se e come il robot sappia "orientarsi". Da un lato, potrebbe essere cargoservice a mantenere la mappa dell'area e indicare al robot passo passo come muoversi, dall'altro cargorobot può essere inizializzato per conoscere la posizione degli slot e i loro nomi ed essere in grado di raggiungerli in autonomia. Un'ulteriore opzione sarebbe quella di costruire dinamicamente la mappa in fase di inizializzazione facendola percorrere al cargorobot, ma rimarrebbe la problematica di riconoscere il nome di ogni slot.

Scegliamo la seconda opzione per semplicità, riservandoci di cambiare approccio in futuro, ad esempio in caso in cui la disposizione della stiva cambi di frequente. Comunicare la mappa della stiva sarà compito di cargoservice.

Flusso di operazioni di cargorobot:
- In fase di inizializzazione, riceve la mappa della stiva da cargoservice e memorizza la posizione degli slot associata ai loro nomi
- Quando riceve una richiesta di carico, il cargorobot comunica al basicrobot di raggiungere la I/O port
- Comunica al basicrobot di prelevare il container
- Dà al basicrobot le indicazioni per raggiungere lo slot assegnato
- Alla fine delle istruzioni, invia al cargoservice la richiesta di rilascio prodotto (```container_release```) e attende conferma
    - Si lascia la gestione di un eventuale errore al successivo confronto col committente
- Poi comunica al basicrobot di rilasciare il prodotto e gli dà le indicazioni per tornare alla HOME
- Terminate queste istruzioni, comunica al cargoservice che l'esecuzione della richiesta è terminata

<!--In caso di ricezione di evento di interrupt, decidiamo per semplicità di far tornare il robot alla HOME e salvare lo slot verso cui era diretto. Al momento di ricezione dell'evento di ripristino, il cargorobot ricomincerà a dare le indicazioni dall'inizio al basicrobot.
$\color{red}\text{Sarà necessario chiedere conferma al committente che questa soluzione non sia in conflitto con i requisiti.}$-->

Notiamo la necessità di aggiungere al modello i seguenti messaggi:
```

Reply containerloaded : containerloaded for loadcontainer
```

## Piano di testing
Poiché nell'analisi del problema abbiamo definito le cause specifiche di rifiuto della richiesta da parte di 
```cargoservice```, abbiamo ritenuto opportuno modificare il seguente test aggiungendo il controllo sulla causa di rifiuto:
* Test di richiesta con PID invalido (<0)
```java
    @Test
    public void testLoadRequestInvalidPID() throws Exception {
        //richiesta con PID invalido
        String req = CommUtils.buildRequest("mock",
                "loadrequest", "load_product(-1)", 
                "cargoservice").toString();
        
        System.out.println("Richiesta: " + req);
        
        String response = conn.request(req);
        
        System.out.println("Risposta: " + response); 
        
        //Verifica che sia stata rifiutata
        assertTrue("Test PID invalido", 
                 response.contains("loadrejected"))&&
                response.contains("no_PID");
    }
```

## Nuova Architettura
