package com.scottlogic.deg.schemas.v2.trends;

public class NormalDistributionTrend extends Trend {
    public Number meanAvg;
    public Number stdDev;
    public Number min;
    public Number max;

    public NormalDistributionTrend()
    {
        super(TrendTypes.NormalDistribution);
    }
}
