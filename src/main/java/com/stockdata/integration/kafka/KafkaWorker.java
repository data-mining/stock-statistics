package com.stockdata.integration.kafka;

import com.stockdata.model.TradeEntity;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Properties;

/**
 * Created by mandarinka on 11.05.17.
 */

@Service
public class KafkaWorker {

    @Value("${kafka.topic}")
    private String topic;

    private Properties properties;

    @Autowired
    private KafkaRecordsConvertor kafkaRecordsConvertor;

    public KafkaWorker() throws IOException {

        FileReader reader;
        Properties props = new Properties();
        try {
            reader = new FileReader("src/main/resources/application.properties");
            props.load(reader);

            Properties kafkaProperties = new Properties();
            kafkaProperties.put("bootstrap.servers", props.getProperty("kafka.bootstrap.servers"));
            kafkaProperties.put("group.id", props.getProperty("kafka.group.id"));
            kafkaProperties.put("enable.auto.commit", props.getProperty("kafka.enable.auto.commit"));
            kafkaProperties.put("auto.commit.interval.ms", props.getProperty("kafka.auto.commit.interval.ms"));
            kafkaProperties.put("key.deserializer", props.getProperty("kafka.key.deserializer"));
            kafkaProperties.put("value.deserializer", props.getProperty("kafka.value.deserializer"));
            reader.close();
            properties = kafkaProperties;
        } catch (IOException e) {

        }
    }

    public Collection<TradeEntity> subscribe() {
        ConsumerRecords<String, String> records = null;
        try {

            KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(properties);
            consumer.subscribe(Arrays.asList(topic));
            System.out.println("Subscribed to topic " + topic);

            records = consumer.poll(100);
            consumer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return kafkaRecordsConvertor.kafkaRecordsToCollectionModel(records);
    }
}
