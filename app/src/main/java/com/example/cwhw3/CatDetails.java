package com.example.cwhw3;

import java.io.Serializable;

public class CatDetails implements Serializable {
    public String key;
    public String value;

    public CatDetails(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }
}
