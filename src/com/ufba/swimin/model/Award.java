package com.ufba.swimin.model;

import java.util.Date;

public class Award {
    protected Long id;
    protected Long athlete_id;
    protected String name;
    protected Integer type;

    public Award() {}

    public Award(Long athlete_id, String name, Integer type) {
        this.athlete_id =athlete_id;
        this.name = name;
        this.type = type;
    }

    public Award(long id, Long athlete_id, String name, Integer type) {
        this.id = id;
        this.athlete_id =athlete_id;
        this.name = name;
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAthlete_id() {
        return athlete_id;
    }

    public void setAthlete_id(Long athlete_id) {
        this.athlete_id = athlete_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
