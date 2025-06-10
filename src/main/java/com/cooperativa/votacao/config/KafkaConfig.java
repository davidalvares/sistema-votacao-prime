package com.cooperativa.votacao.config;

import com.cooperativa.votacao.application.dto.VoteMessage;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.*;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
//@Profile("!dev")
public class KafkaConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Bean
    public NewTopic votacaoTopic() {
        return TopicBuilder.name("votacao-topic")
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic votacaoVotesTopic() {
        return TopicBuilder.name("votacao-votes")
                .partitions(3)  // Multiple partitions for better scalability
                .replicas(1)
                .build();
    }

    @Bean
    public ProducerFactory<String, VoteMessage> voteProducerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public ConsumerFactory<String, VoteMessage> voteConsumerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configProps.put(ConsumerConfig.GROUP_ID_CONFIG, "votacao-processor-group");
        configProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        configProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        configProps.put(JsonDeserializer.TRUSTED_PACKAGES, "com.cooperativa.votacao.application.dto");
        configProps.put(JsonDeserializer.USE_TYPE_INFO_HEADERS, false);
        configProps.put(JsonDeserializer.VALUE_DEFAULT_TYPE, "com.cooperativa.votacao.application.dto.VoteMessage");
        return new DefaultKafkaConsumerFactory<>(configProps);
    }


    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, VoteMessage> voteKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, VoteMessage> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(voteConsumerFactory());
        return factory;
    }

    @Bean
    public KafkaTemplate<String, VoteMessage> voteKafkaTemplate() {
        return new KafkaTemplate<>(voteProducerFactory());
    }
} 