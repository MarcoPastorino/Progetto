package com.example.ProgettoPISSIR;

import org.eclipse.paho.client.mqttv3.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


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
            GestionePrenotazioni.creaOrdine3(message.toString());
        }
        if("GestionePrenotazioni/addPrenotazione".equals(topic)){
            GestionePrenotazioni.addPrenotazione(message.toString());
        }
        if("GestionePrenotazioni/deleteOrderSL1".equals(topic)){
            GestionePrenotazioni.deleteOrderSL1();
        }
        if("GestionePrenotazioni/deleteOrderSL3".equals(topic)){
            GestionePrenotazioni.deleteOrderSL3();
        }
        if("GestionePrenotazioni/setInConferma1".equals(topic)){
            GestionePrenotazioni.setInConferma1(message.toString());
        }
        if("GestionePrenotazioni/setInConferma3".equals(topic)){
            GestionePrenotazioni.setInConferma3(message.toString());
        }

    }
    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
    }
}


public class GestionePrenotazioni {

    static Publisher pub = new Publisher("GestionePrenotazioniPub", "gestioneprenotazioni", "gestioneprenotazioni");

    private static MqttClient client;
    private static MqttConnectOptions options;
    public static String BROKER_URL = "tcp://localhost:1883";
    private static final String clientId = "GestionePrenotazioni";

    public static String prenotazioni = "";

    public static boolean statoPrenSL1 = false;
    public static boolean statoPrenSL3 = false;
    public static boolean inConfermaSL1 = true;
    public static boolean inConfermaSL3 = true;

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
        options.setUserName("gestioneprenotazioni");
        options.setPassword("gestioneprenotazioni".toCharArray());
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
        return GestionePrenotazioni.statoPrenSL1 + ";" + GestionePrenotazioni.statoPrenSL3;
    }

    public static void addPrenotazione(String pren){
        prenotazioni = prenotazioni + pren + "&";
    }

    public static void creaOrdine1(String ordine) throws MqttException {
        System.out.println("Gestione Prenotazione: creaOrdine1 in gestione Prenotazione...");
        statoPrenSL1= true;
        pub.publishMessage(ordine, "SmartLocker1/creaOrdine");
    }

    public static void creaOrdine3(String ordine) throws MqttException {
        System.out.println("Gestione Prenotazione: creaOrdine3 in gestione Prenotazione...");
        statoPrenSL3 = true;
        pub.publishMessage(ordine, "SmartLocker3/creaOrdine");
    }

    public static boolean getInConferma1(){
        return inConfermaSL1;
    }

    public static void setInConferma1(String s){
        if (s.equals("true")){
            inConfermaSL1 = true;
        } else if (s.equals("false")){
            inConfermaSL1 = false;
        }
    }

    public static boolean getInConferma3(){
        return inConfermaSL3;
    }

    public static void setInConferma3(String s){
        if (s.equals("true")){
            inConfermaSL3 = true;
        } else if (s.equals("false")){
            inConfermaSL3 = false;
        }
    }

    public static void deleteOrderSL1() throws MqttException {
        //pub.publishMessage("deleteOrder", "SmartLocker1/deleteOrder");
        statoPrenSL1 = false;
        inConfermaSL1 = true;
        String prenTemp = "";
        for (String p: prenotazioni.split("&")) {
            if (p.contains("SmartLocker1")){

            } else{
                prenTemp = prenTemp + "&";
            }
        }
        prenotazioni = prenTemp;
    }

    public static void deleteOrderSL3() throws MqttException {
        //pub.publishMessage("deleteOrder", "SmartLocker3/deleteOrder");
        statoPrenSL3 = false;
        inConfermaSL3 = true;
        String prenTemp = "";
        for (String p: prenotazioni.split("&")) {
            if (p.contains("SmartLocker3")){

            } else{
                prenTemp = prenTemp + "&";
            }
        }
        prenotazioni = prenTemp;
    }


    public static String getDatiPrenotazione(String user){
        String info = "";
        if (!prenotazioni.equals("")){
            for (String p: prenotazioni.split("&")) {
                if (p.contains(user)){
                    info = info + p;
                }
            }
        } else{
            return "Non ci sono prenotazioni";
        }
        return info;
    }


    public static void main(String[] args) {
        settingClient(BROKER_URL, clientId);
        System.out.println("Avvio GestionePrenotazioni...");
        Subscriber sub = new Subscriber(client, "GestionePrenotazioni/#");
        sub.start();
    }




}
