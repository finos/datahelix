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
            TextualRestrictions textualRestrictions = (TextualRestrictions) other;
            if (!wouldAffectValuesProduced(textualRestrictions)){
                return this; //no impact on values produced by this type
            }

            return new NoStringsPossibleStringRestrictions("Cannot merge textual constraints with aValid constraints");
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

    private boolean wouldAffectValuesProduced(TextualRestrictions textualRestrictions) {
        boolean hasRegexRestrictions = !textualRestrictions.containingRegex.isEmpty()
            || !textualRestrictions.matchingRegex.isEmpty()
            || !textualRestrictions.notMatchingRegex.isEmpty()
            || !textualRestrictions.notContainingRegex.isEmpty();

        if (hasRegexRestrictions){
            return true; //because we dont know they wouldn't affect the values - see #487
        }

        int maxLength = textualRestrictions.maxLength != null ? textualRestrictions.maxLength : Integer.MAX_VALUE;
        int minLength = textualRestrictions.minLength != null ? textualRestrictions.minLength : 0;
        int codeLength = getCodeLength(type);

        return codeLength < minLength || codeLength > maxLength || textualRestrictions.excludedLengths.contains(codeLength);
    }

    private int getCodeLength(StandardConstraintTypes type) {
        switch (type){
            case ISIN:
                return IsinStringGenerator.VALUE_LENGTH;
            case SEDOL:
                return SedolStringGenerator.VALUE_LENGTH;
        }

        throw new UnsupportedOperationException(String.format("Unable to check string restrictions for: %s", type));
    }

    @Override
    public boolean isContradictory() {
        return false;
    }
}
