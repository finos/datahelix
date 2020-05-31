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

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class OneToManyRangeTests {
    @Test
    public void isEmpty_whenMinAndMaxAreEqual_returnsFalse(){
        OneToManyRange range = new OneToManyRange(1, 1);

        assertThat(range.isEmpty(), is(false));
    }

    @Test
    public void isEmpty_whenMinIsGreaterThanMax_returnsTrue(){
        OneToManyRange range = new OneToManyRange(2, 1);

        assertThat(range.isEmpty(), is(true));
    }

    @Test
    public void isEmpty_whenMaxIsNull_returnsFalse(){
        OneToManyRange range = new OneToManyRange(1, null);

        assertThat(range.isEmpty(), is(false));
    }

    @Test
    public void isEmpty_whenMinAndMaxAre0_returnsTrue(){
        OneToManyRange range = new OneToManyRange(0, 0);

        assertThat(range.isEmpty(), is(true));
    }

    @Test
    public void withMin_whereValueIsLessThanExistingMin_doesNotRetrictFurther() {
        OneToManyRange range = new OneToManyRange(2, 3);

        OneToManyRange newRange = range.withMin(1);

        assertThat(newRange.getMin(), is(2));
        assertThat(newRange.getMax(), is(3));
    }

    @Test
    public void withMin_whereValueIsEqualToExistingMin_doesNotRetrictFurther() {
        OneToManyRange range = new OneToManyRange(2, 3);

        OneToManyRange newRange = range.withMin(2);

        assertThat(newRange.getMin(), is(2));
        assertThat(newRange.getMax(), is(3));
    }

    @Test
    public void withMin_whereValueIsGreaterThanExistingMin_restrictsRangeToNewValue() {
        OneToManyRange range = new OneToManyRange(2, 3);

        OneToManyRange newRange = range.withMin(3);

        assertThat(newRange.getMin(), is(3));
        assertThat(newRange.getMax(), is(3));
    }

    @Test
    public void withMin_whereValueIsGreaterThanExistingMax_restrictsRangeToEmptyRange() {
        OneToManyRange range = new OneToManyRange(2, 3);

        OneToManyRange newRange = range.withMin(4);

        assertThat(newRange.getMin(), is(4));
        assertThat(newRange.getMax(), is(3));
        assertThat(newRange.isEmpty(), is(true));
    }

    @Test
    public void withMax_whereValueIsGreaterThanExistingMax_doesNotRetrictFurther() {
        OneToManyRange range = new OneToManyRange(2, 3);

        OneToManyRange newRange = range.withMax(4);

        assertThat(newRange.getMin(), is(2));
        assertThat(newRange.getMax(), is(3));
    }

    @Test
    public void withMax_whereValueIsEqualToExistingMax_doesNotRetrictFurther() {
        OneToManyRange range = new OneToManyRange(2, 3);

        OneToManyRange newRange = range.withMax(3);

        assertThat(newRange.getMin(), is(2));
        assertThat(newRange.getMax(), is(3));
    }

    @Test
    public void withMax_whereValueIsLessThanExistingMax_restrictsRangeToNewValue() {
        OneToManyRange range = new OneToManyRange(2, 3);

        OneToManyRange newRange = range.withMax(2);

        assertThat(newRange.getMin(), is(2));
        assertThat(newRange.getMax(), is(2));
    }

    @Test
    public void withMax_whereValueIsLessThanExistingMin_restrictsRangeEmptyRange() {
        OneToManyRange range = new OneToManyRange(2, 3);

        OneToManyRange newRange = range.withMax(1);

        assertThat(newRange.getMin(), is(2));
        assertThat(newRange.getMax(), is(1));
        assertThat(newRange.isEmpty(), is(true));
    }
}
