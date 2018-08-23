package com.scottlogic.deg.generator.Utils;

import com.scottlogic.deg.generator.utils.IStringGenerator;
import com.scottlogic.deg.generator.utils.StringGenerator;
import org.hamcrest.core.Is;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.not;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.collection.IsIterableContainingInOrder.contains;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class StringGeneratorTests {

    @Test
    void shouldGenerateStringsInLexicographicalOrder() {

        IStringGenerator generator = new StringGenerator("aa(bb|cc)d?", new TestRandomGenerator());

        Assert.assertThat(generator.getValueCount(), Is.is(4L));

        Assert.assertThat(generator.IsFinite(), Is.is(true));
        Assert.assertThat(generator.getMatchedString(1), Is.is("aabb"));
        Assert.assertThat(generator.getMatchedString(2), Is.is("aabbd"));
        Assert.assertThat(generator.getMatchedString(3), Is.is("aacc"));
        Assert.assertThat(generator.getMatchedString(4), Is.is("aaccd"));
    }

    @Test
    void shouldCorrectlyIterateFiniteResults() {

        IStringGenerator generator = new StringGenerator("xyz(xyz)?xyz", new TestRandomGenerator());

        List<String> actual = new ArrayList<>();
        generator.generateAllValues().forEachRemaining(actual::add);

        Assert.assertThat(actual, contains("xyzxyz", "xyzxyzxyz"));
    }

    @Test
    void shouldCorrectlyReplaceCharacterGroups() {

        IStringGenerator generator = new StringGenerator("\\d", new TestRandomGenerator());
        String actual = generator.getMatchedString(1);

        Assert.assertThat(actual, Is.is("0"));

    }

    @Test
    void shouldCorrectlyIterateInfiniteResults() {

        IStringGenerator generator = new StringGenerator("[a]+", new TestRandomGenerator());

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
        IStringGenerator generator = new StringGenerator("THIS_IS_A_SINGLETON", new TestRandomGenerator());
        Assert.assertThat(generator.canProduceValues(), Is.is(true));
        Assert.assertThat(generator.getValueCount(), Is.is(1L));
    }

    @Test
    void shouldProduceIntersection() {

        IStringGenerator infiniteGenerator = new StringGenerator("[a-z]+", new TestRandomGenerator());
        IStringGenerator rangeGenerator = new StringGenerator("(a|b){1,10}", new TestRandomGenerator());

        IStringGenerator actual = infiniteGenerator.intersect(rangeGenerator);

        Assert.assertThat(actual.IsFinite(), Is.is(true));

        List<String> actualResults = new ArrayList<>();
        actual.generateAllValues().forEachRemaining(actualResults::add);

        Assert.assertThat(actualResults.size(), Is.is(2046));

    }

    @Test
    void shouldProduceCompliment() {

        IStringGenerator limitedRangeGenerator = new StringGenerator("[a-m]", new TestRandomGenerator());
        IStringGenerator complimentedGenerator = limitedRangeGenerator.complement();

        Assert.assertThat(complimentedGenerator.IsFinite(), Is.is(false));

        String actual = complimentedGenerator.generateRandomValue(1);

        int firstChar = (int) actual.charAt(0);
        Assert.assertThat(firstChar, not(allOf(greaterThan((int) 'a'), lessThan((int) 'm'))));
        //todo: more robust tests
    }

    @Test
    void shouldThrowWhenCountingNonFinite() {
        IStringGenerator infiniteGenerator = new StringGenerator(".*", new TestRandomGenerator());

        assertThrows(UnsupportedOperationException.class, () -> infiniteGenerator.getValueCount());
    }
}
