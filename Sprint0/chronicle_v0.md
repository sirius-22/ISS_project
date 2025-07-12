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

## Numero di nodi computazionali

## Modello

### Perché usare QAK
[Documentazione](../commons/resources/qakDoc)

### Architettura iniziale

## Piano di testing

## Piano di lavoro
