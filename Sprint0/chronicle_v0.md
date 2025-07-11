<img src="../commons/resources/.referenceDocProf/organizzazioneSprint.png">

# Chronicle 0

## Goal
L’obiettivo dello sprint0 è analizzare i requisiti forniti dal committente riguardo al TemaFinale25 per chiarire eventuali dubbi ed eliminare ambiguità. Successivamente questi verranno formalizzati attraverso l’uso di componenti software per poi procedere con una prima definizione dell’architettura logica del sistema costituita da macrocomponenti (evidenziabili dai bounded context individuati) e le varie interazioni tra questi. Essendo un sistema complesso andremo ad utilizzare macrocomponenti sia già precedentemente sviluppate che sviluppabili per le nostre esigenze con annesso piano di testing affinché si possano isolare problemi o errori.
Infine sarà necessario sviluppare un piano di lavoro per definire deadline e i successivi sprint.

## Requisiti
I requisiti richiesti dal committente si possono consultare al seguente link:
[Requisiti del committente](../commons/requirements/requirements.md)

## Vocabolario

## Software fornito dal committente

## Macrocomponenti del sistema

## Numero di nodi computazionali

## Modello

### Perché usare QAK
[Documentazione](../commons/resources/qakDoc)

### Architettura iniziale

## Piano di testing
[Codice](./systemOverview/src/test/java/SystemTest.java)
In questo sprint si formulano dei test generici su servizi mock, al fine di testare il funzionamento delle interazioni individuate tra i macrocomponenti del sistema. 
Nel [modello QAK](./systemOverview/src/system_overview.qak) vengono definite le seguenti richieste e relative risposte: 
```text
    // ClientSimulator -> ProductService
Request registrationrequest:	registrationrequest(Weight)

// ProductService -> ClientSimulator
Reply registrationaccepted: registrationaccepted(PID) for registrationrequest

// ClientSimulator -> CargoService
Request loadrequest:	loadrequest(PID)

// CargoService -> ProductService
Request productdatareq: productdatareq(PID)

// ProductService -> CargoService

Reply productdata: productdata(Weight)
Reply errorproductdata: errorproductdata(M) //PID doesn't exist

// CargoService -> ClientSimulator

Reply loadaccepted:		loadaccepted(Slot) for loadrequest
Reply loadrejected:		loadrejected(M) for loadrequest
```

Vengono quindi definiti dei test per verificare che il servizio restituisca le risposte corrette nelle situazioni descritte dai requisiti. 

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
                 response.contains("loadrejected"));
    }
```

* Test di richiesta accettata
```java
    @Test
	public void testLoadRequestAccepted() throws Exception {
		String req = CommUtils.buildRequest("mock",
                "loadrequest", "load_product(1)", 
                "cargoservice").toString();
        
        System.out.println("Richiesta: " + req);
        
        String response = conn.request(req);
        
        System.out.println("Risposta: " + response); 
        
        //Verifica che sia stata accettata
        assertTrue("Test richiesta accettata", 
                response.contains("loadaccepted"));
    }
```
* Test di richiesta rifiutata per peso superiore a \texttt{MaxLoad}
```java
    @Test
	public void testLoadRequestTooHeavy() throws Exception {
		String req = CommUtils.buildRequest("mock",
                "loadrequest", "load_product(2)", 
                "cargoservice").toString();
        
        System.out.println("Richiesta: " + req);
        
        String response = conn.request(req);
        
        System.out.println("Risposta: " + response); 
        
        //Verifica che sia stata rifiutata per il peso
        assertTrue("Test troppo pesante", 
                response.contains("loadrejected") && 
                response.contains("too_heavy"));
    }
```
* Test di richiesta rifiutata per mancanza di slot liberi
```java
    @Test
	public void testLoadRequestNoFreeSlots() throws Exception {
		String req = CommUtils.buildRequest("mock",
                "loadrequest", "load_product(3)", 
                "cargoservice").toString();
        
        System.out.println("Richiesta: " + req);
        
        String response = conn.request(req);
        
        System.out.println("Risposta: " + response); 
        
        //Verifica che sia stata rifiutata per mancanza di slot liberi
        assertTrue("Test slot pieni", 
                response.contains("loadrejected") && 
                response.contains("no_slots"));
    }
```


## Piano di lavoro
