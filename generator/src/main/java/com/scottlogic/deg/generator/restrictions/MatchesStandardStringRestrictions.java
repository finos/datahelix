/*
 * Copyright 2019 Scott Logic Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
            StringGenerator combinedGenerator = getCombinedGenerator(textualRestrictions);

            if (!combinedGenerator.generateAllValues().iterator().hasNext()){
                return MergeResult.unsuccessful();
            }

            return new MergeResult<>(copyWithGenerator(combinedGenerator));
        }

        if (!(other instanceof MatchesStandardStringRestrictions)){
            return MergeResult.unsuccessful();
        }

        MatchesStandardStringRestrictions that = (MatchesStandardStringRestrictions) other;
        if (that.type != type) {
            return MergeResult.unsuccessful();
        }

        return that.negated == negated
            ? new MergeResult<>(this)
            : MergeResult.unsuccessful();
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
