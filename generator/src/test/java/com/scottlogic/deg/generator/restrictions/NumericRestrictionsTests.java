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
import com.scottlogic.deg.generator.restrictions.linear.NumericLimit;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNot.not;

public class NumericRestrictionsTests {

    @Test
    void equals_whenNumericRestrictionsAreEqual_returnsTrue() {
        NumericRestrictions restriction1 = new NumericRestrictions();
        NumericRestrictions restriction2 = new NumericRestrictions();

        restriction1.min = new NumericLimit(new BigDecimal(0), false);
        restriction2.min = new NumericLimit(new BigDecimal(0), false);
        restriction1.max = new NumericLimit(new BigDecimal(2), false);
        restriction2.max = new NumericLimit(new BigDecimal(2), false);

        boolean result = restriction1.equals(restriction2);

        Assert.assertTrue(result);
    }

    @Test
    void equals_whenNumericRestrictionsNumericLimitMinValuesAreNotEqual_returnsFalse() {
        NumericRestrictions restriction1 = new NumericRestrictions();
        NumericRestrictions restriction2 = new NumericRestrictions();

        restriction1.min = new NumericLimit(new BigDecimal(1), false);
        restriction2.min = new NumericLimit(new BigDecimal(0), false);
        restriction1.max = new NumericLimit(new BigDecimal(2), false);
        restriction2.max = new NumericLimit(new BigDecimal(2), false);

        boolean result = restriction1.equals(restriction2);

        Assert.assertFalse(result);
    }

    @Test
    void equals_whenOneNumericRestrictionsLimitMinValueIsNull_returnsFalse() {
        NumericRestrictions restriction1 = new NumericRestrictions();
        NumericRestrictions restriction2 = new NumericRestrictions();

        restriction1.min = null;
        restriction2.min = new NumericLimit(new BigDecimal(0), false);
        restriction1.max = new NumericLimit(new BigDecimal(2), false);
        restriction2.max = new NumericLimit(new BigDecimal(2), false);

        boolean result = restriction1.equals(restriction2);

        Assert.assertFalse(result);
    }

    @Test
    void equals_whenNumericRestrictionsLimitMinValuesAreNull_returnsTrue() {
        NumericRestrictions restriction1 = new NumericRestrictions();
        NumericRestrictions restriction2 = new NumericRestrictions();

        restriction1.min = null;
        restriction2.min = null;
        restriction1.max = new NumericLimit(new BigDecimal(2), false);
        restriction2.max = new NumericLimit(new BigDecimal(2), false);

        boolean result = restriction1.equals(restriction2);

        Assert.assertTrue(result);
    }

    @Test
    void equals_whenNumericRestrictionsLimitMaxValuesAreNotEqual_returnsFalse() {
        NumericRestrictions restriction1 = new NumericRestrictions();
        NumericRestrictions restriction2 = new NumericRestrictions();

        restriction1.min = new NumericLimit(new BigDecimal(0), true);
        restriction2.min = new NumericLimit(new BigDecimal(0), true);
        restriction1.max = new NumericLimit(new BigDecimal(2), true);
        restriction2.max = new NumericLimit(new BigDecimal(3), true);

        boolean result = restriction1.equals(restriction2);

        Assert.assertFalse(result);
    }

    @Test
    void equals_whenOneNumericRestrictionsLimitMaxValueIsNull_returnsFalse() {
        NumericRestrictions restriction1 = new NumericRestrictions();
        NumericRestrictions restriction2 = new NumericRestrictions();

        restriction1.min = new NumericLimit(new BigDecimal(0), false);
        restriction2.min = new NumericLimit(new BigDecimal(0), false);
        restriction1.max = null;
        restriction2.max = new NumericLimit(new BigDecimal(2), false);

        boolean result = restriction1.equals(restriction2);

        Assert.assertFalse(result);
    }

    @Test
    void equals_whenNumericRestrictionsLimitMaxValuesAreNull_returnsTrue() {
        NumericRestrictions restriction1 = new NumericRestrictions();
        NumericRestrictions restriction2 = new NumericRestrictions();

        restriction1.min = new NumericLimit(new BigDecimal(0), false);
        restriction2.min = new NumericLimit(new BigDecimal(0), false);
        restriction1.max = null;
        restriction2.max = null;

        boolean result = restriction1.equals(restriction2);

        Assert.assertTrue(result);
    }

    @Test
    void equals_whenNumericRestrictionsLimitsMinInclusiveValuesAreNotEqual_returnsFalse() {
        NumericRestrictions restriction1 = new NumericRestrictions();
        NumericRestrictions restriction2 = new NumericRestrictions();

        restriction1.min = new NumericLimit(new BigDecimal(0), true);
        restriction2.min = new NumericLimit(new BigDecimal(0), false);
        restriction1.max = new NumericLimit(new BigDecimal(2), false);
        restriction2.max = new NumericLimit(new BigDecimal(2), false);

        boolean result = restriction1.equals(restriction2);

        Assert.assertFalse(result);
    }

    @Test
    void equals_whenNumericRestrictionsLimitsMaxInclusiveValuesAreNotEqual_returnsFalse() {
        NumericRestrictions restriction1 = new NumericRestrictions();
        NumericRestrictions restriction2 = new NumericRestrictions();

        restriction1.min = new NumericLimit(new BigDecimal(0), true);
        restriction2.min = new NumericLimit(new BigDecimal(0), true);
        restriction1.max = new NumericLimit(new BigDecimal(2), false);
        restriction2.max = new NumericLimit(new BigDecimal(2), true);

        boolean result = restriction1.equals(restriction2);

        Assert.assertFalse(result);
    }


    @Test
    void equals_whenNumericRestrictionsLimitsAreEqualAndNegative_returnsTrue() {
        NumericRestrictions restriction1 = new NumericRestrictions();
        NumericRestrictions restriction2 = new NumericRestrictions();

        restriction1.min = new NumericLimit(new BigDecimal(-1), false);
        restriction2.min = new NumericLimit(new BigDecimal(-1), false);
        restriction1.max = new NumericLimit(new BigDecimal(-1), false);
        restriction2.max = new NumericLimit(new BigDecimal(-1), false);

        boolean result = restriction1.equals(restriction2);

        Assert.assertTrue(result);
    }


    @Test
    void equals_whenOneNumericRestrictionsLimitIsOfScientificNotationButAllValuesAreEqual_returnsTrue() {
        NumericRestrictions restriction1 = new NumericRestrictions();
        NumericRestrictions restriction2 = new NumericRestrictions();

        restriction1.min = new NumericLimit(new BigDecimal(50), false);
        restriction2.min = new NumericLimit(new BigDecimal(5E1), false);
        restriction1.max = new NumericLimit(new BigDecimal(100), false);
        restriction2.max = new NumericLimit(new BigDecimal(100), false);

        boolean result = restriction1.equals(restriction2);

        Assert.assertTrue(result);
    }

    @Test
    void hashCode_whenNumericRestrictionsAreEqual_returnsEqualHashCode() {
        NumericRestrictions restriction1 = new NumericRestrictions();
        NumericRestrictions restriction2 = new NumericRestrictions();

        restriction1.min = new NumericLimit(new BigDecimal(10), false);
        restriction2.min = new NumericLimit(new BigDecimal(10), false);
        restriction1.max = new NumericLimit(new BigDecimal(30), false);
        restriction2.max = new NumericLimit(new BigDecimal(30), false);

        int hashCode1 = restriction1.hashCode();
        int hashCode2 = restriction2.hashCode();

        Assert.assertEquals(hashCode1, hashCode2);
    }

    @Test
    void hashCode_whenNumericRestrictionLimitsAreInverted_returnsFalse() {
        NumericRestrictions restriction1 = new NumericRestrictions();
        NumericRestrictions restriction2 = new NumericRestrictions();

        restriction1.min = new NumericLimit(new BigDecimal(10), false);
        restriction2.min = new NumericLimit(new BigDecimal(20), false);
        restriction1.max = new NumericLimit(new BigDecimal(20), false);
        restriction2.max = new NumericLimit(new BigDecimal(10), false);

        int hashCode1 = restriction1.hashCode();
        int hashCode2 = restriction2.hashCode();

        Assert.assertNotEquals(hashCode1, hashCode2);
    }

    @Test
    void hashCode_whenNumericRestrictionsAreEqual_returnsTrue() {
        NumericRestrictions restriction1 = new NumericRestrictions();
        NumericRestrictions restriction2 = new NumericRestrictions();

        restriction1.min = new NumericLimit(new BigDecimal(0), false);
        restriction2.min = new NumericLimit(new BigDecimal(0), false);
        restriction1.max = new NumericLimit(new BigDecimal(2), false);
        restriction2.max = new NumericLimit(new BigDecimal(2), false);

        int hashCode1 = restriction1.hashCode();
        int hashCode2 = restriction2.hashCode();

        Assert.assertEquals(hashCode1, hashCode2);
    }

    @Test
    void hashCode_whenNumericRestrictionsLimitMinValuesAreNotEqual_returnsFalse() {
        NumericRestrictions restriction1 = new NumericRestrictions();
        NumericRestrictions restriction2 = new NumericRestrictions();

        restriction1.min = new NumericLimit(new BigDecimal(1), false);
        restriction2.min = new NumericLimit(new BigDecimal(0), false);
        restriction1.max = new NumericLimit(new BigDecimal(2), false);
        restriction2.max = new NumericLimit(new BigDecimal(2), false);

        int hashCode1 = restriction1.hashCode();
        int hashCode2 = restriction2.hashCode();

        Assert.assertNotEquals(hashCode1, hashCode2);
    }

    @Test
    void hashCode_whenOneNumericRestrictionsLimitMinValueIsNull_returnsFalse() {
        NumericRestrictions restriction1 = new NumericRestrictions();
        NumericRestrictions restriction2 = new NumericRestrictions();

        restriction1.min = null;
        restriction2.min = new NumericLimit(new BigDecimal(0), false);
        restriction1.max = new NumericLimit(new BigDecimal(2), false);
        restriction2.max = new NumericLimit(new BigDecimal(2), false);

        int hashCode1 = restriction1.hashCode();
        int hashCode2 = restriction2.hashCode();

        Assert.assertNotEquals(hashCode1, hashCode2);
    }

    @Test
    void hashCode_whenNumericRestrictionsLimitMinValuesAreNull_returnsTrue() {
        NumericRestrictions restriction1 = new NumericRestrictions();
        NumericRestrictions restriction2 = new NumericRestrictions();

        restriction1.min = null;
        restriction2.min = null;
        restriction1.max = new NumericLimit(new BigDecimal(2), false);
        restriction2.max = new NumericLimit(new BigDecimal(2), false);

        int hashCode1 = restriction1.hashCode();
        int hashCode2 = restriction2.hashCode();

        Assert.assertEquals(hashCode1, hashCode2);
    }

    @Test
    void hashCode_whenNumericRestrictionsLimitMaxValuesAreNotEqual_returnsFalse() {
        NumericRestrictions restriction1 = new NumericRestrictions();
        NumericRestrictions restriction2 = new NumericRestrictions();

        restriction1.min = new NumericLimit(new BigDecimal(0), true);
        restriction2.min = new NumericLimit(new BigDecimal(0), true);
        restriction1.max = new NumericLimit(new BigDecimal(2), true);
        restriction2.max = new NumericLimit(new BigDecimal(3), true);

        int hashCode1 = restriction1.hashCode();
        int hashCode2 = restriction2.hashCode();

        Assert.assertNotEquals(hashCode1, hashCode2);
    }

    @Test
    void hashCode_whenOneNumericRestrictionsLimitMaxValueIsNull_returnsFalse() {
        NumericRestrictions restriction1 = new NumericRestrictions();
        NumericRestrictions restriction2 = new NumericRestrictions();

        restriction1.min = new NumericLimit(new BigDecimal(0), false);
        restriction2.min = new NumericLimit(new BigDecimal(0), false);
        restriction1.max = null;
        restriction2.max = new NumericLimit(new BigDecimal(2), false);

        int hashCode1 = restriction1.hashCode();
        int hashCode2 = restriction2.hashCode();

        Assert.assertNotEquals(hashCode1, hashCode2);
    }

    @Test
    void hashCode_whenNumericRestrictionsLimitMaxValuesAreNull_returnsTrue() {
        NumericRestrictions restriction1 = new NumericRestrictions();
        NumericRestrictions restriction2 = new NumericRestrictions();

        restriction1.min = new NumericLimit(new BigDecimal(0), false);
        restriction2.min = new NumericLimit(new BigDecimal(0), false);
        restriction1.max = null;
        restriction2.max = null;

        int hashCode1 = restriction1.hashCode();
        int hashCode2 = restriction2.hashCode();

        Assert.assertEquals(hashCode1, hashCode2);
    }

    @Test
    void hashCode_whenNumericRestrictionsLimitsMinInclusiveValuesAreNotEqual_returnsFalse() {
        NumericRestrictions restriction1 = new NumericRestrictions();
        NumericRestrictions restriction2 = new NumericRestrictions();

        restriction1.min = new NumericLimit(new BigDecimal(0), true);
        restriction2.min = new NumericLimit(new BigDecimal(0), false);
        restriction1.max = new NumericLimit(new BigDecimal(2), false);
        restriction2.max = new NumericLimit(new BigDecimal(2), false);

        int hashCode1 = restriction1.hashCode();
        int hashCode2 = restriction2.hashCode();

        Assert.assertNotEquals(hashCode1, hashCode2);
    }

    @Test
    void hashCode_whenNumericRestrictionsLimitsMaxInclusiveValuesAreNotEqual_returnsFalse() {
        NumericRestrictions restriction1 = new NumericRestrictions();
        NumericRestrictions restriction2 = new NumericRestrictions();

        restriction1.min = new NumericLimit(new BigDecimal(0), true);
        restriction2.min = new NumericLimit(new BigDecimal(0), true);
        restriction1.max = new NumericLimit(new BigDecimal(2), false);
        restriction2.max = new NumericLimit(new BigDecimal(2), true);

        int hashCode1 = restriction1.hashCode();
        int hashCode2 = restriction2.hashCode();

        Assert.assertNotEquals(hashCode1, hashCode2);
    }


    @Test
    void hashCode_whenNumericRestrictionsLimitsAreEqualAndNegative_returnsTrue() {
        NumericRestrictions restriction1 = new NumericRestrictions();
        NumericRestrictions restriction2 = new NumericRestrictions();

        restriction1.min = new NumericLimit(new BigDecimal(-1), false);
        restriction2.min = new NumericLimit(new BigDecimal(-1), false);
        restriction1.max = new NumericLimit(new BigDecimal(-1), false);
        restriction2.max = new NumericLimit(new BigDecimal(-1), false);

        int hashCode1 = restriction1.hashCode();
        int hashCode2 = restriction2.hashCode();

        Assert.assertEquals(hashCode1, hashCode2);
    }

    @Test
    void hashCode_whenOneNumericRestrictionsLimitIsOfScientificNotationButAllValuesAreEqual_returnsTrue() {
        NumericRestrictions restriction1 = new NumericRestrictions();
        NumericRestrictions restriction2 = new NumericRestrictions();

        restriction1.min = new NumericLimit(new BigDecimal(50), false);
        restriction2.min = new NumericLimit(new BigDecimal(5E1), false);
        restriction1.max = new NumericLimit(new BigDecimal(100), false);
        restriction2.max = new NumericLimit(new BigDecimal(100), false);

        int hashCode1 = restriction1.hashCode();
        int hashCode2 = restriction2.hashCode();

        Assert.assertEquals(hashCode1, hashCode2);
    }

    @Test
    void areLimitValuesInteger_minAndMaxValueAreWithinIntegerRangeAndDoNotContainDecimalValues_returnsTrue() {
        NumericRestrictions restrictions = new NumericRestrictions();
        restrictions.min = new NumericLimit(new BigDecimal(10), false);
        restrictions.max = new NumericLimit(new BigDecimal(1000), false);

        boolean result = areLimitValuesInteger(restrictions);

        Assert.assertTrue(result);
    }

    @Test
    void areLimitValuesInteger_minAndMaxValuesHaveEdgeCaseValuesAndDoNotContainDecimalValues_returnsTrue() {
        NumericRestrictions restrictions = new NumericRestrictions();
        restrictions.min = new NumericLimit(new BigDecimal(-2147483648), false);
        restrictions.max = new NumericLimit(new BigDecimal(2147483647), false);

        boolean result = areLimitValuesInteger(restrictions);

        Assert.assertTrue(result);
    }

    @Test
    void areLimitValuesInteger_minValueIsBelowIntegerMinimumAndMaxValueIsIntegerAndBothDoNotContainDecimalValues_returnsFalse() {
        NumericRestrictions restrictions = new NumericRestrictions();
        restrictions.min = new NumericLimit(new BigDecimal("1E+38"), false);
        restrictions.max = new NumericLimit(new BigDecimal(10), false);

        boolean result = areLimitValuesInteger(restrictions);

        Assert.assertFalse(result);
    }

    @Test
    void areLimitValuesInteger_maxValueIsAboveIntegerMaximumAndMinValueIsIntegerAndBothDoNotContainDecimalValues_returnsFalse() {
        NumericRestrictions restrictions = new NumericRestrictions();
        restrictions.min = new NumericLimit(new BigDecimal(0), false);
        restrictions.max = new NumericLimit(new BigDecimal("1E+38"), false);

        boolean result = areLimitValuesInteger(restrictions);

        Assert.assertFalse(result);
    }

    @Test
    void areLimitValuesInteger_minAndMaxValuesAreWithinIntegerRangeAndMinValueContainsDecimalValue_returnsFalse() {
        NumericRestrictions restrictions = new NumericRestrictions();
        restrictions.min = new NumericLimit(new BigDecimal(15.5), false);
        restrictions.max = new NumericLimit(new BigDecimal(100), false);

        boolean result = areLimitValuesInteger(restrictions);

        Assert.assertFalse(result);
    }

    @Test
    void areLimitValuesInteger_minAndMaxValuesAreWithinIntegerRangeAndMaxValueContainsDecimalValue_returnsFalse() {
        NumericRestrictions restrictions = new NumericRestrictions();
        restrictions.min = new NumericLimit(new BigDecimal(0), false);
        restrictions.max = new NumericLimit(new BigDecimal(500.25), false);

        boolean result = areLimitValuesInteger(restrictions);

        Assert.assertFalse(result);
    }

    @Test
    void areLimitValuesInteger_minValueIsNull_returnsFalse() {
        NumericRestrictions restrictions = new NumericRestrictions();
        restrictions.max = new NumericLimit(new BigDecimal(100), false);

        boolean result = areLimitValuesInteger(restrictions);

        Assert.assertFalse(result);
    }

    @Test
    void areLimitValuesInteger_maxValueIsNull_returnsFalse() {
        NumericRestrictions restrictions = new NumericRestrictions();
        restrictions.min = new NumericLimit(new BigDecimal(100), false);

        boolean result = areLimitValuesInteger(restrictions);

        Assert.assertFalse(result);
    }

    @Test
    void areLimitValuesInteger_minValueIsOneBelowEdgeCaseIntegerValue_returnsFalse() {
        NumericRestrictions restrictions = new NumericRestrictions();
        restrictions.min = new NumericLimit(new BigDecimal("-2147483649"), false);
        restrictions.max = new NumericLimit(new BigDecimal("100"), false);

        boolean result = areLimitValuesInteger(restrictions);

        Assert.assertFalse(result);
    }

    @Test
    void areLimitValuesInteger_maxValueIsOneAboveEdgeCaseIntegerValue_returnsFalse() {
        NumericRestrictions restrictions = new NumericRestrictions();
        restrictions.min = new NumericLimit(new BigDecimal("0"), false);
        restrictions.max = new NumericLimit(new BigDecimal("2147483648"), false);

        boolean result = areLimitValuesInteger(restrictions);

        Assert.assertFalse(result);
    }

    @Test
    void areLimitValuesInteger_minAndMaxHaveValuesWithTrailingZeros_returnsFalse() {
        NumericRestrictions restrictions = new NumericRestrictions();
        restrictions.max = new NumericLimit(new BigDecimal(5.00), false);
        restrictions.max = new NumericLimit(new BigDecimal(10.00), false);

        boolean result = areLimitValuesInteger(restrictions);

        Assert.assertFalse(result);
    }



    public boolean areLimitValuesInteger(NumericRestrictions restrictions) {
        if (restrictions.min == null || restrictions.max == null) {
            return false;
        }

        // If either of the min or max values have decimal points or if the sign differs when converting to an integer
        // the value is not an integer
        BigDecimal minLimit = restrictions.min.getValue();
        BigDecimal maxLimit = restrictions.max.getValue();
        if (minLimit.scale() > 0 || maxLimit.scale() > 0 ||
            minLimit.signum() != Integer.signum(minLimit.intValue()) ||
            maxLimit.signum() != Integer.signum(maxLimit.intValue())) {
            return false;
        }

        return (minLimit.toBigInteger().signum() == 0 || minLimit.intValue() != 0) &&
            (maxLimit.toBigInteger().signum() == 0 || maxLimit.intValue() != 0);
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

    private static NumericRestrictions restrictions(double numericScale){
        NumericRestrictions restrictions = new NumericRestrictions(
            ParsedGranularity.parse(BigDecimal.valueOf(numericScale)).getNumericGranularity().scale()
        );

        return restrictions;
    }

}


