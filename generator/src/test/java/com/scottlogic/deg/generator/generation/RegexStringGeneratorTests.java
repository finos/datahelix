package com.scottlogic.deg.generator.generation;

import com.scottlogic.deg.generator.utils.IterableAsStream;
import com.scottlogic.deg.generator.utils.JavaUtilRandomNumberGenerator;
import org.hamcrest.core.Is;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.collection.IsIterableContainingInOrder.contains;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

public class RegexStringGeneratorTests {
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

        Iterable<String> resultsIterable = generator.generateInterestingValues();

        Assert.assertThat(
            resultsIterable,
            containsInAnyOrder(
                "Test_000_apple",
                "Test_AAAAA_banana"));
    }

    @Test
    void interestingValuesShouldBePrintable() {
        StringGenerator generator = new RegexStringGenerator("Test.Test", true);

        Iterable<String> resultsIterable = generator.generateInterestingValues();

        for (String interestingValue : resultsIterable) {
            for (char character : interestingValue.toCharArray()) {
                Assert.assertThat(character, greaterThanOrEqualTo((char)32));
            }
        }
    }

    @Test
    void interestingValuesShouldBeBounds() {
        StringGenerator generator = new RegexStringGenerator(".{10,20}", true);

        Iterable<String> resultsIterable = generator.generateInterestingValues();

        ArrayList<String> results = new ArrayList<>();

        resultsIterable.iterator().forEachRemaining(results::add);

        Assert.assertThat(results.size(), Is.is(2));
        Assert.assertThat(results.get(0).length(), Is.is(10));
        Assert.assertThat(results.get(1).length(), Is.is(20));
    }

    @Test
    void iterableShouldBeRepeatable() {
        StringGenerator generator = new RegexStringGenerator("Test", true);

        Iterable<String> resultsIterable = generator.generateInterestingValues();

        resultsIterable.iterator().forEachRemaining(string -> {
        }); // once

        resultsIterable.iterator().forEachRemaining(string -> {
        }); // twice
    }

    @Test
    void shouldCreateZeroLengthInterestingValue() {
        StringGenerator generator = new RegexStringGenerator("(Test)?", true);

        Iterable<String> resultsIterable = generator.generateInterestingValues();

        String[] sampleValues =
                IterableAsStream.convert(resultsIterable)
                        .toArray(String[]::new);

        Assert.assertThat(
                sampleValues,
                arrayContainingInAnyOrder(
                        "",
                        "Test"));
    }

    @Test
    void shouldCorrectlySampleInfiniteResults() {
        StringGenerator generator = new RegexStringGenerator("[a]+", false);

        Iterable<String> resultsIterable = generator.generateRandomValues(new JavaUtilRandomNumberGenerator(0));

        List<String> sampleValues =
                IterableAsStream.convert(resultsIterable)
                        .limit(1000)
                        .collect(Collectors.toList());

        Assert.assertThat(sampleValues, not(contains(null, "")));
    }

    @Test
    void shouldExpandSingletons() {
        StringGenerator generator = new RegexStringGenerator("THIS_IS_A_SINGLETON", true);
        Assert.assertThat(generator.getValueCount(), Is.is(1L));
    }

    @Test
    void shouldProduceIntersection() {
        StringGenerator infiniteGenerator = new RegexStringGenerator("[a-z]+", false);

        StringGenerator rangeGenerator = new RegexStringGenerator("(a|b){1,10}", true);

        StringGenerator actual = infiniteGenerator.intersect(rangeGenerator);

        Assert.assertThat(actual.isFinite(), Is.is(true));

        List<String> actualResults = new ArrayList<>();
        actual.generateAllValues().iterator().forEachRemaining(actualResults::add);

        Assert.assertThat(actualResults.size(), Is.is(2046));
    }

    @Test
    void shouldProduceComplement() {
        StringGenerator limitedRangeGenerator = new RegexStringGenerator("[a-m]", true);
        StringGenerator complementedGenerator = limitedRangeGenerator.complement();

        Assert.assertThat(complementedGenerator.isFinite(), equalTo(false));

        String sampleValue = complementedGenerator
                .generateRandomValues(new JavaUtilRandomNumberGenerator(0))
                .iterator().next();

        Assert.assertThat(
                sampleValue,
                not(matchesPattern("^[a-m]$")));
        //todo: more robust tests
    }

    @Test
    void shouldThrowWhenCountingNonFinite() {
        StringGenerator infiniteGenerator = new RegexStringGenerator(".*", false);

        assertThrows(
                UnsupportedOperationException.class,
                infiniteGenerator::getValueCount);
    }

    @Test
    void shouldThrowWhenGeneratingAllFromNonFinite() {
        StringGenerator infiniteGenerator = new RegexStringGenerator(".*", false);

        assertThrows(
                UnsupportedOperationException.class,
                infiniteGenerator::generateAllValues);
    }

    @Test
    void shouldReturnNoValuesWhenContradictingConstraints() {
        StringGenerator firstGenerator = new RegexStringGenerator("[b]{2}", true);
        StringGenerator secondGenerator = new RegexStringGenerator(".{0,1}", true);

        StringGenerator contradictingGenerator = firstGenerator.intersect(secondGenerator);

        Assert.assertEquals(0, contradictingGenerator.getValueCount());
    }

    @Test
    void shouldReturnValuesWhenNonContradictingConstraints() {
        StringGenerator firstGenerator = new RegexStringGenerator("[b]{2}", true);
        StringGenerator secondGenerator = new RegexStringGenerator(".{0,2}", true);

        StringGenerator nonContradictingGenerator = firstGenerator.intersect(secondGenerator);

        Assert.assertNotEquals(0, nonContradictingGenerator.getValueCount());
    }

    @Test
    void shouldNotGenerateInvalidUnicodeCodePoints() {
        StringGenerator generator = new RegexStringGenerator("[😁-😘]{1}", true);
        Iterable<String> resultsIterable = generator.generateAllValues();
        for (String s : resultsIterable) {
            if (s != null && doesStringContainSurrogates(s)) {
                fail("string contains surrogate character");
            }
        }
    }

    private final boolean doesStringContainSurrogates(String testString) {
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

        Assert.assertEquals(expectedValues.length, generatedValues.size());
        for (int i = 0; i < expectedValues.length; i++) {
            Assert.assertEquals(expectedValues[i], generatedValues.get(i));
        }
    }

    private void expectAnyOrder(String... expectedValues) {
        StringGenerator generator = constructGenerator(true);

        List<String> generatedValues = new ArrayList<>();
        generator.generateAllValues().iterator().forEachRemaining(generatedValues::add);

        Assert.assertEquals(expectedValues.length, generatedValues.size());
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

        Assert.assertThat(
                actualValue,
                equalTo(expectedValue));
    }

    private void expectMatch(String subject, boolean matchFullString) {
        StringGenerator generator = constructGenerator(matchFullString);

        Assert.assertTrue(generator.match(subject));
    }

    private void expectNoMatch(String subject, boolean matchFullString) {
        StringGenerator generator = constructGenerator(matchFullString);

        Assert.assertFalse(generator.match(subject));
    }
}
