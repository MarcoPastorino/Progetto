package com.example.ProgettoPISSIR;

import org.eclipse.paho.client.mqttv3.*;

public class Publisher {

    public static String TOPIC = "SmartLocker/#";
    private static MqttConnectOptions options;

    private MqttClient client;


    public Publisher (String id, String utente, String password){
        try{
            client = new MqttClient("tcp://localhost:1883", id);
        } catch(MqttException e){
            e.printStackTrace();
            System.exit(1);
        }
        options = new MqttConnectOptions();
        options.setCleanSession(false);
        options.setWill(client.getTopic("Progetto/LWT"), (client.getClientId().toString()+" si e' disattivato ").getBytes(), 0, false);
        options.setUserName(utente);
        options.setPassword(password.toCharArray());
        //client.setCallback(new SubscribeCallBack());

        try{
            client.connect(options);
        } catch(MqttException e){
            e.printStackTrace();
            System.exit(1);
        }
    }



    void publishMessage(String msg, String topic) throws MqttException {
        //final MqttTopic messageTopic = client.getTopic(topic+"/"+client.getClientId().toString());
        final MqttTopic messageTopic = client.getTopic(topic);
        messageTopic.publish(new MqttMessage(msg.getBytes()));
    }


}

