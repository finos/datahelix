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

import com.scottlogic.deg.common.ValidationException;
import com.scottlogic.deg.generator.utils.RandomNumberGenerator;

import java.util.stream.Stream;

public class ChecksumStringGenerator implements StringGenerator {

    private final StringGenerator checksumlessGenerator;
    private final ChecksumMaker checksumMaker;

    public ChecksumStringGenerator(StringGenerator checksumlessGenerator, ChecksumMaker checksumMaker) {
        this.checksumlessGenerator = checksumlessGenerator;
        this.checksumMaker = checksumMaker;
    }

    @Override
    public Stream<String> generateAllValues() {
        return checksumlessGenerator.generateAllValues()
            .map(string -> string + checksumMaker.makeChecksum(string));
    }

    @Override
    public Stream<String> generateRandomValues(RandomNumberGenerator randomNumberGenerator) {
        return checksumlessGenerator.generateRandomValues(randomNumberGenerator)
            .map(string -> string + checksumMaker.makeChecksum(string));
    }

    @Override
    public Stream<String> generateInterestingValues() {
        return checksumlessGenerator.generateInterestingValues()
            .map(string -> string + checksumMaker.makeChecksum(string));
    }

    @Override
    public boolean matches(String string) {
        if (string.length() < 1) {
            return false;
        }
        String preChecksumComponent = string.substring(0, string.length() - 1);
        Character checksumComponent = string.charAt(string.length() - 1);
        return
            checksumlessGenerator.matches(preChecksumComponent) &&
            checksumMaker.makeChecksum(preChecksumComponent).equals(checksumComponent);
    }

    @Override
    public StringGenerator intersect(StringGenerator stringGenerator) {
        throw new ValidationException("These constraints cannot be combined.");
    }
}
