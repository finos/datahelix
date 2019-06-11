package com.scottlogic.deg.generator.restrictions;

import com.scottlogic.deg.common.profile.constraints.atomic.StandardConstraintTypes;
import com.scottlogic.deg.generator.generation.*;

/**
 * Represents the restriction of a field to an `aValid` operator
 * Holds the type of the value that is required and whether the field has been negated
 */
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
            case CUSIP:
                return new CusipStringGenerator();
        }

        throw new UnsupportedOperationException(String.format("Unable to create string generator for: %s", type));
    }

    /**
     * Will combine/intersect another StringRestrictions within this instance
     *
     * Rules are:
     * Return this instance within modification if the other restrictions matches the following:
     *   1. It is an equivalent MatchesStandardStringRestrictions instance (all properties match)
     *   2. It is a TextualRestrictions that has:
     *     2.1. no regex restrictions of any kind
     *     2.2. any present length restrictions do not impact the ability to create any value
     *
     * @param other The other restrictions to combine/intersect
     * @return Either this instance (success) or a NoStringsPossibleStringRestrictions if the other restrictions could not be merged or intersected
     */
    @Override
    public MergeResult<StringRestrictions> intersect(StringRestrictions other) {
        if (other instanceof TextualRestrictions){
            TextualRestrictions textualRestrictions = (TextualRestrictions) other;
            if (getImpactOnValueProduction(textualRestrictions) == Impact.NONE){
                return new MergeResult<>(this); //no impact on values produced by this type
            }

            //must not be reported as contradictory, yet at least
            return new MergeResult<>(
                new NoStringsPossibleStringRestrictions("Cannot merge textual constraints with aValid constraints"));
        }

        if (!(other instanceof MatchesStandardStringRestrictions)){
            return new MergeResult<>(
                new NoStringsPossibleStringRestrictions(
                    String.format("Intersection of %s and aValid constraints", other.getClass().getName())));
        }

        MatchesStandardStringRestrictions that = (MatchesStandardStringRestrictions) other;
        if (that.type != type) {
            return new MergeResult<>(
                new NoStringsPossibleStringRestrictions(
                    String.format("Intersection of aValid %s and aValid %s)", type, that.type)));
        }

        return that.negated == negated
            ? new MergeResult<>(this)
            : new MergeResult<>(
                new NoStringsPossibleStringRestrictions(
                    String.format("Intersection of aValid %s and not(aValid %s)", type, type)));
    }

    /**
     * Calculate if the given TextualRestrictions could impact the ability to create some or all values
     * If there are ANY regex statements then they COULD affect the ability to create values (therefore yield true)
     * Get the length of the codes that would be produced, e.g. an ISIN is 12 characters.
     * Check if any length restrictions exist that would prevent strings of this length being produced.
     *
     * @param textualRestrictions The other restrictions type to check
     */
    private Impact getImpactOnValueProduction(TextualRestrictions textualRestrictions) {
        boolean hasRegexRestrictions = !textualRestrictions.containingRegex.isEmpty()
            || !textualRestrictions.matchingRegex.isEmpty()
            || !textualRestrictions.notMatchingRegex.isEmpty()
            || !textualRestrictions.notContainingRegex.isEmpty();

        if (hasRegexRestrictions){
            return Impact.POTENTIAL; //because we dont know they wouldn't affect the values - see #487
        }

        int maxLength = textualRestrictions.maxLength != null ? textualRestrictions.maxLength : Integer.MAX_VALUE;
        int minLength = textualRestrictions.minLength != null ? textualRestrictions.minLength : 0;
        int codeLength = getCodeLength(type);

        return (codeLength < minLength || codeLength > maxLength || textualRestrictions.excludedLengths.contains(codeLength))
            ? Impact.CONFIRMED
            : Impact.NONE;
    }

    private enum Impact
    {
        /**
         * There is the potential for some impact, but it cannot be confirmed or denied
         */
        POTENTIAL,

        /**
         * There is no impact on the production of values
         */
        NONE,

        /**
         * There is a confirmed impact on the generation of values
         */
        CONFIRMED
    }

    private int getCodeLength(StandardConstraintTypes type) {
        switch (type)
        {
            case ISIN:
                return IsinStringGenerator.ISIN_LENGTH;
            case SEDOL:
                return SedolStringGenerator.SEDOL_LENGTH;
            case CUSIP:
                return CusipStringGenerator.CUSIP_LENGTH;
            default:
                throw new UnsupportedOperationException(String.format("Unable to check string restrictions for: %s", type));
        }
    }

    @Override
    public String toString() {
        if (negated){
            return "not " + type.name();
        }
        return type.name();
    }
}
