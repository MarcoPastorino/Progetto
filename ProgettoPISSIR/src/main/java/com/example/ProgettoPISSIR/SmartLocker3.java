package com.example.ProgettoPISSIR;

import org.eclipse.paho.client.mqttv3.*;

import java.util.UUID;

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
        if("SmartLocker3/creaOrdine".equals(topic)){
            SmartLocker3.creaOrdine(message.toString());
        }
        if ("SmartLocker3/setInConferma".equals(topic)){
            SmartLocker3.setInConferma(message.toString());
        }
        if("SmartLocker3/deleteOrder".equals(topic)){
            SmartLocker3.deleteOrder();
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
    private static final String clientId = "SmartLock3";

    public static boolean statoUtilizzo = false;
    public static boolean inConferma = true;
    public static String codiceSblocco = "";

    public static String utente = "";
    public static String dataOrdine = "";
    public static String ordine = "";
    public static String nCarta = "";

    static Publisher pub = new Publisher("SmartLocker3Pub", "smartlocker3","smartlocker3");

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
        options.setUserName("smartlocker3");
        options.setPassword("smartlocker3".toCharArray());
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


    public static void setInConferma(String s){
        if (s.equals("true")){
            inConferma = true;
        } else if (s.equals("false")){
            inConferma = false;
        }
    }

    public static boolean getInConferma(){
        return inConferma;
    }

    public static void setStato(String s){
        if (s.equals("true")){
            statoUtilizzo = true;
        } else if (s.equals("false")) {
            statoUtilizzo = false;
        }

    }


    public static void creaOrdine(String Ordine) throws MqttException {
        if (statoUtilizzo == false){
            System.out.println("creaOrdine in smartLocker3");
            String[] parti = Ordine.split("_");
            utente = parti[0];
            dataOrdine = parti[1];
            ordine = parti[2];
            nCarta = parti[3];
            statoUtilizzo = true;
            codiceSblocco = UUID.randomUUID().toString();
            System.out.println("creaOrdine in smartlocker3: " + utente + "  " + dataOrdine+ "  " + ordine+ "  " + nCarta + " " + codiceSblocco);
            pub.publishMessage(Ordine+"_"+codiceSblocco+"_SmartLocker3", "GestionePrenotazioni/addPrenotazione");
        }

    }


    public static void deleteOrder() throws MqttException {
        utente = "";
        dataOrdine = "";
        ordine = "";
        nCarta = "";
        statoUtilizzo = false;
        inConferma = true;
        codiceSblocco = "";
        pub.publishMessage("del", "GestionePrenotazioni/deleteOrderSL3");
    }

}
