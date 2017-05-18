package com.stockdata.integration.kafka;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stockdata.model.Trade;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

/**
 * Created by mandarinka on 13.05.17.
 */

@Service
public class KafkaRecordsConvertor {

    public Trade kafkaRecordToModel(ConsumerRecord<String, String> consumerRecord){
        ObjectMapper mapper = new ObjectMapper();
        String json = consumerRecord.value();

        Map<String, Object> map = new HashMap<String, Object>();
        try {
            map = mapper.readValue(json, new TypeReference<Map<String, Object>>() {
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        Trade trade = new Trade(
                new Long(map.get("firstInstrumentId").toString()),
                new Long(map.get("firstInstrumentSettlCurrencyId").toString()),
                new Long(map.get("price4One").toString()).doubleValue(),
                new Date());
        return trade;
    }

    public Collection<Trade> kafkaRecordsToCollectionModel(ConsumerRecords<String, String> consumerRecords){
        Collection<Trade> collectionTrade = new ArrayList<>();
        for (ConsumerRecord<String,String> c: consumerRecords) {
            collectionTrade.add(kafkaRecordToModel(c));
        }
        return collectionTrade;
    }

}
