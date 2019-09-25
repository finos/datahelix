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

import com.scottlogic.deg.common.profile.constraintdetail.ParsedGranularity;
import com.scottlogic.deg.generator.restrictions.linear.Limit;
import com.scottlogic.deg.generator.restrictions.linear.NumericRestrictions;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static com.scottlogic.deg.generator.utils.Defaults.NUMERIC_MAX_LIMIT;
import static com.scottlogic.deg.generator.utils.Defaults.NUMERIC_MIN_LIMIT;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNot.not;

public class NumericRestrictionsTests {

    @Test
    void equals_whenNumericRestrictionsAreEqual_returnsTrue() {
        NumericRestrictions restriction1 = new NumericRestrictions(
            new Limit<>(new BigDecimal(0), false),
            new Limit<>(new BigDecimal(2), false));
        NumericRestrictions restriction2 = new NumericRestrictions(
            new Limit<>(new BigDecimal(0), false),
            new Limit<>(new BigDecimal(2), false));


        boolean result = restriction1.equals(restriction2);

        Assert.assertTrue(result);
    }

    @Test
    void equals_whenNumericRestrictionsNumericLimitMinValuesAreNotEqual_returnsFalse() {
        NumericRestrictions restriction1 = new NumericRestrictions(
            new Limit<>(new BigDecimal(1), false),
            new Limit<>(new BigDecimal(2), false));
        NumericRestrictions restriction2 = new NumericRestrictions(
            new Limit<>(new BigDecimal(0), false),
            new Limit<>(new BigDecimal(2), false));

        boolean result = restriction1.equals(restriction2);

        Assert.assertFalse(result);
    }

    @Test
    void equals_whenOneNumericRestrictionsLimitMinValueIsNull_returnsFalse() {
        NumericRestrictions restriction1 = new NumericRestrictions(
            NUMERIC_MIN_LIMIT,
            new Limit<>(new BigDecimal(2), false));
        NumericRestrictions restriction2 = new NumericRestrictions(
            new Limit<>(new BigDecimal(0), false),
            new Limit<>(new BigDecimal(2), false));

        boolean result = restriction1.equals(restriction2);

        Assert.assertFalse(result);
    }

    @Test
    void equals_whenNumericRestrictionsLimitMaxValuesAreNotEqual_returnsFalse() {
        NumericRestrictions restriction1 = new NumericRestrictions(
            new Limit<>(new BigDecimal(0), false),
            new Limit<>(new BigDecimal(2), false));
        NumericRestrictions restriction2 = new NumericRestrictions(
            new Limit<>(new BigDecimal(0), false),
            new Limit<>(new BigDecimal(3), false));


        boolean result = restriction1.equals(restriction2);

        Assert.assertFalse(result);
    }

    @Test
    void equals_whenOneNumericRestrictionsLimitMaxValueIsNull_returnsFalse() {
        NumericRestrictions restriction1 = new NumericRestrictions(
            new Limit<>(new BigDecimal(0), false),
            NUMERIC_MAX_LIMIT);
        NumericRestrictions restriction2 = new NumericRestrictions(
            new Limit<>(new BigDecimal(0), false),
            new Limit<>(new BigDecimal(2), false));

        boolean result = restriction1.equals(restriction2);

        Assert.assertFalse(result);
    }

    @Test
    void equals_whenNumericRestrictionsLimitsMinInclusiveValuesAreNotEqual_returnsFalse() {
        NumericRestrictions restriction1 = new NumericRestrictions(
            new Limit<>(new BigDecimal(0), true),
            new Limit<>(new BigDecimal(2), false));
        NumericRestrictions restriction2 = new NumericRestrictions(
            new Limit<>(new BigDecimal(0), false),
            new Limit<>(new BigDecimal(2), false));

        boolean result = restriction1.equals(restriction2);

        Assert.assertFalse(result);
    }

    @Test
    void equals_whenNumericRestrictionsLimitsMaxInclusiveValuesAreNotEqual_returnsFalse() {
        NumericRestrictions restriction1 = new NumericRestrictions(
            new Limit<>(new BigDecimal(0), true),
            new Limit<>(new BigDecimal(2), false));
        NumericRestrictions restriction2 = new NumericRestrictions(
            new Limit<>(new BigDecimal(0), true),
            new Limit<>(new BigDecimal(2), true));

        boolean result = restriction1.equals(restriction2);

        Assert.assertFalse(result);
    }


    @Test
    void equals_whenNumericRestrictionsLimitsAreEqualAndNegative_returnsTrue() {
        NumericRestrictions restriction1 = new NumericRestrictions(
            new Limit<>(new BigDecimal(-1), false),
            new Limit<>(new BigDecimal(-1), false));
        NumericRestrictions restriction2 = new NumericRestrictions(
            new Limit<>(new BigDecimal(-1), false),
            new Limit<>(new BigDecimal(-1), false));

        boolean result = restriction1.equals(restriction2);

        Assert.assertTrue(result);
    }


    @Test
    void equals_whenOneNumericRestrictionsLimitIsOfScientificNotationButAllValuesAreEqual_returnsTrue() {
        NumericRestrictions restriction1 = new NumericRestrictions(
            new Limit<>(new BigDecimal(50), false),
            new Limit<>(new BigDecimal(100), false));
        NumericRestrictions restriction2 = new NumericRestrictions(
            new Limit<>(new BigDecimal(5E1), false),
            new Limit<>(new BigDecimal(100), false));

        boolean result = restriction1.equals(restriction2);

        Assert.assertTrue(result);
    }

    @Test
    void hashCode_whenNumericRestrictionsAreEqual_returnsEqualHashCode() {
        NumericRestrictions restriction1 = new NumericRestrictions(
            new Limit<>(new BigDecimal(10), false),
            new Limit<>(new BigDecimal(30), false));
        NumericRestrictions restriction2 = new NumericRestrictions(
            new Limit<>(new BigDecimal(10), false),
            new Limit<>(new BigDecimal(30), false));

        int hashCode1 = restriction1.hashCode();
        int hashCode2 = restriction2.hashCode();

        Assert.assertEquals(hashCode1, hashCode2);
    }

    @Test
    void hashCode_whenNumericRestrictionLimitsAreInverted_returnsFalse() {
        NumericRestrictions restriction1 = new NumericRestrictions(
            new Limit<>(new BigDecimal(10), false),
            new Limit<>(new BigDecimal(20), false));
        NumericRestrictions restriction2 = new NumericRestrictions(
            new Limit<>(new BigDecimal(20), false),
            new Limit<>(new BigDecimal(10), false));

        int hashCode1 = restriction1.hashCode();
        int hashCode2 = restriction2.hashCode();

        Assert.assertNotEquals(hashCode1, hashCode2);
    }

    @Test
    void hashCode_whenNumericRestrictionsLimitsMinInclusiveValuesAreNotEqual_returnsFalse() {
        NumericRestrictions restriction1 = new NumericRestrictions(
            new Limit<>(new BigDecimal(0), true),
            new Limit<>(new BigDecimal(2), false));
        NumericRestrictions restriction2 = new NumericRestrictions(
            new Limit<>(new BigDecimal(0), false),
            new Limit<>(new BigDecimal(2), false));

        int hashCode1 = restriction1.hashCode();
        int hashCode2 = restriction2.hashCode();

        Assert.assertNotEquals(hashCode1, hashCode2);
    }

    @Test
    void hashCode_whenNumericRestrictionsLimitsMaxInclusiveValuesAreNotEqual_returnsFalse() {
        NumericRestrictions restriction1 = new NumericRestrictions(
            new Limit<>(new BigDecimal(0), false),
            new Limit<>(new BigDecimal(2), true));
        NumericRestrictions restriction2 = new NumericRestrictions(
            new Limit<>(new BigDecimal(0), false),
            new Limit<>(new BigDecimal(2), false));

        int hashCode1 = restriction1.hashCode();
        int hashCode2 = restriction2.hashCode();

        Assert.assertNotEquals(hashCode1, hashCode2);
    }

    @Test
    void hashCode_whenOneNumericRestrictionsLimitIsOfScientificNotationButAllValuesAreEqual_returnsTrue() {
        NumericRestrictions restriction1 = new NumericRestrictions(
            new Limit<>(new BigDecimal(50), false),
            new Limit<>(new BigDecimal(2), false));
        NumericRestrictions restriction2 = new NumericRestrictions(
            new Limit<>(new BigDecimal(5E1), false),
            new Limit<>(new BigDecimal(2), false));

        int hashCode1 = restriction1.hashCode();
        int hashCode2 = restriction2.hashCode();

        Assert.assertEquals(hashCode1, hashCode2);
    }

    @Test
    public void shouldBeEqualIfNumericScaleIsTheSame(){
        NumericRestrictions a = restrictions(0.1);
        NumericRestrictions b = restrictions(0.1);

        Assert.assertThat(a, equalTo(b));
        Assert.assertThat(a.hashCode(), equalTo(b.hashCode()));
    }

    @Test
    public void shouldBeUnequalIfNumericScalesAreDifferent(){
        NumericRestrictions a = restrictions(0.1);
        NumericRestrictions b = restrictions(0.01);

        Assert.assertThat(a, not(equalTo(b)));
    }

    @Test
    public void limitsShouldBeCappedAtTheMaximumValueAllowedForBigDecimal() {
        Limit<BigDecimal> limit = new Limit<>(new BigDecimal("1e21"),true);
        NumericRestrictions restrictions = new NumericRestrictions(NUMERIC_MIN_LIMIT, limit,  1);

        Assert.assertFalse(restrictions.getMax().getValue().compareTo(NUMERIC_MAX_LIMIT.getValue()) > 0);

    }

    @Test
    public void limitsShouldBeCappedAtTheMinimumValueAllowedForBigDecimal() {
        Limit<BigDecimal> limit = new Limit<>(new BigDecimal("-1e21"),true);
        NumericRestrictions restrictions = new NumericRestrictions(limit, NUMERIC_MAX_LIMIT,  1);

        Assert.assertFalse(restrictions.getMin().getValue().compareTo(NUMERIC_MIN_LIMIT.getValue()) < 0);

    }

    private static NumericRestrictions restrictions(double numericScale){
        NumericRestrictions restrictions = new NumericRestrictions(
            NUMERIC_MIN_LIMIT, NUMERIC_MAX_LIMIT,
            ParsedGranularity.parse(BigDecimal.valueOf(numericScale)).getNumericGranularity().scale()
        );

        return restrictions;
    }

}


