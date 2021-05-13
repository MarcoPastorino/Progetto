package com.example.ProgettoPISSIR;
import org.eclipse.paho.client.mqttv3.*;


public class Subscriber extends Thread {

    public String TOPIC;

    private MqttClient mqttClient;

    public Subscriber(MqttClient mqttC, String topic) {
        this.mqttClient = mqttC;
        TOPIC = topic;
    }

    @Override
    public void run() {
        try {
            mqttClient.subscribe(TOPIC);
            System.out.println("Subscriber is now listening to "+ TOPIC);

        } catch (MqttException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

}
