Come generare un’immagine Docker:

1.	Entrare nella cartella CargoServiceCore
2.	Fare ```gradle clean distTar``` → genera un file tar
3.	Fare ```docker build -t cargoservice:1.0 .``` → crea l’immagine Docker (il file Dockerfile è già pronto)
4.	Fare ```docker save cargoservice:1.0 -o cargoservice.tar``` -> serve per salvarsi il .tar dell’immagine Docker da poter condividere
5.	Fare ```docker load -i cargoservice.tar``` se si vuole caricare un’immagine Docker una volta ricevuto un file tar con essa

N.B. sul file cargoservice.qak ci sono commenti che riguardano il contesto del sonar per vari utilizzi (containerizzazione, far partire il tutto senza containerizzazione ma solo facendo gradle run, far partire tutto senza containerizzazione ma con sonar_mock)

N.B. bisogna unzippare la cartella con il tar prima di fare la load dell'immagine
