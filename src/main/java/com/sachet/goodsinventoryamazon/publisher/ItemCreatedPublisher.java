package com.sachet.goodsinventoryamazon.publisher;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sachet.goodsinventoryamazon.model.Item;
import com.sachet.goodsinventoryamazon.model.ItemPublish;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Component
@Slf4j
public class ItemCreatedPublisher {

    private static final Logger LOGGER = LoggerFactory.getLogger(ItemCreatedPublisher.class);

    private final String TOPIC_NAME;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public ItemCreatedPublisher(@Value("${spring.kafka.topic}")
                                String TOPIC_NAME,
                                KafkaTemplate<String, String> kafkaTemplate,
                                ObjectMapper objectMapper) {
        this.TOPIC_NAME = TOPIC_NAME;
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    public CompletableFuture<SendResult<String, String>> sendItemCreatedEvent(Item item) throws JsonProcessingException {
        String key = item.getId();
        ItemPublish itemPublish = new ItemPublish(item.getId(), item.getPrice(), item.getProductName());
        String rawItem = objectMapper.writeValueAsString(itemPublish);
        LOGGER.info("Send kafka event, for item created event "+rawItem);
        ProducerRecord<String, String> producerRecord = new ProducerRecord<>(TOPIC_NAME, key, rawItem);
        var completableFuture = kafkaTemplate.send(producerRecord);
        return completableFuture.whenComplete((result, ex) -> {
            if (ex != null) {
                LOGGER.error("sendItemCreatedEvent, Error sending the message");
            }else {
                LOGGER.info("sendItemCreatedEvent, Message successfully send and received a feedback "
                + "Partition: "+result.getRecordMetadata().partition()
                + " | Topic: "+result.getRecordMetadata().topic());
            }
        });
    }
}
