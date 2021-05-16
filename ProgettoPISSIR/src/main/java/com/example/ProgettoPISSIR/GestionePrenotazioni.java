package com.example.ProgettoPISSIR;

import org.eclipse.paho.client.mqttv3.*;


public class GestionePrenotazioni {


    public static String getDisp(){
        return SmartLocker1.getStato() + ";" + SmartLocker3.getStato();
    }


}
