package com.scottlogic.deg.generator.restrictions;

import com.scottlogic.deg.common.profile.constraints.atomic.StandardConstraintTypes;
import com.scottlogic.deg.generator.generation.string.CusipStringGenerator;
import com.scottlogic.deg.generator.generation.string.IsinStringGenerator;
import com.scottlogic.deg.generator.generation.string.SedolStringGenerator;
import com.scottlogic.deg.generator.generation.string.StringGenerator;

/**
 * Represents the restriction of a field to an `aValid` operator
 * Holds the type of the value that is required and whether the field has been negated
 */
public class MatchesStandardStringRestrictions implements StringRestrictions{
    private final StandardConstraintTypes type;
    private final boolean negated;
    private StringGenerator generator;

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
        if (generator == null) {
            switch (type) {
                case ISIN:
                    generator = new IsinStringGenerator();
                    break;
                case SEDOL:
                    generator = new SedolStringGenerator();
                    break;
                case CUSIP:
                    generator = new CusipStringGenerator();
                    break;
                default:
                    throw new UnsupportedOperationException(String.format("Unable to create string generator for: %s", type));
            }
        }
        return generator;
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
            Impact impact = getImpactOnValueProduction(textualRestrictions);
            if (impact == Impact.NONE) {
                return new MergeResult<>(this); //no impact on values produced by this type
            }
            if (impact == Impact.PARTIAL) {
                return new MergeResult<>(copyWithGenerator(getCombinedGenerator(textualRestrictions)));
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
     * If there are regex statements, determine if the intersection of those regexes with the regex for
     * a valid string can produce any values or not.
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

        if (hasRegexRestrictions) {
            StringGenerator ourGenerator = getStringGenerator();
            StringGenerator combinedGenerator =
                ourGenerator.intersect(textualRestrictions.createGenerator());
            if (combinedGenerator.isFinite() && combinedGenerator.getValueCount() == 0) {
                return Impact.FULL;
            }
            if (combinedGenerator.getValueCount() < ourGenerator.getValueCount()) {
                return Impact.PARTIAL;
            }
            return Impact.NONE;
        }

        int maxLength = textualRestrictions.maxLength != null ? textualRestrictions.maxLength : Integer.MAX_VALUE;
        int minLength = textualRestrictions.minLength != null ? textualRestrictions.minLength : 0;
        int codeLength = getCodeLength(type);

        return (codeLength < minLength || codeLength > maxLength || textualRestrictions.excludedLengths.contains(codeLength))
            ? Impact.FULL
            : Impact.NONE;
    }

    private StringGenerator getCombinedGenerator(TextualRestrictions textualRestrictions) {
        StringGenerator ourGenerator = getStringGenerator();
        return ourGenerator.intersect(textualRestrictions.createGenerator());
    }

    private MatchesStandardStringRestrictions copyWithGenerator(StringGenerator generator) {
        MatchesStandardStringRestrictions newRestrictions =
            new MatchesStandardStringRestrictions(type, negated);
        newRestrictions.generator = generator;
        return newRestrictions;
    }

    private enum Impact
    {
        /**
         * There is definitely a partial impact on value production, but its level is
         * currently unknown.
         */
        PARTIAL,

        /**
         * There is potentially no impact on the production of values
         */
        NONE,

        /**
         * The impact is such that values definitely cannot be produced.
         */
        FULL
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
