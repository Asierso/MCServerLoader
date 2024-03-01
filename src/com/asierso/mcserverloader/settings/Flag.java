package com.asierso.mcserverloader.settings;

public class Flag {
    public String key;
    public Object value;

    public Flag(String key,Object value) {
        this.key = key;
        this.value = value;
    }

    public Flag(){

    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
