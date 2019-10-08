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

import com.scottlogic.deg.common.ValidationException;
import com.scottlogic.deg.generator.profile.constraints.atomic.StandardConstraintTypes;
import com.scottlogic.deg.generator.generation.string.generators.NoStringsStringGenerator;
import com.scottlogic.deg.generator.generation.string.generators.RegexStringGenerator;
import com.scottlogic.deg.generator.generation.string.generators.StringGenerator;

import java.util.Optional;

import static com.scottlogic.deg.generator.generation.string.generators.ChecksumStringGeneratorFactory.*;

/**
 * Represents the restriction of a field to an `aValid` operator
 * Holds the type of the value that is required and whether the field has been negated
 */
public class MatchesStandardStringRestrictions implements StringRestrictions{
    private final StandardConstraintTypes type;
    private StringGenerator generator;

    public MatchesStandardStringRestrictions(StandardConstraintTypes type) {
        this.type = type;
    }

    @Override
    public boolean match(String x) {
        return createGenerator().matches(x);
    }

    public StringGenerator createGenerator() {
        if (generator == null) {
            switch (type) {
                case ISIN:
                    generator = createIsinGenerator();
                    break;
                case SEDOL:
                    generator = createSedolGenerator();
                    break;
                case CUSIP:
                    generator = createCusipGenerator();
                    break;
                default:
                    throw new UnsupportedOperationException(String.format("Unable to create string generator for: %s", type));
            }
        }
        return generator;
    }

    @Override
    public Optional<StringRestrictions> intersect(StringRestrictions other) {

        if (other instanceof TextualRestrictions){
            return isLengthAcceptable((TextualRestrictions) other);
        }

        MatchesStandardStringRestrictions that = (MatchesStandardStringRestrictions) other;
        if (that.type != type) {
            return Optional.empty();
        }

        return Optional.of(this);
    }

    private Optional<StringRestrictions> isLengthAcceptable(TextualRestrictions other) {
        if (anyRegexes(other)){
            throw new ValidationException("Combining a regex constraint with a " + this.toString() + " constraint is not supported.");
        }

        StringGenerator intersect = other.createGenerator().intersect(new RegexStringGenerator(type.getRegex(), true));

        if (intersect instanceof NoStringsStringGenerator){
            return Optional.empty();
        }

        return Optional.of(this);
    }

    private boolean anyRegexes(TextualRestrictions other) {
        return !other.containingRegex.isEmpty() || !other.matchingRegex.isEmpty() || !other.notContainingRegex.isEmpty() || !other.notMatchingRegex.isEmpty();
    }

    @Override
    public String toString() {
        return type.name();
    }
}
