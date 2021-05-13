package com.example.ProgettoPISSIR;

import org.eclipse.paho.client.mqttv3.*;

public class SubscribeCallBack implements MqttCallback {

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
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

    }


}
