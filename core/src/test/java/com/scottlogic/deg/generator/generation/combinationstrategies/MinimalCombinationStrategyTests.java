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

package com.scottlogic.deg.generator.generation.combinationstrategies;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.stream.Stream;

import static com.scottlogic.deg.generator.generation.combinationstrategies.CombinationStrategyTester.bag;

class MinimalCombinationStrategyTests {
    private CombinationStrategyTester tester;

    @BeforeEach
    void beforeEach() {
        tester = new CombinationStrategyTester(new MinimalCombinationStrategy());
    }

    @Test
    void shouldCombineMinimally() {
        tester.given(
            Stream.of(bag("A"), bag("B"), bag("C")),
            Stream.of(bag("1"), bag("2"), bag("3")));

        tester.expect(
            Stream.of(bag("A","1"), bag("B","2"), bag("C","3")));
    }

    @Test
    void shouldCombineSequencesOfDifferentLengths() {
        tester.given(
            Stream.of(bag("X")),
            Stream.of(bag("A"), bag("B"), bag("C")),
            Stream.of(bag("1"), bag("2"), bag("3"), bag("4"), bag("5")));

        tester.expect(
            Stream.of(
                bag("X", "A", "1"),
                bag("X", "B", "2"),
                bag("X", "C", "3"),
                bag("X", "C", "4"),
                bag("X", "C", "5")));
    }

    @Test
    void shouldGiveInputForSingleSequence() {
        tester.given(Stream.of(bag("A"), bag("B"), bag("C")));

        tester.expect(Stream.of(bag("A"), bag("B"), bag("C")));
    }

    @Test
    void shouldGiveNoResultsForSingleEmptySequence() {
        tester.given(
            Stream.of(bag("A"), bag("B"), bag("C")),
            Stream.of());

        tester.expectEmpty();
    }
}
