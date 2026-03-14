package org.example;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class ClickEventConsumer {

    private final Counter clickCounter;

    public ClickEventConsumer(MeterRegistry registry) {
        // this metric name is what Prometheus scrapes
        // and what Grafana displays
        this.clickCounter = Counter.builder("button_clicks_total")
                .description("Total number of button clicks")
                .register(registry);
    }

    @KafkaListener(topics = "button-clicks", groupId = "click-group")
    public void consumeClickEvent(String message) {

        System.out.println("📨 CONSUMED: " + message);

        // increment counter by 1 for every click
        clickCounter.increment();

        System.out.println("📊 TOTAL CLICKS: " + (int) clickCounter.count());
    }
}