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
**Requisito 1**
- riceve una request per il carico di un prodotto
- nel caso in cui ci siano richieste successive vengono accodate finchè la richiesta presa in carico non è stata gestita (come di seguito specificato)
- dalla request prendiamo il PID
- interroghiamo slot management per sapere se c'è uno slot libero e quale
    - se risposta positiva, slotmanagement manda il nome dello slot libero e si va avanti
    - se risposta negativa, si rifiuta la request (```loadrejected```)
- interroghiamo productservice sull'esistenza di questo PID
  - se risposta negativa, ovvero errore, messaggio di errore
  - se risposta positiva il productservice ci vengono mandati i dati del prodotto, in particolare il peso *
- viene interrogato slotmanagement sul peso totale in hold
- ricevuta risposta controllo che l'aggiunta del peso del nuovo prodotto rimanga entro i limiti di MaxLoad
  - se il controllo va a buon fine viene accettata la richiesta (```loadaccepted(SLOT)```)
  - se il controllo fallisce la request è rifiutata, ovvero viene propagato un messaggio di errore (```loadrejected```)
 
* nel caso in cui ci fosse in futuro l'opportunità di dover chiedere altre informazioni del prodotto e non solo il peso, rispetta il principio aperto/chiuso

Dovendo gestire diverse situazioni di fallimento, abbiamo ritenuto opportuno l'introduzione di diverse cause di messaggio rifiutato, in particolare:
- ```too_heavy```
- ```no_slots```
- ```no_PID```

**Requisito 5**
- gestione interrupt attraverso evento

### Cargorobot
Cargorobot è il componente che si occupa di portare un container nello slot specificato dal cargoservice. Essendo, dunque, un componente reattivo e proattivo lo andremo a considerare come attore.
Nella fase di inzializzazione il cargorobot cono

## Architettura iniziale di riferimento

## Piano di testing
Poiché nell'analisi del problema abbiamo definito le cause specifiche di rifiuto della richiesta da parte di 
```cargoservice```, abbiamo ritenuto opportuno modificare il seguente test aggiungendo il controllo sulla causa di rifiuto:
Test di richiesta con PID invalido (<0)
    ```Java
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
                response.contains("no_PID"));
    }
```

## Nuova Archietettura
