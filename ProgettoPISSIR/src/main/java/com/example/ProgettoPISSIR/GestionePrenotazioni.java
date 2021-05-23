package com.example.ProgettoPISSIR;

import org.eclipse.paho.client.mqttv3.*;


class SubscribeCallBackGestionePrenotazioni implements MqttCallback {

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
        if("GestionePrenotazioni/creazioneOrdine1".equals(topic)){
            GestionePrenotazioni.creaOrdine1(message.toString());
        }
        if("GestionePrenotazioni/creazioneOrdine3".equals(topic)){
        }
    }
    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
    }
}


public class GestionePrenotazioni {

    static Publisher pub = new Publisher("GestionePrenotazioniPub");

    private static MqttClient client;
    private static MqttConnectOptions options;
    public static String BROKER_URL = "tcp://localhost:1883";
    private static final String clientId = "GestionePrenotazioni";


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

        client.setCallback(new SubscribeCallBackGestionePrenotazioni());

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


    public static String getDisp(){
        return SmartLocker1.getStato() + ";" + SmartLocker3.getStato();
    }


    public static void creaOrdine1(String ordine) throws MqttException {
        pub.publishMessage(ordine, "SmartLocker1/creaOrdine");
        System.out.println("creaOrdine1 in gestione Prenotazione...");
    }

    public static void creaOrdine3(String ordine){

    }


    public static void main(String[] args) {
        settingClient(BROKER_URL, clientId);
        System.out.println("Avvio GestionePrenotazioni...");
        Subscriber sub = new Subscriber(client, "GestionePrenotazioni/#");
        sub.start();
    }




}
