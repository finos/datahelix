package com.scottlogic.deg.generator.generation.combination_strategies;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.stream.Stream;

import static com.scottlogic.deg.generator.generation.combination_strategies.CombinationStrategyTester.bag;

class PinningCombinationStrategyTests {
    private CombinationStrategyTester tester;

    @BeforeEach
    void beforeEach() {
        tester = new CombinationStrategyTester(new PinningCombinationStrategy());
    }

    @Test
    void shouldCombineUsingPinning() {
        tester.given(
            Stream.of(bag("A"), bag("B"), bag("C")),
            Stream.of(bag("1"), bag("2"), bag("3")));

        tester.expect(
            Stream.of(
                bag("A","1"),
                bag("A","2"),
                bag("A","3"),
                bag("B","1"),
                bag("C","1")));
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
                bag("X", "B", "1"),
                bag("X", "C", "1"),
                bag("X", "A", "2"),
                bag("X", "A", "3"),
                bag("X", "A", "4"),
                bag("X", "A", "5")));
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
