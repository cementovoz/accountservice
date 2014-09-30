package com.bitbucket.pelenthium.accountservice.client;


import com.bitbucket.pelenthium.accountservice.client.data.Statistic;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.parser.JSONParser;

import java.util.Map;

/**
 * Created by cementovoz on 23.08.14.
 */
public class AccountConector {

    public static final Logger LOG = LogManager.getLogger(AccountConector.class);

    private static final String BASE_URL = "http://localhost:8080/service/";

    public Long getAmount (int id) throws Exception {
        String content = Request.get(BASE_URL + "json/" + String.valueOf(id)).execute().getContent();
        return Long.valueOf(content.trim());
    }

    public void addAmount (int id, long amount) throws Exception {
        Request.put(BASE_URL + "json/" + String.valueOf(id))
                .param("amount", amount)
                .execute().getContent();
    }

    public Statistic getStatistic () throws Exception {
        String content = Request.get(BASE_URL + "stats/").execute().getContent();
        JSONParser parser = new JSONParser();
        Map data = (Map) parser.parse(content);
        return new Statistic(data);
    }

    public Statistic clearStatistic () throws Exception {
        String content = Request.get(BASE_URL + "stats/clear").execute().getContent();
        JSONParser parser = new JSONParser();
        Map data = (Map) parser.parse(content);
        return new Statistic(data);
    }

}
