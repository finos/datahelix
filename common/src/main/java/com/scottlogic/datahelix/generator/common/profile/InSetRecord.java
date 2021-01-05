/*
 * Copyright 2019-2021 Scott Logic Ltd
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
package com.scottlogic.datahelix.generator.common.profile;

import java.util.Objects;
import java.util.function.Function;

public class InSetRecord {
    private static final double DEFAULT_WEIGHT = 1.0;
    private final Object element;
    private final Double weight;

    public InSetRecord(Object element) {
        this(element, null);
    }

    public InSetRecord(Object element, double weight) {
        this(element, (Double)weight);
    }

    public InSetRecord(Object element, Double weight) {
        this.element = element;
        this.weight = weight;
    }

    public Object getElement() {
        return element;
    }

    public boolean hasWeightPresent() {
        return weight != null;
    }

    public double getWeightValueOrDefault() {
        return weight != null ? weight : DEFAULT_WEIGHT;
    }

    public InSetRecord mapValue(Function<Object, Object> parse) {
        Object value = parse.apply(element);

        return weight != null
            ? new InSetRecord(value, weight)
            : new InSetRecord(value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InSetRecord that = (InSetRecord) o;

        if (!Objects.equals(element, that.element)) {
            return false;
        }
        return weight == null || that.weight == null
            ? Objects.equals(weight, that.weight)
            : Double.compare(weight, that.weight) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(element, weight);
    }

    @Override
    public String toString() {
        return "InSetRecord{" +
            "element=" + element +
            ", weight=" + weight +
            '}';
    }
}
