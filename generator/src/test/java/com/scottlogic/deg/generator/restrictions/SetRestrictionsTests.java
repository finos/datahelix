package com.scottlogic.deg.generator.restrictions;

import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.Matchers.equalTo;

class SetRestrictionsTests {
    @Test
    void whenItemIsOnABlacklistAndAWhitelist() {
        givenFirstInputWhitelist(1, 2, 3);
        givenSecondInputBlacklist(1);

        expectOutputWhitelist(2, 3);
        expectOutputBlacklist(); //empty
    }

    @Test
    void shouldIntersectWhitelists() {
        givenFirstInputWhitelist(1, 2, 3);
        givenSecondInputWhitelist(2, 3, 4);

        expectOutputWhitelist(2, 3);
    }

    @Test
    void shouldUnionBlacklists() {
        givenFirstInputBlacklist(1, 2, 3);
        givenSecondInputBlacklist(2, 3, 4);

        expectOutputBlacklist(1, 2, 3, 4);
    }

    @Test
    void shouldDiscardBlacklistIfWhitelistPopulated() {
        givenFirstInputWhitelist(1, 2, 3);
        givenSecondInputBlacklist(7, 8);

        expectOutputWhitelist(1, 2, 3);
        expectOutputBlacklist(); //empty
    }

    @Test
    void shouldDeclareUnsatisfiabilityIfResultingWhitelistIsEmpty() {
        givenFirstInputWhitelist(1, 2);
        givenSecondInputWhitelist(3, 4);

        expectUnsatisfiable();
    }


    @BeforeEach
    private void beforeEach() {
        this.firstInputWhitelist = null;
        this.firstInputBlacklist = null;
        this.secondInputWhitelist = null;
        this.secondInputBlacklist = null;
        this.actualOutput = null;
    }

    private Set<Object> firstInputWhitelist = null;
    private Set<Object> firstInputBlacklist = null;
    private Set<Object> secondInputWhitelist = null;
    private Set<Object> secondInputBlacklist = null;

    private MergeResult<SetRestrictions> actualOutput = null;

    private MergeResult<SetRestrictions> getActualOutput() {
        if (actualOutput == null) {
            actualOutput = SetRestrictions.merge(
                new SetRestrictions(firstInputWhitelist, firstInputBlacklist),
                new SetRestrictions(secondInputWhitelist, secondInputBlacklist));
        }

        return actualOutput;
    }

    private void givenFirstInputWhitelist(Object... expectedValues) {
        firstInputWhitelist = new HashSet<>(Arrays.asList(expectedValues));
    }
    private void givenFirstInputBlacklist(Object... expectedValues) {
        firstInputBlacklist = new HashSet<>(Arrays.asList(expectedValues));
    }
    private void givenSecondInputWhitelist(Object... expectedValues) {
        secondInputWhitelist = new HashSet<>(Arrays.asList(expectedValues));
    }
    private void givenSecondInputBlacklist(Object... expectedValues) {
        secondInputBlacklist = new HashSet<>(Arrays.asList(expectedValues));
    }

    private void expectOutputWhitelist(Object... expectedValues) {
        MergeResult<SetRestrictions> actualOutput = getActualOutput();

        Assert.assertTrue(actualOutput.successful);

        Assert.assertThat(
            actualOutput.restrictions.getWhitelist(),
            equalTo(new HashSet<>(Arrays.asList(expectedValues))));
    }

    private void expectOutputBlacklist(Object... expectedValues) {
        MergeResult<SetRestrictions> actualOutput = getActualOutput();

        Assert.assertTrue(actualOutput.successful);

        Assert.assertThat(
            actualOutput.restrictions.getBlacklist(),
            equalTo(new HashSet<>(Arrays.asList(expectedValues))));
    }

    private void expectUnsatisfiable() {
        MergeResult<SetRestrictions> actualOutput = getActualOutput();

        Assert.assertFalse(actualOutput.successful);
    }
}
