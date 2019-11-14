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
package com.scottlogic.deg.generator.generation.string.generators;

import com.scottlogic.datahelix.generator.common.utils.FinancialCodeUtils;

public class ChecksumStringGeneratorFactory {

    public static StringGenerator createSedolGenerator() {
        return new ChecksumStringGenerator(
            new RegexStringGenerator("[B-DF-HJ-NP-TV-Z0-9]{6}", true),
            FinancialCodeUtils::calculateSedolCheckDigit);
    }

    public static StringGenerator createCusipGenerator() {
        return new ChecksumStringGenerator(
            new RegexStringGenerator("[0-9]{3}[0-9A-Z]{5}", true),
            FinancialCodeUtils::calculateCusipCheckDigit);
    }

    public static StringGenerator createIsinGenerator() {
        return new ChecksumStringGenerator(
            new ChecksumlessIsinGenerator(),
            FinancialCodeUtils::calculateIsinCheckDigit);
    }
}
