package com.jbkit.booking;

import com.jbkit.common.events.ShowSeatCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.RedisKeyValueAdapter;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@EnableKafka
@EnableScheduling
@SpringBootApplication
@EnableRedisRepositories(enableKeyspaceEvents = RedisKeyValueAdapter.EnableKeyspaceEvents.ON_STARTUP)
public class BookingApp {

    public static void main(String[] args) {
        SpringApplication.run(BookingApp.class, args);
    }

    @Bean
    public ConsumerFactory<String, ShowSeatCreatedEvent> seatCreateEventConsumerFactory() {
        JsonDeserializer<ShowSeatCreatedEvent> deserializer =
                new JsonDeserializer<>(ShowSeatCreatedEvent.class, false);
        deserializer.addTrustedPackages("com.jbkit.booking.events");
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "booking-service");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, deserializer);
        return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), deserializer);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, ShowSeatCreatedEvent> seatCreateEventKafkaListenerContainerFactory(
            ConsumerFactory<String, ShowSeatCreatedEvent> seatCreateEventConsumerFactory) {
        ConcurrentKafkaListenerContainerFactory<String, ShowSeatCreatedEvent> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(seatCreateEventConsumerFactory);
        return factory;
    }
}
