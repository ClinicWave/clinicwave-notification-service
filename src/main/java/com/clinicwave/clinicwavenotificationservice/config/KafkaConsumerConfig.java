package com.clinicwave.clinicwavenotificationservice.config;

import com.clinicwave.clinicwavenotificationservice.dto.NotificationRequestDto;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

/**
 * This class is responsible for configuring the Kafka Consumer.
 * It sets the bootstrap servers, group ID, key deserializer, and value deserializer.
 *
 * @author aamir on 8/21/24
 */
@Configuration
public class KafkaConsumerConfig {
  @Value("${spring.kafka.bootstrap-servers}")
  private String bootstrapServers;

  private static final String GROUP_ID = "notification-group";

  /**
   * This method creates a ConsumerFactory object with the configuration properties.
   *
   * @return ConsumerFactory object
   */
  @Bean
  public ConsumerFactory<String, NotificationRequestDto> consumerFactory() {
    Map<String, Object> props = new HashMap<>();
    props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
    props.put(ConsumerConfig.GROUP_ID_CONFIG, GROUP_ID);
    props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
    props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
    props.put(JsonDeserializer.TRUSTED_PACKAGES, "com.clinicwave.clinicwavenotificationservice.dto");
    props.put(JsonDeserializer.TYPE_MAPPINGS, "notificationRequest:com.clinicwave.clinicwavenotificationservice.dto.NotificationRequestDto");

    return new DefaultKafkaConsumerFactory<>(
            props,
            new StringDeserializer(),
            new ErrorHandlingDeserializer<>(new JsonDeserializer<>(NotificationRequestDto.class))
    );
  }

  /**
   * This method creates a ConcurrentKafkaListenerContainerFactory object with the ConsumerFactory object.
   *
   * @return ConcurrentKafkaListenerContainerFactory object
   */
  @Bean
  public ConcurrentKafkaListenerContainerFactory<String, NotificationRequestDto> kafkaListenerContainerFactory() {
    ConcurrentKafkaListenerContainerFactory<String, NotificationRequestDto> factory = new ConcurrentKafkaListenerContainerFactory<>();
    factory.setConsumerFactory(consumerFactory());
    return factory;
  }
}
