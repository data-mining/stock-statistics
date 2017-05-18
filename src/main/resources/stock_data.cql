//Create keyspace
CREATE KEYSPACE IF NOT EXISTS StockData WITH replication = {'class':'SimpleStrategy', 'replication_factor':1};

CREATE KEYSPACE IF NOT EXISTS StockDataTest WITH replication = {'class':'SimpleStrategy', 'replication_factor':1};

//Create table
CREATE TABLE quote(first_instrument_id bigint, first_instrument_isin text, first_instrument_settl_currency_id bigint,
first_instrument_settl_currency_short_name text, quote_settlement_type text, quote_operation_type text, price4one double,
instrument_quantity bigint, quote_timestamp timestamp, PRIMARY KEY((first_instrument_id, first_instrument_settl_currency_id), quote_timestamp))
WITH CLUSTERING ORDER BY (quote_timestamp DESC);

CREATE TABLE trade(instrument_id bigint, currency_id bigint,
 price4one double, trade_timestamp timestamp, PRIMARY KEY((instrument_id, currency_id), trade_timestamp))
WITH CLUSTERING ORDER BY (trade_timestamp DESC);