package com.stockdata.integration.spark;

import com.datastax.spark.connector.japi.CassandraJavaUtil;
import com.datastax.spark.connector.japi.rdd.CassandraJavaRDD;
import com.stockdata.model.Quote;
import com.stockdata.repository.QuoteRepository;
import com.stockdata.type.PriceStatistics;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import scala.Tuple2;

import java.io.FileReader;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collection;
import java.util.Date;
import java.util.Properties;

import static com.datastax.spark.connector.japi.CassandraJavaUtil.mapRowTo;

/**
 * Created by mandarinka on 13.05.17.
 */

@Service
public class SparkWorker {

    //private SparkConf sparkConf;

    @Value("${spark.app.name}")
    private String sparkAppName;

    @Value("${spark.master}")
    private String sparkMaster;

    @Value("${spring.data.cassandra.contact-points}")
    private String cassandraHost;

    @Value("${spring.data.cassandra.keyspace-name}")
    private String cassandraKeyspace;

    @Autowired
    private QuoteRepository quoteRepository;

    static Function2<Tuple2<Long, Long>, Tuple2<Long, Long>, Tuple2<Long, Long>> functionReduceForPrice = new Function2<Tuple2<Long, Long>, Tuple2<Long, Long>, Tuple2<Long, Long>>() {
        @Override
        public Tuple2<Long, Long> call(Tuple2<Long, Long> priceAndOne1, Tuple2<Long, Long> priceAndOne2) throws Exception {
            return new Tuple2<Long, Long>(priceAndOne1._1() + priceAndOne2._1(), priceAndOne1._2() + priceAndOne2._2());
        }
    };

    public SparkWorker() {

        //Проблема при разворачивании с адресами
//        FileReader reader;
//        Properties props = new Properties();
//        try {
//            System.out.println("befor prop");
//            reader = new FileReader("src/main/resources/application.properties");
//            if(reader==null)
//                System.out.println("red = null");
//            props.load(reader);
//            sparkConf = new SparkConf()
//                    .setAppName(props.getProperty("spark.app.name"))
//                    .set("spark.cassandra.connection.host", props.getProperty("spring.data.cassandra.contact-points"))
//                    .setMaster(props.getProperty("spark.master"));
//            if(sparkConf==null)
//                System.out.println();
//            reader.close();
//        } catch (IOException e) {
//        }
    }

    public Collection<PriceStatistics> averagePriceCalculation(int amountHour) {

//
        SparkConf sparkConf = new SparkConf()
                .setAppName(sparkAppName)
                .set("spark.cassandra.connection.host", cassandraHost)
                .setMaster(sparkMaster);
//

        JavaSparkContext sparkContext = new JavaSparkContext(sparkConf);

        LocalDateTime dateTime = LocalDateTime.now().minusHours(amountHour);//устанвка времени, начиная с которого считываются записи
        Instant dateTimeInstant = dateTime.atZone(ZoneId.systemDefault()).toInstant();

        CassandraJavaRDD<Quote> quoteRecords = CassandraJavaUtil.javaFunctions(sparkContext)
                .cassandraTable(cassandraKeyspace, "quote", mapRowTo(Quote.class))
                .where("quote_timestamp > ?", Date.from(dateTimeInstant));


        JavaPairRDD<Tuple2<Long, Long>, Long> result = quoteRecords
                .mapToPair(quote -> new Tuple2<Tuple2<Long, Long>, Tuple2<Long, Long>>
                        (new Tuple2<Long, Long>(quote.getFirstInstrumentId(), quote.getFirstInstrumentSettlCurrencyId()), new Tuple2<Long, Long>(quote.getPrice4one(), new Long(1))))
                .reduceByKey(functionReduceForPrice)
                .mapToPair(record -> new Tuple2<Tuple2<Long, Long>, Long>(record._1(), record._2()._1() / record._2()._2()));


        JavaRDD<PriceStatistics> priceStatisticsRDD = result.map(record -> new PriceStatistics(record._1()._1(), record._1()._2(), record._2(), new Date().getTime(), amountHour));

        Collection<PriceStatistics> priceStatisticsCollection = priceStatisticsRDD.collect();

        sparkContext.close();
        return priceStatisticsCollection;
    }

    public void initializeStorage() {
        quoteRepository.save(new Quote(new Long(1), new Long(123), "ISIN", new Long(999), "USD", "Settl", "BUY", new Long(100), new Long(100), new Date()));
        quoteRepository.save(new Quote(new Long(1), new Long(123), "ISIN", new Long(999), "USD", "Settl", "SELL", new Long(120), new Long(200), new Date()));
        quoteRepository.save(new Quote(new Long(1), new Long(345), "ISIN", new Long(786), "DRM", "Settl", "BUY", new Long(133), new Long(120), new Date()));
        ;
    }
}
