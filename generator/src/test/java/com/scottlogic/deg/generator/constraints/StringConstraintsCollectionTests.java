package com.scottlogic.deg.generator.constraints;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.constraints.StringConstraintsCollection;
import com.scottlogic.deg.generator.constraints.atomic.IsStringLongerThanConstraint;
import com.scottlogic.deg.generator.constraints.atomic.IsStringShorterThanConstraint;
import com.scottlogic.deg.generator.constraints.atomic.StringHasLengthConstraint;
import com.scottlogic.deg.generator.inputs.RuleInformation;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.core.Is.is;

public class StringConstraintsCollectionTests {
    private static final Field field = new Field("field");
    private static final Set<RuleInformation> rules = Collections.emptySet();

    @Test
    public void shouldReportShorterThan5AndLongerThan10AsAContradiction(){
        IsStringShorterThanConstraint shorterThan5 = new IsStringShorterThanConstraint(field, 5, rules);
        IsStringLongerThanConstraint longerThan10 = new IsStringLongerThanConstraint(field, 10, rules);

        StringConstraintsCollection constraints = new StringConstraintsCollection(
            new HashSet<>(Arrays.asList(shorterThan5, longerThan10)));

        Assert.assertThat(constraints.isContradictory(), is(true));
    }

    @Test
    public void shouldReportShorterThan5AndLongerThan5AsAContradiction(){
        IsStringShorterThanConstraint shorterThan5 = new IsStringShorterThanConstraint(field, 5, rules);
        IsStringLongerThanConstraint longerThan5 = new IsStringLongerThanConstraint(field, 5, rules);

        StringConstraintsCollection constraints = new StringConstraintsCollection(
            new HashSet<>(Arrays.asList(shorterThan5, longerThan5)));

        Assert.assertThat(constraints.isContradictory(), is(true));
    }

    @Test
    public void shouldReportShorterThan5AndLongerThan4AsAContradiction(){
        IsStringShorterThanConstraint shorterThan5 = new IsStringShorterThanConstraint(field, 5, rules);
        IsStringLongerThanConstraint longerThan4 = new IsStringLongerThanConstraint(field, 4, rules);

        StringConstraintsCollection constraints = new StringConstraintsCollection(
            new HashSet<>(Arrays.asList(shorterThan5, longerThan4)));

        Assert.assertThat(constraints.isContradictory(), is(true));
    }

    @Test
    public void shouldReportShorterThan5AndLongerThan3AsNonContradictory(){
        IsStringShorterThanConstraint shorterThan5 = new IsStringShorterThanConstraint(field, 5, rules);
        IsStringLongerThanConstraint longerThan3 = new IsStringLongerThanConstraint(field, 3, rules);

        StringConstraintsCollection constraints = new StringConstraintsCollection(
            new HashSet<>(Arrays.asList(shorterThan5, longerThan3)));

        Assert.assertThat(constraints.isContradictory(), is(false));
    }

    @Test
    public void shouldReportShorterThan5AndOfLength5AsAContradiction(){
        IsStringShorterThanConstraint shorterThan5 = new IsStringShorterThanConstraint(field, 5, rules);
        StringHasLengthConstraint ofLength5 = new StringHasLengthConstraint(field, 5, rules);

        StringConstraintsCollection constraints = new StringConstraintsCollection(
            new HashSet<>(Arrays.asList(shorterThan5, ofLength5)));

        Assert.assertThat(constraints.isContradictory(), is(true));
    }

    @Test
    public void shouldReportShorterThan5AndOfLength10AsAContradiction(){
        IsStringShorterThanConstraint shorterThan5 = new IsStringShorterThanConstraint(field, 5, rules);
        StringHasLengthConstraint ofLength10 = new StringHasLengthConstraint(field, 10, rules);

        StringConstraintsCollection constraints = new StringConstraintsCollection(
            new HashSet<>(Arrays.asList(shorterThan5, ofLength10)));

        Assert.assertThat(constraints.isContradictory(), is(true));
    }

    @Test
    public void shouldReportShorterThan5AndOfLength1AsNonContradictory(){
        IsStringShorterThanConstraint shorterThan5 = new IsStringShorterThanConstraint(field, 5, rules);
        StringHasLengthConstraint ofLength1 = new StringHasLengthConstraint(field, 1, rules);

        StringConstraintsCollection constraints = new StringConstraintsCollection(
            new HashSet<>(Arrays.asList(shorterThan5, ofLength1)));

        Assert.assertThat(constraints.isContradictory(), is(false));
    }

    @Test
    public void shouldReportShorterThan5AndOfLength4AsNonContradictory(){
        IsStringShorterThanConstraint shorterThan5 = new IsStringShorterThanConstraint(field, 5, rules);
        StringHasLengthConstraint ofLength4 = new StringHasLengthConstraint(field, 4, rules);

        StringConstraintsCollection constraints = new StringConstraintsCollection(
            new HashSet<>(Arrays.asList(shorterThan5, ofLength4)));

        Assert.assertThat(constraints.isContradictory(), is(false));
    }

    @Test
    public void shouldReportLongerThan5AndOfLength5AsAContradiction(){
        IsStringLongerThanConstraint longerThan5 = new IsStringLongerThanConstraint(field, 5, rules);
        StringHasLengthConstraint ofLength5 = new StringHasLengthConstraint(field, 5, rules);

        StringConstraintsCollection constraints = new StringConstraintsCollection(
            new HashSet<>(Arrays.asList(longerThan5, ofLength5)));

        Assert.assertThat(constraints.isContradictory(), is(true));
    }

    @Test
    public void shouldReportLongerThan5AndOfLength4AsAContradiction(){
        IsStringLongerThanConstraint longerThan5 = new IsStringLongerThanConstraint(field, 5, rules);
        StringHasLengthConstraint ofLength4 = new StringHasLengthConstraint(field, 4, rules);

        StringConstraintsCollection constraints = new StringConstraintsCollection(
            new HashSet<>(Arrays.asList(longerThan5, ofLength4)));

        Assert.assertThat(constraints.isContradictory(), is(true));
    }

    @Test
    public void shouldReportLongerThan5AndOfLength6AsNonContradictory(){
        IsStringLongerThanConstraint longerThan5 = new IsStringLongerThanConstraint(field, 5, rules);
        StringHasLengthConstraint ofLength6 = new StringHasLengthConstraint(field, 6, rules);

        StringConstraintsCollection constraints = new StringConstraintsCollection(
            new HashSet<>(Arrays.asList(longerThan5, ofLength6)));

        Assert.assertThat(constraints.isContradictory(), is(false));
    }

    @Test
    public void shouldReportOfLength6AndOfLength6AsNonContradictory(){
        StringHasLengthConstraint ofLength6a = new StringHasLengthConstraint(field, 6, rules);
        StringHasLengthConstraint ofLength6b = new StringHasLengthConstraint(field, 6, rules);

        StringConstraintsCollection constraints = new StringConstraintsCollection(
            new HashSet<>(Arrays.asList(ofLength6a, ofLength6b)));

        Assert.assertThat(constraints.isContradictory(), is(false));
    }

    @Test
    public void shouldReportOfLength6AndOfLength7AsAContradiction(){
        StringHasLengthConstraint ofLength6 = new StringHasLengthConstraint(field, 6, rules);
        StringHasLengthConstraint ofLength7 = new StringHasLengthConstraint(field, 7, rules);

        StringConstraintsCollection constraints = new StringConstraintsCollection(
            new HashSet<>(Arrays.asList(ofLength6, ofLength7)));

        Assert.assertThat(constraints.isContradictory(), is(true));
    }

    @Test
    public void shouldReportNegatedShorterOfLengthConstraintsAsNonContradictory(){
        StringHasLengthConstraint ofLength6 = new StringHasLengthConstraint(field, 6, rules);
        StringHasLengthConstraint ofLength7 = new StringHasLengthConstraint(field, 7, rules);

        StringConstraintsCollection constraints = new StringConstraintsCollection(
            new HashSet<>(Arrays.asList(ofLength6.negate(), ofLength7)));

        Assert.assertThat(constraints.isContradictory(), is(false));
    }

    @Test
    public void shouldReportNegatedLongerOfLengthConstraintsAsNonContradictory(){
        StringHasLengthConstraint ofLength6 = new StringHasLengthConstraint(field, 7, rules);
        StringHasLengthConstraint ofLength7 = new StringHasLengthConstraint(field, 6, rules);

        StringConstraintsCollection constraints = new StringConstraintsCollection(
            new HashSet<>(Arrays.asList(ofLength6.negate(), ofLength7)));

        Assert.assertThat(constraints.isContradictory(), is(false));
    }

    @Test
    public void shouldReportNotShorterThan5AndNotOfLength6AsNonContradictory(){
        IsStringShorterThanConstraint shorterThan5 = new IsStringShorterThanConstraint(field, 5, rules);
        StringHasLengthConstraint ofLength6 = new StringHasLengthConstraint(field, 6, rules);

        StringConstraintsCollection constraints = new StringConstraintsCollection(
            new HashSet<>(Arrays.asList(shorterThan5.negate(), ofLength6.negate())));

        Assert.assertThat(constraints.isContradictory(), is(false));
    }

    @Test
    public void shouldReportNotShorterThan5AndOfLength5AsNonContradictory(){
        IsStringShorterThanConstraint shorterThan5 = new IsStringShorterThanConstraint(field, 5, rules);
        StringHasLengthConstraint ofLength5 = new StringHasLengthConstraint(field, 5, rules);

        StringConstraintsCollection constraints = new StringConstraintsCollection(
            new HashSet<>(Arrays.asList(shorterThan5.negate(), ofLength5)));

        Assert.assertThat(constraints.isContradictory(), is(false));
    }

    @Test
    public void shouldReportNotShorterThan5AndOfLength4AsAContradiction(){
        IsStringShorterThanConstraint shorterThan5 = new IsStringShorterThanConstraint(field, 5, rules);
        StringHasLengthConstraint ofLength4 = new StringHasLengthConstraint(field, 4, rules);

        StringConstraintsCollection constraints = new StringConstraintsCollection(
            new HashSet<>(Arrays.asList(shorterThan5.negate(), ofLength4)));

        Assert.assertThat(constraints.isContradictory(), is(true));
    }

    @Test
    public void shouldReportNotLongerThan5AndOfLength4AsNonContradictory(){
        IsStringLongerThanConstraint longerThan5 = new IsStringLongerThanConstraint(field, 5, rules);
        StringHasLengthConstraint ofLength4 = new StringHasLengthConstraint(field, 4, rules);

        StringConstraintsCollection constraints = new StringConstraintsCollection(
            new HashSet<>(Arrays.asList(longerThan5.negate(), ofLength4)));

        Assert.assertThat(constraints.isContradictory(), is(false));
    }

    @Test
    public void shouldReportNotLongerThan5AndNotOfLength4AsNonContradictory(){
        IsStringLongerThanConstraint longerThan5 = new IsStringLongerThanConstraint(field, 5, rules);
        StringHasLengthConstraint ofLength4 = new StringHasLengthConstraint(field, 4, rules);

        StringConstraintsCollection constraints = new StringConstraintsCollection(
            new HashSet<>(Arrays.asList(longerThan5.negate(), ofLength4.negate())));

        Assert.assertThat(constraints.isContradictory(), is(false));
    }

    @Test
    public void shouldReportNotLongerThan5AndOfLength5AsNonContradictory(){
        IsStringLongerThanConstraint longerThan5 = new IsStringLongerThanConstraint(field, 5, rules);
        StringHasLengthConstraint ofLength5 = new StringHasLengthConstraint(field, 5, rules);

        StringConstraintsCollection constraints = new StringConstraintsCollection(
            new HashSet<>(Arrays.asList(longerThan5.negate(), ofLength5)));

        Assert.assertThat(constraints.isContradictory(), is(false));
    }

    @Test
    public void shouldReportNotLongerThan5AndOfLength6AsAContradiction(){
        IsStringLongerThanConstraint longerThan5 = new IsStringLongerThanConstraint(field, 5, rules);
        StringHasLengthConstraint ofLength6 = new StringHasLengthConstraint(field, 6, rules);

        StringConstraintsCollection constraints = new StringConstraintsCollection(
            new HashSet<>(Arrays.asList(longerThan5.negate(), ofLength6)));

        Assert.assertThat(constraints.isContradictory(), is(true));
    }

    @Test
    public void shouldReportOfLength1AndNotOfLength1AsAContradiction(){
        StringHasLengthConstraint ofLength1 = new StringHasLengthConstraint(field, 1, rules);

        StringConstraintsCollection constraints = new StringConstraintsCollection(
            new HashSet<>(Arrays.asList(ofLength1.negate(), ofLength1)));

        Assert.assertThat(constraints.isContradictory(), is(true));
    }

    @Test
    public void shouldReportLongerThan1AndNotLongerThan1AsAContradiction(){
        IsStringLongerThanConstraint longerThan1 = new IsStringLongerThanConstraint(field, 1, rules);

        StringConstraintsCollection constraints = new StringConstraintsCollection(
            new HashSet<>(Arrays.asList(longerThan1.negate(), longerThan1)));

        Assert.assertThat(constraints.isContradictory(), is(true));
    }

    @Test
    public void shouldReportShorterThan1AndNotShorterThan1AsAContradiction(){
        IsStringShorterThanConstraint shorterThan1 = new IsStringShorterThanConstraint(field, 1, rules);

        StringConstraintsCollection constraints = new StringConstraintsCollection(
            new HashSet<>(Arrays.asList(shorterThan1.negate(), shorterThan1)));

        Assert.assertThat(constraints.isContradictory(), is(true));
    }

    @Test
    public void shouldReportOfLength1AndNotShorterThan1AsNonContradictory(){
        StringHasLengthConstraint ofLength1 = new StringHasLengthConstraint(field, 1, rules);
        IsStringShorterThanConstraint shorterThan1 = new IsStringShorterThanConstraint(field, 1, rules);

        StringConstraintsCollection constraints = new StringConstraintsCollection(
            new HashSet<>(Arrays.asList(ofLength1, shorterThan1.negate())));

        Assert.assertThat(constraints.isContradictory(), is(false));
    }

    @Test
    public void shouldReportNotLongerThan1AndNotShorterThan1AsNonContradictory(){
        IsStringLongerThanConstraint longerThan1 = new IsStringLongerThanConstraint(field, 1, rules);
        IsStringShorterThanConstraint shorterThan1 = new IsStringShorterThanConstraint(field, 1, rules);

        StringConstraintsCollection constraints = new StringConstraintsCollection(
            new HashSet<>(Arrays.asList(longerThan1.negate(), shorterThan1.negate())));

        Assert.assertThat(constraints.isContradictory(), is(false));
    }
}
