package org.example.controller;

import org.example.ClickEventProducer;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.http.ResponseEntity;


@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ClickController {
    
    private final ClickEventProducer producer;

    public ClickController(ClickEventProducer producer) {
        this.producer = producer;
    }

    @PostMapping("/producer/event")
    public ResponseEntity<String> handleClick(){
        producer.sendClickEvent("Button clicked!");
        return ResponseEntity.ok("Click event sent to Kafka");
    }
}
