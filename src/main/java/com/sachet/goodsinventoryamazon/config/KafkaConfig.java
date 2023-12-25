package com.sachet.goodsinventoryamazon.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {
    private final String TOPIC_NAME;

    public KafkaConfig(@Value("${spring.kafka.topic}")String TOPIC_NAME) {
        this.TOPIC_NAME = TOPIC_NAME;
    }

    @Bean
    public NewTopic createItemTopic() {
        return TopicBuilder
                .name(TOPIC_NAME)
                .partitions(1)
                .replicas(1)
                .build();
    }

}
