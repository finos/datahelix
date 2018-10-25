package com.scottlogic.deg.generator.generation.combination_strategies;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.scottlogic.deg.generator.generation.combination_strategies.CombinationStrategyTester.bag;
import static com.scottlogic.deg.generator.generation.combination_strategies.CombinationStrategyTester.bagSequence;

class MinimalCombinationStrategyTests {
    private CombinationStrategyTester tester;

    @BeforeEach
    void beforeEach() {
        tester = new CombinationStrategyTester(new MinimalCombinationStrategy());
    }

    @Test
    void shouldCombineMinimally() {
        tester.given(
            bagSequence(bag("A"), bag("B"), bag("C")),
            bagSequence(bag("1"), bag("2"), bag("3")));

        tester.expect(
            bagSequence(bag("A","1"), bag("B","2"), bag("C","3")));
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
                bag("X", "B", "2"),
                bag("X", "C", "3"),
                bag("X", "C", "4"),
                bag("X", "C", "5")));
    }

    @Test
    void shouldGiveNoResultsForSingleEmptySequence() {
        tester.given(
            bagSequence(bag("A"), bag("B"), bag("C")),
            bagSequence());

        tester.expectEmpty();
    }
}
