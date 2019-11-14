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

package com.scottlogic.deg.generator.generation.string;

import com.scottlogic.deg.generator.generation.string.generators.RegexStringGenerator;
import com.scottlogic.deg.generator.generation.string.generators.StringGenerator;
import com.scottlogic.deg.generator.restrictions.string.StringRestrictionsFactory;
import com.scottlogic.datahelix.generator.common.utils.JavaUtilRandomNumberGenerator;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static com.scottlogic.deg.generator.helpers.StringGeneratorHelper.assertGeneratorCanGenerateAtLeastOneStringWithinLengthBounds;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.collection.IsIterableContainingInOrder.contains;
import static org.junit.Assert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class RegexStringGeneratorTests {
    @Test
    void shouldFullStringMatchAnchoredString() {
        givenRegex("^test$");

        expectMatch("test", true);
    }

    @Test
    void shouldContainAnchoredString() {
        givenRegex("^test$");

        expectMatch("test", false);
    }

    @Test
    void shouldNotFullStringMatchAnchoredString() {
        givenRegex("^test$");

        expectNoMatch("testtest", true);
    }

    @Test
    void shouldNotContainAnchoredString() {
        givenRegex("^test$");

        expectNoMatch("testtest", false);
    }

    @Test
    void shouldContainUnAnchoredString() {
        givenRegex("test");

        expectMatch("testtest", false);
    }

    @Test
    void shouldNotMatchUnAnchoredString() {
        givenRegex("test");

        expectNoMatch("testtest", true);
    }

    @Test
    void shouldMatchAllExpectedStringsFromRegex() {
        givenRegex("^aa(bb|cc)d?$");

        expectAnyOrder(
            "aabb",
            "aabbd",
            "aacc",
            "aaccd");
    }

    @Test
    void shouldCorrectlyIterateFiniteResults() {
        givenRegex("^xyz(xyz)?xyz$");

        expectOrderedResults("xyzxyz", "xyzxyzxyz");
    }

    @Test
    void shouldCorrectlyReplaceCharacterGroups() {
        givenRegex("^\\d$");

        expectFirstResult("0");
    }

    @Test
    void generateInterestingValuesShouldGenerateShortestAndLongestValues() {
        StringGenerator generator = new RegexStringGenerator("Test_(\\d{3}|[A-Z]{5})_(banana|apple)", true);

        List<String> results = generator.generateInterestingValues().collect(Collectors.toList());

        assertThat(
            results,
            containsInAnyOrder(
                "Test_000_apple",
                "Test_AAAAA_banana"));
    }

    @Test
    void interestingValuesShouldBePrintable() {
        StringGenerator generator = new RegexStringGenerator("Test.Test", true);

        List<String> results = generator.generateInterestingValues().collect(Collectors.toList());

        for (String interestingValue : results) {
            for (char character : interestingValue.toCharArray()) {
                assertThat(character, greaterThanOrEqualTo((char)32));
            }
        }
    }

    @Test
    void interestingValuesShouldBeBounds() {
        StringGenerator generator = new RegexStringGenerator(".{10,20}", true);

        List<String> results = generator.generateInterestingValues().collect(Collectors.toList());

        assertThat(results.size(), Is.is(2));
        assertThat(results.get(0).length(), Is.is(10));
        assertThat(results.get(1).length(), Is.is(20));
    }

    @Test
    void shouldCreateZeroLengthInterestingValue() {
        StringGenerator generator = new RegexStringGenerator("(Test)?", true);

        List<String> results = generator.generateInterestingValues().collect(Collectors.toList());

        assertThat(
                results,
                containsInAnyOrder(
                        "",
                        "Test"));
    }

    @Test
    void shouldCorrectlySampleInfiniteResults() {
        StringGenerator generator = StringRestrictionsFactory.forStringMatching(Pattern.compile("[a]+"), false).createGenerator();

        Stream<String> results = generator.generateRandomValues(new JavaUtilRandomNumberGenerator(0));

        List<String> sampleValues =
                results
                    .limit(1000)
                    .collect(Collectors.toList());

        assertThat(sampleValues, not(contains(null, "")));
    }

    @Test
    void shouldProduceIntersection() {
        StringGenerator infiniteGenerator = new RegexStringGenerator("[a-z]+", false);

        StringGenerator rangeGenerator = new RegexStringGenerator("(a|b){1,10}", true);

        StringGenerator actual = infiniteGenerator.intersect(rangeGenerator);

        List<String> actualResults = new ArrayList<>();
        actual.generateAllValues().iterator().forEachRemaining(actualResults::add);

        assertThat(actualResults.size(), Is.is(2046));
    }

    @Test
    void shouldProduceComplement() {
        StringGenerator limitedRangeGenerator =
            StringRestrictionsFactory.forStringMatching(Pattern.compile("[a-m]"), false).createGenerator();
        StringGenerator complementedGenerator = limitedRangeGenerator.complement();

        String sampleValue = complementedGenerator
                .generateRandomValues(new JavaUtilRandomNumberGenerator(0))
                .iterator().next();

        assertThat(
                sampleValue,
                not(matchesPattern("^[a-m]$")));
        //todo: more robust tests
    }

    @Test
    void shouldReturnNoValuesWhenContradictingConstraints() {
        StringGenerator firstGenerator = new RegexStringGenerator("[b]{2}", true);
        StringGenerator secondGenerator = new RegexStringGenerator(".{0,1}", true);

        StringGenerator contradictingGenerator = firstGenerator.intersect(secondGenerator);

        assertFalse(contradictingGenerator.generateAllValues().iterator().hasNext());
    }

    @Test
    void shouldReturnValuesWhenNonContradictingConstraints() {
        StringGenerator firstGenerator = new RegexStringGenerator("[b]{2}", true);
        StringGenerator secondGenerator = new RegexStringGenerator(".{0,2}", true);

        StringGenerator nonContradictingGenerator = firstGenerator.intersect(secondGenerator);

        assertTrue(nonContradictingGenerator.generateAllValues().iterator().hasNext());
    }

    @Test
    void shouldNotGenerateInvalidUnicodeCodePoints() {
        StringGenerator generator = new RegexStringGenerator("[😁-😘]{1}", true);
        List<String> results = generator.generateAllValues().collect(Collectors.toList());
        for (String s : results) {
            if (s != null && doesStringContainSurrogates(s)) {
                fail("string contains surrogate character");
            }
        }
    }

    @Test
    void generateAllShouldGenerateLetterStringsOfLength12() {
        RegexStringGenerator generator = new RegexStringGenerator("[a-z]{12}", true);
        generator.generateAllValues();

        assertGeneratorCanGenerateAtLeastOneStringWithinLengthBounds(generator, 12, 12);
    }

    @Test
    void generateAllShouldGenerateLatinCharacterStringsOfLength10() {
        RegexStringGenerator generator = new RegexStringGenerator("[\u0020-\u007E]{10}", true);
        generator.generateAllValues();

        assertGeneratorCanGenerateAtLeastOneStringWithinLengthBounds(generator, 10, 10);
    }

    @Test
    void generateAllShouldGenerateLatinCharacterStringsOfLength12() {
        RegexStringGenerator generator = new RegexStringGenerator("[\u0020-\u007E]{12}", true);
        generator.generateAllValues();

        assertGeneratorCanGenerateAtLeastOneStringWithinLengthBounds(generator, 12, 12);
    }

    @Test
    void generateAllShouldGenerateNonNullCharacterStringsOfLength12() {
        RegexStringGenerator generator = new RegexStringGenerator("[\u0001-\uFFFF]{12}", true);
        generator.generateAllValues();

        assertGeneratorCanGenerateAtLeastOneStringWithinLengthBounds(generator, 12, 12);
    }

    @Test
    void generateAllShouldGenerateStringsOfLength12() {
        RegexStringGenerator generator = new RegexStringGenerator(".{12}", true);
        generator.generateAllValues();

        assertGeneratorCanGenerateAtLeastOneStringWithinLengthBounds(generator, 12, 12);
    }

    @Test
    void generateAllShouldGenerateStringsWithLongNonContiguousRegex(){
        RegexStringGenerator generator = new RegexStringGenerator("[ace]{50}", true);
        generator.generateAllValues();

        assertGeneratorCanGenerateAtLeastOneStringWithinLengthBounds(generator, 50, 50);
    }

    @Test
    void generateAllShouldGenerateStringsWithNonContiguousRegex(){
        RegexStringGenerator generator = new RegexStringGenerator("[abcefg]{1,5}", true);
        generator.generateAllValues();

        assertGeneratorCanGenerateAtLeastOneStringWithinLengthBounds(generator, 1, 5);
    }


    private boolean doesStringContainSurrogates(String testString) {
        for (char c : testString.toCharArray()) {
            if (Character.isSurrogate(c)) {
                return true;
            }
        }
        return false;
    }

    private final List<String> regexes = new ArrayList<>();

    private void givenRegex(String regex) {
        this.regexes.add(regex);
    }

    @BeforeEach
    private void beforeEach() {
        this.regexes.clear();
    }

    private StringGenerator constructGenerator(boolean matchFullString) {
        StringGenerator generator = null;
        for (String regex : regexes) {
            RegexStringGenerator correspondingGenerator = new RegexStringGenerator(regex, matchFullString);

            if (generator == null)
                generator = correspondingGenerator;
            else
                generator = generator.intersect(correspondingGenerator);
        }

        return generator;
    }

    private void expectOrderedResults(String... expectedValues) {
        StringGenerator generator = constructGenerator(true);

        List<String> generatedValues = new ArrayList<>();
        generator.generateAllValues().iterator().forEachRemaining(generatedValues::add);

        assertEquals(expectedValues.length, generatedValues.size());
        for (int i = 0; i < expectedValues.length; i++) {
            assertEquals(expectedValues[i], generatedValues.get(i));
        }
    }

    private void expectAnyOrder(String... expectedValues) {
        StringGenerator generator = constructGenerator(true);

        List<String> generatedValues = new ArrayList<>();
        generator.generateAllValues().iterator().forEachRemaining(generatedValues::add);

        assertEquals(expectedValues.length, generatedValues.size());
        generatedValues.containsAll(Arrays.asList(expectedValues));
    }

    private void expectFirstResult(String expectedValue) {
        StringGenerator generator = constructGenerator(true);

        String actualValue = StreamSupport
            .stream(
                Spliterators.spliteratorUnknownSize(
                    generator.generateAllValues().iterator(),
                    Spliterator.ORDERED),
                false)
            .limit(1)
            .findFirst().orElse(null);

        assertThat(
                actualValue,
                equalTo(expectedValue));
    }

    private void expectMatch(String subject, boolean matchFullString) {
        StringGenerator generator = constructGenerator(matchFullString);

        assertTrue(generator.matches(subject));
    }

    private void expectNoMatch(String subject, boolean matchFullString) {
        StringGenerator generator = constructGenerator(matchFullString);

        assertFalse(generator.matches(subject));
    }

    @Test
    void isStringValidUtf8() {
        String invalidStr = "a simple invalid 😘 string";
        String validStr = "a simple valid 亮 string";

        assertFalse(StringUtils.isStringValidUtf8(invalidStr));
        assertTrue(StringUtils.isStringValidUtf8(validStr));
    }

    @Test
    void isCharValidUtf8() {
        char invalidChar = 0xD83D;
        char validChar = '亮';

        assertFalse(StringUtils.isCharValidUtf8(invalidChar));
        assertTrue(StringUtils.isCharValidUtf8(validChar));
    }

    @Test
    void generateInterestingValues_withShorterThanAndContainingAndMatchingRegex_shouldBeAbleToCreateAString(){
        RegexStringGenerator matchingRegex = new RegexStringGenerator("^[a-z0-9]+\\@[a-z0-9]+\\.co(m|\\.uk)$", true);
        RegexStringGenerator containingRegex = new RegexStringGenerator("\\@", false);
        RegexStringGenerator shorterThan = new RegexStringGenerator("^.{0,20}$", true);

        StringGenerator intersected = shorterThan.intersect(matchingRegex).intersect(containingRegex);

        List<String> results = intersected.generateInterestingValues().collect(Collectors.toList());

        assertThat(results, not(empty()));
    }

    @Test
    void generateInterestingValues_withShorterThanAndMatchingRegex_shouldBeAbleToCreateAString(){
        RegexStringGenerator matchingRegex = new RegexStringGenerator("^[a-z0-9]+\\@[a-z0-9]+\\.co(m|\\.uk)$", true);
        RegexStringGenerator shorterThan = new RegexStringGenerator("^.{0,255}$", true);

        StringGenerator intersected = shorterThan.intersect(matchingRegex);

        List<String> results = intersected.generateInterestingValues().collect(Collectors.toList());

        assertThat(results, not(empty()));
    }

    @Test
    void match_withInputMatchingRequiredLength_shouldMatch(){
        RegexStringGenerator shorterThan = new RegexStringGenerator("^.{0,1}$", true);

        boolean match = shorterThan.matches("a");

        assertThat(match, is(true));
    }

    @Test
    void match_withInputMatchingFirstRequiredLength_shouldMatch(){
        RegexStringGenerator shorterThan = new RegexStringGenerator("^(.{0,1}|.{3,10})$", true);

        boolean match = shorterThan.matches("a");

        assertThat(match, is(true));
    }

    @Test
    void match_withInputMatchingSecondRequiredLength_shouldMatch(){
        RegexStringGenerator shorterThan = new RegexStringGenerator("^(.{0,1}|.{3,10})$", true);

        boolean match = shorterThan.matches("aaa");

        assertThat(match, is(true));
    }

    @Test
    void match_withInputNotMatchingAnyRequiredLength_shouldNotMatch(){
        RegexStringGenerator shorterThan = new RegexStringGenerator("^(.{0,1}|.{3,10})$", true);

        boolean match = shorterThan.matches("aa");

        assertThat(match, is(false));
    }
}
