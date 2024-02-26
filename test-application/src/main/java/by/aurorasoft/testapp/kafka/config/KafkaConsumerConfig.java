package by.aurorasoft.testapp.kafka.config;

import by.aurorasoft.kafka.serialize.AvroGenericRecordDeserializer;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.common.serialization.LongDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;

import java.util.Map;

import static org.apache.kafka.clients.admin.AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.*;
import static org.springframework.kafka.listener.ContainerProperties.AckMode.MANUAL_IMMEDIATE;

@Configuration
public class KafkaConsumerConfig {
    private static final String SCHEMA_CONFIG_NAME = "SCHEMA";

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapAddress;

    @Bean
    @Autowired
    public ConsumerFactory<Long, GenericRecord> consumerFactorySyncPerson(
            @Value("${kafka.topic.sync-person.consumer.group-id}") final String groupId,
            @Value("${kafka.topic.sync-person.consumer.max-poll-records}") final int maxPollRecords,
            @Value("${kafka.topic.sync-person.consumer.fetch-max-wait-ms}") final int fetchMaxWaitMs,
            @Value("${kafka.topic.sync-person.consumer.fetch-min-bytes}") final int fetchMinBytes,
            @Qualifier("replicationSchema") final Schema schema
    ) {
        return createConsumerFactory(groupId, maxPollRecords, fetchMaxWaitMs, fetchMinBytes, schema);
    }

    @Bean
    @Autowired
    public ConcurrentKafkaListenerContainerFactory<Long, GenericRecord> listenerContainerFactorySyncPerson(
            @Qualifier("consumerFactorySyncPerson") final ConsumerFactory<Long, GenericRecord> consumerFactory
    ) {
        return createListenerContainerFactory(consumerFactory);
    }

    private <K, V> ConsumerFactory<K, V> createConsumerFactory(final String groupId,
                                                               final int maxPollRecords,
                                                               final int fetchMaxWaitMs,
                                                               final int fetchMinBytes,
                                                               final Schema schema) {
        final Map<String, Object> configsByNames = Map.of(
                BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress,
                GROUP_ID_CONFIG, groupId,
                KEY_DESERIALIZER_CLASS_CONFIG, LongDeserializer.class,
                VALUE_DESERIALIZER_CLASS_CONFIG, AvroGenericRecordDeserializer.class,
                MAX_POLL_RECORDS_CONFIG, maxPollRecords,
                FETCH_MAX_WAIT_MS_CONFIG, fetchMaxWaitMs,
                FETCH_MIN_BYTES_CONFIG, fetchMinBytes,
                ENABLE_AUTO_COMMIT_CONFIG, false,
                SCHEMA_CONFIG_NAME, schema
        );
        return new DefaultKafkaConsumerFactory<>(configsByNames);
    }

    private static <K, V> ConcurrentKafkaListenerContainerFactory<K, V> createListenerContainerFactory(
            final ConsumerFactory<K, V> consumerFactory
    ) {
        final ConcurrentKafkaListenerContainerFactory<K, V> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setBatchListener(true);
        factory.setConsumerFactory(consumerFactory);
        factory.getContainerProperties().setAckMode(MANUAL_IMMEDIATE);
        return factory;
    }
}
