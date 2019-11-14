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

package com.scottlogic.datahelix.generator.core.generation.fieldvaluesources;

import com.scottlogic.datahelix.generator.core.utils.RandomNumberGenerator;

import java.util.Set;
import java.util.stream.Stream;

public class BooleanFieldValueSource implements FieldValueSource<Boolean>
{
    private final Set<Boolean> blacklist;

    public BooleanFieldValueSource(Set<Boolean> blacklist) {
        this.blacklist = blacklist;
    }

    @Override
    public Stream<Boolean> generateAllValues() {
        return Stream.of(true, false).filter(this::notInBlacklist);
    }

    @Override
    public Stream<Boolean> generateInterestingValues() {
        return generateAllValues();
    }

    @Override
    public Stream<Boolean> generateRandomValues(RandomNumberGenerator randomNumberGenerator) {
        return Stream.generate(() -> randomNumberGenerator.nextInt() % 2 == 0).filter(this::notInBlacklist);
    }

    private boolean notInBlacklist(Boolean b) {
        return blacklist.stream().noneMatch(x -> x.equals(b));
    }
}
