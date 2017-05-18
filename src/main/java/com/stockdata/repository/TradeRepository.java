package com.stockdata.repository;

import com.stockdata.model.Trade;
import org.springframework.data.cassandra.repository.CassandraRepository;

/**
 * Created by mandarinka on 11.05.17.
 */
public interface TradeRepository extends CassandraRepository<Trade> {
}

