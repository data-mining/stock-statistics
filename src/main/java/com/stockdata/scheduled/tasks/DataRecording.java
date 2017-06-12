package com.stockdata.scheduled.tasks;

import com.stockdata.integration.kafka.KafkaWorker;
import com.stockdata.model.TradeEntity;
import com.stockdata.repository.TradeRepository;
import org.joda.time.DateTime;
import org.joda.time.DateTimeComparator;
import org.joda.time.DateTimeFieldType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.cassandra.core.CassandraOperations;
import org.springframework.scheduling.support.CronSequenceGenerator;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by mandarinka on 09.05.17.
 */

@Service
public class DataRecording implements Task {

    @Value("${cron.kafka}")
    private String cronKafka;

    @Value("${kafka.enable}")
    private Boolean kafkaEnable;

    @Autowired
    private KafkaWorker kafkaWorker;

    @Autowired
    private CassandraOperations cassandraTemplate;

    @Override
    public void execute() {
        if(kafkaEnable) {
            Collection<TradeEntity> collectionFromKafka = kafkaWorker.subscribe();
            if(collectionFromKafka!=null && collectionFromKafka.size()!=0) {
                cassandraTemplate.insert(collectionFromKafka);
            }
        }
    }

    @Override
    public boolean isAppliable() {
        CronSequenceGenerator generator = new CronSequenceGenerator(cronKafka);
        DateTime dateTime = new DateTime();
        DateTime cronTime = new DateTime(generator.next(dateTime.minusSeconds(1).toDate()));
        DateTimeComparator comparator = DateTimeComparator.getInstance(DateTimeFieldType.secondOfMinute());
        return comparator.compare(dateTime, cronTime) == 0;
    }
}
