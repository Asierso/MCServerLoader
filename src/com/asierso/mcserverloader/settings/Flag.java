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

    public final String getKey() {
        return key;
    }

    public final void setKey(String key) {
        this.key = key;
    }

    public final Object getValue() {
        return value;
    }

    public final void setValue(Object value) {
        this.value = value;
    }
}
