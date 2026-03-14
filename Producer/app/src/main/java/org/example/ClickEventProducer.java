package org.example;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class ClickEventProducer {
    
    private final KafkaTemplate<String, String> kafkaTemplate;

    private static final String TOPIC = "button-clicks";

    public ClickEventProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendClickEvent(String event){
        String message = "Button clicked at: " + event;
        kafkaTemplate.send(TOPIC, message);
    }
}
