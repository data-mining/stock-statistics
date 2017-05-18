package com.stockdata.model;

import org.springframework.cassandra.core.PrimaryKeyType;
import org.springframework.data.cassandra.mapping.Column;
import org.springframework.data.cassandra.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.mapping.Table;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by mandarinka on 11.05.17.
 */

@Table
public class Trade implements Serializable {

    @PrimaryKeyColumn(name = "instrument_id", ordinal = 0,type = PrimaryKeyType.PARTITIONED)
    private Long instrumentId;

    @PrimaryKeyColumn(name = "currency_id", ordinal = 1,type = PrimaryKeyType.PARTITIONED)
    private Long currencyId;

    @Column(value = "price4one")
    private Double price4one;

    @PrimaryKeyColumn(name = "trade_timestamp", type = PrimaryKeyType.CLUSTERED)
    private Date tradeTimestamp;

    public Trade() {
    }

    public Trade(Long instrumentId, Long currencyId, Double price4one, Date tradeTimestamp) {
        this.instrumentId = instrumentId;
        this.currencyId = currencyId;
        this.price4one = price4one;
        this.tradeTimestamp = tradeTimestamp;
    }

    public Long getInstrumentId() {
        return instrumentId;
    }

    public void setInstrumentId(Long instrumentId) {
        this.instrumentId = instrumentId;
    }

    public Long getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(Long currencyId) {
        this.currencyId = currencyId;
    }

    public Double getPrice4one() {
        return price4one;
    }

    public void setPrice4one(Double price4one) {
        this.price4one = price4one;
    }

    public Date getTradeTimestamp() {
        return tradeTimestamp;
    }

    public void setTradeTimestamp(Date tradeTimestamp) {
        this.tradeTimestamp = tradeTimestamp;
    }
}
