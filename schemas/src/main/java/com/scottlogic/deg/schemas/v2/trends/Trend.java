package com.scottlogic.deg.schemas.v2.trends;

public abstract class Trend {
    public final TrendTypes type;

    protected Trend(TrendTypes type)
    {
        this.type = type;
    }
}
