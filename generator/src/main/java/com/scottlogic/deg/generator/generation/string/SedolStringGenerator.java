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

package com.scottlogic.deg.generator.generation.string;

import com.scottlogic.deg.generator.generation.fieldvaluesources.FieldValueSource;
import com.scottlogic.deg.generator.utils.*;

import static com.scottlogic.deg.generator.generation.string.streamy.ChecksumStringGeneratorFactory.createSedolGenerator;

public class SedolStringGenerator extends ChecksummedCodeStringGenerator {
    public final static int SEDOL_LENGTH = 7;
    public final static String STANDARD_REGEX_REPRESENTATION = "[B-DF-HJ-NP-TV-Z0-9]{6}[0-9]";

    public SedolStringGenerator() {
        super(STANDARD_REGEX_REPRESENTATION, SEDOL_LENGTH, 0);
    }

    public SedolStringGenerator(String prefix) { this(prefix, ""); }

    public SedolStringGenerator(String prefix, String suffix) {
        super(prefix + STANDARD_REGEX_REPRESENTATION + suffix, SEDOL_LENGTH, 0);
    }

    public SedolStringGenerator(String prefix, String suffix, RegexStringGenerator additionalRestrictions) {
        super(prefix + STANDARD_REGEX_REPRESENTATION + suffix, additionalRestrictions, SEDOL_LENGTH, prefix.length());
    }

    private SedolStringGenerator(RegexStringGenerator sedolGenerator) {
        super(sedolGenerator, false, SEDOL_LENGTH, 0);
    }

    private SedolStringGenerator(StringGenerator sedolGenerator, boolean negate) {
        super(sedolGenerator, negate, SEDOL_LENGTH, 0);
    }

    @Override
    public char calculateCheckDigit(String str) {
        return FinancialCodeUtils.calculateSedolCheckDigit(
            str.substring(prefixLength, SEDOL_LENGTH + prefixLength - 1)
        );
    }

    @Override
    public int getLength() {
        return SEDOL_LENGTH;
    }

    @Override
    public StringGenerator complement() {
        return new SedolStringGenerator(regexGenerator, !negate);
    }

    @Override
    public boolean match(String subject) {
        boolean matches = FinancialCodeUtils.isValidSedolNsin(subject, prefixLength);
        return matches != negate;
    }

    @Override
    ChecksummedCodeStringGenerator instantiate(RegexStringGenerator generator) {
        return new SedolStringGenerator(generator);
    }

    @Override
    public FieldValueSource asFieldValueSource(){
        return new StreamStringGeneratorAsFieldValueSource(createSedolGenerator());
    }
}
