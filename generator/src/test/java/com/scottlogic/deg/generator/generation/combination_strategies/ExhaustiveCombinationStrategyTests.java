package com.scottlogic.deg.generator.generation.combination_strategies;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.stream.Stream;

import static com.scottlogic.deg.generator.generation.combination_strategies.CombinationStrategyTester.bag;

class ExhaustiveCombinationStrategyTests {
    private CombinationStrategyTester tester;

    @BeforeEach
    void beforeEach(){
        tester = new CombinationStrategyTester(new ExhaustiveCombinationStrategy());
    }

    @Test
    void shouldCombineExhaustively() {
        tester.given(
            Stream.of(bag("A"), bag("B"), bag("C")),
            Stream.of(bag("1"), bag("2"), bag("3")));

        tester.expectIllegalState();
        /*The implementation has changed to keep streams of values, but a list of fields, as such streams are re-read
        * in the implementation, which throws. The previous implementation would exhaust the stream of each field before
        * emitting a value, which isn't a viable option for production */
    }

    @Test
    void shouldCombineSequencesOfDifferentLengths() {
        tester.given(
            Stream.of(bag("X")),
            Stream.of(bag("A"), bag("B"), bag("C")),
            Stream.of(bag("1"), bag("2"), bag("3"), bag("4"), bag("5")));

        tester.expectIllegalState();
        /*The implementation has changed to keep streams of values, but a list of fields, as such streams are re-read
         * in the implementation, which throws. The previous implementation would exhaust the stream of each field before
         * emitting a value, which isn't a viable option for production */
    }

    @Test
    void shouldGiveInputForSingleSequence() {
        tester.given(Stream.of(bag("A"), bag("B"), bag("C")));

        tester.expect(Stream.of(bag("A"), bag("B"), bag("C")));
    }

    @Test
    void shouldGiveNoResultsForEmptySequenceLast() {
        tester.given(
            Stream.of(bag("A"), bag("B"), bag("C")),
            Stream.of());

        tester.expectIllegalState();
        /*The implementation has changed to keep streams of values, but a list of fields, as such streams are re-read
         * in the implementation, which throws. The previous implementation would exhaust the stream of each field before
         * emitting a value, which isn't a viable option for production */
    }

    @Test
    void shouldGiveNoResultsForEmptySequenceFirst() {
        tester.given(
            Stream.of(),
            Stream.of(bag("A"), bag("B"), bag("C")));

        tester.expectEmpty();
    }
}
