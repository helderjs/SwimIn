package com.ufba.swimin.model;

import java.util.Date;

public class Coach extends Person {
    public Coach() {
        super();

        this.type = 1;
    }

    public Coach(String name, Date birthday) {
        super(name, birthday);

        this.type = 1;
    }

    public Coach(long id, String name, Date birthday) {
        super(id, name, birthday);

        this.type = 1;
    }
}
