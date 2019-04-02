package com.scottlogic.deg.generator.restrictions;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.constraints.atomic.*;
import com.scottlogic.deg.generator.generation.IsinStringGenerator;
import com.scottlogic.deg.generator.generation.StringGenerator;
import com.scottlogic.deg.generator.inputs.RuleInformation;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Iterator;
import java.util.Set;
import java.util.regex.Pattern;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.IsNull.nullValue;

class StringRestrictionsTests {
    private static final Field field = new Field("field");
    private static final Set<RuleInformation> rules = Collections.emptySet();

    @Test
    void createGenerator_firstCall_shouldCreateAGenerator() {
        StringRestrictions restrictions = new StringRestrictions(ofLength(10), false);

        StringGenerator generator = restrictions.createGenerator();

        Assert.assertThat(generator, not(nullValue()));
    }

    @Test
    void createGenerator_secondCall_shouldReturnSameGenerator() {
        StringRestrictions restrictions = new StringRestrictions(ofLength(10), false);
        StringGenerator firstGenerator = restrictions.createGenerator();

        StringGenerator secondGenerator = restrictions.createGenerator();

        Assert.assertThat(secondGenerator, sameInstance(firstGenerator));
    }

    @Test
    void createGenerator_withMaxLengthConstraint_shouldCreateStringsToMaxLength() {
        StringRestrictions restrictions = new StringRestrictions(shorterThan(10), false);

        StringGenerator generator = restrictions.createGenerator();

        Assert.assertThat(generator.toString(), equalTo("/^.{0,9}$/"));
    }

    @Test
    void createGenerator_withMinLengthConstraint_shouldCreateStringsFromMinLengthToDefaultLength() {
        StringRestrictions restrictions = new StringRestrictions(longerThan(10), false);

        StringGenerator generator = restrictions.createGenerator();

        Assert.assertThat(generator.toString(), equalTo("/^.{11,255}$/"));
    }

    @Test
    void createGenerator_withOfLengthConstraint_shouldCreateStringsOfLength() {
        StringRestrictions restrictions = new StringRestrictions(ofLength(10), false);

        StringGenerator generator = restrictions.createGenerator();

        Assert.assertThat(generator.toString(), equalTo("/^.{10}$/"));
    }

    @Test
    void createGenerator_withMinAndNonContradictingMaxLengthConstraint_shouldCreateStringsBetweenLengths() {
        StringRestrictions restrictions =
            new StringRestrictions(longerThan(5), false)
                .intersect(new StringRestrictions(shorterThan(10), false));

        StringGenerator generator = restrictions.createGenerator();

        Assert.assertThat(generator.toString(), equalTo("/^.{6,9}$/"));
    }

    @Test
    void createGenerator_withMinAndContradictingMaxLengthConstraint_shouldCreateNoStrings() {
        StringRestrictions restrictions =
            new StringRestrictions(longerThan(10), false)
                .intersect(new StringRestrictions(shorterThan(5), false));

        StringGenerator generator = restrictions.createGenerator();

        assertGeneratorCannotGenerateAnyStrings(generator);
    }

    @Test
    void createGenerator_withMinAndNonContradictingOfLengthConstraint_shouldCreateStringsOfLength() {
        StringRestrictions restrictions =
            new StringRestrictions(longerThan(5), false)
                .intersect(new StringRestrictions(ofLength(10), false));

        StringGenerator generator = restrictions.createGenerator();

        Assert.assertThat(generator.toString(), equalTo("/^.{10}$/"));
    }

    @Test
    void createGenerator_withMinAndContradictingOfLengthConstraint_shouldCreateNoStrings() {
        StringRestrictions restrictions =
            new StringRestrictions(longerThan(10), false)
                .intersect(new StringRestrictions(ofLength(5), false));

        StringGenerator generator = restrictions.createGenerator();

        assertGeneratorCannotGenerateAnyStrings(generator);
    }

    @Test
    void createGenerator_withMaxAndNonContradictingOfLengthConstraint_shouldCreateStringsOfLength() {
        StringRestrictions restrictions =
            new StringRestrictions(shorterThan(10), false)
                .intersect(new StringRestrictions(ofLength(5), false));

        StringGenerator generator = restrictions.createGenerator();

        Assert.assertThat(generator.toString(), equalTo("/^.{5}$/"));
    }

    @Test
    void createGenerator_withMaxAndContradictingOfLengthConstraint_shouldCreateNoStrings() {
        StringRestrictions restrictions =
            new StringRestrictions(shorterThan(5), false)
                .intersect(new StringRestrictions(ofLength(10), false));

        StringGenerator generator = restrictions.createGenerator();

        assertGeneratorCannotGenerateAnyStrings(generator);
    }

    @Test
    void createGenerator_withMinMaxAndNonContradictingOfLengthConstraint_shouldCreateStringsOfLength() {
        StringRestrictions restrictions =
            new StringRestrictions(longerThan(5), false)
                .intersect(new StringRestrictions(shorterThan(10), false))
                .intersect(new StringRestrictions(ofLength(7), false));

        StringGenerator generator = restrictions.createGenerator();

        Assert.assertThat(generator.toString(), equalTo("/^.{7}$/"));
    }

    @Test
    void createGenerator_with2MinLengthConstraints_shouldCreateStringsOfLongerThatGreatestMin() {
        StringRestrictions restrictions =
            new StringRestrictions(longerThan(5), false)
                .intersect(new StringRestrictions(longerThan(10), false));

        StringGenerator generator = restrictions.createGenerator();

        Assert.assertThat(generator.toString(), equalTo("/^.{11,255}$/"));
    }

    @Test
    void createGenerator_with2MaxLengthConstraints_shouldCreateStringsOfShortestThatLowestMax() {
        StringRestrictions restrictions =
            new StringRestrictions(shorterThan(5), false)
                .intersect(new StringRestrictions(shorterThan(10), false));

        StringGenerator generator = restrictions.createGenerator();

        Assert.assertThat(generator.toString(), equalTo("/^.{0,4}$/"));
    }

    @Test
    void createGenerator_with2OfLengthConstraints_shouldCreateNoStrings() {
        StringRestrictions restrictions =
            new StringRestrictions(ofLength(5), false)
                .intersect(new StringRestrictions(ofLength(10), false));

        StringGenerator generator = restrictions.createGenerator();

        assertGeneratorCannotGenerateAnyStrings(generator);
    }

    @Test
    void createGenerator_withOnlyAMatchingRegexConstraint_shouldCreateStringsMatchingRegex() {
        StringRestrictions restrictions = new StringRestrictions(matchingRegex("[a-z]{0,9}"), false);

        StringGenerator generator = restrictions.createGenerator();

        Assert.assertThat(generator.toString(), equalTo("/^.{0,255}$/ ∩ /[a-z]{0,9}/"));
    }

    @Test
    void createGenerator_withNonContradictingMinLengthAndMatchingRegexConstraint_shouldCreateStringsMatchingRegexAndLongerThanMinLength() {
        StringRestrictions restrictions = new StringRestrictions(matchingRegex("[a-z]{0,9}"), false)
            .intersect(new StringRestrictions(longerThan(5), false));

        StringGenerator generator = restrictions.createGenerator();

        Assert.assertThat(generator.toString(), equalTo("/^.{6,255}$/ ∩ /[a-z]{0,9}/"));
    }

    @Test
    void createGenerator_withContradictingMinLengthAndMatchingRegexConstraint_shouldCreateNoStrings() {
        StringRestrictions restrictions = new StringRestrictions(matchingRegex("[a-z]{0,9}"), false)
            .intersect(new StringRestrictions(longerThan(100), false));

        StringGenerator generator = restrictions.createGenerator();

        assertGeneratorCannotGenerateAnyStrings(generator);
    }

    @Test
    void createGenerator_withNonContradictingMaxLengthAndMatchingRegexConstraint_shouldCreateStringsMatchingRegexAndShorterThanMinLength() {
        StringRestrictions restrictions = new StringRestrictions(matchingRegex("[a-z]{0,9}"), false)
            .intersect(new StringRestrictions(shorterThan(5), false));

        StringGenerator generator = restrictions.createGenerator();

        Assert.assertThat(generator.toString(), equalTo("/^.{0,4}$/ ∩ /[a-z]{0,9}/"));
    }

    @Test
    void createGenerator_withContradictingMaxLengthAndMatchingRegexConstraint_shouldCreateNoStrings() {
        StringRestrictions restrictions = new StringRestrictions(matchingRegex("[a-z]{5,9}"), false)
            .intersect(new StringRestrictions(shorterThan(2), false));

        StringGenerator generator = restrictions.createGenerator();

        assertGeneratorCannotGenerateAnyStrings(generator);
    }

    @Test
    void createGenerator_withNonContradictingOfLengthAndMatchingRegexConstraint_shouldCreateStringsMatchingRegexAndOfPrescribedLength() {
        StringRestrictions restrictions = new StringRestrictions(matchingRegex("[a-z]{0,9}"), false)
            .intersect(new StringRestrictions(ofLength(5), false));

        StringGenerator generator = restrictions.createGenerator();

        Assert.assertThat(generator.toString(), equalTo("/^.{5}$/ ∩ /[a-z]{0,9}/"));
    }

    @Test
    void createGenerator_withContradictingOfLengthAndMatchingRegexConstraint_shouldCreateNoStrings() {
        StringRestrictions restrictions = new StringRestrictions(matchingRegex("[a-z]{0,9}"), false)
            .intersect(new StringRestrictions(ofLength(100), false));

        StringGenerator generator = restrictions.createGenerator();

        assertGeneratorCannotGenerateAnyStrings(generator);
    }

    @Test
    void createGenerator_withMinAndMaxLengthAndMatchingRegexConstraint_shouldCreateStringsMatchingRegexAndBetweenLengths() {
        StringRestrictions restrictions = new StringRestrictions(matchingRegex("[a-z]{0,9}"), false)
            .intersect(new StringRestrictions(longerThan(2), false))
            .intersect(new StringRestrictions(shorterThan(8), false));

        StringGenerator generator = restrictions.createGenerator();

        Assert.assertThat(generator.toString(), equalTo("/^.{3,7}$/ ∩ /[a-z]{0,9}/"));
    }

    @Test
    void createGenerator_withOnlyAContainingRegexConstraint_shouldCreateStringsContainingRegex() {
        StringRestrictions restrictions = new StringRestrictions(containsRegex("[a-z]{0,9}"), false);

        StringGenerator generator = restrictions.createGenerator();

        Assert.assertThat(generator.toString(), equalTo("/^.{0,255}$/ ∩ */[a-z]{0,9}/*"));
    }

    @Test
    void createGenerator_withNonContradictingMinLengthAndContainingRegexConstraint_shouldCreateStringsContainingRegexAndLongerThanMinLength() {
        StringRestrictions restrictions = new StringRestrictions(containsRegex("[a-z]{0,9}"), false)
            .intersect(new StringRestrictions(longerThan(5), false));

        StringGenerator generator = restrictions.createGenerator();

        Assert.assertThat(generator.toString(), equalTo("/^.{6,255}$/ ∩ */[a-z]{0,9}/*"));
    }

    @Test
    void createGenerator_withContradictingMinLengthAndContainingRegexConstraint_shouldCreateNoStrings() {
        StringRestrictions restrictions = new StringRestrictions(containsRegex("[a-z]{0,9}"), false)
            .intersect(new StringRestrictions(longerThan(100), false));

        StringGenerator generator = restrictions.createGenerator();

        assertGeneratorCannotGenerateAnyStrings(generator);
    }

    @Test
    void createGenerator_withNonContradictingMaxLengthAndContainingRegexConstraint_shouldCreateStringsContainingRegexAndShorterThanMinLength() {
        StringRestrictions restrictions = new StringRestrictions(containsRegex("[a-z]{0,9}"), false)
            .intersect(new StringRestrictions(shorterThan(5), false));

        StringGenerator generator = restrictions.createGenerator();

        Assert.assertThat(generator.toString(), equalTo("/^.{0,4}$/ ∩ */[a-z]{0,9}/*"));
    }

    @Test
    void createGenerator_withContradictingMaxLengthAndContainingRegexConstraint_shouldCreateNoStrings() {
        StringRestrictions restrictions = new StringRestrictions(containsRegex("[a-z]{5,9}"), false)
            .intersect(new StringRestrictions(shorterThan(2), false));

        StringGenerator generator = restrictions.createGenerator();

        assertGeneratorCannotGenerateAnyStrings(generator);
    }

    @Test
    void createGenerator_withNonContradictingOfLengthAndContainingRegexConstraint_shouldCreateStringsContainingRegexAndOfPrescribedLength() {
        StringRestrictions restrictions = new StringRestrictions(containsRegex("[a-z]{0,9}"), false)
            .intersect(new StringRestrictions(ofLength(5), false));

        StringGenerator generator = restrictions.createGenerator();

        Assert.assertThat(generator.toString(), equalTo("/^.{5}$/ ∩ */[a-z]{0,9}/*"));
    }

    @Test
    void createGenerator_withContradictingOfLengthAndContainingRegexConstraint_shouldCreateNoStrings() {
        StringRestrictions restrictions = new StringRestrictions(containsRegex("[a-z]{0,9}"), false)
            .intersect(new StringRestrictions(ofLength(100), false));

        StringGenerator generator = restrictions.createGenerator();

        assertGeneratorCannotGenerateAnyStrings(generator);
    }

    @Test
    void createGenerator_withMinAndMaxLengthAndContainingRegexConstraint_shouldCreateStringsContainingRegexAndBetweenLengths() {
        StringRestrictions restrictions = new StringRestrictions(containsRegex("[a-z]{0,9}"), false)
            .intersect(new StringRestrictions(longerThan(2), false))
            .intersect(new StringRestrictions(shorterThan(8), false));

        StringGenerator generator = restrictions.createGenerator();

        Assert.assertThat(generator.toString(), equalTo("/^.{3,7}$/ ∩ */[a-z]{0,9}/*"));
    }

    @Test
    void createGenerator_withOnlyAMatchingStandardConstraint_shouldCreateSomeStrings() {
        StringRestrictions restrictions = new StringRestrictions(aValid(StandardConstraintTypes.ISIN), false);

        StringGenerator generator = restrictions.createGenerator();

        Assert.assertThat(generator, instanceOf(IsinStringGenerator.class));
    }

    @Test
    void createGenerator_withMinLengthAndMatchingStandardConstraint_shouldCreateNoStrings() {
        StringRestrictions restrictions = new StringRestrictions(aValid(StandardConstraintTypes.ISIN), false)
            .intersect(new StringRestrictions(longerThan(1), false));

        StringGenerator generator = restrictions.createGenerator();

        assertGeneratorCannotGenerateAnyStrings(generator);
    }

    @Test
    void createGenerator_withMaxLengthAndMatchingStandardConstraint_shouldCreateNoStrings() {
        StringRestrictions restrictions = new StringRestrictions(aValid(StandardConstraintTypes.ISIN), false)
            .intersect(new StringRestrictions(shorterThan(100), false));

        StringGenerator generator = restrictions.createGenerator();

        assertGeneratorCannotGenerateAnyStrings(generator);
    }

    @Test
    void createGenerator_withOfLengthAndMatchingStandardConstraint_shouldCreateNoStrings() {
        StringRestrictions restrictions = new StringRestrictions(aValid(StandardConstraintTypes.ISIN), false)
            .intersect(new StringRestrictions(ofLength(12), false));

        StringGenerator generator = restrictions.createGenerator();

        assertGeneratorCannotGenerateAnyStrings(generator);
    }

    @Test
    void createGenerator_withMatchingRegexAndMatchingStandardConstraint_shouldCreateNoStrings() {
        StringRestrictions restrictions = new StringRestrictions(aValid(StandardConstraintTypes.ISIN), false)
            .intersect(new StringRestrictions(matchingRegex("[a-zA-Z0-9]{12}"), false));

        StringGenerator generator = restrictions.createGenerator();

        assertGeneratorCannotGenerateAnyStrings(generator);
    }

    @Test
    void createGenerator_withContainingRegexAndMatchingStandardConstraint_shouldCreateNoStrings() {
        StringRestrictions restrictions = new StringRestrictions(aValid(StandardConstraintTypes.ISIN), false)
            .intersect(new StringRestrictions(containsRegex("[a-zA-Z0-9]{12}"), false));

        StringGenerator generator = restrictions.createGenerator();

        assertGeneratorCannotGenerateAnyStrings(generator);
    }

    @Test
    void createGenerator_withNegatedMaxLengthConstraint_shouldCreateStringsFromLength() {
        StringRestrictions restrictions = new StringRestrictions(shorterThan(10), true);

        StringGenerator generator = restrictions.createGenerator();

        Assert.assertThat(generator.toString(), equalTo("/^.{10,255}$/"));
    }

    @Test
    void createGenerator_withNegatedMinLengthConstraint_shouldCreateStringsUpToLength() {
        StringRestrictions restrictions = new StringRestrictions(longerThan(10), true);

        StringGenerator generator = restrictions.createGenerator();

        Assert.assertThat(generator.toString(), equalTo("/^.{0,10}$/"));
    }

    @Test
    void createGenerator_withNegatedOfLengthConstraint_shouldCreateStringsShorterThanAndLongerThanExcludedLength() {
        StringRestrictions restrictions = new StringRestrictions(ofLength(10), true);

        StringGenerator generator = restrictions.createGenerator();

        Assert.assertThat(generator.toString(), equalTo("/^(.{0,9}|.{11,255})$/"));
    }

    @Test
    void createGenerator_withNegatedMinAndNonContradictingMaxLengthConstraint_shouldCreateStringsBetweenLengths() {
        StringRestrictions restrictions =
            new StringRestrictions(longerThan(5), true)
                .intersect(new StringRestrictions(shorterThan(10), false));

        StringGenerator generator = restrictions.createGenerator();

        Assert.assertThat(generator.toString(), equalTo("/^.{0,5}$/"));
    }

    @Test
    void createGenerator_withNegatedMinAndContradictingMaxLengthConstraint_shouldCreateShorterThanLowestLength() {
        StringRestrictions restrictions =
            new StringRestrictions(longerThan(10), true)
                .intersect(new StringRestrictions(shorterThan(5), false));

        StringGenerator generator = restrictions.createGenerator();

        Assert.assertThat(generator.toString(), equalTo("/^.{0,4}$/"));
    }

    @Test
    void createGenerator_withNegatedMinAndNonContradictingOfLengthConstraint_shouldCreateNoStrings() {
        StringRestrictions restrictions =
            new StringRestrictions(longerThan(5), true)
                .intersect(new StringRestrictions(ofLength(10), false));

        StringGenerator generator = restrictions.createGenerator();

        assertGeneratorCannotGenerateAnyStrings(generator);
    }

    @Test
    void createGenerator_withNegatedMinAndContradictingOfLengthConstraint_shouldCreateStringsOfLength() {
        StringRestrictions restrictions =
            new StringRestrictions(longerThan(10), true)
                .intersect(new StringRestrictions(ofLength(5), false));

        StringGenerator generator = restrictions.createGenerator();

        Assert.assertThat(generator.toString(), equalTo("/^.{5}$/"));
    }

    @Test
    void createGenerator_withNegatedMaxAndNonContradictingOfLengthConstraint_shouldCreateNoStrings() {
        StringRestrictions restrictions =
            new StringRestrictions(shorterThan(10), true)
                .intersect(new StringRestrictions(ofLength(5), false));

        StringGenerator generator = restrictions.createGenerator();

        assertGeneratorCannotGenerateAnyStrings(generator);
    }

    @Test
    void createGenerator_withNegatedMaxAndContradictingOfLengthConstraint_shouldCreateStringsShorterThanMaximumLength() {
        StringRestrictions restrictions =
            new StringRestrictions(shorterThan(5), false)
                .intersect(new StringRestrictions(ofLength(10), true));

        StringGenerator generator = restrictions.createGenerator();

        Assert.assertThat(generator.toString(), equalTo("/^.{0,4}$/"));
    }

    @Test
    void createGenerator_withNegatedMinMaxAndNonContradictingOfLengthConstraint_should() {
        StringRestrictions restrictions =
            new StringRestrictions(longerThan(5), true)
                .intersect(new StringRestrictions(shorterThan(10), true))
                .intersect(new StringRestrictions(ofLength(7), true));

        StringGenerator generator = restrictions.createGenerator();

        assertGeneratorCannotGenerateAnyStrings(generator);
    }

    @Test
    void createGenerator_withNegated2MinLengthConstraints_shouldCreateStringsUptoShortestLength() {
        StringRestrictions restrictions =
            new StringRestrictions(longerThan(5), true)
                .intersect(new StringRestrictions(longerThan(10), true));

        StringGenerator generator = restrictions.createGenerator();

        Assert.assertThat(generator.toString(), equalTo("/^.{0,5}$/"));
    }

    @Test
    void createGenerator_withNegated2MaxLengthConstraints_shouldCreateStringsFromShortestLengthToDefaultMax() {
        StringRestrictions restrictions =
            new StringRestrictions(shorterThan(5), true)
                .intersect(new StringRestrictions(shorterThan(10), true));

        StringGenerator generator = restrictions.createGenerator();

        Assert.assertThat(generator.toString(), equalTo("/^.{10,255}$/"));
    }

    @Test
    void createGenerator_with2OfDifferentLengthConstraintsWhereOneIsNegated_shouldCreateStringsOfNonNegatedLength() {
        StringRestrictions restrictions =
            new StringRestrictions(ofLength(5), false)
                .intersect(new StringRestrictions(ofLength(10), true));

        StringGenerator generator = restrictions.createGenerator();

        Assert.assertThat(generator.toString(), equalTo("/^.{5}$/"));
    }

    @Test
    void createGenerator_with2OfLengthConstraintsWhereOneIsNegated_should() {
        StringRestrictions restrictions =
            new StringRestrictions(ofLength(5), false)
                .intersect(new StringRestrictions(ofLength(5), true));

        StringGenerator generator = restrictions.createGenerator();

        assertGeneratorCannotGenerateAnyStrings(generator);
    }

    @Test
    void createGenerator_withNotOfLengthSameAsMaxLength_shouldPermitStringsUpToMaxLengthLess1() {
        StringRestrictions restrictions =
            new StringRestrictions(shorterThan(5), false)
                .intersect(new StringRestrictions(ofLength(4), true));

        StringGenerator generator = restrictions.createGenerator();

        Assert.assertThat(generator.toString(), equalTo("/^.{0,3}$/"));
    }

    private static StringHasLengthConstraint ofLength(int length){
        return new StringHasLengthConstraint(field, length, rules);
    }

    private static IsStringShorterThanConstraint shorterThan(int length){
        return new IsStringShorterThanConstraint(field, length, rules);
    }

    private static IsStringLongerThanConstraint longerThan(int length){
        return new IsStringLongerThanConstraint(field, length, rules);
    }

    private static MatchesRegexConstraint matchingRegex(String regex){
        return new MatchesRegexConstraint(field, Pattern.compile(regex), rules);
    }

    private static ContainsRegexConstraint containsRegex(String regex){
        return new ContainsRegexConstraint(field, Pattern.compile(regex), rules);
    }

    private static MatchesStandardConstraint aValid(@SuppressWarnings("SameParameterValue") StandardConstraintTypes type){
        return new MatchesStandardConstraint(field, type, rules);
    }

    private static void assertGeneratorCannotGenerateAnyStrings(StringGenerator generator){
        Iterator<String> stringValueIterator = generator.generateAllValues().iterator();
        Assert.assertThat(stringValueIterator.hasNext(), is(false));
    }
}