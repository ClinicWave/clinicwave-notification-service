package com.clinicwave.clinicwavenotificationservice.config;

import com.clinicwave.clinicwavenotificationservice.dto.NotificationRequestDto;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * This class tests the KafkaConsumerConfig class.
 * It tests the configuration of the ConsumerFactory and KafkaListenerContainerFactory beans.
 *
 * @author aamir on 8/31/24
 */
@SpringBootTest
class KafkaConsumerConfigTest {
  @Value("${spring.kafka.bootstrap-servers}")
  private String bootstrapServers;

  private final KafkaConsumerConfig kafkaConsumerConfig;
  private final ConsumerFactory<String, NotificationRequestDto> consumerFactory;

  /**
   * Constructor for dependency injection.
   *
   * @param kafkaConsumerConfig The KafkaConsumerConfig object to be tested
   * @param consumerFactory     The ConsumerFactory object to be tested
   */
  @Autowired
  public KafkaConsumerConfigTest(KafkaConsumerConfig kafkaConsumerConfig, ConsumerFactory<String, NotificationRequestDto> consumerFactory) {
    this.kafkaConsumerConfig = kafkaConsumerConfig;
    this.consumerFactory = consumerFactory;
  }

  @Test
  @DisplayName("Test KafkaConsumerConfig bean")
  void testKafkaConsumerConfig() {
    assertNotNull(kafkaConsumerConfig);
  }

  @Test
  @DisplayName("Test KafkaConsumerConfig properties")
  void testConsumerFactoryConfiguration() {
    assertNotNull(consumerFactory);

    // Test if the ConsumerFactory is correctly configured
    Map<String, Object> configs = consumerFactory.getConfigurationProperties();
    assertEquals(bootstrapServers, configs.get(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG));
    assertEquals("notification-group", configs.get(ConsumerConfig.GROUP_ID_CONFIG));
    assertEquals(StringDeserializer.class, configs.get(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG));
    assertEquals(JsonDeserializer.class, configs.get(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG));
    assertEquals("com.clinicwave.clinicwavenotificationservice.dto",
            configs.get(JsonDeserializer.TRUSTED_PACKAGES));
    assertEquals("notificationRequest:com.clinicwave.clinicwavenotificationservice.dto.NotificationRequestDto",
            configs.get(JsonDeserializer.TYPE_MAPPINGS));
  }

  @Test
  @DisplayName("kafkaListenerContainerFactory creates a ConcurrentKafkaListenerContainerFactory with correct ConsumerFactory")
  void kafkaListenerContainerFactoryCreatesConcurrentKafkaListenerContainerFactoryWithCorrectConsumerFactory() {
    ConcurrentKafkaListenerContainerFactory<String, NotificationRequestDto> factory = kafkaConsumerConfig.kafkaListenerContainerFactory();
    assertNotNull(factory);
    assertEquals(consumerFactory, factory.getConsumerFactory());
  }
}