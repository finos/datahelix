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

import com.scottlogic.deg.common.util.Defaults;
import com.scottlogic.deg.common.util.NumberUtils;
import com.scottlogic.deg.generator.restrictions.linear.Limit;
import com.scottlogic.deg.generator.restrictions.linear.LinearRestrictions;
import com.scottlogic.deg.generator.utils.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static com.scottlogic.deg.generator.utils.SetUtils.stream;

public class RealNumberFieldValueSource implements FieldValueSource {
    private final BigDecimal inclusiveUpperLimit;
    private final BigDecimal inclusiveLowerLimit;
    private final Set<BigDecimal> blacklist;
    private final static BigDecimal exclusivityAdjuster = BigDecimal.valueOf(Double.MIN_VALUE);

    private final LinearRestrictions<BigDecimal> restrictions;

    public RealNumberFieldValueSource(LinearRestrictions<BigDecimal> restrictions, Set<Object> blacklist) {
        this.restrictions = restrictions;

        Limit<BigDecimal> lowerLimit = getLowerLimit(restrictions);

        this.inclusiveLowerLimit =
            lowerLimit.isInclusive()
                ? lowerLimit.getValue()
                : restrictions.getGranularity().getNext(lowerLimit.getValue());

        Limit<BigDecimal> upperLimit = getUpperLimit(restrictions);

        this.inclusiveUpperLimit =
            upperLimit.isInclusive()
                ? upperLimit.getValue()
                : restrictions.getGranularity().trimToGranularity(
                    upperLimit.getValue().subtract(exclusivityAdjuster));

        this.blacklist = blacklist.stream()
            .map(NumberUtils::coerceToBigDecimal)
            .filter(Objects::nonNull)
            .map(i -> restrictions.getGranularity().trimToGranularity(i))
            .filter(i -> this.inclusiveLowerLimit.compareTo(i) <= 0 && i.compareTo(this.inclusiveUpperLimit) <= 0)
            .collect(Collectors.toSet());
    }

    private Limit<BigDecimal> getUpperLimit(LinearRestrictions<BigDecimal> restrictions) {
        BigDecimal maxValue = Defaults.NUMERIC_MAX;
        if (restrictions.getMax() == null) {
            return new Limit<>(maxValue, true);
        }

        // Returns the smaller of the two maximum restrictions
        return new Limit<>(maxValue.min(restrictions.getMax().getValue()), restrictions.getMax().isInclusive());
    }

    private Limit<BigDecimal> getLowerLimit(LinearRestrictions<BigDecimal> restrictions) {
        BigDecimal minValue = Defaults.NUMERIC_MIN;
        if (restrictions.getMin() == null) {
            return new Limit<>(minValue, true);
        }

        // Returns the larger of the two minimum restrictions
        return new Limit<>(minValue.max(restrictions.getMin().getValue()), restrictions.getMin().isInclusive());
    }

    @Override
    public Stream<Object> generateInterestingValues() {
        List<BigDecimal> list = new ArrayList<>();
        list.add(inclusiveLowerLimit);
        if (restrictions.getMin().isBefore(BigDecimal.ZERO)){
            list.add(restrictions.getGranularity().trimToGranularity(new BigDecimal(0)));
        }
        list.add(inclusiveUpperLimit);

        return list.stream()
            .distinct()
            .filter(this::notInBlacklist)
            .map(Function.identity());
    }

    @Override
    public Stream<Object> generateAllValues() {
        return stream(new LinearIterator<>(restrictions))
            .filter(this::notInBlacklist)
            .map(Function.identity());
    }

    @Override
    public Stream<Object> generateRandomValues(RandomNumberGenerator randomNumberGenerator) {
        return Stream.generate(() ->
            randomNumberGenerator.nextBigDecimal(
                inclusiveLowerLimit,
                inclusiveUpperLimit))
            .map(val -> restrictions.getGranularity().trimToGranularity(val))
            .filter(this::notInBlacklist)
            .map(Function.identity());
    }


    private boolean notInBlacklist(BigDecimal i) {
        return blacklist.stream().noneMatch(x->x.compareTo(i)==0);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        RealNumberFieldValueSource otherSource = (RealNumberFieldValueSource) obj;
        return inclusiveUpperLimit.equals(otherSource.inclusiveUpperLimit) &&
            inclusiveLowerLimit.equals(otherSource.inclusiveLowerLimit) &&
            blacklist.equals(otherSource.blacklist);
    }

    @Override
    public int hashCode() {
        return Objects.hash(inclusiveLowerLimit, inclusiveUpperLimit, blacklist);
    }

    private Stream<Object> streamOf(Iterable<Object> iterable){
        return StreamSupport.stream(iterable.spliterator(), false);
    }
}