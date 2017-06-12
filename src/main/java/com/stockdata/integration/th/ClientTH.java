package com.stockdata.integration.th;

import com.google.gson.Gson;
import com.stockdata.request.Request;
import com.stockdata.type.PriceStatistics;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collection;

/**
 * Created by mandarinka on 13.05.17.
 */

@Service
public class ClientTH {

    @Value("${th.enable}")
    private Boolean thEnable;

    @Value("${th.host}")
    private String thHost;

    @Value("${th.port}")
    private String thPort;


    public void sendMessage(Collection<PriceStatistics> priceStatisticses) throws Exception {
        if (thEnable) {
            Request request = new Request(Request.HttpMethod.POST, thHost, thPort);
            request.setUrl(request.getUrl() + "/api/stockStatistics/price");
            request.setUsername("admin");
            request.setPassword("Operations1!");

            HttpClient httpClient = HttpClientBuilder.create().build();

            Gson gson = new Gson();

            request.setJson(gson.toJson(priceStatisticses));

            httpClient.execute(Request.makeRequest(request));

            //Успешность доставки не отслеживается
        }
    }
}
