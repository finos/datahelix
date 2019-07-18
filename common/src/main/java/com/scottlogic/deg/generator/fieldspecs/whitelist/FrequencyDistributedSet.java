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

package com.scottlogic.deg.generator.fieldspecs.whitelist;

import java.util.*;
import java.util.stream.Collectors;

public class FrequencyDistributedSet<T> implements DistributedSet<T> {

    private static final FrequencyDistributedSet<?> EMPTY = new FrequencyDistributedSet<>(Collections.emptySet());

    private final Set<WeightedElement<T>> underlyingWeights;

    private List<WeightedElement<T>> underlyingCumulativeWeights;

    public FrequencyDistributedSet(final Set<WeightedElement<T>> underlyingWeights) {
        if (underlyingWeights.isEmpty()) {
            this.underlyingWeights = underlyingWeights;
        } else {
            if (underlyingWeights.contains(null)) {
                throw new IllegalArgumentException("DistributedSet should not contain null elements");
            }

            double total = total(underlyingWeights);

            this.underlyingWeights = underlyingWeights.stream()
                .map(holder -> new WeightedElement<>(holder.element(), holder.weight() / total))
                .collect(Collectors.toSet());
        }
    }

    public static <T> double total(final Set<WeightedElement<T>> set) {
        return set.stream()
            .map(WeightedElement::weight)
            .reduce(0.0D, Double::sum);
    }

    public static <T> FrequencyDistributedSet<T> uniform(final Set<T> underlyingSet) {
        return new FrequencyDistributedSet<>(
            underlyingSet.stream()
                .map(e -> new WeightedElement<T>(e, 1.0D))
                .collect(Collectors.toSet()));
    }

    private List<WeightedElement<T>> cumulative() {
        if (underlyingCumulativeWeights == null) {
            underlyingCumulativeWeights = generateCumulative(underlyingWeights);
        }
        return underlyingCumulativeWeights;
    }

    private static <T> List<WeightedElement<T>> generateCumulative(Set<WeightedElement<T>> nonCumulative) {
        List<WeightedElement<T>> cumulative = new LinkedList<>();
        double runningTotal = 0.0D;
        for (WeightedElement<T> holder : nonCumulative) {
            runningTotal += holder.weight();
            cumulative.add(new WeightedElement<>(holder.element(), runningTotal));
        }

        if (!cumulative.isEmpty()) {
            int lastIndex = cumulative.size() - 1;
            WeightedElement<T> last = cumulative.get(lastIndex);
            cumulative.remove(lastIndex);
            cumulative.add(new WeightedElement<>(last.element(), 1.0D));
        }

        return new ArrayList<>(cumulative);
    }

    @SuppressWarnings("unchecked")
    public static <T> FrequencyDistributedSet<T> empty() {
        return (FrequencyDistributedSet<T>) EMPTY;
    }

    @Override
    public Set<WeightedElement<T>> distributedSet() {
        return new HashSet<>(underlyingWeights);
    }

    @Override
    public T pick(double random) {
        List<WeightedElement<T>> cumulativeWeights = cumulative();

        int index = Collections.binarySearch(cumulativeWeights,
            new WeightedElement<>(null, 1.0D - random),
            Comparator.comparingDouble(WeightedElement::weight));
        if (index < 0) {
            index = -(index) - 1;
        }

        return cumulativeWeights.get(index).element();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FrequencyDistributedSet<?> that = (FrequencyDistributedSet<?>) o;
        return Objects.equals(underlyingWeights, that.underlyingWeights);
    }

    @Override
    public int hashCode() {
        return Objects.hash(underlyingWeights);
    }

    @Override
    public String toString() {
        return "FrequencyDistributedSet{" +
            "underlyingWeights=" + underlyingWeights +
            '}';
    }
}
