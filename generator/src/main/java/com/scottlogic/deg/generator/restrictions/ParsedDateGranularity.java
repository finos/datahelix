package com.scottlogic.deg.generator.restrictions;

import com.scottlogic.deg.generator.generation.fieldvaluesources.datetime.Timescale;

public class ParsedDateGranularity {
    private final Timescale granularity;

    public ParsedDateGranularity(Timescale granularity) {
        this.granularity = granularity;
    }

    public static ParsedDateGranularity parse(String granularityExpression) {
        return new ParsedDateGranularity(Timescale.getByName(granularityExpression));
    }

    public Timescale getGranularity() {
        return granularity;
    }
}
