package com.stockdata.scheduled.tasks;

import com.stockdata.integration.spark.SparkWorker;
import com.stockdata.integration.th.ClientTH;
import com.stockdata.repository.TradeRepository;
import com.stockdata.type.PriceStatistics;
import org.joda.time.DateTime;
import org.joda.time.DateTimeComparator;
import org.joda.time.DateTimeFieldType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.support.CronSequenceGenerator;
import org.springframework.stereotype.Service;

import java.util.*;


/**
 * Created by mandarinka on 11.05.17.
 */

@Service
public class DataProcessing implements Task {

    @Value("${cron.spark}")
    private String cronSpark;

    @Value("${spark.enable}")
    private Boolean sparkEnable;

    @Autowired
    private TradeRepository tradeRepository;

    @Autowired
    private SparkWorker sparkWorker;

    @Autowired
    private ClientTH clientTH;

    @Override
    public void execute() throws Exception {
        if (sparkEnable) {
            sparkWorker.initializeStorage();
            Collection<PriceStatistics> result = sparkWorker.averagePriceCalculation(1);
            for (PriceStatistics pr : result) {
                System.out.println(" InstrumentId :" + pr.getInstrumentId() + " CurrencyId :" + pr.getCurrencyId() + " Price :" + pr.getPrice());
            }
            clientTH.sendMessage(result);
        }
    }


    @Override
    public boolean isAppliable() {
        CronSequenceGenerator generator = new CronSequenceGenerator(cronSpark);
        DateTime dateTime = new DateTime();
        DateTime cronTime = new DateTime(generator.next(dateTime.minusSeconds(1).toDate()));
        DateTimeComparator comparator = DateTimeComparator.getInstance(DateTimeFieldType.secondOfMinute());
        return comparator.compare(dateTime, cronTime) == 0;
    }
}