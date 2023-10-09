package com.backend.resources;

import org.json.JSONObject;

public class Portfolio {
    int id;
    String portfolioName;

    public int getId() {
        return id;
    }

    public String getPortfolioName() {
        return portfolioName;
    }

    public JSONObject toJson() {
        JSONObject jo = new JSONObject();
        jo.put("id", id);
        jo.put("portfolioName", portfolioName);
        return jo;
//        return "{\"id\" : " + id + ", \"portfolioName\": " + portfolioName + "}";
    }
}
