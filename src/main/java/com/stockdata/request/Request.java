package com.stockdata.request;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;

import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Created by mandarinka on 13.05.17.
 */
public class Request {

    private HttpMethod httpMethod;
    private String url;
    private String username;
    private String password;
    private String json;

    public enum HttpMethod {
        POST,
        GET
    }

    public Request() {
    }

    public Request(HttpMethod httpMethod, String url, String username, String password, String json) {
        this.httpMethod = httpMethod;
        this.url = url;
        this.username = username;
        this.password = password;
        this.json = json;
    }

    public Request(HttpMethod httpMethod, String host, String port) {
        this.httpMethod = httpMethod;
        this.url = "http://" + host + ":" + port;
    }

    public Request(HttpMethod httpMethod, String host, String port, LinkedHashMap<String, String> parametrs) throws Exception {
        this.httpMethod = httpMethod;
        String u = "";
        String uri = url.substring(u.length(), url.length());
        URIBuilder builder = new URIBuilder();
        builder.setScheme("http").setHost(host).setPort(Integer.valueOf(port)).setPath(uri);

        if (parametrs != null && parametrs.size() != 0)
            for (HashMap.Entry value : parametrs.entrySet()) {
                builder.setParameter(value.getKey().toString(), value.getValue().toString());
            }
        String buildURL = builder.build().toString();
        this.url = buildURL;
    }

    public HttpMethod getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(HttpMethod httpMethod) {
        this.httpMethod = httpMethod;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }

    public static HttpRequestBase makeRequest(Request request) throws Exception {
        HttpRequestBase requestBase;
        StringEntity entity = null;
        if (request.getJson() != null && request.getJson().length() != 0) {
            entity = new StringEntity(request.getJson());
        }

        switch (request.getHttpMethod()) {
            case GET:
                requestBase = new HttpGet(request.getUrl());
                break;
            case POST:
                requestBase = new HttpPost(request.getUrl());
                ((HttpPost) requestBase).setEntity(entity);
                break;
            default:
                throw new IllegalArgumentException();
        }

        requestBase.addHeader("Content-Type", "application/json");
        String authString = request.getUsername() + ":" + request.getPassword();
        String encoding = Base64.encode(authString.getBytes());
        requestBase.addHeader("Authorization", "Basic " + encoding);

        return requestBase;
    }


}

