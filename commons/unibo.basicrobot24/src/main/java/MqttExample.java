package main.java;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class MqttExample {

	public static void main(String[] args) {
        String broker = "tcp://localhost:1883"; // Sostituisci IP1 con l'indirizzo del PC
        String clientId = "JavaSampleClient";

        try {
            MqttClient client = new MqttClient(broker, clientId);
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);

            System.out.println("Connessione al broker: " + broker);
            client.connect(connOpts);
            System.out.println("Connesso");

            // Pubblica un messaggio di esempio
            String topic = "test/topic";
            String content = "Hello from Java!";
            int qos = 2;
            MqttMessage message = new MqttMessage(content.getBytes());
            message.setQos(qos);
            client.publish(topic, message);
            System.out.println("Messaggio pubblicato");

            // Disconnetti
            client.disconnect();
            System.out.println("Disconnesso");

        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
}
