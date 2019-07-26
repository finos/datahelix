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

package com.scottlogic.deg.generator.restrictions;

import com.scottlogic.deg.common.profile.constraints.atomic.StandardConstraintTypes;
import com.scottlogic.deg.generator.generation.string.IsinStringGenerator;
import com.scottlogic.deg.generator.generation.string.RegexStringGenerator;
import com.scottlogic.deg.generator.generation.string.StringGenerator;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Iterator;
import java.util.regex.Pattern;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.IsNull.nullValue;

class TextualRestrictionsTests {
    @Test
    void createGenerator_firstCall_shouldCreateAGenerator() {
        StringRestrictions restrictions = ofLength(10, false);

        StringGenerator generator = restrictions.createGenerator();

        Assert.assertThat(generator, not(nullValue()));
    }

    @Test
    void createGenerator_secondCall_shouldReturnSameGenerator() {
        StringRestrictions restrictions = ofLength(10, false);
        StringGenerator firstGenerator = restrictions.createGenerator();

        StringGenerator secondGenerator = restrictions.createGenerator();

        Assert.assertThat(secondGenerator, sameInstance(firstGenerator));
    }

    @Test
    void createGenerator_withMaxLengthConstraint_shouldCreateStringsToMaxLength() {
        StringRestrictions restrictions = maxLength(9);

        StringGenerator generator = restrictions.createGenerator();

        Assert.assertThat(generator.toString(), equalTo("/^.{0,9}$/"));
    }

    @Test
    void createGenerator_withMinLengthConstraint_shouldCreateStringsFromMinLengthToDefaultLength() {
        StringRestrictions restrictions = minLength(11);

        StringGenerator generator = restrictions.createGenerator();

        Assert.assertThat(generator.toString(), equalTo("/^.{11,}$/"));
    }

    @Test
    void createGenerator_withOfLengthConstraint_shouldCreateStringsOfLength() {
        StringRestrictions restrictions = ofLength(10, false);

        StringGenerator generator = restrictions.createGenerator();

        Assert.assertThat(generator.toString(), equalTo("/^.{10}$/"));
    }

    @Test
    void createGenerator_withMinAndNonContradictingMaxLengthConstraint_shouldCreateStringsBetweenLengths() {
        StringRestrictions restrictions =
            minLength(6)
                .intersect(maxLength(9)).restrictions;

        StringGenerator generator = restrictions.createGenerator();

        Assert.assertThat(generator.toString(), equalTo("/^.{6,9}$/"));
    }

    @Test
    void createGenerator_withMinAndContradictingMaxLengthConstraint_shouldCreateNoStrings() {
        StringRestrictions restrictions =
            minLength(11)
                .intersect(maxLength(4)).restrictions;

        StringGenerator generator = restrictions.createGenerator();

        assertGeneratorCannotGenerateAnyStrings(generator);
    }

    @Test
    void createGenerator_withMinAndNonContradictingOfLengthConstraint_shouldCreateStringsOfLength() {
        StringRestrictions restrictions =
            minLength(5)
                .intersect(ofLength(10, false)).restrictions;

        StringGenerator generator = restrictions.createGenerator();

        Assert.assertThat(generator.toString(), equalTo("/^.{10}$/"));
    }

    @Test
    void createGenerator_withMinAndContradictingOfLengthConstraint_shouldCreateNoStrings() {
        StringRestrictions restrictions =
            minLength(10)
                .intersect(ofLength(5, false)).restrictions;

        StringGenerator generator = restrictions.createGenerator();

        assertGeneratorCannotGenerateAnyStrings(generator);
    }

    @Test
    void createGenerator_withMaxAndNonContradictingOfLengthConstraint_shouldCreateStringsOfLength() {
        StringRestrictions restrictions =
            maxLength(10)
                .intersect(ofLength(5, false)).restrictions;

        StringGenerator generator = restrictions.createGenerator();

        Assert.assertThat(generator.toString(), equalTo("/^.{5}$/"));
    }

    @Test
    void createGenerator_withMaxAndContradictingOfLengthConstraint_shouldCreateNoStrings() {
        StringRestrictions restrictions =
            maxLength(5)
                .intersect(ofLength(10, false)).restrictions;

        StringGenerator generator = restrictions.createGenerator();

        assertGeneratorCannotGenerateAnyStrings(generator);
    }

    @Test
    void createGenerator_withMinMaxAndNonContradictingOfLengthConstraint_shouldCreateStringsOfLength() {
        StringRestrictions restrictions =
            minLength(5)
                .intersect(maxLength(10)).restrictions
                .intersect(ofLength(7, false)).restrictions;

        StringGenerator generator = restrictions.createGenerator();

        Assert.assertThat(generator.toString(), equalTo("/^.{7}$/"));
    }

    @Test
    void createGenerator_with2MinLengthConstraints_shouldCreateStringsOfLongerThatGreatestMin() {
        StringRestrictions restrictions =
            minLength(5)
                .intersect(minLength(11)).restrictions;

        StringGenerator generator = restrictions.createGenerator();

        Assert.assertThat(generator.toString(), equalTo("/^.{11,}$/"));
    }

    @Test
    void createGenerator_with2MaxLengthConstraints_shouldCreateStringsOfShortestThatLowestMax() {
        StringRestrictions restrictions =
            maxLength(4)
                .intersect(maxLength(10)).restrictions;

        StringGenerator generator = restrictions.createGenerator();

        Assert.assertThat(generator.toString(), equalTo("/^.{0,4}$/"));
    }

    @Test
    void createGenerator_with2OfLengthConstraints_shouldCreateNoStrings() {
        StringRestrictions restrictions =
            ofLength(5, false)
                .intersect(ofLength(10, false)).restrictions;

        StringGenerator generator = restrictions.createGenerator();

        assertGeneratorCannotGenerateAnyStrings(generator);
    }

    @Test
    void createGenerator_withOnlyAMatchingRegexConstraint_shouldCreateStringsMatchingRegex() {
        StringRestrictions restrictions = matchingRegex("[a-z]{0,9}", false);

        StringGenerator generator = restrictions.createGenerator();

        Assert.assertThat(generator.toString(), equalTo("/[a-z]{0,9}/"));
    }

    @Test
    void createGenerator_withNonContradictingMinLengthAndMatchingRegexConstraint_shouldCreateStringsMatchingRegexAndLongerThanMinLength() {
        StringRestrictions restrictions = matchingRegex("[a-z]{0,9}", false)
            .intersect(minLength(6)).restrictions;

        StringGenerator generator = restrictions.createGenerator();

        Assert.assertThat(generator.toString(), equalTo("(/^.{6,}$/ ∩ /[a-z]{0,9}/)"));
    }

    @Test
    void intersect_withContradictingMinLengthAndMatchingRegexConstraint_shouldReturnUnsuccessful() {
        MergeResult<StringRestrictions> intersect = matchingRegex("[a-z]{0,9}", false)
            .intersect(minLength(100));

        Assert.assertThat(intersect, equalTo(MergeResult.unsuccessful()));
    }

    @Test
    void createGenerator_withNonContradictingMaxLengthAndMatchingRegexConstraint_shouldCreateStringsMatchingRegexAndShorterThanMinLength() {
        StringRestrictions restrictions = matchingRegex("[a-z]{0,9}", false)
            .intersect(maxLength(4)).restrictions;

        StringGenerator generator = restrictions.createGenerator();

        Assert.assertThat(generator.toString(), equalTo("(/^.{0,4}$/ ∩ /[a-z]{0,9}/)"));
    }

    @Test
    void createGenerator_withContradictingMaxLengthAndMatchingRegexConstraint_shouldReturnUnsuccessful() {
        MergeResult<StringRestrictions> intersect = matchingRegex("[a-z]{5,9}", false)
            .intersect(maxLength(2));

        Assert.assertThat(intersect, equalTo(MergeResult.unsuccessful()));
    }

    @Test
    void createGenerator_withNonContradictingOfLengthAndMatchingRegexConstraint_shouldCreateStringsMatchingRegexAndOfPrescribedLength() {
        StringRestrictions restrictions = matchingRegex("[a-z]{0,9}", false)
            .intersect(ofLength(5, false)).restrictions;

        StringGenerator generator = restrictions.createGenerator();

        Assert.assertThat(generator.toString(), equalTo("(/^.{5}$/ ∩ /[a-z]{0,9}/)"));
    }

    @Test
    void intersect_withContradictingOfLengthAndMatchingRegexConstraint_shouldReturnUnsuccessful() {
        MergeResult<StringRestrictions> intersect = matchingRegex("[a-z]{0,9}", false)
            .intersect(ofLength(100, false));

        Assert.assertThat(intersect, equalTo(MergeResult.unsuccessful()));
    }

    @Test
    void createGenerator_withMinAndMaxLengthAndMatchingRegexConstraint_shouldCreateStringsMatchingRegexAndBetweenLengths() {
        StringRestrictions restrictions = matchingRegex("[a-z]{0,9}", false)
            .intersect(minLength(3)).restrictions
            .intersect(maxLength(7)).restrictions;

        StringGenerator generator = restrictions.createGenerator();

        Assert.assertThat(generator.toString(), equalTo("(/^.{3,7}$/ ∩ /[a-z]{0,9}/)"));
    }

    @Test
    void createGenerator_withOnlyAContainingRegexConstraint_shouldCreateStringsContainingRegex() {
        StringRestrictions restrictions = containsRegex("[a-z]{0,9}", false);

        StringGenerator generator = restrictions.createGenerator();

        Assert.assertThat(generator.toString(), equalTo("*/[a-z]{0,9}/*"));
    }

    @Test
    void createGenerator_withNonContradictingMinLengthAndContainingRegexConstraint_shouldCreateStringsContainingRegexAndLongerThanMinLength() {
        StringRestrictions restrictions = containsRegex("[a-z]{0,9}", false)
            .intersect(minLength(6)).restrictions;

        StringGenerator generator = restrictions.createGenerator();

        Assert.assertThat(generator.toString(), equalTo("(/^.{6,}$/ ∩ */[a-z]{0,9}/*)"));
    }

    @Test
    void createGenerator_withContradictingMinLengthAndContainingRegexConstraint_shouldCreateNoStrings() {
        StringRestrictions restrictions = containsRegex("[a-z]{0,9}", false)
            .intersect(minLength(100)).restrictions;

        StringGenerator generator = restrictions.createGenerator();

        Assert.assertThat(generator, instanceOf(RegexStringGenerator.class));
        Assert.assertThat(generator.isFinite(), is(false));
    }

    @Test
    void createGenerator_withNonContradictingMaxLengthAndContainingRegexConstraint_shouldCreateStringsContainingRegexAndShorterThanMinLength() {
        StringRestrictions restrictions = containsRegex("[a-z]{0,9}", false)
            .intersect(maxLength(4)).restrictions;

        StringGenerator generator = restrictions.createGenerator();

        Assert.assertThat(generator.toString(), equalTo("(/^.{0,4}$/ ∩ */[a-z]{0,9}/*)"));
    }

    @Test
    void intersect_withContradictingMaxLengthAndContainingRegexConstraint_shouldReturnUnsuccessful() {
        MergeResult<StringRestrictions> intersect = containsRegex("[a-z]{5,9}", false)
            .intersect(maxLength(2));


        Assert.assertThat(intersect, equalTo(MergeResult.unsuccessful()));
    }

    @Test
    void createGenerator_withNonContradictingOfLengthAndContainingRegexConstraint_shouldCreateStringsContainingRegexAndOfPrescribedLength() {
        StringRestrictions restrictions = containsRegex("[a-z]{0,9}", false)
            .intersect(ofLength(5, false)).restrictions;

        StringGenerator generator = restrictions.createGenerator();

        Assert.assertThat(generator.toString(), equalTo("(/^.{5}$/ ∩ */[a-z]{0,9}/*)"));
    }

    @Test
    void createGenerator_withContradictingOfLengthAndContainingRegexConstraint_shouldCreateNoStrings() {
        StringRestrictions restrictions = containsRegex("[a-z]{102}", false)
            .intersect(ofLength(100, false)).restrictions;

        StringGenerator generator = restrictions.createGenerator();

        assertGeneratorCannotGenerateAnyStrings(generator);
    }

    @Test
    void createGenerator_withMinAndMaxLengthAndContainingRegexConstraint_shouldCreateStringsContainingRegexAndBetweenLengths() {
        StringRestrictions restrictions = containsRegex("[a-z]{0,9}", false)
            .intersect(minLength(3)).restrictions
            .intersect(maxLength(7)).restrictions;

        StringGenerator generator = restrictions.createGenerator();

        Assert.assertThat(generator.toString(), equalTo("(/^.{3,7}$/ ∩ */[a-z]{0,9}/*)"));
    }

    @Test
    void createGenerator_withOnlyAMatchingStandardConstraint_shouldCreateSomeStrings() {
        StringRestrictions restrictions = aValid(StandardConstraintTypes.ISIN, false);

        StringGenerator generator = restrictions.createGenerator();

        Assert.assertThat(generator, instanceOf(IsinStringGenerator.class));
    }

    @Test
    void createGenerator_withMinLengthAndMatchingStandardConstraint_shouldCreateSomeStrings() {
        StringRestrictions restrictions = aValid(StandardConstraintTypes.ISIN, false)
            .intersect(minLength(1)).restrictions;

        StringGenerator generator = restrictions.createGenerator();

        Assert.assertThat(generator, instanceOf(IsinStringGenerator.class));
    }

    @Test
    void createGenerator_withMaxLengthShorterThanCodeLengthAndMatchingStandardConstraint_shouldCreateNoStrings() {
        MergeResult<StringRestrictions> intersect = aValid(StandardConstraintTypes.ISIN, false)
            .intersect(maxLength(10));

        Assert.assertFalse(intersect.successful);
    }

    @Test
    void createGenerator_withMaxLengthAtLengthOfCodeLengthAndMatchingStandardConstraint_shouldCreateSomeStrings() {
        StringRestrictions restrictions = aValid(StandardConstraintTypes.ISIN, false)
            .intersect(maxLength(12)).restrictions;

        StringGenerator generator = restrictions.createGenerator();

        Assert.assertThat(generator, instanceOf(IsinStringGenerator.class));
    }

    @Test
    void createGenerator_withMaxLengthLongerThanCodeLengthAndMatchingStandardConstraint_shouldCreateSomeStrings() {
        StringRestrictions restrictions = aValid(StandardConstraintTypes.ISIN, false)
            .intersect(maxLength(100)).restrictions;

        StringGenerator generator = restrictions.createGenerator();

        Assert.assertThat(generator, instanceOf(IsinStringGenerator.class));
    }

    @Test
    void createGenerator_withOfLengthAndMatchingStandardConstraint_shouldCreateSomeStrings() {
        StringRestrictions restrictions = aValid(StandardConstraintTypes.ISIN, false)
            .intersect(ofLength(12, false)).restrictions;

        StringGenerator generator = restrictions.createGenerator();

        Assert.assertThat(generator, instanceOf(IsinStringGenerator.class));
    }

    @Test
    void createGenerator_withMatchingRegexAndMatchingStandardConstraint_shouldCreateStrings() {
        StringRestrictions restrictions = aValid(StandardConstraintTypes.ISIN, false)
            .intersect(matchingRegex("[a-zA-Z0-9]{12}", false)).restrictions;

        StringGenerator generator = restrictions.createGenerator();

        assertGeneratorCanGenerateAtLeastOneString(generator);
    }

    @Test
    void createGenerator_withContainingRegexAndMatchingStandardConstraint_shouldCreateStrings() {
        StringRestrictions restrictions = aValid(StandardConstraintTypes.ISIN, false)
            .intersect(containsRegex("[a-zA-Z0-9]{12}", false)).restrictions;

        StringGenerator generator = restrictions.createGenerator();

        assertGeneratorCanGenerateAtLeastOneString(generator);
    }

    @Test
    void createGenerator_withNegatedMaxLengthConstraint_shouldCreateStringsFromLength() {
        StringRestrictions restrictions = minLength(10);

        StringGenerator generator = restrictions.createGenerator();

        Assert.assertThat(generator.toString(), equalTo("/^.{10,}$/"));
    }

    @Test
    void createGenerator_withNegatedMinLengthConstraint_shouldCreateStringsUpToLength() {
        StringRestrictions restrictions = maxLength(10);

        StringGenerator generator = restrictions.createGenerator();

        Assert.assertThat(generator.toString(), equalTo("/^.{0,10}$/"));
    }

    @Test
    void createGenerator_withNegatedOfLengthConstraint_shouldCreateStringsShorterThanAndLongerThanExcludedLength() {
        StringRestrictions restrictions = ofLength(10, true);

        StringGenerator generator = restrictions.createGenerator();

        Assert.assertThat(generator.toString(), equalTo("/^(.{0,9}|.{11,})$/"));
    }

    @Test
    void createGenerator_withNegatedMinAndNonContradictingMaxLengthConstraint_shouldCreateStringsBetweenLengths() {
        StringRestrictions restrictions =
            maxLength(5)
                .intersect(maxLength(10)).restrictions;

        StringGenerator generator = restrictions.createGenerator();

        Assert.assertThat(generator.toString(), equalTo("/^.{0,5}$/"));
    }

    @Test
    void createGenerator_withNegatedMinAndContradictingMaxLengthConstraint_shouldCreateShorterThanLowestLength() {
        StringRestrictions restrictions =
            maxLength(10)
                .intersect(maxLength(4)).restrictions;

        StringGenerator generator = restrictions.createGenerator();

        Assert.assertThat(generator.toString(), equalTo("/^.{0,4}$/"));
    }

    @Test
    void createGenerator_withNegatedMinAndNonContradictingOfLengthConstraint_shouldCreateNoStrings() {
        StringRestrictions restrictions =
            maxLength(5)
                .intersect(ofLength(10, false)).restrictions;

        StringGenerator generator = restrictions.createGenerator();

        assertGeneratorCannotGenerateAnyStrings(generator);
    }

    @Test
    void createGenerator_withNegatedMinAndContradictingOfLengthConstraint_shouldCreateStringsOfLength() {
        StringRestrictions restrictions =
            maxLength(10)
                .intersect(ofLength(5, false)).restrictions;

        StringGenerator generator = restrictions.createGenerator();

        Assert.assertThat(generator.toString(), equalTo("/^.{5}$/"));
    }

    @Test
    void createGenerator_withNegatedMaxAndNonContradictingOfLengthConstraint_shouldCreateNoStrings() {
        StringRestrictions restrictions =
            minLength(10)
                .intersect(ofLength(5, false)).restrictions;

        StringGenerator generator = restrictions.createGenerator();

        assertGeneratorCannotGenerateAnyStrings(generator);
    }

    @Test
    void createGenerator_withNegatedMaxAndContradictingOfLengthConstraint_shouldCreateStringsShorterThanMaximumLength() {
        StringRestrictions restrictions =
            maxLength(4)
                .intersect(ofLength(10, true)).restrictions;

        StringGenerator generator = restrictions.createGenerator();

        Assert.assertThat(generator.toString(), equalTo("/^.{0,4}$/"));
    }

    @Test
    void createGenerator_withNegatedMinMaxAndNonContradictingOfLengthConstraint_should() {
        StringRestrictions restrictions =
            maxLength(5)
                .intersect(minLength(10)).restrictions
                .intersect(ofLength(7, true)).restrictions;

        StringGenerator generator = restrictions.createGenerator();

        assertGeneratorCannotGenerateAnyStrings(generator);
    }

    @Test
    void createGenerator_withNegated2MinLengthConstraints_shouldCreateStringsUptoShortestLength() {
        StringRestrictions restrictions =
            maxLength(5)
                .intersect(maxLength(10)).restrictions;

        StringGenerator generator = restrictions.createGenerator();

        Assert.assertThat(generator.toString(), equalTo("/^.{0,5}$/"));
    }

    @Test
    void createGenerator_withNegated2MaxLengthConstraints_shouldCreateStringsFromShortestLengthToDefaultMax() {
        StringRestrictions restrictions =
            minLength(5)
                .intersect(minLength(10)).restrictions;

        StringGenerator generator = restrictions.createGenerator();

        Assert.assertThat(generator.toString(), equalTo("/^.{10,}$/"));
    }

    @Test
    void createGenerator_with2OfDifferentLengthConstraintsWhereOneIsNegated_shouldCreateStringsOfNonNegatedLength() {
        StringRestrictions restrictions =
            ofLength(5, false)
                .intersect(ofLength(10, true)).restrictions;

        StringGenerator generator = restrictions.createGenerator();

        Assert.assertThat(generator.toString(), equalTo("/^.{5}$/"));
    }

    @Test
    void createGenerator_with2OfLengthConstraintsWhereOneIsNegated_should() {
        StringRestrictions restrictions =
            ofLength(5, false)
                .intersect(ofLength(5, true)).restrictions;

        StringGenerator generator = restrictions.createGenerator();

        assertGeneratorCannotGenerateAnyStrings(generator);
    }

    @Test
    void createGenerator_withNotOfLengthSameAsMaxLength_shouldPermitStringsUpToMaxLengthLess1() {
        StringRestrictions restrictions =
            maxLength(5)
                .intersect(ofLength(4, true)).restrictions;

        StringGenerator generator = restrictions.createGenerator();

        Assert.assertThat(generator.toString(), equalTo("/^.{0,3}$/"));
    }

    private static StringRestrictions ofLength(int length, boolean negate){
        return new TextualRestrictions(
            negate ? null : length,
            negate ? null : length,
            Collections.emptySet(),
            Collections.emptySet(),
            negate ? Collections.singleton(length) : Collections.emptySet(),
            Collections.emptySet(),
            Collections.emptySet());
    }

    private static StringRestrictions maxLength(int length){
        return new TextualRestrictions(
            null,
            length,
            Collections.emptySet(),
            Collections.emptySet(),
            Collections.emptySet(),
            Collections.emptySet(),
            Collections.emptySet());
    }

    private static StringRestrictions minLength(int length){
        return new TextualRestrictions(
            length,
            null,
            Collections.emptySet(),
            Collections.emptySet(),
            Collections.emptySet(),
            Collections.emptySet(),
            Collections.emptySet());
    }

    private static StringRestrictions matchingRegex(String regex, @SuppressWarnings("SameParameterValue") boolean negate){
        Pattern pattern = Pattern.compile(regex);

        return new TextualRestrictions(
            null,
            null,
            negate ? Collections.emptySet() : Collections.singleton(pattern),
            Collections.emptySet(),
            Collections.emptySet(),
            negate ? Collections.singleton(pattern) : Collections.emptySet(),
            Collections.emptySet());
    }

    private static StringRestrictions containsRegex(String regex, @SuppressWarnings("SameParameterValue") boolean negate){
        Pattern pattern = Pattern.compile(regex);

        return new TextualRestrictions(
            null,
            null,
            Collections.emptySet(),
            negate ? Collections.emptySet() : Collections.singleton(pattern),
            Collections.emptySet(),
            Collections.emptySet(),
            negate ? Collections.singleton(pattern) : Collections.emptySet());
    }

    private static StringRestrictions aValid(@SuppressWarnings("SameParameterValue") StandardConstraintTypes type, @SuppressWarnings("SameParameterValue") boolean negate){
        return new MatchesStandardStringRestrictions(type, negate);
    }

    private static void assertGeneratorCannotGenerateAnyStrings(StringGenerator generator) {
        Iterator<String> stringValueIterator = generator.generateAllValues().iterator();
        Assert.assertThat(stringValueIterator.hasNext(), is(false));
    }

    private static void assertGeneratorCanGenerateAtLeastOneString(StringGenerator generator) {
        Iterator<String> stringValueIterator = generator.generateAllValues().iterator();
        Assert.assertThat(stringValueIterator.hasNext(), is(true));
    }
}