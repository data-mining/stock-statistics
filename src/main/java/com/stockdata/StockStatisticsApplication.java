package com.stockdata;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;


@EnableScheduling
@SpringBootApplication
public class StockStatisticsApplication {
    public static void main(String[] args) {
        SpringApplication.run(StockStatisticsApplication.class, args);
    }
}
