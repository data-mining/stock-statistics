package com.stockdata.repository;

import com.stockdata.model.Quote;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;

import java.util.List;

/**
 * Created by mandarinka on 11.05.17.
 */
public interface QuoteRepository extends CassandraRepository<Quote> {

    @Query("Select * from quote where firstInstrumentISIN=?0 ")
    public Quote findByISIN(String ISIN);

    @Query("Select * from quote where quoteOperationType=?0")
    public List<Quote> findByOperationType(String operationType);

    @Query("Select * from quote where id=?0 ")
    public Quote findById(Long id);

}

