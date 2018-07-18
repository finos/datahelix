package com.scottlogic.deg.schemas.v2.trends;

import java.util.HashMap;
import java.util.Map;

public class EnumMemberPrevalenceTrend extends Trend {
    public Map<String, Number> prevalences;

    public EnumMemberPrevalenceTrend()
    {
        super(TrendTypes.EnumMemberPrevalence);

        this.prevalences = new HashMap<String, Number>();
    }
}
