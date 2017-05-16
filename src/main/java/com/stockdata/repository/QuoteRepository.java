package com.stockdata.repository;

import com.stockdata.model.Quote;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;

import java.util.List;

/**
 * Created by mandarinka on 11.05.17.
 */
public interface QuoteRepository extends CassandraRepository<Quote> {
}

