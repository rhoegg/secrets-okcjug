package org.okcjug;

import java.util.Date;

public class Member {
    public long id;
    public String name;
    public Date firstJug;

    public Member() {
    }

    public Member(long id, String name, Date firstJug) {
        this.id = id;
        this.name = name;
        this.firstJug = firstJug;
    }
}
