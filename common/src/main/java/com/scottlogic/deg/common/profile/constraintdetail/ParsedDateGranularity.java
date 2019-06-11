package com.scottlogic.deg.common.profile.constraintdetail;

import java.util.Objects;
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

    @Override
    public int hashCode(){
        return Objects.hash(granularity);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (getClass() != o.getClass())
            return false;
        ParsedDateGranularity parsedDateGranularity = (ParsedDateGranularity) o;
        return Objects.equals(granularity, parsedDateGranularity.granularity);
    }



}
