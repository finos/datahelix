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

package com.scottlogic.deg.generator.generation;

import com.scottlogic.deg.common.profile.Field;

import java.util.Objects;

/**
 * A pair of fields. The equals method does not care about the order.
 *
 */
public final class FieldPair {
    private final Field first;
    private final Field second;

    public FieldPair(Field first, Field second) {
        this.first = first;
        this.second = second;
    }

    public Field first() {
        return first;
    }

    public Field second() {
        return second;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FieldPair fieldPair = (FieldPair) o;
        return bothEquals(fieldPair) || bothEqualsFlipped(fieldPair);
    }

    private boolean bothEquals(FieldPair fieldPair) {
        return Objects.equals(first, fieldPair.first) && Objects.equals(second, fieldPair.second);
    }

    private boolean bothEqualsFlipped(FieldPair fieldPair) {
        return Objects.equals(first, fieldPair.second) && Objects.equals(second, fieldPair.first);
    }

    @Override
    public int hashCode() {
        return Objects.hash(first) + Objects.hash(second);
    }
}
