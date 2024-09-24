package com.example.userservice.configs;

import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;

@Configuration
public class KafkaProducerConfig {
                     // <Topic, Event(Message)>
    public KafkaTemplate<String, String> kafkaTemplate;
    public KafkaProducerConfig(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }
    public void publishEvent(String topic, String message) {
        kafkaTemplate.send(topic, message);
    }
}
