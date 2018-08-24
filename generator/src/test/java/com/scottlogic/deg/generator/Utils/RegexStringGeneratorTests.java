package com.scottlogic.deg.generator.Utils;

import com.scottlogic.deg.generator.utils.IStringGenerator;
import com.scottlogic.deg.generator.utils.JavaUtilRandomNumberGenerator;
import com.scottlogic.deg.generator.utils.RegexStringGenerator;
import org.hamcrest.core.Is;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.collection.IsIterableContainingInOrder.contains;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class RegexStringGeneratorTests {

    @Test
    void shouldGenerateStringsInLexicographicalOrder() {

        IStringGenerator generator = new RegexStringGenerator(
            "aa(bb|cc)d?",
            new JavaUtilRandomNumberGenerator(0));

        Assert.assertThat(generator.getValueCount(), Is.is(4L));

        Assert.assertThat(generator.IsFinite(), Is.is(true));

        Assert.assertThat(generator.getMatchedString(1), Is.is("aabb"));
        Assert.assertThat(generator.getMatchedString(2), Is.is("aabbd"));
        Assert.assertThat(generator.getMatchedString(3), Is.is("aacc"));
        Assert.assertThat(generator.getMatchedString(4), Is.is("aaccd"));
    }

    @Test
    void shouldCorrectlyIterateFiniteResults() {

        IStringGenerator generator = new RegexStringGenerator(
            "xyz(xyz)?xyz",
            new JavaUtilRandomNumberGenerator(0));

        List<String> actual = new ArrayList<>();
        generator.generateAllValues().forEachRemaining(actual::add);

        Assert.assertThat(actual, contains("xyzxyz", "xyzxyzxyz"));
    }

    @Test
    void shouldCorrectlyReplaceCharacterGroups() {

        IStringGenerator generator = new RegexStringGenerator(
            "\\d",
            new JavaUtilRandomNumberGenerator(0));
        String actual = generator.getMatchedString(1);

        Assert.assertThat(actual, Is.is("0"));

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
    void shouldProduceCompliment() {

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
}
