package com.scottlogic.deg.generator.restrictions;

import com.scottlogic.deg.generator.generation.fieldvaluesources.datetime.Timescale;

import java.util.Optional;

public class ParsedDateGranularity {
    private final Timescale granularity;

    public ParsedDateGranularity(Timescale granularity) {
        this.granularity = granularity;
    }

    public static ParsedDateGranularity parse(String granularityExpression) {
        return new ParsedDateGranularity(Timescale.getByName(granularityExpression));
    }

    public static Optional<ParsedDateGranularity> tryParse(String granularityExpression){
        try{
            ParsedDateGranularity parsedGranularityToReturn = parse(granularityExpression);
            return Optional.of(parsedGranularityToReturn);
        }
        catch(Exception exp){
            return Optional.empty();
        }
    }

    public Timescale getGranularity() {
        return granularity;
    }
}
