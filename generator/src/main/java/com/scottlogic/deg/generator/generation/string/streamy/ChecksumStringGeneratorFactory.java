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
package com.scottlogic.deg.generator.generation.string.streamy;

import com.scottlogic.deg.generator.utils.FinancialCodeUtils;

public class ChecksumStringGeneratorFactory {

    public static StreamStringGenerator createSedolGenerator() {
        return new ChecksumStreamStringGenerator(
            new RegexStreamStringGenerator("[B-DF-HJ-NP-TV-Z0-9]{6}"),
            FinancialCodeUtils::calculateSedolCheckDigit);
    }

    public static StreamStringGenerator createCusipGenerator() {
        return new ChecksumStreamStringGenerator(
            new RegexStreamStringGenerator("[0-9]{3}[0-9A-Z]{5}"),
            FinancialCodeUtils::calculateCusipCheckDigit);
    }

    public static StreamStringGenerator createIsinGenerator() {
        return new ChecksumStreamStringGenerator(
            new ChecksumlessIsinGenerator(),
            FinancialCodeUtils::calculateIsinCheckDigit);
    }
}
