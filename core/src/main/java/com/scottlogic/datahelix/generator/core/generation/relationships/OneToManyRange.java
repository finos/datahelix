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

package com.scottlogic.datahelix.generator.core.generation.relationships;

public class OneToManyRange {
    private final int min;
    private final Integer max;

    public OneToManyRange(int min, Integer max) {
        this.min = min;
        this.max = max;
    }

    public int getMin() {
        return min;
    }

    public Integer getMax() {
        return max;
    }

    public OneToManyRange withMin(int min) {
        if (min > this.min) {
            return new OneToManyRange(min, max);
        }

        return this;
    }

    public OneToManyRange withMax(int max) {
        if (this.max == null || max < this.max) {
            return new OneToManyRange(min, max);
        }

        return this;
    }

    public boolean isEmpty() {
        return this.max != null && (this.min > this.max || (this.min == 0 && this.max == 0));
    }

    @Override
    public String toString() {
        if (max == null) {
            return min + "..";
        }

        return min + ".." + max;
    }
}
