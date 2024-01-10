package com.sachet.goodsinventoryamazon.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sachet.goodsinventoryamazon.model.Item;
import com.sachet.goodsinventoryamazon.model.OrderCreatedEventModal;
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
            groupId = "${spring.kafka.itemserviceconsumer.group-id}",
            containerFactory = "kafkaOrderCreatedListenerContainerFactory"
    )
    @Override
    public void onMessage(ConsumerRecord<String, String> data, Acknowledgment acknowledgment) {
        LOGGER.info("Order Consumer Record {}", data);
        try {
            //Find the Item which this order is reserving
            OrderCreatedEventModal order = objectMapper.readValue(data.value(), OrderCreatedEventModal.class);
            Optional<Item> item = repository.findById(order.getItemId());

            //Mark certain quantity of order being reserved
            Item savedItem = item.orElseThrow();
            savedItem.setTotalQuantity(savedItem.getTotalQuantity() - order.getOrderQuantity());

            repository.save(savedItem);

            acknowledgment.acknowledge();

        }catch (JsonProcessingException ex) {
            LOGGER.error("Error while processing the order {}", data.value());
        }catch (Exception ex) {
            LOGGER.error("Item not found exception");
        }
    }
}











