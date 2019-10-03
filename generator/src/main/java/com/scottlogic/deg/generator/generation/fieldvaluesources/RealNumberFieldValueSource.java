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

import com.scottlogic.deg.common.util.NumberUtils;
import com.scottlogic.deg.generator.restrictions.linear.LinearRestrictions;
import com.scottlogic.deg.generator.utils.RandomNumberGenerator;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static com.scottlogic.deg.generator.utils.SetUtils.stream;

public class RealNumberFieldValueSource implements FieldValueSource<BigDecimal> {
    private final Set<BigDecimal> blacklist;
    private final LinearRestrictions<BigDecimal> restrictions;

    public RealNumberFieldValueSource(LinearRestrictions<BigDecimal> restrictions, Set<Object> blacklist) {
        this.restrictions = restrictions;

        this.blacklist = blacklist.stream()
            .map(NumberUtils::coerceToBigDecimal)
            .filter(Objects::nonNull)
            .map(i -> restrictions.getGranularity().trimToGranularity(i))
            .filter(i -> restrictions.getMin().compareTo(i) <= 0 && i.compareTo(restrictions.getMax()) <= 0)
            .collect(Collectors.toSet());
    }

    @Override
    public Stream<BigDecimal> generateInterestingValues() {
        return Stream.of(restrictions.getMin(), BigDecimal.ZERO, restrictions.getMax())
            .distinct()
            .filter(restrictions::match)
            .filter(this::notInBlacklist);
    }

    @Override
    public Stream<BigDecimal> generateAllValues() {
        return stream(new LinearIterator<>(restrictions))
            .filter(this::notInBlacklist);
    }

    @Override
    public Stream<BigDecimal> generateRandomValues(RandomNumberGenerator randomNumberGenerator) {
        return Stream.generate(() ->
            randomNumberGenerator.nextBigDecimal(
                restrictions.getMin(),
                restrictions.getMax()))
            .map(val -> restrictions.getGranularity().trimToGranularity(val))
            .filter(this::notInBlacklist);
    }

    private boolean notInBlacklist(BigDecimal i) {
        return blacklist.stream().noneMatch(x->x.compareTo(i)==0);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        RealNumberFieldValueSource otherSource = (RealNumberFieldValueSource) obj;
        return Objects.equals(restrictions, otherSource.restrictions) && Objects.equals(blacklist, otherSource.blacklist);
    }

    @Override
    public int hashCode() {
        return Objects.hash(restrictions, blacklist);
    }

}