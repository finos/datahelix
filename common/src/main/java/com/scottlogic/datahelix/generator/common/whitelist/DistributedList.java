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

package com.scottlogic.datahelix.generator.common.whitelist;

import com.scottlogic.datahelix.generator.common.utils.RandomNumberGenerator;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DistributedList<T> {

    private static final DistributedList<?> EMPTY = new DistributedList<>(Collections.emptyList());

    private final List<WeightedElement<T>> underlyingWeights;

    private final List<WeightedElement<T>> underlyingCumulativeWeights;

    public DistributedList(final List<WeightedElement<T>> underlyingWeights) {
        if (underlyingWeights.contains(null)) {
            throw new IllegalArgumentException("DistributedSet should not contain null elements");
        }

        List<WeightedElement<T>> normalisedWeights = normalise(underlyingWeights);
        this.underlyingWeights = Collections.unmodifiableList(normalisedWeights);
        this.underlyingCumulativeWeights = cumulative(normalisedWeights);
    }

    private static <T> List<WeightedElement<T>> normalise(final List<WeightedElement<T>> denormalised) {
        final double total = denormalised.stream()
            .map(WeightedElement::weight)
            .reduce(0.0D, Double::sum);

        // Stream (even with 0 elements) ensure a copy of the original set is returned
        return denormalised.stream()
            .map(holder -> new WeightedElement<>(holder.element(), holder.weight() / total))
            .collect(Collectors.toList());
    }

    public static <T> DistributedList<T> singleton(final T element) {
        return DistributedList.uniform(Collections.singleton(element));
    }

    public static <T> DistributedList<T> uniform(final Collection<T> underlyingSet) {
        return new DistributedList<>(
            underlyingSet.stream()
                .map(WeightedElement::withDefaultWeight)
                .collect(Collectors.toList()));
    }

    private static <T> List<WeightedElement<T>> cumulative(List<WeightedElement<T>> nonCumulative) {
        List<WeightedElement<T>> cumulative = new LinkedList<>();
        double runningTotal = 0.0D;
        for (WeightedElement<T> holder : nonCumulative) {
            runningTotal += holder.weight();
            cumulative.add(new WeightedElement<>(holder.element(), runningTotal));
        }

        if (!cumulative.isEmpty()) {
           replaceLastCumulativeElement(cumulative);
        }

        return new ArrayList<>(cumulative);
    }

    private static <T> void replaceLastCumulativeElement(List<WeightedElement<T>> cumulative) {
        int lastIndex = cumulative.size() - 1;
        WeightedElement<T> last = cumulative.get(lastIndex);
        cumulative.remove(lastIndex);
        cumulative.add(new WeightedElement<>(last.element(), 1.0D));
    }

    @SuppressWarnings("unchecked")
    public static <T> DistributedList<T> empty() {
        return (DistributedList<T>) EMPTY;
    }

    public List<WeightedElement<T>> distributedList() {
        return underlyingWeights;
    }

    public T pickRandomly(RandomNumberGenerator random) {
        return getElementFromCumulativeDistribution(1.0D - random.nextDouble(0.0D, 1.0D));
    }

    private T getElementFromCumulativeDistribution(final double value) {
        List<Double> weights = underlyingCumulativeWeights.stream()
            .map(WeightedElement::weight)
            .collect(Collectors.toList());

        final int index = binarySearch(weights, value);

        return underlyingCumulativeWeights.get(index).element();
    }

    private static int binarySearch(List<Double> weights, double target) {
        final int index = Collections.binarySearch(weights, target, Double::compare);

        // We need to resolve the index.
        // A positive index represents an exact match
        // A negative index represents an inexact match
        // See Collections.binarySearch javadoc for more information
        if (index < 0) {
            return (-index) - 1;
        } else {
            return index;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DistributedList<?> that = (DistributedList<?>) o;
        return Objects.equals(underlyingWeights, that.underlyingWeights);
    }

    @Override
    public int hashCode() {
        return Objects.hash(underlyingWeights);
    }

    @Override
    public String toString() {
        return list().toString();
    }

    public Stream<T> stream() {
        return distributedList().stream().map(WeightedElement::element);
    }

    public List<T> list() {
        return stream().collect(Collectors.toList());
    }

    public boolean isEmpty(){
        return distributedList().isEmpty();
    }
}
