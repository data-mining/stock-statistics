package com.stockdata.model;

import com.datastax.spark.connector.cql.ClusteringColumn;
import org.springframework.cassandra.core.PrimaryKeyType;
import org.springframework.data.cassandra.mapping.Column;
import org.springframework.data.cassandra.mapping.PrimaryKey;
import org.springframework.data.cassandra.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.mapping.Table;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by mandarinka on 11.05.17.
 */

@Table
public class Quote implements Serializable {

    @PrimaryKeyColumn(name = "first_instrument_id", ordinal = 0,type = PrimaryKeyType.PARTITIONED)
    private Long firstInstrumentId;

    @Column(value = "first_instrument_isin")
    private String firstInstrumentISIN;

    @PrimaryKeyColumn(name = "first_instrument_settl_currency_id", ordinal = 1,type = PrimaryKeyType.PARTITIONED)
    private Long firstInstrumentSettlCurrencyId;

    @Column(value = "first_instrument_settl_currency_short_name")
    private String firstInstrumentSettlCurrencyShortName;

    @Column(value = "quote_settlement_type")
    private String quoteSettlementType;

    @Column(value = "quote_operation_type")
    private String quoteOperationType;

    @Column(value = "price4one")
    private Long price4one;

    @Column(value = "instrument_quantity")
    private Long instrumentQuantity;

    @PrimaryKeyColumn(name = "quote_timestamp", type = PrimaryKeyType.CLUSTERED)
    private Date quoteTimestamp;

    public Quote() {
    }

    public Quote(Long firstInstrumentId, String firstInstrumentISIN, Long firstInstrumentSettlCurrencyId, String firstInstrumentSettlCurrencyShortName, String quoteSettlementType, String quoteOperationType, Long price4one, Long instrumentQuantity, Date quoteTimestamp) {
        this.firstInstrumentId = firstInstrumentId;
        this.firstInstrumentISIN = firstInstrumentISIN;
        this.firstInstrumentSettlCurrencyId = firstInstrumentSettlCurrencyId;
        this.firstInstrumentSettlCurrencyShortName = firstInstrumentSettlCurrencyShortName;
        this.quoteSettlementType = quoteSettlementType;
        this.quoteOperationType = quoteOperationType;
        this.price4one = price4one;
        this.instrumentQuantity = instrumentQuantity;
        this.quoteTimestamp = quoteTimestamp;
    }

    public Long getFirstInstrumentId() {
        return firstInstrumentId;
    }

    public void setFirstInstrumentId(Long firstInstrumentId) {
        this.firstInstrumentId = firstInstrumentId;
    }

    public String getFirstInstrumentISIN() {
        return firstInstrumentISIN;
    }

    public void setFirstInstrumentISIN(String firstInstrumentISIN) {
        this.firstInstrumentISIN = firstInstrumentISIN;
    }

    public Long getFirstInstrumentSettlCurrencyId() {
        return firstInstrumentSettlCurrencyId;
    }

    public void setFirstInstrumentSettlCurrencyId(Long firstInstrumentSettlCurrencyId) {
        this.firstInstrumentSettlCurrencyId = firstInstrumentSettlCurrencyId;
    }

    public String getFirstInstrumentSettlCurrencyShortName() {
        return firstInstrumentSettlCurrencyShortName;
    }

    public void setFirstInstrumentSettlCurrencyShortName(String firstInstrumentSettlCurrencyShortName) {
        this.firstInstrumentSettlCurrencyShortName = firstInstrumentSettlCurrencyShortName;
    }

    public String getQuoteSettlementType() {
        return quoteSettlementType;
    }

    public void setQuoteSettlementType(String quoteSettlementType) {
        this.quoteSettlementType = quoteSettlementType;
    }

    public String getQuoteOperationType() {
        return quoteOperationType;
    }

    public void setQuoteOperationType(String quoteOperationType) {
        this.quoteOperationType = quoteOperationType;
    }

    public Long getPrice4one() {
        return price4one;
    }

    public void setPrice4one(Long price4one) {
        this.price4one = price4one;
    }

    public Long getInstrumentQuantity() {
        return instrumentQuantity;
    }

    public void setInstrumentQuantity(Long instrumentQuantity) {
        this.instrumentQuantity = instrumentQuantity;
    }

    public Date getQuoteTimestamp() {
        return quoteTimestamp;
    }

    public void setQuoteTimestamp(Date quoteTimestamp) {
        this.quoteTimestamp = quoteTimestamp;
    }
}
