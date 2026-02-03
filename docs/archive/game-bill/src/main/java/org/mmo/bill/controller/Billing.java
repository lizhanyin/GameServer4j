package org.mmo.bill.controller;

/**
 * 账单
 */
public class Billing {
    private long id;
    private String info;

    public Billing(long id, String info) {
        this.id = id;
        this.info = info;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
