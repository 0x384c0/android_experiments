package com.desu.experiments.model.JSONResponseRealm;

import io.realm.RealmObject;

public class Email extends RealmObject {
    private String address;
    private boolean active;

    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isActive() {
        return active;
    }
    public void setActive(boolean active) {
        this.active = active;
    }
}