# Sprint2

## Architettura iniziale dello sprint
<img src='../Sprint1/logicModel/logic_modelarch.png'>

## Obiettivi

Come concordato negli sprint precedenti, l’obiettivo di questo sprint è lo sviluppo dei componenti ```sonardevice``` e ```slotmanagement```, in modo da soddisfare i requisiti a essi associati. Prima di procedere con l’implementazione, verrà effettuata un’attenta analisi del problema e una fase di progettazione di ciascun componente. In particolare i requisiti su cui ci concentreremo in questo sprint sono:

>  The sensor:
>  * The ${\color{magenta}\text{sensor}}$ put in front of the ${\color{blue}\text{IOPort}}$ is a sonar used to detect the presence of a product container, when it measures a distance  ${\color{blue}\texttt{D}}$, such that  ${\color{blue}\texttt{D < DFREE/2}}$, during a reasonable time (e.g.  ${\color{blue}\texttt{3}}$ secs).
>    
>   2\. Detects (by means of the ${\color{blue}\text{sonar}}$ ${\color{magenta}\text{sensor}}$) the presence of the product container at the ${\color{blue}\text{ioport}}$.
>
>   5\. ${\color{brown}\text{Interrupts}}$ any activity and turns on a LED if the ${\color{blue}\text{sonar sensor}}$ measures a distance
   $D > D_{FREE}$
   for at least \$3\$ seconds (possibly a sonar failure).
   The service continues its activities as soon as the sonar measures a distance
   $D \leq D_{FREE}$


Abbiamo deciso di mantenere i requisiti originali in inglese per non correre il rischio di alterarne il significato, tuttavia è necessario specificare che il punto 5 verrà sviluppato parzialmente appoggiandoci a un componente mock per ```leddevice``` che verrà implementato in seguito. 

## Analisi del problema

### sonardevice
Il sensore sonar deve essere in grado di effettuare misurazioni per la rilevazione di container o malfunzionamenti. Essendo, dunque, un componente reattivo e proattivo lo andremo a considerare come attore.

Flusso di operazioni di sonardevice:
- nella fase di inizializzazione il ```sonardevice``` attiva il sensore fisico
- il sonar continuamente effettua misurazioni, se per 3 secondi la misurazione è consistente (ovvero non cambai significativamente rispetto un margine di errore, da valutare in base all'hardware fornito dal committente) essa viene valutata da ```sonardevice``` nelle seguenti casistiche:
<br>
1. 0 <= D < D_{FREE}/2 -> un container è arrivato alla IO-port dunque il sonardevice dovrà mandare un evento (```containerhere```) per avvisarne la presenza
2. D > D_{FREE} -> è avvenuto un malfunzionamento del sistema, dunque ```sonardevice``` dovrà mandare un evento (```stopActions```) per interrompere le attività di tutto il sistema. In questo caso ```sonardevice``` aspetta una misurazione D <= D_{FREE} per sbloccare il sistema (```resumeActions```) ignorando le altre casistiche


Formalizziamo i messaggi sopraccitati che erano già stati introdotti nel sistema dello Sprint1 per la realizzazione di ```sonar_mock```.

```
  Event stopActions : stopActions(M)
  Event resumeActions : resumeActions(M)
  Event containerhere : containerhere(M)

```

Analizzando il flusso di ```sonardevice```abbiamo appurato che per mantenere al meglio il principio di single responsability sia opportuno dividere i compiti di ```sonardevice```in due attori distinti, uno che si occupi delle misurazioni vere e proprie controllando il sensore fisico e uno che si occupi del processamento di tali misurazioni.

<!--d < dfree/2 -> container
d <= dfree -> torna dal malfunzionamento
d > dfree -> mafunzionamento-->








### Cargorobot
```cargorobot``` è il componente che si occupa di portare un container nello slot specificato dal ```cargoservice```. Essendo anch'esso un componente reattivo e proattivo lo andremo a considerare come attore, analogamente al ```cargoservice```.

A questo punto si presenta la necessità di stabilire se e come il robot sappia "orientarsi". Da un lato, potrebbe essere ```cargoservice``` a mantenere la mappa dell'area e indicare al robot passo passo come muoversi, dall'altro ```cargorobot``` può essere inizializzato per conoscere la posizione degli slot e i loro nomi ed essere in grado di raggiungerli in autonomia. Un'ulteriore opzione sarebbe quella di costruire dinamicamente la mappa in fase di inizializzazione facendola percorrere al robot, ma rimarrebbe la problematica di riconoscere il nome di ogni slot.

Scegliamo la seconda opzione (l'inizializzazione statica della mappa) per semplicità, riservandoci di cambiare approccio in futuro, ad esempio in caso in cui la disposizione della stiva cambi di frequente. Inizializziamo il ```cargorobot``` con la mappa salvata su un file di configurazione a parte.

Flusso di operazioni di ```cargorobot```:
- Inizializzazione di ```cargorobot``` con salvataggio della mappa della stiva e memorizzazione della posizione degli slot associata ai loro nomi

Poi, ciclicamente per ogni richiesta:
- Richiesta di ```engage``` da parte di ```cargorobot``` a ```basicrobot```
- In caso di rifiuto della richiesta, verrà nuovamente fatta la richiesta di ```engage```
- In caso di successo della richiesta, quando riceve una richiesta di carico, il ```cargorobot``` comunica al ```basicrobot``` di raggiungere la I/O port
- Comunica al ```basicrobot``` di prelevare il container
- Dà al ```basicrobot``` le indicazioni per raggiungere lo slot assegnato
- Per la considerazione fatta in precedenza, nel caso in cui il ```basicrobot``` non dovesse finire tutte le istruzioni a lui fornite, l'idea è che torni all'I/O port e riesegua tutte le istruzioni
- Poi il ```cargorobot``` comunica al ```basicrobot``` di rilasciare il prodotto e gli dà le indicazioni per tornare alla HOME
- Terminate queste istruzioni, comunica al ```cargoservice``` che l'esecuzione della richiesta è terminata

N.B. Parliamo già, ad esempio, di richiesta di ```engage``` nonostante non sia nei requisiti in quanto si è andati a studiare il software fornito dal committente.
<!--In caso di ricezione di evento di interrupt, decidiamo per semplicità di far tornare il robot alla HOME e salvare lo slot verso cui era diretto. Al momento di ricezione dell'evento di ripristino, il cargorobot ricomincerà a dare le indicazioni dall'inizio al basicrobot.
$\color{red}\text{Sarà necessario chiedere conferma al committente che questa soluzione non sia in conflitto con i requisiti.}$-->

Notiamo la necessità di aggiungere al modello i seguenti messaggi:
```
//CargoService -> CargoRobot
Request loadcontainer:		loadcontainer(Slot)

// CargoRobot -> CargoService
Reply containerloaded : containerloaded(M) for loadcontainer

// CargoRobot -> BasicRobot
Event alarm: 		alarm(STOP) // to stop basicRobot

Request engage : engage(CALLER)
Reply engagedone : engagedone(ARG) for engage
Reply engagerefused : engagerefused(ARG) for engage

Request moverobot    :  moverobot(TARGETX, TARGETY)
Reply moverobotdone  :  moverobotok(ARG)                    for moverobot
Reply moverobotfailed:  moverobotfailed(PLANDONE, PLANTODO) for moverobot
```

### Modello
Per chiarezza inseriamo un link per il modello. [Modello QAK](./logicModel/src/logicModel.qak)


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

Per il momento risulta difficile individuare altri test in quanto utilizzando software del committente per ```basicrobot``` il suo stato non è direttamente verificabile, ci riserviamo aggiunte nella fase di progettazione dopo un riscontro con il committente.

## Nuova Architettura
In seguito, la nuova architettura alla fine dell'analisi del problema, ci riserviamo la possibilità di cambiamenti a seguito della progettazione dell'attuale sprint. 

![Immagine architettura](./logicModel/logic_modelarch.png)
<!--<img src='./logicModel/logic_modelarch.png'>-->

## Progettazione

Grazie alla modellazione tramite QAK è stato possibile avere uno scheletro del sistema da implementare, a cui sono state semplicemente aggiunte alcune variabili globali per la gestione della logica di business. 

La mappa della stiva che ci è stata fornita dal committente è stata tradotta in formato json:

```json
{
  "Home": { "x": 0, "y": 0, "direction":"s" },
  "SlotsObstacles": [
    { "name": "SlotObstacle1", "x": 2, "y": 1 , "direction":"r" },
    { "name": "SlotObstacle2", "x": 3, "y": 1, "direction":"l" },
    { "name": "SlotObstacle3", "x": 2, "y": 3, "direction":"r" },
    { "name": "SlotObstacle4", "x": 3, "y": 3, "direction":"l" }
  ],
  "LaydownPositions": [
    { "name": "Slot1", "x": 1, "y": 1, "direction":"r" },
    { "name": "Slot2", "x": 1, "y": 3, "direction":"r" },
    { "name": "Slot3", "x": 4, "y": 1, "direction":"l" },
    { "name": "Slot4", "x": 4, "y": 3, "direction":"l" }
  ],
  "IoPort": { "x": 0, "y": 5, "direction":"s" },
  "PickupContainerPosition": { "x": 0, "y": 4, "direction":"s" }
}

```
Ci siamo rese conto che avevamo bisogno di definire ulteriori coordinate per identificare i luoghi in cui si deve spostare il robot.

Abbiamo utilizzato un pojo per leggere il file json e rendere le coordinate facilmente fruibili a cargorobot.
```java
    public class MapService {
		//Map of map Object -> map of its coordinates "x" ->0 "y" ->0
    private final Map<String, MapLocation> lookupMap = new HashMap<>();

    public MapService(String jsonFilePath) throws IOException {
        String content = Files.readString(Paths.get(jsonFilePath));
        JsonObject jsonData = JsonParser.parseString(content).getAsJsonObject();

        // Carica Home
        lookupMap.put("Home", extractCoords(jsonData.getAsJsonObject("Home")));
        // Carica IoPort e PickupContainerPosition
        lookupMap.put("IO", extractCoords(jsonData.getAsJsonObject("IoPort")));
        lookupMap.put("Pickup", extractCoords(jsonData.getAsJsonObject("PickupContainerPosition")));

        // Carica SlotObstacles 
        //real positiones of the slots
        for (JsonElement e : jsonData.getAsJsonArray("SlotsObstacles")) {
            JsonObject obj = e.getAsJsonObject();
            lookupMap.put(obj.get("name").getAsString(), extractCoords(obj));
        }

        // Carica LaydownPositions
        //we name them as "Slot1", "Slot2", etc.
        // This is a list of positions where the robot can lay down containers
        for (JsonElement e : jsonData.getAsJsonArray("LaydownPositions")) {
            JsonObject obj = e.getAsJsonObject();
            lookupMap.put(obj.get("name").getAsString(), extractCoords(obj));
        }
    }

    public MapLocation getCoordinates(String name) {
        return lookupMap.get(name);
    }

    private MapLocation extractCoords(JsonObject obj) {
        
    	Map<String, Integer> coords = new HashMap<>();
        coords.put("x", obj.get("x").getAsInt());
        coords.put("y", obj.get("y").getAsInt());
        Aril dir = Aril.valueOf(obj.get("direction").getAsString());
        MapLocation loc = new MapLocation(coords, dir);
        return loc;
    }
    
 ```
Infine, seguendo il pattern Singleton, è stata creata una classe che restituisce un'istanza di MapService  

```java
    public class MapServiceSingleton {
    private static MapService instance;

    public static void init(String jsonPath) throws IOException {
        if (instance == null) {
            instance = new MapService(jsonPath);
        }
    }

    public static MapService getInstance() {
        return instance;
    }
}
```

Questa viene utilizzata all'interno di cargotobot nel seguente modo:

```
	import "main.java.map*"
		//init map
		[#
			MapServiceSingleton.init("map.json");
			var Map = MapServiceSingleton.getInstance()

			// Home Coordinates
			var HomeLoc = Map.getCoordinates("Home");
			var Homecoords = HomeLoc.getCoords();
			var Home_X = Homecoords.get("x");
			var Home_Y = Homecoords.get("y");
			var Homedir = HomeLoc.getFacingDir()
        #]

```

## Deployment

1. Andare nella cartella ```Sprint1/Implementation```
2. Eseguire il comando ```docker load -i basicrobot24.tar``` per caricare l'immagine Docker del basicrobot
3. Creare la rete ```docker network create iss-network```
4. Eseguire  il comando ```docker compose -f arch1.yaml up``` per far partire vari componenti del sistema (cargorobot, productservice)
5. Aprire il browser su ```localhost:8090``` per visualizzare l’ambiente WEnv in cui lavorerà il DDR robot
7. Eseguire il comando ```./gradlew run``` oppure ```gradle run``` per far partire il resto del sistema cargoservice

*Note:*

a. Per far eseguire il punto 2 è bene ricordarsi di far partire il demone Docker </br>
b. Il sistema cargoservice si appoggia a productservice che ha un database Mongo per la persistenza dei prodotti, questo è già stato riempito con opportuni prodotti di test attraverso il file ```setup_mongo.js```
