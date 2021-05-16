package com.example.ProgettoPISSIR;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.eclipse.paho.client.mqttv3.*;
import java.util.Collections;
import java.util.Map;

@RestController
public class Controller {

    static Publisher pub = new Publisher();

//    @GetMapping("/")
//    public String helloWorld() {
//        return "Home Page";
//    }

    @GetMapping("/not-restricted")
    public String notRestricted() {
        return "you don't need to be logged in";
    }

    @GetMapping("/restricted")
    public String restricted() {
        return "if you see this you are logged in";
    }

    @GetMapping("/user")
    public String user(@AuthenticationPrincipal OAuth2User principal) {
        return principal.getAttributes().get("name").toString();
    }

    @GetMapping("/prenotaArmadietto")
    public String prenotazioneArmadietto() {
        return "Pagina per effettuare una prenotazione";
    }

    @GetMapping("/msgToSmartlocker1")
    public void msgToSmartlocker1() throws MqttException {
        pub.publishMessage("Prova messaggio", "SmartLocker1");
    }

    @GetMapping("/verificaDisp")
    public String verificaDisp() throws MqttException {
       return GestionePrenotazioni.getDisp();
    }


}