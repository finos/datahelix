package com.scottlogic.deg.generator.restrictions;

import com.scottlogic.deg.generator.constraints.atomic.StandardConstraintTypes;
import com.scottlogic.deg.generator.generation.IsinStringGenerator;
import com.scottlogic.deg.generator.generation.SedolStringGenerator;
import com.scottlogic.deg.generator.generation.StringGenerator;

public class MatchesStandardStringRestrictions implements StringRestrictions{
    private final StandardConstraintTypes type;
    private final boolean negated;

    public MatchesStandardStringRestrictions(StandardConstraintTypes type, boolean negated) {
        this.type = type;
        this.negated = negated;
    }

    @Override
    public boolean match(String x) {
        return createGenerator().match(x);
    }

    public StringGenerator createGenerator() {
        StringGenerator generator = getStringGenerator();

        return negated
            ? generator.complement()
            : generator;
    }

    private StringGenerator getStringGenerator() {
        switch (type){
            case ISIN:
                return new IsinStringGenerator();
            case SEDOL:
                return new SedolStringGenerator();
        }

        throw new UnsupportedOperationException(String.format("Unable to create string generator for: %s", type));
    }

    @Override
    public StringRestrictions intersect(StringRestrictions other) {
        if (other instanceof TextualRestrictions){
            return other.intersect(this);
        }

        if (!(other instanceof MatchesStandardStringRestrictions)){
            return new NoStringsPossibleStringRestrictions(String.format("Intersection of %s and aValid constraints", other.getClass().getName()));
        }

        MatchesStandardStringRestrictions that = (MatchesStandardStringRestrictions) other;
        if (that.type != type) {
            return new NoStringsPossibleStringRestrictions(String.format("Intersection of aValid %s and aValid %s)", type, that.type));
        }

        return that.negated == negated
            ? this
            : new NoStringsPossibleStringRestrictions(String.format("Intersection of aValid %s and not(aValid %s)", type, type));
    }
}
