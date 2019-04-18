package com.scottlogic.deg.generator.restrictions;

import com.scottlogic.deg.generator.generation.fieldvaluesources.datetime.Timescale;
import com.scottlogic.deg.generator.utils.NumberUtils;

import java.math.BigDecimal;
import java.sql.Time;

/**
 * Granularity expressions could be interpreted differently depending on other constraints on a field (eg, type constraints),
 * so we store all possible parsings in this class, ready to make a GranularityRestrictions object
 */
public class ParsedDateGranularity {
    private final Timescale granularity;

    public ParsedDateGranularity(Timescale granularity) {
        this.granularity = granularity;
    }

    public static ParsedDateGranularity parse(String granularityExpression) {
        return new ParsedDateGranularity(Timescale.valueOf(granularityExpression));
    }

    public Timescale getGranularity() {
        return granularity;
    }
}
