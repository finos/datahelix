package com.scottlogic.deg.schemas.v2.trends;

public class NullPrevalenceTrend extends Trend {
    public Number nullPrevalence;

    protected NullPrevalenceTrend() {
        super(TrendTypes.NullPrevalenceTrend);
    }
}
