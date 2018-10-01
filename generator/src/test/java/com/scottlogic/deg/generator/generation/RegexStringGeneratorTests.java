package com.scottlogic.deg.generator.generation;

import com.scottlogic.deg.generator.utils.IterableAsStream;
import com.scottlogic.deg.generator.utils.JavaUtilRandomNumberGenerator;
import org.hamcrest.core.Is;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.collection.IsIterableContainingInOrder.contains;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
    void shouldGenerateStringsInLexicographicalOrder() {
        givenRegex("^aa(bb|cc)d?$");

        expectOrderedResults(
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
    void shouldCreateInterestingValues() {
        IStringGenerator generator = new RegexStringGenerator("Test_(\\d{3}|[A-Z]{5})_(banana|apple)", true);

        Iterable<String> resultsIterable = generator.generateInterestingValues();

        String[] sampleValues =
                IterableAsStream.convert(resultsIterable)
                        .toArray(String[]::new);

        Assert.assertThat(
                sampleValues,
                arrayContainingInAnyOrder(
                        "Test_000_banana",
                        "Test_000_apple",
                        "Test_AAAAA_apple",
                        "Test_AAAAA_banana"));
    }

    @Test
    void interestingValuesShouldBePrintable() {
        IStringGenerator generator = new RegexStringGenerator("Test.Test", true);

        Iterable<String> resultsIterable = generator.generateInterestingValues();

        for (String interestingValue : resultsIterable) {
            for (char character : interestingValue.toCharArray()) {
                Assert.assertThat(character, greaterThanOrEqualTo((char)32));
            }
        }
    }

    @Test
    void interestingValuesShouldBeBounds() {
        IStringGenerator generator = new RegexStringGenerator(".{10,20}", true);

        Iterable<String> resultsIterable = generator.generateInterestingValues();

        ArrayList<String> results = new ArrayList<>();

        resultsIterable.iterator().forEachRemaining(results::add);

        Assert.assertThat(results.size(), Is.is(2));
        Assert.assertThat(results.get(0).length(), Is.is(10));
        Assert.assertThat(results.get(1).length(), Is.is(20));
    }

    @Test
    void iterableShouldBeRepeatable() {
        IStringGenerator generator = new RegexStringGenerator("Test", true);

        Iterable<String> resultsIterable = generator.generateInterestingValues();

        resultsIterable.iterator().forEachRemaining(string -> {
        }); // once

        resultsIterable.iterator().forEachRemaining(string -> {
        }); // twice
    }

    @Test
    void shouldCreateZeroLengthInterestingValue() {
        IStringGenerator generator = new RegexStringGenerator("(Test)?", true);

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
        IStringGenerator generator = new RegexStringGenerator("[a]+", false);

        Iterable<String> resultsIterable = generator.generateRandomValues(new JavaUtilRandomNumberGenerator(0));

        List<String> sampleValues =
                IterableAsStream.convert(resultsIterable)
                        .limit(1000)
                        .collect(Collectors.toList());

        Assert.assertThat(sampleValues, not(contains(null, "")));
    }

    @Test
    void shouldExpandSingletons() {
        IStringGenerator generator = new RegexStringGenerator("THIS_IS_A_SINGLETON", true);
        Assert.assertThat(generator.getValueCount(), Is.is(1L));
    }

    @Test
    void shouldProduceIntersection() {
        IStringGenerator infiniteGenerator = new RegexStringGenerator("[a-z]+", false);

        IStringGenerator rangeGenerator = new RegexStringGenerator("(a|b){1,10}", true);

        IStringGenerator actual = infiniteGenerator.intersect(rangeGenerator);

        Assert.assertThat(actual.isFinite(), Is.is(true));

        List<String> actualResults = new ArrayList<>();
        actual.generateAllValues().iterator().forEachRemaining(actualResults::add);

        Assert.assertThat(actualResults.size(), Is.is(2046));
    }

    @Test
    void shouldProduceComplement() {
        IStringGenerator limitedRangeGenerator = new RegexStringGenerator("[a-m]", true);
        IStringGenerator complementedGenerator = limitedRangeGenerator.complement();

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
        IStringGenerator infiniteGenerator = new RegexStringGenerator(".*", false);

        assertThrows(
                UnsupportedOperationException.class,
                infiniteGenerator::getValueCount);
    }

    @Test
    void shouldThrowWhenGeneratingAllFromNonFinite() {
        IStringGenerator infiniteGenerator = new RegexStringGenerator(".*", false);

        assertThrows(
                UnsupportedOperationException.class,
                infiniteGenerator::generateAllValues);
    }


    private final List<String> regexes = new ArrayList<>();

    private void givenRegex(String regex) {
        this.regexes.add(regex);
    }

    @BeforeEach
    private void beforeEach() {
        this.regexes.clear();
    }

    private IStringGenerator constructGenerator(boolean matchFullString) {
        IStringGenerator generator = null;
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
        IStringGenerator generator = constructGenerator(true);

        List<String> generatedValues = new ArrayList<>();
        for (String generatedValue : generator.generateAllValues()) {
//            System.out.println("generated: " + generatedValue);
            generatedValues.add(generatedValue);
        }

        String actual = generatedValues.get(0);
        String expected = Arrays.asList(expectedValues).get(0);

//        System.out.println("actual: " + actual);
//        System.out.println("expected: " + expected);

        Assert.assertThat(
                actual,
                equalTo(expected));
    }

    private void expectFirstResult(String expectedValue) {
        IStringGenerator generator = constructGenerator(true);

        String actualValue = generator.generateAllValues().iterator().next();

        Assert.assertThat(
                actualValue,
                equalTo(expectedValue));
    }

    private void expectMatch(String subject, boolean matchFullString) {
        IStringGenerator generator = constructGenerator(matchFullString);

        Assert.assertTrue(generator.match(subject));
    }

    private void expectNoMatch(String subject, boolean matchFullString) {
        IStringGenerator generator = constructGenerator(matchFullString);

        Assert.assertFalse(generator.match(subject));
    }
}
