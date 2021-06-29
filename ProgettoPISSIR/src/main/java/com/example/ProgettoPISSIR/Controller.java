package com.example.ProgettoPISSIR;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.eclipse.paho.client.mqttv3.*;
import java.util.Collections;
import java.util.Map;

@RestController
public class Controller {

    static Publisher pub = new Publisher("MainServerPub", "controller", "controller");

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

    @GetMapping("/creaPrenotazioneSL1")
    public String creaPrenotazioneSL1(@RequestParam(value = "ordine", defaultValue = "error") String ordine) throws MqttException {
        if (ordine.equals("error")){
            return "Qualcosa è andato storto nella crezione dell'ordine";
        }
        System.out.println("faccio pub su GestionePrenotazioni/creazioneOrdine1 dal controller");
        pub.publishMessage(ordine, "GestionePrenotazioni/creazioneOrdine1");
        return "Ordine creato";
    }

    @GetMapping("/creaPrenotazioneSL3")
    public String creaPrenotazioneSL3(@RequestParam(value = "ordine", defaultValue = "error") String ordine) throws MqttException {
        if (ordine.equals("error")){
            return "Qualcosa è andato storto nella crezione dell'ordine";
        }
        System.out.println("faccio pub su GestionePrenotazioni/creazioneOrdine3 dal controller");
        pub.publishMessage(ordine, "GestionePrenotazioni/creazioneOrdine3");
        return "Ordine creato";
    }

    @GetMapping("/verificaPrenotazioni")
    public String verificaPrenotazioniUtente(@RequestParam(value = "user", defaultValue = "error") String user) throws MqttException {
        if (user.equals("error")){
            return "Qualcosa è andato storto nella ricerca delle tue prenotazioni";
        }
        System.out.println("Controller: " + user);
        String datiPrenotazione = GestionePrenotazioni.getDatiPrenotazione(user);
        return datiPrenotazione;
    }

    @GetMapping("/verificaInConferma")
    public String verificaInConferma1(@RequestParam(value = "user", defaultValue = "error") String user, @RequestParam (value = "locker", defaultValue = "error") String locker){
        if (user.equals("error")){
            return "Qualcosa è andato storto con la verifca";
        }
        if (locker.equals("SmartLocker1")){
            Boolean res = GestionePrenotazioni.getInConferma1();
            return res.toString();
        } else if (locker.equals("SmartLocker3")){
            Boolean res = GestionePrenotazioni.getInConferma3();
            return res.toString();
        }
        return "error";
    }

    @GetMapping("/deleteOrdineSmartLocker1")
    public String deleteOrdineSL1() throws MqttException {
        pub.publishMessage("deleteOrderSL1", "GestionePrenotazioni/deleteOrderSL1");
        return "Ordine Eliminato<br><a href=\"/index.html\">Home</a>";
    }

    @GetMapping("/deleteOrdineSmartLocker3")
    public String deleteOrdineSL3() throws MqttException {
        pub.publishMessage("deleteOrderSL3", "GestionePrenotazioni/deleteOrderSL3");
        return "Ordine Eliminato<br><a href=\"/index.html\">Home</a>";
    }

    @GetMapping("/sbloccoSmartLocker1")
    public String sbloccoSmartLocker1(@RequestParam(value = "codice", defaultValue = "error") String codice) throws MqttException {
        if (!codice.equals("error")){
            if (SmartLocker1.codiceSblocco.equals(codice)){
                pub.publishMessage("delete", "GestionePrenotazioni/deleteOrderSL1");
                return "Codice Corretto, preleva il tuo ordine";
            } else{
                return "Codice Errato";
            }
        } else{
            return "errore nell'invio del codice";
        }
    }

    @GetMapping("/sbloccoSmartLocker3")
    public String sbloccoSmartLocker3(@RequestParam(value = "codice", defaultValue = "error") String codice) throws MqttException {
        if (!codice.equals("error")){
            if (SmartLocker3.codiceSblocco.equals(codice)){
                pub.publishMessage("delete", "GestionePrenotazioni/deleteOrderSL3");
                return "Codice Corretto, preleva il tuo ordine";
            } else{
                return "Codice Errato";
            }
        } else{
            return "errore nell'invio del codice";
        }
    }




}