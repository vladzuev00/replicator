package by.aurorasoft.testapp.kafka.config;

import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.common.serialization.LongDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.Map;

import static org.apache.kafka.clients.admin.AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.*;
import static org.springframework.kafka.listener.ContainerProperties.AckMode.MANUAL_IMMEDIATE;

//TODO: refactor
@Configuration
public class KafkaConsumerConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapAddress;

    @Bean
    @Autowired
    public ConsumerFactory<Long, String> consumerFactorySyncPerson(
            @Value("${kafka.topic.sync-person.consumer.group-id}") final String groupId
    ) {
        return createConsumerFactory(groupId);
    }

    @Bean
    @Autowired
    public ConcurrentKafkaListenerContainerFactory<Long, String> listenerContainerFactorySyncPerson(
            @Qualifier("consumerFactorySyncPerson") final ConsumerFactory<Long, String> consumerFactory
    ) {
        return createListenerContainerFactory(consumerFactory);
    }

    private <K, V> ConsumerFactory<K, V> createConsumerFactory(final String groupId) {
        final Map<String, Object> configsByNames = Map.of(
                BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress,
                GROUP_ID_CONFIG, groupId,
                KEY_DESERIALIZER_CLASS_CONFIG, LongDeserializer.class,
                VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class
        );
        return new DefaultKafkaConsumerFactory<>(configsByNames);
    }

    private static <K, V> ConcurrentKafkaListenerContainerFactory<K, V> createListenerContainerFactory(
            final ConsumerFactory<K, V> consumerFactory
    ) {
        final ConcurrentKafkaListenerContainerFactory<K, V> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
        return factory;
    }
}
