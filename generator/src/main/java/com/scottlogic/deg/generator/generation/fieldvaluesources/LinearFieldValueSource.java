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

package com.scottlogic.deg.generator.generation.fieldvaluesources;

import com.scottlogic.deg.generator.restrictions.linear.LinearRestrictions;
import com.scottlogic.deg.generator.utils.RandomNumberGenerator;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.scottlogic.deg.generator.utils.SetUtils.stream;

public class LinearFieldValueSource<T extends Comparable<T>> implements FieldValueSource {

    private final LinearRestrictions<T> restrictions;
    private final Set<T> blacklist;

    public LinearFieldValueSource(LinearRestrictions<T> restrictions, Set<T> blacklist) {
        this.restrictions = restrictions;
        this.blacklist = blacklist.stream()
            .map(i -> restrictions.getGranularity().trimToGranularity(i))
            .collect(Collectors.toSet());
    }

    @Override
    public Stream<T> generateAllValues() {
        return stream(new LinearIterator<>(restrictions))
            .filter(this::notInBlacklist);
    }

    @Override
    public Stream<T> generateInterestingValues() {
        return Stream.of(restrictions.getMin(), restrictions.getMax())
            .distinct()
            .filter(this::notInBlacklist);
    }

    @Override
    public Stream<T> generateRandomValues(RandomNumberGenerator randomNumberGenerator) {
        return Stream.generate(() -> restrictions.getGranularity()
                .getRandom(restrictions.getMin(), restrictions.getMax(), randomNumberGenerator))
            .filter(this::notInBlacklist);
    }


    private boolean notInBlacklist(T t) {
        return blacklist.stream().noneMatch(x->x.compareTo(t)==0);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        LinearFieldValueSource otherSource = (LinearFieldValueSource) obj;
        return restrictions.equals(otherSource.restrictions) &&
            blacklist.equals(otherSource.blacklist);
    }

    @Override
    public int hashCode() {
        return Objects.hash(restrictions, blacklist);
    }
}
