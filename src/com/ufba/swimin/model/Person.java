package com.ufba.swimin.model;

import java.util.Date;

abstract public class Person {
    protected long id;
    protected String name;
    protected Date birthday;
    protected int type;

    public Person() {}

    public Person(String name, Date birthday) {
        this.name = name;
        this.birthday = birthday;
    }

    public Person(long id, String name, Date birthday) {
        this.id = id;
        this.name = name;
        this.birthday = birthday;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public int getType() {
        return type;
    }
}
