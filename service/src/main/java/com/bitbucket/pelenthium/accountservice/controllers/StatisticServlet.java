package com.bitbucket.pelenthium.accountservice.controllers;

import com.bitbucket.pelenthium.accountservice.services.StatisticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by cementovoz on 19.08.14.
 */
@Controller
@RequestMapping(value = "/stats")
public class StatisticServlet {

    @Autowired
    private StatisticService statisticService;

    @RequestMapping(value = "/", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public Map<String, Long> getStat() {
        return new HashMap<String, Long>() {{
            put("countGet", statisticService.getCountGet());
            put("countAdd", statisticService.getCountAdd());
            put("time", statisticService.getTime());
        }};
    }

    @RequestMapping(value = "/clear", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public Map<String, Long> clearStat() {
        statisticService.clearCounter();
        return new HashMap<String, Long>() {{
            put("countGet", statisticService.getCountGet());
            put("countAdd", statisticService.getCountAdd());
            put("time", statisticService.getTime());
        }};
    }
}
