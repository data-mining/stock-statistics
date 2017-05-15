package com.stockdata.integration.kafka;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stockdata.model.Quote;
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

    public Quote kafkaRecordToModel(ConsumerRecord<String, String> consumerRecord){
        ObjectMapper mapper = new ObjectMapper();
        String json = consumerRecord.value();

        Map<String, Object> map = new HashMap<String, Object>();
        try {
            map = mapper.readValue(json, new TypeReference<Map<String, Object>>() {
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        Quote quote = new Quote(
                new Long(1), //стоит вопрос с primary key
                new Long(map.get("firstInstrumentId").toString()),
                map.get("firstInstrumentISIN").toString(),
                new Long(map.get("firstInstrumentSettlCurrencyId").toString()),
                map.get("firstInstrumentSettlCurrencyShortName").toString(),
                map.get("quoteSettlementType").toString(),
                map.get("quoteOperationType").toString(),
                new Long(map.get("price4One").toString()),
                new Long(map.get("instrumentQuantity").toString()),
                new Date());
        return quote;
    }

    public Collection<Quote> kafkaRecordsToCollectionModel(ConsumerRecords<String, String> consumerRecords){
        Collection<Quote> collectionQuote = new ArrayList<>();
        for (ConsumerRecord<String,String> c: consumerRecords) {
            collectionQuote.add(kafkaRecordToModel(c));
        }
        return collectionQuote;
    }

}
