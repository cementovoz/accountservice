package com.bitbucket.pelenthium.accountservice.client;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by cementovoz on 23.08.14.
 */
public class Request {

    private static  Logger LOG = LogManager.getLogger(Request.class);

    private HttpClient client;
    private HttpUriRequest request;
    private HttpResponse response;

    public Request() {
        this.client = HttpClients.createDefault();
    }

    private void setRequest(HttpUriRequest get) {
        this.request = get;
    }

    public static Request get(String url) {
        LOG.debug("GET:url " + url);
        Request request = new Request();
        request.setRequest(new HttpGet(url));
        return request;
    }

    public static Request post (String url) {
        LOG.debug("POST:url " + url);
        Request request = new Request();
        request.setRequest(new HttpPost(url));
        return request;
    }

    public static Request put(String url) {
        LOG.debug("PUT:url " + url);
        Request request = new Request();
        request.setRequest(new HttpPut(url));
        return request;
    }

    public Request param (String name,  Object value) throws UnsupportedEncodingException {
        List<NameValuePair> nvps = new ArrayList<>();
        nvps.add(new BasicNameValuePair(name, value.toString()));
        ((HttpEntityEnclosingRequestBase) request).setEntity(new UrlEncodedFormEntity(nvps));
        return this;
    }

    public Request execute () throws IOException {
        LOG.debug("execute");
        response = client.execute(request);
        return this;
    }

    public String getContent() throws IOException {
        String s = EntityUtils.toString(response.getEntity());
        LOG.debug("load content : " + s);
        return s;
    }
}
