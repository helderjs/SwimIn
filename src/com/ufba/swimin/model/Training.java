package com.ufba.swimin.model;

import java.util.Date;

public class Training {
    protected Long id;
    protected Date date;
    protected String type;
    protected Long athlete_id;
    protected String distance;
    protected Long time;

    public Training() {}

    public Training(Date date, String type, Long athlete_id, String distance, Long time) {
        this.date = date;
        this.type = type;
        this.athlete_id = athlete_id;
        this.distance = distance;
        this.time = time;
    }

    public Training(long id, Date date, String type, Long athlete_id, String distance, Long time) {
        this.id = id;
        this.date = date;
        this.type = type;
        this.athlete_id = athlete_id;
        this.distance = distance;
        this.time = time;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getAthlete_id() {
        return athlete_id;
    }

    public void setAthlete_id(Long athlete_id) {
        this.athlete_id = athlete_id;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }
}
