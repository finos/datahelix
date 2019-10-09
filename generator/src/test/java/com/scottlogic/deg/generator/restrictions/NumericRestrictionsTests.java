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

import com.scottlogic.deg.common.profile.constraintdetail.NumericGranularityFactory;
import com.scottlogic.deg.generator.restrictions.linear.Limit;
import com.scottlogic.deg.generator.restrictions.linear.LinearRestrictions;
import com.scottlogic.deg.generator.restrictions.linear.LinearRestrictionsFactory;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static com.scottlogic.deg.common.util.Defaults.DEFAULT_NUMERIC_GRANULARITY;
import static com.scottlogic.deg.generator.utils.Defaults.NUMERIC_MAX_LIMIT;
import static com.scottlogic.deg.generator.utils.Defaults.NUMERIC_MIN_LIMIT;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNot.not;

public class NumericRestrictionsTests {

    @Test
    void equals_whenNumericRestrictionsAreEqual_returnsTrue() {
        LinearRestrictions<BigDecimal> restriction1 = LinearRestrictionsFactory.createNumericRestrictions(
            new Limit<>(new BigDecimal(0), false),
            new Limit<>(new BigDecimal(2), false));
        LinearRestrictions<BigDecimal> restriction2 = LinearRestrictionsFactory.createNumericRestrictions(
            new Limit<>(new BigDecimal(0), false),
            new Limit<>(new BigDecimal(2), false));


        boolean result = restriction1.equals(restriction2);

        Assert.assertTrue(result);
    }

    @Test
    void equals_whenNumericRestrictionsNumericLimitMinValuesAreNotEqual_returnsFalse() {
        LinearRestrictions<BigDecimal> restriction1 = LinearRestrictionsFactory.createNumericRestrictions(
            new Limit<>(new BigDecimal(1), false),
            new Limit<>(new BigDecimal(2), false));
        LinearRestrictions<BigDecimal> restriction2 = LinearRestrictionsFactory.createNumericRestrictions(
            new Limit<>(new BigDecimal(0), false),
            new Limit<>(new BigDecimal(2), false));

        boolean result = restriction1.equals(restriction2);

        Assert.assertFalse(result);
    }

    @Test
    void equals_whenOneNumericRestrictionsLimitMinValueIsNull_returnsFalse() {
        LinearRestrictions<BigDecimal> restriction1 = LinearRestrictionsFactory.createNumericRestrictions(
            NUMERIC_MIN_LIMIT,
            new Limit<>(new BigDecimal(2), false));
        LinearRestrictions<BigDecimal> restriction2 = LinearRestrictionsFactory.createNumericRestrictions(
            new Limit<>(new BigDecimal(0), false),
            new Limit<>(new BigDecimal(2), false));

        boolean result = restriction1.equals(restriction2);

        Assert.assertFalse(result);
    }

    @Test
    void equals_whenNumericRestrictionsLimitMaxValuesAreNotEqual_returnsFalse() {
        LinearRestrictions<BigDecimal> restriction1 = LinearRestrictionsFactory.createNumericRestrictions(
            new Limit<>(new BigDecimal(0), false),
            new Limit<>(new BigDecimal(2), false));
        LinearRestrictions<BigDecimal> restriction2 = LinearRestrictionsFactory.createNumericRestrictions(
            new Limit<>(new BigDecimal(0), false),
            new Limit<>(new BigDecimal(3), false));


        boolean result = restriction1.equals(restriction2);

        Assert.assertFalse(result);
    }

    @Test
    void equals_whenOneNumericRestrictionsLimitMaxValueIsNull_returnsFalse() {
        LinearRestrictions<BigDecimal> restriction1 = LinearRestrictionsFactory.createNumericRestrictions(
            new Limit<>(new BigDecimal(0), false),
            NUMERIC_MAX_LIMIT);
        LinearRestrictions<BigDecimal> restriction2 = LinearRestrictionsFactory.createNumericRestrictions(
            new Limit<>(new BigDecimal(0), false),
            new Limit<>(new BigDecimal(2), false));

        boolean result = restriction1.equals(restriction2);

        Assert.assertFalse(result);
    }

    @Test
    void equals_whenNumericRestrictionsLimitsMinInclusiveValuesAreNotEqual_returnsFalse() {
        LinearRestrictions<BigDecimal> restriction1 = LinearRestrictionsFactory.createNumericRestrictions(
            new Limit<>(new BigDecimal(0), true),
            new Limit<>(new BigDecimal(2), false));
        LinearRestrictions<BigDecimal> restriction2 = LinearRestrictionsFactory.createNumericRestrictions(
            new Limit<>(new BigDecimal(0), false),
            new Limit<>(new BigDecimal(2), false));

        boolean result = restriction1.equals(restriction2);

        Assert.assertFalse(result);
    }

    @Test
    void equals_whenNumericRestrictionsLimitsMaxInclusiveValuesAreNotEqual_returnsFalse() {
        LinearRestrictions<BigDecimal> restriction1 = LinearRestrictionsFactory.createNumericRestrictions(
            new Limit<>(new BigDecimal(0), true),
            new Limit<>(new BigDecimal(2), false));
        LinearRestrictions<BigDecimal> restriction2 = LinearRestrictionsFactory.createNumericRestrictions(
            new Limit<>(new BigDecimal(0), true),
            new Limit<>(new BigDecimal(2), true));

        boolean result = restriction1.equals(restriction2);

        Assert.assertFalse(result);
    }


    @Test
    void equals_whenNumericRestrictionsLimitsAreEqualAndNegative_returnsTrue() {
        LinearRestrictions<BigDecimal> restriction1 = LinearRestrictionsFactory.createNumericRestrictions(
            new Limit<>(new BigDecimal(-1), false),
            new Limit<>(new BigDecimal(-1), false));
        LinearRestrictions<BigDecimal> restriction2 = LinearRestrictionsFactory.createNumericRestrictions(
            new Limit<>(new BigDecimal(-1), false),
            new Limit<>(new BigDecimal(-1), false));

        boolean result = restriction1.equals(restriction2);

        Assert.assertTrue(result);
    }


    @Test
    void equals_whenOneNumericRestrictionsLimitIsOfScientificNotationButAllValuesAreEqual_returnsTrue() {
        LinearRestrictions<BigDecimal> restriction1 = LinearRestrictionsFactory.createNumericRestrictions(
            new Limit<>(new BigDecimal(50), false),
            new Limit<>(new BigDecimal(100), false));
        LinearRestrictions<BigDecimal> restriction2 = LinearRestrictionsFactory.createNumericRestrictions(
            new Limit<>(new BigDecimal(5E1), false),
            new Limit<>(new BigDecimal(100), false));

        boolean result = restriction1.equals(restriction2);

        Assert.assertTrue(result);
    }

    @Test
    void hashCode_whenNumericRestrictionsAreEqual_returnsEqualHashCode() {
        LinearRestrictions<BigDecimal> restriction1 = LinearRestrictionsFactory.createNumericRestrictions(
            new Limit<>(new BigDecimal(10), false),
            new Limit<>(new BigDecimal(30), false));
        LinearRestrictions<BigDecimal> restriction2 = LinearRestrictionsFactory.createNumericRestrictions(
            new Limit<>(new BigDecimal(10), false),
            new Limit<>(new BigDecimal(30), false));

        int hashCode1 = restriction1.hashCode();
        int hashCode2 = restriction2.hashCode();

        Assert.assertEquals(hashCode1, hashCode2);
    }

    @Test
    void hashCode_whenNumericRestrictionLimitsAreInverted_returnsFalse() {
        LinearRestrictions<BigDecimal> restriction1 = LinearRestrictionsFactory.createNumericRestrictions(
            new Limit<>(new BigDecimal(10), false),
            new Limit<>(new BigDecimal(20), false));
        LinearRestrictions<BigDecimal> restriction2 = LinearRestrictionsFactory.createNumericRestrictions(
            new Limit<>(new BigDecimal(20), false),
            new Limit<>(new BigDecimal(10), false));

        int hashCode1 = restriction1.hashCode();
        int hashCode2 = restriction2.hashCode();

        Assert.assertNotEquals(hashCode1, hashCode2);
    }

    @Test
    void hashCode_whenNumericRestrictionsLimitsMinInclusiveValuesAreNotEqual_returnsFalse() {
        LinearRestrictions<BigDecimal> restriction1 = LinearRestrictionsFactory.createNumericRestrictions(
            new Limit<>(new BigDecimal(0), true),
            new Limit<>(new BigDecimal(2), false));
        LinearRestrictions<BigDecimal> restriction2 = LinearRestrictionsFactory.createNumericRestrictions(
            new Limit<>(new BigDecimal(0), false),
            new Limit<>(new BigDecimal(2), false));

        int hashCode1 = restriction1.hashCode();
        int hashCode2 = restriction2.hashCode();

        Assert.assertNotEquals(hashCode1, hashCode2);
    }

    @Test
    void hashCode_whenNumericRestrictionsLimitsMaxInclusiveValuesAreNotEqual_returnsFalse() {
        LinearRestrictions<BigDecimal> restriction1 = LinearRestrictionsFactory.createNumericRestrictions(
            new Limit<>(new BigDecimal(0), false),
            new Limit<>(new BigDecimal(2), true));
        LinearRestrictions<BigDecimal> restriction2 = LinearRestrictionsFactory.createNumericRestrictions(
            new Limit<>(new BigDecimal(0), false),
            new Limit<>(new BigDecimal(2), false));

        int hashCode1 = restriction1.hashCode();
        int hashCode2 = restriction2.hashCode();

        Assert.assertNotEquals(hashCode1, hashCode2);
    }

    @Test
    void hashCode_whenOneNumericRestrictionsLimitIsOfScientificNotationButAllValuesAreEqual_returnsTrue() {
        LinearRestrictions<BigDecimal> restriction1 = LinearRestrictionsFactory.createNumericRestrictions(
            new Limit<>(new BigDecimal(50), false),
            new Limit<>(new BigDecimal(2), false));
        LinearRestrictions<BigDecimal> restriction2 = LinearRestrictionsFactory.createNumericRestrictions(
            new Limit<>(new BigDecimal(5E1), false),
            new Limit<>(new BigDecimal(2), false));

        int hashCode1 = restriction1.hashCode();
        int hashCode2 = restriction2.hashCode();

        Assert.assertEquals(hashCode1, hashCode2);
    }

    @Test
    public void shouldBeEqualIfNumericScaleIsTheSame(){
        LinearRestrictions<BigDecimal> a = restrictions(0.1);
        LinearRestrictions<BigDecimal> b = restrictions(0.1);

        Assert.assertThat(a, equalTo(b));
        Assert.assertThat(a.hashCode(), equalTo(b.hashCode()));
    }

    @Test
    public void shouldBeUnequalIfNumericScalesAreDifferent(){
        LinearRestrictions<BigDecimal> a = restrictions(0.1);
        LinearRestrictions<BigDecimal> b = restrictions(0.01);

        Assert.assertThat(a, not(equalTo(b)));
    }

    @Test
    public void limitsShouldBeCappedAtTheMaximumValueAllowedForBigDecimal() {
        Limit<BigDecimal> limit = new Limit<>(new BigDecimal("1e21"),true);
        LinearRestrictions<BigDecimal> restrictions = LinearRestrictionsFactory.createNumericRestrictions(NUMERIC_MIN_LIMIT, limit);

        Assert.assertFalse(restrictions.getMax().compareTo(NUMERIC_MAX_LIMIT.getValue()) > 0);

    }

    @Test
    public void limitsShouldBeCappedAtTheMinimumValueAllowedForBigDecimal() {
        Limit<BigDecimal> limit = new Limit<>(new BigDecimal("-1e21"),true);
        LinearRestrictions<BigDecimal> restrictions = LinearRestrictionsFactory.createNumericRestrictions(limit, NUMERIC_MAX_LIMIT);

        Assert.assertFalse(restrictions.getMin().compareTo(NUMERIC_MIN_LIMIT.getValue()) < 0);

    }

    private static LinearRestrictions<BigDecimal> restrictions(double numericScale){
        LinearRestrictions<BigDecimal> restrictions = LinearRestrictionsFactory.createNumericRestrictions(
            NUMERIC_MIN_LIMIT, NUMERIC_MAX_LIMIT,
            NumericGranularityFactory.create(BigDecimal.valueOf(numericScale))
        );

        return restrictions;
    }

}


