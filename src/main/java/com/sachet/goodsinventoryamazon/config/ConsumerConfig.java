package com.sachet.goodsinventoryamazon.config;

import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class ConsumerConfig {

    private final String bootstrapServer;
    private final String orderCreatedConsumerGroup;
    private final String orderCancelledConsumerGroup;
    private final String orderExpiredConsumerGroup;

    public ConsumerConfig(
            @Value("${spring.kafka.bootstrap-servers}")
            String bootstrapServer,
            @Value("${spring.kafka.ordereventconsumer.group-id}")
            String orderCreatedConsumerGroup,
            @Value("${spring.kafka.ordercancelconsumer.group-id}")
            String orderCancelledConsumerGroup,
            @Value("${spring.kafka.orderexpiredconsumer.group-id}")
            String orderExpiredConsumerGroup) {
        this.bootstrapServer = bootstrapServer;
        this.orderCreatedConsumerGroup = orderCreatedConsumerGroup;
        this.orderCancelledConsumerGroup = orderCancelledConsumerGroup;
        this.orderExpiredConsumerGroup = orderExpiredConsumerGroup;
    }

    private ConsumerFactory<String, String> createConsumerFactory(String groupName) {
        Map<String, Object> props = new HashMap<>();
        props.put(org.apache.kafka.clients.consumer.ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
        props.put(org.apache.kafka.clients.consumer.ConsumerConfig.GROUP_ID_CONFIG, groupName);
        props.put(org.apache.kafka.clients.consumer.ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
                StringDeserializer.class);
        props.put(org.apache.kafka.clients.consumer.ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
                StringDeserializer.class);
        props.put(org.apache.kafka.clients.consumer.ConsumerConfig.REQUEST_TIMEOUT_MS_CONFIG, "600000");
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        return new DefaultKafkaConsumerFactory<>(props);
    }

    @Bean
    ConcurrentKafkaListenerContainerFactory<String,String> kafkaOrderCreatedListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, String> factory
                = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(createConsumerFactory(orderCreatedConsumerGroup));
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL);
        return factory;
    }

    @Bean
    ConcurrentKafkaListenerContainerFactory<String,String> kafkaOrderCancelledListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, String> factory
                = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(createConsumerFactory(orderCancelledConsumerGroup));
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL);
        return factory;
    }

    @Bean
    ConcurrentKafkaListenerContainerFactory<String,String> kafkaOrderExpiredListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, String> factory
                = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(createConsumerFactory(orderExpiredConsumerGroup));
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL);
        return factory;
    }
}
