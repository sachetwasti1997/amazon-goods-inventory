package com.sachet.goodsinventoryamazon.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sachet.goodsinventoryamazon.model.Item;
import com.sachet.goodsinventoryamazon.model.OrderExpiredModal;
import com.sachet.goodsinventoryamazon.repository.ItemsRepository;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.listener.AcknowledgingMessageListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class OrderExpiredConsumer implements AcknowledgingMessageListener<String, String> {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderExpiredConsumer.class);
    private final ItemsRepository repository;
    private final ObjectMapper objectMapper;

    public OrderExpiredConsumer(ItemsRepository repository,
                                ObjectMapper objectMapper) {
        this.repository = repository;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(
            topics = {"expireorder"},
            groupId = "${spring.kafka.itemserviceconsumer.group-id}",
            containerFactory = "kafkaOrderExpiredListenerContainerFactory"
    )
    @Override
    public void onMessage(ConsumerRecord<String, String> data, Acknowledgment acknowledgment) {
        LOGGER.info("Consuming Order Expired Event {}", data);
        try {
            //Find the item
            OrderExpiredModal modal = objectMapper.readValue(data.value(), OrderExpiredModal.class);
            Optional<Item> item = repository.findById(modal.getItemId());

            //Add the reserved item back to item
            Item savedItem = item.orElseThrow();
            savedItem.setTotalQuantity(savedItem.getTotalQuantity() + modal.getOrderQuantity());

            //save the item and acknowledge
            assert acknowledgment != null;
            repository.save(savedItem);
            acknowledgment.acknowledge();
        }catch (JsonProcessingException ex) {
            LOGGER.error("Error while processing data: {}", ex.getMessage());
        }
    }
}
