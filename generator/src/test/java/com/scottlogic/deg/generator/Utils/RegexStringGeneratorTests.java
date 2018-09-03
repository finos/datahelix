package com.scottlogic.deg.generator.Utils;

import com.scottlogic.deg.generator.utils.IStringGenerator;
import com.scottlogic.deg.generator.utils.IterableAsStream;
import com.scottlogic.deg.generator.utils.JavaUtilRandomNumberGenerator;
import com.scottlogic.deg.generator.utils.RegexStringGenerator;
import org.hamcrest.core.Is;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.arrayContainingInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.matchesPattern;
import static org.hamcrest.collection.IsIterableContainingInOrder.contains;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class RegexStringGeneratorTests {
    @Test
    void shouldGenerateStringsInLexicographicalOrder() {
        givenRegex("aa(bb|cc)d?");

        expectOrderedResults(
            "aabb",
            "aabbd",
            "aacc",
            "aaccd");
    }

    @Test
    void shouldCorrectlyIterateFiniteResults() {
        givenRegex("xyz(xyz)?xyz");

        expectOrderedResults("xyzxyz", "xyzxyzxyz");
    }

    @Test
    void shouldCorrectlyReplaceCharacterGroups() {
        givenRegex("\\d");

        expectFirstResult("0");
    }

    @Test
    void shouldCreateBoundaryValues() {
        IStringGenerator generator = new RegexStringGenerator("Test_(\\d{3}|[A-Z]{5})_(banana|apple)");

        Iterable<String> resultsIterable = generator.generateBoundaryValues();

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
    void shouldCorrectlySampleInfiniteResults() {
        IStringGenerator generator = new RegexStringGenerator("[a]+");

        Iterable<String> resultsIterable = generator.generateRandomValues(new JavaUtilRandomNumberGenerator(0));

        List<String> sampleValues =
            IterableAsStream.convert(resultsIterable)
                .limit(1000)
                .collect(Collectors.toList());

        Assert.assertThat(sampleValues, not(contains(null, "")));
    }

    @Test
    void shouldExpandSingletons() {
        IStringGenerator generator = new RegexStringGenerator("THIS_IS_A_SINGLETON");
        Assert.assertThat(generator.getValueCount(), Is.is(1L));
    }

    @Test
    void shouldProduceIntersection() {
        IStringGenerator infiniteGenerator = new RegexStringGenerator("[a-z]+");

        IStringGenerator rangeGenerator = new RegexStringGenerator("(a|b){1,10}");

        IStringGenerator actual = infiniteGenerator.intersect(rangeGenerator);

        Assert.assertThat(actual.isFinite(), Is.is(true));

        List<String> actualResults = new ArrayList<>();
        actual.generateAllValues().iterator().forEachRemaining(actualResults::add);

        Assert.assertThat(actualResults.size(), Is.is(2046));
    }

    @Test
    void shouldProduceComplement() {
        IStringGenerator limitedRangeGenerator = new RegexStringGenerator("[a-m]");
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
        IStringGenerator infiniteGenerator = new RegexStringGenerator(".*");

        assertThrows(
            UnsupportedOperationException.class,
            infiniteGenerator::getValueCount);
    }

    @Test
    void shouldThrowWhenGeneratingAllFromNonFinite() {
        IStringGenerator infiniteGenerator = new RegexStringGenerator(".*");

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

    private IStringGenerator constructGenerator() {
        IStringGenerator generator = null;
        for (String regex : regexes){
            RegexStringGenerator correspondingGenerator = new RegexStringGenerator(regex);

            if (generator == null)
                generator = correspondingGenerator;
            else
                generator = generator.intersect(correspondingGenerator);
        }

        return generator;
    }

    private void expectOrderedResults(String... expectedValues) {
        IStringGenerator generator = constructGenerator();

        List<String> generatedValues = new ArrayList<>();
        for (String generatedValue : generator.generateAllValues()) {
            generatedValues.add(generatedValue);
        }

        Assert.assertThat(
            generatedValues,
            equalTo(Arrays.asList(expectedValues)));
    }

    private void expectFirstResult(String expectedValue) {
        IStringGenerator generator = constructGenerator();

        String actualValue = generator.generateAllValues().iterator().next();

        Assert.assertThat(
            actualValue,
            equalTo(expectedValue));
    }
}
