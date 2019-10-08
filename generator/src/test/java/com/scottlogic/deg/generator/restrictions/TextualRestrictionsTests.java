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

import com.scottlogic.deg.generator.profile.constraints.atomic.StandardConstraintTypes;
import com.scottlogic.deg.generator.generation.string.generators.RegexStringGenerator;
import com.scottlogic.deg.generator.generation.string.generators.StringGenerator;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Iterator;
import java.util.Optional;
import java.util.regex.Pattern;

import static com.scottlogic.deg.generator.helpers.StringGeneratorHelper.*;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
        Optional<StringRestrictions> result =
            minLength(6)
                .intersect(maxLength(9));

        StringGenerator generator = ((StringRestrictions)result.get()).createGenerator();

        Assert.assertThat(generator.toString(), equalTo("/^.{6,9}$/"));
    }

    @Test
    void createGenerator_withMinAndContradictingMaxLengthConstraint_shouldCreateNoStrings() {
        Optional<StringRestrictions> result =
            minLength(11)
                .intersect(maxLength(4));

        Assert.assertThat(result, equalTo(Optional.empty()));
    }

    @Test
    void createGenerator_withMinAndNonContradictingOfLengthConstraint_shouldCreateStringsOfLength() {
        Optional<StringRestrictions> result =
            minLength(5)
                .intersect(ofLength(10, false));

        StringGenerator generator = ((StringRestrictions)result.get()).createGenerator();

        Assert.assertThat(generator.toString(), equalTo("/^.{10}$/"));
    }

    @Test
    void createGenerator_withMinAndContradictingOfLengthConstraint_shouldCreateNoStrings() {
        Optional<StringRestrictions> result =
            minLength(10)
                .intersect(ofLength(5, false));

        Assert.assertThat(result, equalTo(Optional.empty()));
    }

    @Test
    void createGenerator_withMaxAndNonContradictingOfLengthConstraint_shouldCreateStringsOfLength() {
        Optional<StringRestrictions> result =
            maxLength(10)
                .intersect(ofLength(5, false));

        StringGenerator generator = ((StringRestrictions)result.get()).createGenerator();

        Assert.assertThat(generator.toString(), equalTo("/^.{5}$/"));
    }

    @Test
    void createGenerator_withMaxAndContradictingOfLengthConstraint_shouldCreateNoStrings() {
        Optional<StringRestrictions> result =
            maxLength(5)
                .intersect(ofLength(10, false));

        Assert.assertThat(result, equalTo(Optional.empty()));
    }

    @Test
    void createGenerator_withMinMaxAndNonContradictingOfLengthConstraint_shouldCreateStringsOfLength() {
        TypedRestrictions intermediateResult =
            minLength(5)
                .intersect(maxLength(10)).get();
        Optional<StringRestrictions> result =
            ((StringRestrictions)intermediateResult).intersect(ofLength(7, false));


        StringGenerator generator = ((StringRestrictions)result.get()).createGenerator();

        Assert.assertThat(generator.toString(), equalTo("/^.{7}$/"));
    }

    @Test
    void createGenerator_with2MinLengthConstraints_shouldCreateStringsOfLongerThatGreatestMin() {
        Optional<StringRestrictions> result =
            minLength(5)
                .intersect(minLength(11));

        StringGenerator generator = ((StringRestrictions)result.get()).createGenerator();

        Assert.assertThat(generator.toString(), equalTo("/^.{11,}$/"));
    }

    @Test
    void createGenerator_with2MaxLengthConstraints_shouldCreateStringsOfShortestThatLowestMax() {
        Optional<StringRestrictions> result =
            maxLength(4)
                .intersect(maxLength(10));

        StringGenerator generator = ((StringRestrictions)result.get()).createGenerator();

        Assert.assertThat(generator.toString(), equalTo("/^.{0,4}$/"));
    }

    @Test
    void createGenerator_with2OfLengthConstraints_shouldCreateNoStrings() {
        Optional<StringRestrictions> result =
            ofLength(5, false)
                .intersect(ofLength(10, false));

        Assert.assertThat(result, equalTo(Optional.empty()));
    }

    @Test
    void createGenerator_withOnlyAMatchingRegexConstraint_shouldCreateStringsMatchingRegex() {
        StringRestrictions restrictions = matchingRegex("[a-z]{0,9}", false);

        StringGenerator generator = restrictions.createGenerator();

        Assert.assertThat(generator.toString(), equalTo("/[a-z]{0,9}/"));
    }

    @Test
    void createGenerator_withNonContradictingMinLengthAndMatchingRegexConstraint_shouldCreateStringsMatchingRegexAndLongerThanMinLength() {
        Optional<StringRestrictions> result =
            matchingRegex("[a-z]{0,9}", false)
            .intersect(minLength(6));

        StringGenerator generator = ((StringRestrictions)result.get()).createGenerator();

        Assert.assertThat(generator.toString(), equalTo("(/^.{6,}$/ ∩ /[a-z]{0,9}/)"));
    }

    @Test
    void intersect_withContradictingMinLengthAndMatchingRegexConstraint_shouldReturnUnsuccessful() {
        Optional<StringRestrictions> intersect = matchingRegex("[a-z]{0,9}", false)
            .intersect(minLength(100));

        Assert.assertThat(intersect, equalTo(Optional.empty()));
    }

    @Test
    void createGenerator_withNonContradictingMaxLengthAndMatchingRegexConstraint_shouldCreateStringsMatchingRegexAndShorterThanMinLength() {
        Optional<StringRestrictions> result = matchingRegex("[a-z]{0,9}", false)
            .intersect(maxLength(4));

        StringGenerator generator = ((StringRestrictions)result.get()).createGenerator();

        Assert.assertThat(generator.toString(), equalTo("(/^.{0,4}$/ ∩ /[a-z]{0,9}/)"));
    }

    @Test
    void createGenerator_withContradictingMaxLengthAndMatchingRegexConstraint_shouldReturnUnsuccessful() {
        Optional<StringRestrictions> intersect = matchingRegex("[a-z]{5,9}", false)
            .intersect(maxLength(2));

        Assert.assertThat(intersect, equalTo(Optional.empty()));
    }

    @Test
    void createGenerator_withContradictingLength10AndMatchingRegexConstraintThatIsShorter_shouldCreateNoStrings() {
        Optional<StringRestrictions> intersect = matchingRegex("[a-z]{5,9}", false)
            .intersect(ofLength(10, false));

        Assert.assertThat(intersect, equalTo(Optional.empty()));
    }

    @Test
    void createGenerator_withContradictingLength15AndMatchingRegexConstraintThatIsShorter_shouldCreateNoStrings() {
        Optional<StringRestrictions> intersect = matchingRegex("[a-z]{5,9}", false)
            .intersect(ofLength(15, false));

        Assert.assertThat(intersect, equalTo(Optional.empty()));
    }

    @Test
    void createGenerator_withNonContradictingOfLengthAndMatchingRegexConstraint_shouldCreateStringsMatchingRegexAndOfPrescribedLength() {
        Optional<StringRestrictions> result = matchingRegex("[a-z]{0,9}", false)
            .intersect(ofLength(5, false));

        StringGenerator generator = ((StringRestrictions)result.get()).createGenerator();

        Assert.assertThat(generator.toString(), equalTo("(/^.{5}$/ ∩ /[a-z]{0,9}/)"));
    }

    @Test
    void intersect_withContradictingOfLengthAndMatchingRegexConstraint_shouldReturnUnsuccessful() {
        Optional<StringRestrictions> intersect = matchingRegex("[a-z]{0,9}", false)
            .intersect(ofLength(100, false));

        Assert.assertThat(intersect, equalTo(Optional.empty()));
    }

    @Test
    void createGenerator_withMinAndMaxLengthAndMatchingRegexConstraint_shouldCreateStringsMatchingRegexAndBetweenLengths() {
        TypedRestrictions intermediateResult =
            matchingRegex("[a-z]{0,9}", false)
                .intersect(minLength(3)).get();
        Optional<StringRestrictions> result =
            ((StringRestrictions)intermediateResult).intersect(maxLength(7));

        StringGenerator generator = ((StringRestrictions)result.get()).createGenerator();

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
        Optional<StringRestrictions> result = containsRegex("[a-z]{0,9}", false)
            .intersect(minLength(6));

        StringGenerator generator = ((StringRestrictions)result.get()).createGenerator();

        Assert.assertThat(generator.toString(), equalTo("(/^.{6,}$/ ∩ */[a-z]{0,9}/*)"));
    }

    @Test
    void createGenerator_withContradictingMinLengthAndContainingRegexConstraint_shouldCreateNoStrings() {
        Optional<StringRestrictions> result = containsRegex("[a-z]{0,9}", false)
            .intersect(minLength(100));

        StringGenerator generator = ((StringRestrictions)result.get()).createGenerator();

        Assert.assertThat(generator, instanceOf(RegexStringGenerator.class));//???
    }

    @Test
    void createGenerator_withNonContradictingMaxLengthAndContainingRegexConstraint_shouldCreateStringsContainingRegexAndShorterThanMinLength() {
        Optional<StringRestrictions> result = containsRegex("[a-z]{0,9}", false)
            .intersect(maxLength(4));

        StringGenerator generator = ((StringRestrictions)result.get()).createGenerator();

        Assert.assertThat(generator.toString(), equalTo("(/^.{0,4}$/ ∩ */[a-z]{0,9}/*)"));
    }

    @Test
    void intersect_withContradictingMaxLengthAndContainingRegexConstraint_shouldReturnUnsuccessful() {
        Optional<StringRestrictions> intersect = containsRegex("[a-z]{5,9}", false)
            .intersect(maxLength(2));


        Assert.assertThat(intersect, equalTo(Optional.empty()));
    }

    @Test
    void createGenerator_withNonContradictingOfLengthAndContainingRegexConstraint_shouldCreateStringsContainingRegexAndOfPrescribedLength() {
        Optional<StringRestrictions> result = containsRegex("[a-z]{0,9}", false)
            .intersect(ofLength(5, false));

        StringGenerator generator = ((StringRestrictions)result.get()).createGenerator();

        Assert.assertThat(generator.toString(), equalTo("(/^.{5}$/ ∩ */[a-z]{0,9}/*)"));
    }

    @Test
    void createGenerator_withContradictingOfLengthAndContainingRegexConstraint_shouldCreateNoStrings() {
        Optional<StringRestrictions> intersect = containsRegex("[a-z]{102}", false)
            .intersect(ofLength(100, false));
        Assert.assertThat(intersect, equalTo(Optional.empty()));
    }

    @Test
    void createGenerator_OfLength11_shouldCreateStrings() {
        StringRestrictions restrictions = ofLength(11, false);
        StringGenerator generator = restrictions.createGenerator();

        Assert.assertThat(generator.toString(), equalTo("/^.{11}$/"));
        assertGeneratorCanGenerateAtLeastOneStringWithinLengthBounds(generator, 11, 11);
    }

    @Test
    void createGenerator_OfLength12_shouldCreateStrings() {
        StringRestrictions restrictions = ofLength(12, false);
        StringGenerator generator = restrictions.createGenerator();

        Assert.assertThat(generator.toString(), equalTo("/^.{12}$/"));
        assertGeneratorCanGenerateAtLeastOneStringWithinLengthBounds(generator, 12, 12);
    }

    @Test
    void createGenerator_withContradictingOfLength10AndContainingRegexConstraint_shouldCreateNoStrings() {
        Optional<StringRestrictions> intersect = containsRegex("[a-z]{11,12}", false)
            .intersect(ofLength(10, false));

        Assert.assertThat(intersect, equalTo(Optional.empty()));
    }

    @Test
    void createGenerator_withContradictingOfLength100AndContainingRegexConstraint_shouldCreateNoStrings() {
        Optional<StringRestrictions> intersect = containsRegex("[a-z]{102,103}", false)
            .intersect(ofLength(100, false));

        Assert.assertThat(intersect, equalTo(Optional.empty()));
    }

    @Test
    void createGenerator_withMinAndMaxLengthAndContainingRegexConstraint_shouldCreateStringsContainingRegexAndBetweenLengths() {
        TypedRestrictions intermediateResult =
            containsRegex("[a-z]{0,9}", false)
                .intersect(minLength(3)).get();
        Optional<StringRestrictions> result =
            ((StringRestrictions)intermediateResult).intersect(maxLength(7));

        StringGenerator generator = ((StringRestrictions)result.get()).createGenerator();

        Assert.assertThat(generator.toString(), equalTo("(/^.{3,7}$/ ∩ */[a-z]{0,9}/*)"));
        assertGeneratorCanGenerateAtLeastOneStringWithinLengthBounds(generator, 3, 7);
    }

    @Test
    void createGenerator_withOnlyAMatchingStandardConstraint_shouldCreateSomeStrings() {
        StringRestrictions restrictions = aValid(StandardConstraintTypes.ISIN);

        StringGenerator generator = restrictions.createGenerator();

        assertTrue(generator.generateAllValues().limit(1).count() > 0);
    }

    @Test
    void createGenerator_withMinLengthAndMatchingStandardConstraint_shouldCreateSomeStrings() {
        Optional<StringRestrictions> result = aValid(StandardConstraintTypes.ISIN)
            .intersect(minLength(1));

        StringGenerator generator = ((StringRestrictions)result.get()).createGenerator();

        assertTrue(generator.generateAllValues().limit(1).count() > 0);
    }

    @Test
    void createGenerator_withMaxLengthShorterThanCodeLengthAndMatchingStandardConstraint_shouldCreateNoStrings() {
        Optional<StringRestrictions> intersect = aValid(StandardConstraintTypes.ISIN)
            .intersect(maxLength(10));

        Assert.assertFalse(intersect.isPresent());
    }

    @Test
    void createGenerator_withMaxLengthAtLengthOfCodeLengthAndMatchingStandardConstraint_shouldCreateSomeStrings() {
        Optional<StringRestrictions> result = aValid(StandardConstraintTypes.ISIN)
            .intersect(maxLength(12));

        StringGenerator generator = ((StringRestrictions)result.get()).createGenerator();

        assertTrue(generator.generateAllValues().limit(1).count() > 0);
    }

    @Test
    void createGenerator_withMaxLengthLongerThanCodeLengthAndMatchingStandardConstraint_shouldCreateSomeStrings() {
        Optional<StringRestrictions> result = aValid(StandardConstraintTypes.ISIN)
            .intersect(maxLength(100));

        StringGenerator generator = ((StringRestrictions)result.get()).createGenerator();

        assertTrue(generator.generateAllValues().limit(1).count() > 0);
    }

    @Test
    void createGenerator_withOfLengthAndMatchingStandardConstraint_shouldCreateSomeStrings() {
        Optional<StringRestrictions> result = aValid(StandardConstraintTypes.ISIN)
            .intersect(ofLength(12, false));

        StringGenerator generator = ((StringRestrictions)result.get()).createGenerator();

        assertTrue(generator.generateAllValues().limit(1).count() > 0);
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
        Optional<StringRestrictions> result =
            maxLength(5)
                .intersect(maxLength(10));

        StringGenerator generator = ((StringRestrictions)result.get()).createGenerator();

        Assert.assertThat(generator.toString(), equalTo("/^.{0,5}$/"));
    }

    @Test
    void createGenerator_withNegatedMinAndContradictingMaxLengthConstraint_shouldCreateShorterThanLowestLength() {
        Optional<StringRestrictions> result =
            maxLength(10)
                .intersect(maxLength(4));

        StringGenerator generator = ((StringRestrictions)result.get()).createGenerator();

        Assert.assertThat(generator.toString(), equalTo("/^.{0,4}$/"));
    }

    @Test
    void intersect_withNegatedMinAndNonContradictingOfLengthConstraint_shouldBeUnsuccessful() {
        Optional<StringRestrictions> result =
            maxLength(5)
                .intersect(ofLength(10, false));

        Assert.assertThat(result, equalTo(Optional.empty()));
    }

    @Test
    void createGenerator_withNegatedMinAndContradictingOfLengthConstraint_shouldCreateStringsOfLength() {
        Optional<StringRestrictions> result =
            maxLength(10)
                .intersect(ofLength(5, false));

        StringGenerator generator = ((StringRestrictions)result.get()).createGenerator();

        Assert.assertThat(generator.toString(), equalTo("/^.{5}$/"));
    }

    @Test
    void intersect_withNegatedMaxAndNonContradictingOfLengthConstraint_shouldCreateNoStrings() {
        Optional<StringRestrictions> result =
            minLength(10)
                .intersect(ofLength(5, false));

        Assert.assertThat(result, equalTo(Optional.empty()));
    }

    @Test
    void createGenerator_withNegatedMaxAndContradictingOfLengthConstraint_shouldCreateStringsShorterThanMaximumLength() {
        Optional<StringRestrictions> result =
            maxLength(4)
                .intersect(ofLength(10, true));

        StringGenerator generator = ((StringRestrictions)result.get()).createGenerator();

        Assert.assertThat(generator.toString(), equalTo("/^.{0,4}$/"));
    }

    @Test
    void createGenerator_withNegated2MinLengthConstraints_shouldCreateStringsUptoShortestLength() {
        Optional<StringRestrictions> result =
            maxLength(5)
                .intersect(maxLength(10));

        StringGenerator generator = ((StringRestrictions)result.get()).createGenerator();

        Assert.assertThat(generator.toString(), equalTo("/^.{0,5}$/"));
    }

    @Test
    void createGenerator_withNegated2MaxLengthConstraints_shouldCreateStringsFromShortestLengthToDefaultMax() {
        Optional<StringRestrictions> result =
            minLength(5)
                .intersect(minLength(10));

        StringGenerator generator = ((StringRestrictions)result.get()).createGenerator();

        Assert.assertThat(generator.toString(), equalTo("/^.{10,}$/"));
    }

    @Test
    void createGenerator_with2OfDifferentLengthConstraintsWhereOneIsNegated_shouldCreateStringsOfNonNegatedLength() {
        Optional<StringRestrictions> result =
            ofLength(5, false)
                .intersect(ofLength(10, true));

        StringGenerator generator = ((StringRestrictions)result.get()).createGenerator();

        Assert.assertThat(generator.toString(), equalTo("/^.{5}$/"));
    }

    @Test
    void intersect_with2OfLengthConstraintsWhereOneIsNegated_should() {
        Optional<StringRestrictions> result =
            ofLength(5, false)
                .intersect(ofLength(5, true));

        Assert.assertThat(result, equalTo(Optional.empty()));
    }

    @Test
    void createGenerator_withNotOfLengthSameAsMaxLength_shouldPermitStringsUpToMaxLengthLess1() {
        Optional<StringRestrictions> result =
            maxLength(5)
                .intersect(ofLength(4, true));

        StringGenerator generator = ((StringRestrictions)result.get()).createGenerator();

        Assert.assertThat(generator.toString(), equalTo("/^.{0,3}$/"));
    }

    @Test
    void intersect_withStringRestrictionLengthNotSet_shouldReturnUnsuccessful() {
        StringRestrictions left = setLength(0, 0);
        StringRestrictions right = setLength(1, 1000);
        Optional<StringRestrictions> intersect = left.intersect(right);

        Assert.assertThat(intersect, equalTo(Optional.empty()));
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
        return setLength(null, length);
    }

    private static StringRestrictions minLength(int length){
        return setLength(length, null);
    }

    private static StringRestrictions setLength(Integer min, Integer max){
        return new TextualRestrictions(
            min,
            max,
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

    private static StringRestrictions aValid(StandardConstraintTypes type){
        return new MatchesStandardStringRestrictions(type);
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