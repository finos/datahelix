package com.scottlogic.deg.generator.generation.combination_strategies;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.scottlogic.deg.generator.generation.combination_strategies.CombinationStrategyTester.bag;
import static com.scottlogic.deg.generator.generation.combination_strategies.CombinationStrategyTester.bagSequence;

class FieldExhaustiveCombinationStrategyTests {
    private CombinationStrategyTester tester;

    @BeforeEach
    void beforeEach() {
        tester = new CombinationStrategyTester(new FieldExhaustiveCombinationStrategy());
    }

    @Test
    void shouldCombineFieldExhaustively() {
        tester.given(
            bagSequence(bag("A"), bag("B"), bag("C")),
            bagSequence(bag("1"), bag("2"), bag("3")));

        tester.expect(
            bagSequence(
                bag("A","1"),
                bag("A","2"),
                bag("A","3"),
                bag("B","1"),
                bag("C","1")));
    }

    @Test
    void shouldCombineSequencesOfDifferentLengths() {
        tester.given(
            bagSequence(bag("X")),
            bagSequence(bag("A"), bag("B"), bag("C")),
            bagSequence(bag("1"), bag("2"), bag("3"), bag("4"), bag("5")));

        tester.expect(
            bagSequence(
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
        tester.given(bagSequence(bag("A"), bag("B"), bag("C")));

        tester.expect(bagSequence(bag("A"), bag("B"), bag("C")));
    }

    @Test
    void shouldGiveNoResultsForSingleEmptySequence() {
        tester.given(
            bagSequence(bag("A"), bag("B"), bag("C")),
            bagSequence());

        tester.expectEmpty();
    }

    @Test
    void shouldAllowMultipleIterations() {
        tester.expectMultipleIterationsDontThrow();
    }
}
