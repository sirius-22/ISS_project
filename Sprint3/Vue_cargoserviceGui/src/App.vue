<script setup>
// 1. IMPORTAZIONI dalla libreria di Vue
import { ref, onMounted, onUnmounted, computed } from 'vue';

// 2. STATO REATTIVO (usando ref)
// 'ref' crea una variabile "reattiva". Ogni volta che il suo valore cambia,
// il template che la usa si aggiorna automaticamente.
const holdState = ref({ slots: [], totalWeight: 0 });
const connectionStatus = ref('Connecting...');
const pidInput = ref('');
const notification = ref({ message: '', type: '' });
let ws = null; // Riferimento non reattivo all'oggetto WebSocket

// 3. LIFECYCLE HOOKS (onMounted, onUnmounted)
// 'onMounted' esegue il codice quando il componente viene aggiunto al DOM.
// È il posto perfetto per inizializzare connessioni o altre logiche "laterali".
onMounted(() => {
  console.log("Componente montato. Tento di connettermi al WebSocket...");
  connectWebSocket();
});

// 'onUnmounted' esegue il codice prima che il componente venga rimosso.
// È fondamentale per la pulizia, per evitare memory leak o connessioni aperte.
onUnmounted(() => {
  if (ws) {
    console.log("Componente smontato. Chiudo la connessione WebSocket.");
    ws.close();
  }
});

// 4. LOGICA DI BUSINESS (funzioni)
// Con <script setup>, le funzioni sono definite come normali funzioni JavaScript
// e sono automaticamente disponibili nel template.

function connectWebSocket() {
  ws = new WebSocket('ws://localhost:8080/status-updates');

  ws.onopen = () => {
    connectionStatus.value = 'Open';
    console.log('Connessione WebSocket stabilita.');
  };

  ws.onmessage = (event) => {
    try {
      const data = JSON.parse(event.data);
      console.log('Messaggio ricevuto:', data);

      if (data.slots) {
        holdState.value = data; // Aggiornamento dello stato della stiva
      } else if (data.status) {
        handleLoadResponse(data); // Risposta a una richiesta
      }
    } catch (error) {
      console.error("Errore nel parsing del messaggio JSON:", error);
    }
  };

  ws.onerror = (error) => {
    connectionStatus.value = 'Error';
    console.error('Errore WebSocket:', error);
  };

  ws.onclose = () => {
    connectionStatus.value = 'Closed';
    console.log('Connessione WebSocket chiusa. Tento di riconnettermi tra 3 secondi...');
    // Logica opzionale di riconnessione automatica
    setTimeout(connectWebSocket, 3000);
  };
}

function sendLoadRequest() {
  if (!pidInput.value || !ws || ws.readyState !== WebSocket.OPEN) {
    showNotification(`Impossibile inviare: WebSocket non connesso.`, 'error');
    return;
  }
  const message = { type: 'loadrequest', pid: parseInt(pidInput.value) };
  ws.send(JSON.stringify(message));
  pidInput.value = '';
}

function handleLoadResponse(data) {
  if (data.status === 'accepted') {
    showNotification(`Carico per PID ${data.pid} accettato! Assegnato a ${data.slot}.`, 'success');
  } else if (data.status === 'rejected') {
    showNotification(`Carico per PID ${data.pid} rifiutato. Motivo: ${data.reason}.`, 'error');
  }
}

function showNotification(message, type, duration = 4000) {
  notification.value = { message, type };
  setTimeout(() => {
    notification.value = { message: '', type: '' };
  }, duration);
}

// 5. STATO DERIVATO (computed properties)
// 'computed' crea un valore reattivo che si ricalcola automaticamente
// solo quando una delle sue dipendenze reattive cambia.
const statusClass = computed(() => {
  switch (connectionStatus.value) {
    case 'Open': return 'status-open';
    case 'Closed':
    case 'Error': return 'status-closed';
    default: return 'status-connecting';
  }
});
</script>

<template>
  <div id="cargo-gui">
    <header>
      <h1>Cargo Service Status</h1>
      <div class="status" :class="statusClass">
        Connection: <strong>{{ connectionStatus }}</strong>
      </div>
    </header>

    <main>
      <div v-if="notification.message" class="notification" :class="`notification-${notification.type}`">
        {{ notification.message }}
      </div>

      <div class="control-panel card">
        <h2>Send Load Request</h2>
        <div class="form-group">
          <input 
            type="number" 
            v-model="pidInput"
            placeholder="Enter Product ID (PID)"
            @keydown.enter="sendLoadRequest"
          />
          <button @click="sendLoadRequest">Request Load</button>
        </div>
      </div>
      
      <div class="hold-display card">
        <h2>Hold Status</h2>
        <div class="slots-container">
          <div v-for="slot in holdState.slots" :key="slot.slotName" class="slot" :class="{ 'occupied': slot.product }">
            <h3>{{ slot.slotName }}</h3>
            <div v-if="slot.product" class="product-info">
              <p><strong>Product:</strong> {{ slot.product.name }}</p>
              <p><strong>PID:</strong> {{ slot.product.productId }}</p>
              <p><strong>Weight:</strong> {{ slot.product.weight }} kg</p>
            </div>
            <div v-else class="empty-info">
              <p>EMPTY</p>
            </div>
          </div>
          <div class="slot">
             <h3>Slot5</h3>
             <div class="empty-info"><p>EMPTY</p></div>
          </div>
        </div>
        <p class="total-weight">Total Weight: <strong>{{ holdState.totalWeight }} kg</strong></p>
      </div>
    </main>
  </div>
</template>

<style>
/* Gli stili rimangono identici a prima, sono già ottimali */
:root {
  --bg-color: #f4f7f6;
  --card-bg: #ffffff;
  --primary-color: #007bff;
  --success-color: #28a745;
  --error-color: #dc3545;
  --text-color: #333;
  --border-color: #e0e0e0;
  --font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
}

body {
  font-family: var(--font-family);
  background-color: var(--bg-color);
  color: var(--text-color);
  margin: 0;
  padding: 2rem;
}

#cargo-gui {
  max-width: 1200px;
  margin: 0 auto;
}

header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  border-bottom: 2px solid var(--border-color);
  padding-bottom: 1rem;
  margin-bottom: 2rem;
}

h1 {
  color: var(--primary-color);
}

.card {
  background: var(--card-bg);
  border-radius: 8px;
  padding: 1.5rem;
  box-shadow: 0 4px 8px rgba(0,0,0,0.05);
  margin-bottom: 2rem;
}

/* Stili per lo status della connessione */
.status {
  padding: 0.5rem 1rem;
  border-radius: 20px;
  font-size: 0.9rem;
}
.status-connecting { background-color: #ffc107; }
.status-open { background-color: var(--success-color); color: white; }
.status-closed { background-color: var(--error-color); color: white; }

/* Stili per le notifiche */
.notification {
  padding: 1rem;
  border-radius: 5px;
  margin-bottom: 1.5rem;
  text-align: center;
  font-weight: 500;
}
.notification-success { background-color: #d4edda; color: #155724; border: 1px solid #c3e6cb; }
.notification-error { background-color: #f8d7da; color: #721c24; border: 1px solid #f5c6cb; }

/* Stili per il pannello di controllo */
.control-panel .form-group {
  display: flex;
  gap: 1rem;
}
.control-panel input {
  flex-grow: 1;
  padding: 0.75rem;
  border: 1px solid var(--border-color);
  border-radius: 5px;
  font-size: 1rem;
}
.control-panel button {
  padding: 0.75rem 1.5rem;
  border: none;
  background-color: var(--primary-color);
  color: white;
  border-radius: 5px;
  cursor: pointer;
  font-size: 1rem;
  transition: background-color 0.2s;
}
.control-panel button:hover {
  background-color: #0056b3;
}

/* Stili per la visualizzazione della stiva */
.slots-container {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 1.5rem;
  margin-bottom: 1.5rem;
}
.slot {
  border: 2px solid var(--border-color);
  border-radius: 8px;
  padding: 1rem;
  text-align: center;
  transition: all 0.3s ease;
}
.slot.occupied {
  border-color: var(--primary-color);
  background-color: #f8f9fa;
}
.slot h3 {
  margin-top: 0;
  color: var(--primary-color);
  border-bottom: 1px solid var(--border-color);
  padding-bottom: 0.5rem;
}
.product-info p, .empty-info p {
  margin: 0.5rem 0;
  font-size: 0.9rem;
}
.empty-info {
  color: #888;
  font-style: italic;
  padding: 2.25rem 0;
}

.total-weight {
  text-align: right;
  font-size: 1.2rem;
  font-weight: bold;
  color: var(--primary-color);
}
</style>