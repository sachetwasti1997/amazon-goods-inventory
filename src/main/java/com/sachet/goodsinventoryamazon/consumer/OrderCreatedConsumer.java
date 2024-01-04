package com.sachet.goodsinventoryamazon.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sachet.goodsinventoryamazon.repository.ItemsRepository;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.listener.AcknowledgingMessageListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Component
public class OrderCreatedConsumer implements AcknowledgingMessageListener<String, String> {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderCreatedConsumer.class);
    private final ItemsRepository repository;
    private final ObjectMapper objectMapper;

    public OrderCreatedConsumer(ItemsRepository repository, ObjectMapper objectMapper) {
        this.repository = repository;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(
            topics = {"orderevent"},
            groupId = "${spring.kafka.ordereventconsumer.group-id}",
            containerFactory = "kafkaListenerContainerFactory"
    )
    @Override
    public void onMessage(ConsumerRecord<String, String> data, Acknowledgment acknowledgment) {
        LOGGER.info("Order Consumer Record {}", data);
        //Find the Item which this order is reserving

    }
}











