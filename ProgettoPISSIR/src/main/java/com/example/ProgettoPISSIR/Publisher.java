package com.example.ProgettoPISSIR;

import org.eclipse.paho.client.mqttv3.*;

public class Publisher {

    public static String TOPIC = "SmartLocker1/#";
    private static MqttConnectOptions options;

    private MqttClient client;


    public Publisher (){
        try{
            client = new MqttClient("tcp://localhost:1883", "MainServer");
        } catch(MqttException e){
            e.printStackTrace();
            System.exit(1);
        }
        options = new MqttConnectOptions();
        options.setCleanSession(false);
        options.setWill(client.getTopic("Progetto/LWT"), (client.getClientId().toString()+" si e' disattivato ").getBytes(), 0, false);

        client.setCallback(new SubscribeCallBack());

        try{
            client.connect(options);
        } catch(MqttException e){
            e.printStackTrace();
            System.exit(1);
        }
    }



    void publishMessage(String msg) throws org.eclipse.paho.client.mqttv3.MqttException {
        final org.eclipse.paho.client.mqttv3.MqttTopic messageTopic = client.getTopic("SmartLocker1/"+client.getClientId().toString());
        messageTopic.publish(new org.eclipse.paho.client.mqttv3.MqttMessage(msg.getBytes()));
    }


}
