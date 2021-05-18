package com.example.ProgettoPISSIR;

import org.eclipse.paho.client.mqttv3.*;

class SubscribeCallBackSL3 implements MqttCallback {
    @Override
    public void connectionLost(Throwable cause) {
        //This is called when the connection is lost. We could reconnect here.
    }
    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        System.out.println(topic + ": " + message.toString());
        if ("home/LWT".equals(topic)) {
            System.err.println("Sensor gone!");
        }
        if("SmartLocker3/setStato".equals(topic)){
            if (message.toString().equals("true")){
                SmartLocker3.statoUtilizzo = true;
            } else if (message.toString().equals("false")){
                SmartLocker3.statoUtilizzo = false;
            }
            System.out.println("setto stato smartlocker3 = " + SmartLocker3.getStato());
        }
    }
    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
    }
}


public class SmartLocker3 {

    private static MqttClient client;
    private static MqttConnectOptions options;
    public static String BROKER_URL = "tcp://localhost:1883";
    private static String clientId = "SmartLock3";

    public static  boolean statoUtilizzo = false;
    private String codiceSblocco = "";


    public static void settingClient(String hurl, String clientId){
        try{
            client = new MqttClient(hurl, clientId);
        } catch(MqttException e){
            e.printStackTrace();
            System.exit(1);
        }
        options = new MqttConnectOptions();
        options.setCleanSession(false);
        options.setWill(client.getTopic("Progetto/LWT"), (client.getClientId().toString()+" si e' disattivato ").getBytes(), 0, false);

        client.setCallback(new SubscribeCallBackSL3());

        try{
            client.connect(options);
        } catch(MqttException e){
            e.printStackTrace();
            System.exit(1);
        }
    }

    public static void reconnecting(){
        try{
            client.reconnect();
        } catch(MqttException e){
            e.printStackTrace();
            System.exit(1);
        }
    }


    public static void main(String[] args) {

        settingClient(BROKER_URL, clientId);
        System.out.println("Avvio SmartLock3...");
        Subscriber sub = new Subscriber(client, "SmartLocker3/#");
        sub.start();

    }

    public static boolean getStato(){
        return statoUtilizzo;
    }


    public static void setStato(String s){
        if (s.equals("true")){
            statoUtilizzo = true;
        } else if (s.equals("false")) {
            statoUtilizzo = false;
        }
    }

}
