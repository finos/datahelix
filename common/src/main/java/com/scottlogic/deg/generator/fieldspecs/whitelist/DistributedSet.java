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

import com.scottlogic.deg.generator.utils.RandomNumberGenerator;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.Stream;

public class DistributedSet<T> {

    private static final DistributedSet<?> EMPTY = new DistributedSet<>(Collections.emptySet());

    private final Set<WeightedElement<T>> underlyingWeights;

    private final List<WeightedElement<T>> underlyingCumulativeWeights;

    public DistributedSet(final Set<WeightedElement<T>> underlyingWeights) {
        if (underlyingWeights.contains(null)) {
            throw new IllegalArgumentException("DistributedSet should not contain null elements");
        }

        Set<WeightedElement<T>> normalisedWeights = normalise(underlyingWeights);
        this.underlyingWeights = Collections.unmodifiableSet(normalisedWeights);
        this.underlyingCumulativeWeights = cumulative(normalisedWeights);
    }

    private static <T> Set<WeightedElement<T>> normalise(final Set<WeightedElement<T>> denormalised) {
        final double total = denormalised.stream()
            .map(WeightedElement::weight)
            .reduce(0.0D, Double::sum);

        // Stream (even with 0 elements) ensure a copy of the original set is returned
        return denormalised.stream()
            .map(holder -> new WeightedElement<>(holder.element(), holder.weight() / total))
            .collect(Collectors.toSet());
    }

    public static <T> DistributedSet<T> singleton(final T element) {
        return DistributedSet.uniform(Collections.singleton(element));
    }

    public static <T> DistributedSet<T> uniform(final Collection<T> underlyingSet) {
        return new DistributedSet<>(
            underlyingSet.stream()
                .map(WeightedElement::withDefaultWeight)
                .collect(Collectors.toSet()));
    }

    private static <T> List<WeightedElement<T>> cumulative(Set<WeightedElement<T>> nonCumulative) {
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
    public static <T> DistributedSet<T> empty() {
        return (DistributedSet<T>) EMPTY;
    }

    public Set<WeightedElement<T>> distributedSet() {
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
        DistributedSet<?> that = (DistributedSet<?>) o;
        return Objects.equals(underlyingWeights, that.underlyingWeights);
    }

    @Override
    public int hashCode() {
        return Objects.hash(underlyingWeights);
    }

    @Override
    public String toString() {
        return "DistributedSet{" +
            "underlyingWeights=" + underlyingWeights +
            '}';
    }

    public Stream<T> stream() {
        return distributedSet().stream().map(WeightedElement::element);
    }

    public Set<T> set() {
        return stream().collect(Collectors.toSet());
    }

    public boolean isEmpty(){
        return distributedSet().isEmpty();
    }
}
