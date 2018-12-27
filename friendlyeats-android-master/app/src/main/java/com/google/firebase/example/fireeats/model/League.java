package com.google.firebase.example.fireeats.model;

import com.google.firebase.firestore.IgnoreExtraProperties;

@IgnoreExtraProperties
public class League {
    private int pandas_id;
    private String name;
    private String image;
    private String rssLink;
    private int match_count;
    private int status;

    public League() {

    }

    public int getPandas_id() {
        return pandas_id;
    }

    public void setPandas_id(int pandas_id) {
        this.pandas_id = pandas_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getRssLink() {
        return rssLink;
    }

    public void setRssLink(String rssLink) {
        this.rssLink = rssLink;
    }

    public int getMatch_count() {
        return match_count;
    }

    public void setMatch_count(int match_count) {
        this.match_count = match_count;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
