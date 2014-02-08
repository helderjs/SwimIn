package com.ufba.swimin.model;

import java.util.Date;

public class Athlete extends Person {
    protected float weight;
    protected float height;

    public Athlete() {
        super();

        this.type = 2;
    }

    public Athlete(String name, Date birthday, float weight, float height) {
        super(name, birthday);

        this.weight = weight;
        this.height = height;
        this.type = 2;
    }

    public Athlete(long id, String name, Date birthday, float weight, float height) {
        super(id, name, birthday);

        this.weight = weight;
        this.height = height;
        this.type = 2;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }
}
