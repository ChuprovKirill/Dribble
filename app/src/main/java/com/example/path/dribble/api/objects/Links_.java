package com.example.path.dribble.api.objects;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;




public class Links_ implements Serializable {

    @SerializedName("web")
    @Expose
    private String web;
    @SerializedName("twitter")
    @Expose
    private String twitter;

    public String getWeb() {
        return web;
    }

    public void setWeb(String web) {
        this.web = web;
    }

    public String getTwitter() {
        return twitter;
    }

    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }

}
