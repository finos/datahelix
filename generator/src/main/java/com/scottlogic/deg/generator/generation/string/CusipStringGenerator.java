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

import static com.scottlogic.deg.generator.generation.string.streamy.ChecksumStringGeneratorFactory.createCusipGenerator;

public class CusipStringGenerator extends ChecksummedCodeStringGenerator {
    public final static int CUSIP_LENGTH = 9;
    public final static String STANDARD_REGEX_REPRESENTATION = "[0-9]{3}[0-9A-Z]{5}[0-9]";

    public CusipStringGenerator() {
        super(STANDARD_REGEX_REPRESENTATION, CUSIP_LENGTH, 0);
    }

    public CusipStringGenerator(String prefix, String suffix, RegexStringGenerator additionalRestrictions) {
        super(
            prefix + STANDARD_REGEX_REPRESENTATION + suffix,
            additionalRestrictions,
            CUSIP_LENGTH,
            prefix.length()
        );
    }

    private CusipStringGenerator(RegexStringGenerator generator) {
        super(generator, false, CUSIP_LENGTH, 0);
    }

    private CusipStringGenerator(StringGenerator cusipGenerator, boolean negate) {
        super(cusipGenerator, negate, CUSIP_LENGTH, 0);
    }

    @Override
    public char calculateCheckDigit(String str) {
        return FinancialCodeUtils.calculateCusipCheckDigit(
            str.substring(prefixLength, CUSIP_LENGTH + prefixLength - 1)
        );
    }

    @Override
    public int getLength() {
        return CUSIP_LENGTH;
    }

    @Override
    public StringGenerator complement() {
        return new CusipStringGenerator(regexGenerator, !negate);
    }

    @Override
    public boolean match(String subject) {
        boolean matches = FinancialCodeUtils.isValidCusipNsin(subject);
        return matches != negate;
    }

    @Override
    ChecksummedCodeStringGenerator instantiate(RegexStringGenerator generator) {
        return new CusipStringGenerator(generator);
    }

    @Override
    public FieldValueSource asFieldValueSource(){
        return new StreamStringGeneratorAsFieldValueSource(createCusipGenerator());
    }
}
