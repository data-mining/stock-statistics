package com.stockdata.type;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.Date;
/**
 * Created by mandarinka on 13.05.17.
 */
public class PriceStatistics implements Serializable {

    private Long instrumentId;

    private Long currencyId;

    private Double price;

    private Long timestamp;

    private Integer amountHours;

    public PriceStatistics() {
    }

    public PriceStatistics(Long instrumentId, Long currencyId, Double price, Long timestamp, Integer amountHours) {
        this.instrumentId = instrumentId;
        this.currencyId = currencyId;
        this.price = price;
        this.timestamp = timestamp;
        this.amountHours = amountHours;
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

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public Integer getAmountHours() {
        return amountHours;
    }

    public void setAmountHours(Integer amountHours) {
        this.amountHours = amountHours;
    }
}
