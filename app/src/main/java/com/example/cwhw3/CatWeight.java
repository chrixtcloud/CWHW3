package com.example.cwhw3;

import java.io.Serializable;

public class CatWeight implements Serializable {
    private String metric;
    private String imperial;

    public String getMetric ()
    {
        return metric;
    }

    public String getImperial ()
    {
        return imperial;
    }

}