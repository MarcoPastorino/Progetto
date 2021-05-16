package com.example.ProgettoPISSIR;

import org.eclipse.paho.client.mqttv3.*;

public class SmartLocker1 {

    private static MqttClient client;
    private static MqttConnectOptions options;
    public static String BROKER_URL = "tcp://localhost:1883";
    private static final String clientId = "SmartLock1";

    private static boolean statoUtilizzo = false;
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

        client.setCallback(new SubscribeCallBack());

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
        System.out.println("Avvio SmartLock1...");
        Subscriber sub = new Subscriber(client, "SmartLocker1/#");
        sub.start();

    }

    public static boolean getStato(){
        return statoUtilizzo;
    }


    public static void setStato(String s){
        if (s.equals("true")){
            statoUtilizzo = true;
        } else  {
            statoUtilizzo = false;
        }

    }


}
