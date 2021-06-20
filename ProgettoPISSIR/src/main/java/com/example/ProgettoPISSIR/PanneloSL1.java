package com.example.ProgettoPISSIR;


import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.boot.SpringApplication;

import java.util.Scanner;

public class PanneloSL1 {

    public static void pannelloCodice() throws MqttException {
        Scanner in = new Scanner(System.in);
        while (true){
            System.out.println("Inserire il codice di sblocco: ");
            String codice = in.nextLine();
            if (SmartLocker1.codiceSblocco.equals(codice)){
                System.out.println("Codice corretto! " + SmartLocker1.utente + " puoi ritirare il tuo ordine");
                SmartLocker1.deleteOrder();
            } else {
                System.out.println("CODICE ERRATO!");
            }
        }
    }


    public static void main(String[] args) throws MqttException {
	    SmartLocker1.main(null);
        System.out.println("Pannello sblocco SmartLocker1");
        pannelloCodice();
    }


}
