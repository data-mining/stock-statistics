package com.stockdata.integration.spark;

import com.datastax.spark.connector.japi.CassandraJavaUtil;
import com.datastax.spark.connector.japi.rdd.CassandraJavaRDD;
import com.stockdata.model.TradeEntity;
import com.stockdata.repository.TradeRepository;
import com.stockdata.type.PriceStatistics;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.cassandra.core.CassandraOperations;
import org.springframework.data.cassandra.core.CassandraTemplate;
import org.springframework.stereotype.Service;
import scala.Tuple2;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

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
    private TradeRepository tradeRepository;

    @Autowired
    private CassandraOperations cassandraTemplate;

    static Function2<Tuple2<Double, Double>, Tuple2<Double, Double>, Tuple2<Double, Double>> functionReduceForPrice = new Function2<Tuple2<Double, Double>, Tuple2<Double, Double>, Tuple2<Double, Double>>() {
        @Override
        public Tuple2<Double, Double> call(Tuple2<Double, Double> priceAndOne1, Tuple2<Double, Double> priceAndOne2) throws Exception {
            return new Tuple2<Double, Double>(priceAndOne1._1() + priceAndOne2._1(), priceAndOne1._2() + priceAndOne2._2());
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

        SparkConf sparkConf = new SparkConf()
                .setAppName(sparkAppName)
                .set("spark.cassandra.connection.host", cassandraHost)
                .setMaster(sparkMaster);

        JavaSparkContext sparkContext = new JavaSparkContext(sparkConf);

        LocalDateTime basicDateTime = LocalDateTime.now().withMinute(0).withSecond(0).withNano(0);
        Instant basicDateTimeInstant = basicDateTime.atZone(ZoneId.systemDefault()).toInstant();

        LocalDateTime startDateTime = basicDateTime.minusHours(amountHour);//устанвка времени, начиная с которого считываются записи
        Instant startDateTimeInstant = startDateTime.atZone(ZoneId.systemDefault()).toInstant();

        CassandraJavaRDD<TradeEntity> quoteRecords = CassandraJavaUtil.javaFunctions(sparkContext)
                .cassandraTable(cassandraKeyspace, "trade", mapRowTo(TradeEntity.class))
                .where("trade_timestamp >= ?", Date.from(startDateTimeInstant))
//                .where("quote_timestamp < ?", Date.from(basicDateTimeInstant))
                ;

        JavaPairRDD<Tuple2<Long, Long>, Double> result = quoteRecords
                .mapToPair(quote -> new Tuple2<Tuple2<Long, Long>, Tuple2<Double, Double>>
                        (new Tuple2<Long, Long>(quote.getInstrumentId(), quote.getCurrencyId()), new Tuple2<Double, Double>(quote.getPrice4one(), new Double(1))))
                .reduceByKey(functionReduceForPrice)
                .mapToPair(record -> new Tuple2<Tuple2<Long, Long>, Double>(record._1(), record._2()._1() / record._2()._2()));

        JavaRDD<PriceStatistics> priceStatisticsRDD = result.map(record -> new PriceStatistics(record._1()._1(), record._1()._2(), record._2(), basicDateTimeInstant.toEpochMilli(), amountHour));

        Collection<PriceStatistics> priceStatisticsCollection = priceStatisticsRDD.collect();

        sparkContext.close();
        return priceStatisticsCollection;
    }

    public void initializeStorage() {

        List<TradeEntity> tradeEntityCollection = new ArrayList<>();
        tradeEntityCollection.add(new TradeEntity(new Long(288), new Long(1), new Long(100).doubleValue(), new Date()));
        tradeEntityCollection.add(new TradeEntity(new Long(399),  new Long(1), new Long(12013).doubleValue(), new Date()));
        tradeEntityCollection.add(new TradeEntity(new Long(2), new Long(1), new Long(100).doubleValue(), new Date()));
        tradeEntityCollection.add(new TradeEntity(new Long(3),  new Long(1), new Long(12013).doubleValue(), new Date()));
        tradeEntityCollection.add(new TradeEntity(new Long(2),  new Long(2), new Long(13398).doubleValue(),  new Date()));
        tradeEntityCollection.add(new TradeEntity(new Long(123), new Long(999), new Long(100).doubleValue(),  new Date()));
        tradeEntityCollection.add(new TradeEntity(new Long(123),  new Long(999), new Long(12013).doubleValue(),  new Date()));
        tradeEntityCollection.add(new TradeEntity(new Long(345),  new Long(786), new Long(13398).doubleValue(), new Date()));

        cassandraTemplate.insert(tradeEntityCollection);
        ;
    }
}
