# --- FASE 1: Build dell'applicazione Vue ---
# Usiamo un'immagine Node.js leggera (basata su Alpine Linux) come ambiente di build
FROM node:18-alpine AS builder

# Impostiamo la cartella di lavoro all'interno del container
WORKDIR /app

# Copiamo package.json e package-lock.json (o yarn.lock)
# Questo passaggio sfrutta la cache di Docker: le dipendenze verranno reinstallate 
# solo se questi file cambiano.
COPY package*.json ./

# Installiamo le dipendenze del progetto
RUN npm install

# Copiamo tutto il resto del codice sorgente
COPY . .

# Eseguiamo il build di produzione. Questo creerà la cartella /app/dist
RUN npm run build


# --- FASE 2: Servire i file con Nginx ---
# Usiamo un'immagine Nginx ufficiale e molto leggera
FROM nginx:stable-alpine

# Copiamo i file statici generati dalla fase di build (dalla cartella /app/dist)
# nella cartella da cui Nginx serve i file di default.
COPY --from=builder /app/dist /usr/share/nginx/html

# Copiamo la configurazione personalizzata di Nginx (la creeremo nel prossimo step)
COPY nginx.conf /etc/nginx/conf.d/default.conf

# Esponiamo la porta 80, su cui Nginx è in ascolto di default
EXPOSE 80

# Comando per avviare Nginx in primo piano quando il container parte
CMD ["nginx", "-g", "daemon off;"]
