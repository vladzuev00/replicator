package by.aurorasoft.testapp.kafka.config;

import by.aurorasoft.replicator.model.TransportableReplication;
import org.apache.kafka.common.serialization.LongDeserializer;
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
import static org.apache.kafka.clients.consumer.ConsumerConfig.GROUP_ID_CONFIG;

//TODO: think about do in library
//TODO: refactor
@Configuration
public class KafkaConsumerConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapAddress;

    @Bean
    @Autowired
    public ConsumerFactory<Long, TransportableReplication> consumerFactorySyncPerson(
            @Value("${kafka.topic.sync-person.consumer.group-id}") final String groupId
    ) {
        return createConsumerFactory(groupId);
    }

    @Bean
    @Autowired
    public ConcurrentKafkaListenerContainerFactory<Long, TransportableReplication> listenerContainerFactorySyncPerson(
            @Qualifier("consumerFactorySyncPerson") final ConsumerFactory<Long, TransportableReplication> consumerFactory
    ) {
        return createListenerContainerFactory(consumerFactory);
    }

    private ConsumerFactory<Long, TransportableReplication> createConsumerFactory(final String groupId) {
        final Map<String, Object> configsByNames = Map.of(
                BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress,
                GROUP_ID_CONFIG, groupId
//                KEY_DESERIALIZER_CLASS_CONFIG, LongDeserializer.class,
//                VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class
        );
        return new DefaultKafkaConsumerFactory<>(configsByNames, new LongDeserializer(), new JsonDeserializer<>(TransportableReplication.class));
    }

    private static ConcurrentKafkaListenerContainerFactory<Long, TransportableReplication> createListenerContainerFactory(
            final ConsumerFactory<Long, TransportableReplication> consumerFactory
    ) {
        final ConcurrentKafkaListenerContainerFactory<Long, TransportableReplication> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
        return factory;
    }
}
