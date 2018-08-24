package com.scottlogic.deg.generator.Utils;

import com.scottlogic.deg.generator.utils.IStringGenerator;
import com.scottlogic.deg.generator.utils.JavaUtilRandomNumberGenerator;
import com.scottlogic.deg.generator.utils.RegexStringGenerator;
import org.hamcrest.core.Is;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.lessThan;
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
    void shouldCorrectlyIterateInfiniteResults() {

        IStringGenerator generator = new RegexStringGenerator(
            "[a]+",
            new JavaUtilRandomNumberGenerator(0));

        Iterator<String> iterator = generator.generateAllValues();

        // Generate a sample size
        List<String> actual = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            actual.add(iterator.next());
        }

        Assert.assertThat(actual, not(contains(null, "")));
    }

    @Test
    void shouldExpandSingletons() {
        IStringGenerator generator = new RegexStringGenerator(
            "THIS_IS_A_SINGLETON",
            new JavaUtilRandomNumberGenerator(0));
        Assert.assertThat(generator.canProduceValues(), Is.is(true));
        Assert.assertThat(generator.getValueCount(), Is.is(1L));
    }

    @Test
    void shouldProduceIntersection() {

        IStringGenerator infiniteGenerator = new RegexStringGenerator(
            "[a-z]+",
            new JavaUtilRandomNumberGenerator(0));

        IStringGenerator rangeGenerator = new RegexStringGenerator(
            "(a|b){1,10}",
            new JavaUtilRandomNumberGenerator(0));

        IStringGenerator actual = infiniteGenerator.intersect(rangeGenerator);

        Assert.assertThat(actual.IsFinite(), Is.is(true));

        List<String> actualResults = new ArrayList<>();
        actual.generateAllValues().forEachRemaining(actualResults::add);

        Assert.assertThat(actualResults.size(), Is.is(2046));

    }

    @Test
    void shouldProduceComplement() {

        IStringGenerator limitedRangeGenerator = new RegexStringGenerator(
            "[a-m]",
            new JavaUtilRandomNumberGenerator(0));
        IStringGenerator complimentedGenerator = limitedRangeGenerator.complement();

        Assert.assertThat(complimentedGenerator.IsFinite(), Is.is(false));

        String actual = complimentedGenerator.generateRandomValue(1);

        int firstChar = (int) actual.charAt(0);
        Assert.assertThat(firstChar, not(allOf(greaterThan((int) 'a'), lessThan((int) 'm'))));
        //todo: more robust tests
    }

    @Test
    void shouldThrowWhenCountingNonFinite() {
        IStringGenerator infiniteGenerator = new RegexStringGenerator(
            ".*",
            new JavaUtilRandomNumberGenerator(0));

        assertThrows(UnsupportedOperationException.class, () -> infiniteGenerator.getValueCount());
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
            RegexStringGenerator correspondingGenerator = new RegexStringGenerator(
                regex,
                new JavaUtilRandomNumberGenerator(0));

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
        Iterator<String> iterator = generator.generateAllValues();

        while (iterator.hasNext()) {
            generatedValues.add(iterator.next());
        }

        Assert.assertThat(
            generatedValues,
            equalTo(Arrays.asList(expectedValues)));
    }

    private void expectFirstResult(String expectedValue) {
        IStringGenerator generator = constructGenerator();

        String actualValue = generator.generateAllValues().next();

        Assert.assertThat(
            actualValue,
            equalTo(expectedValue));
    }
}
