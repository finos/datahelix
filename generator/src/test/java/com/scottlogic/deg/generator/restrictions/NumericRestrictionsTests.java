package com.scottlogic.deg.generator.restrictions;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

public class NumericRestrictionsTests {
    @Test
    void equals_whenOtherObjectIsNull_returnsFalse() {
        NumericRestrictions restriction = new NumericRestrictions();

        boolean result = restriction.equals(null);

        Assert.assertFalse(result);
    }

    @Test
    void equals_whenOtherObjectIsStringNotTypeNumericRestrictions_returnsFalse() {
        NumericRestrictions restriction = new NumericRestrictions();

        boolean result = restriction.equals("Test");

        Assert.assertFalse(result);
    }

    @Test
    void equals_whenOtherObjectIsIntNotTypeNumericRestrictions_returnsFalse() {
        NumericRestrictions restriction = new NumericRestrictions();

        boolean result = restriction.equals(1);

        Assert.assertFalse(result);
    }

    @Test
    void equals_whenNumericRestrictionsAreEqual_returnsTrue() {
        NumericRestrictions restriction1 = new NumericRestrictions();
        NumericRestrictions restriction2 = new NumericRestrictions();

        restriction1.min = new NumericLimit<>(new BigDecimal(0), false);
        restriction2.min = new NumericLimit<>(new BigDecimal(0), false);
        restriction1.max = new NumericLimit<>(new BigDecimal(2), false);
        restriction2.max = new NumericLimit<>(new BigDecimal(2), false);

        boolean result = restriction1.equals(restriction2);

        Assert.assertTrue(result);
    }

    @Test
    void equals_whenNumericRestrictionsNumericLimitMinValuesAreNotEqual_returnsFalse() {
        NumericRestrictions restriction1 = new NumericRestrictions();
        NumericRestrictions restriction2 = new NumericRestrictions();

        restriction1.min = new NumericLimit<>(new BigDecimal(1), false);
        restriction2.min = new NumericLimit<>(new BigDecimal(0), false);
        restriction1.max = new NumericLimit<>(new BigDecimal(2), false);
        restriction2.max = new NumericLimit<>(new BigDecimal(2), false);

        boolean result = restriction1.equals(restriction2);

        Assert.assertFalse(result);
    }

    @Test
    void equals_whenOneNumericRestrictionsLimitMinValueIsNull_returnsFalse() {
        NumericRestrictions restriction1 = new NumericRestrictions();
        NumericRestrictions restriction2 = new NumericRestrictions();

        restriction1.min = null;
        restriction2.min = new NumericLimit<>(new BigDecimal(0), false);
        restriction1.max = new NumericLimit<>(new BigDecimal(2), false);
        restriction2.max = new NumericLimit<>(new BigDecimal(2), false);

        boolean result = restriction1.equals(restriction2);

        Assert.assertFalse(result);
    }

    @Test
    void equals_whenNumericRestrictionsLimitMinValuesAreNull_returnsTrue() {
        NumericRestrictions restriction1 = new NumericRestrictions();
        NumericRestrictions restriction2 = new NumericRestrictions();

        restriction1.min = null;
        restriction2.min = null;
        restriction1.max = new NumericLimit<>(new BigDecimal(2), false);
        restriction2.max = new NumericLimit<>(new BigDecimal(2), false);

        boolean result = restriction1.equals(restriction2);

        Assert.assertTrue(result);
    }

    @Test
    void equals_whenNumericRestrictionsLimitMaxValuesAreNotEqual_returnsFalse() {
        NumericRestrictions restriction1 = new NumericRestrictions();
        NumericRestrictions restriction2 = new NumericRestrictions();

        restriction1.min = new NumericLimit<>(new BigDecimal(0), true);
        restriction2.min = new NumericLimit<>(new BigDecimal(0), true);
        restriction1.max = new NumericLimit<>(new BigDecimal(2), true);
        restriction2.max = new NumericLimit<>(new BigDecimal(3), true);

        boolean result = restriction1.equals(restriction2);

        Assert.assertFalse(result);
    }

    @Test
    void equals_whenOneNumericRestrictionsLimitMaxValueIsNull_returnsFalse() {
        NumericRestrictions restriction1 = new NumericRestrictions();
        NumericRestrictions restriction2 = new NumericRestrictions();

        restriction1.min = new NumericLimit<>(new BigDecimal(0), false);
        restriction2.min = new NumericLimit<>(new BigDecimal(0), false);
        restriction1.max = null;
        restriction2.max = new NumericLimit<>(new BigDecimal(2), false);

        boolean result = restriction1.equals(restriction2);

        Assert.assertFalse(result);
    }

    @Test
    void equals_whenNumericRestrictionsLimitMaxValuesAreNull_returnsTrue() {
        NumericRestrictions restriction1 = new NumericRestrictions();
        NumericRestrictions restriction2 = new NumericRestrictions();

        restriction1.min = new NumericLimit<>(new BigDecimal(0), false);
        restriction2.min = new NumericLimit<>(new BigDecimal(0), false);
        restriction1.max = null;
        restriction2.max = null;

        boolean result = restriction1.equals(restriction2);

        Assert.assertTrue(result);
    }

    @Test
    void equals_whenNumericRestrictionsLimitsMinInclusiveValuesAreNotEqual_returnsFalse() {
        NumericRestrictions restriction1 = new NumericRestrictions();
        NumericRestrictions restriction2 = new NumericRestrictions();

        restriction1.min = new NumericLimit<>(new BigDecimal(0), true);
        restriction2.min = new NumericLimit<>(new BigDecimal(0), false);
        restriction1.max = new NumericLimit<>(new BigDecimal(2), false);
        restriction2.max = new NumericLimit<>(new BigDecimal(2), false);

        boolean result = restriction1.equals(restriction2);

        Assert.assertFalse(result);
    }

    @Test
    void equals_whenNumericRestrictionsLimitsMaxInclusiveValuesAreNotEqual_returnsFalse() {
        NumericRestrictions restriction1 = new NumericRestrictions();
        NumericRestrictions restriction2 = new NumericRestrictions();

        restriction1.min = new NumericLimit<>(new BigDecimal(0), true);
        restriction2.min = new NumericLimit<>(new BigDecimal(0), true);
        restriction1.max = new NumericLimit<>(new BigDecimal(2), false);
        restriction2.max = new NumericLimit<>(new BigDecimal(2), true);

        boolean result = restriction1.equals(restriction2);

        Assert.assertFalse(result);
    }


    @Test
    void equals_whenNumericRestrictionsLimitsAreEqualAndNegative_returnsTrue() {
        NumericRestrictions restriction1 = new NumericRestrictions();
        NumericRestrictions restriction2 = new NumericRestrictions();

        restriction1.min = new NumericLimit<>(new BigDecimal(-1), false);
        restriction2.min = new NumericLimit<>(new BigDecimal(-1), false);
        restriction1.max = new NumericLimit<>(new BigDecimal(-1), false);
        restriction2.max = new NumericLimit<>(new BigDecimal(-1), false);

        boolean result = restriction1.equals(restriction2);

        Assert.assertTrue(result);
    }


    @Test
    void equals_whenOneNumericRestrictionsLimitIsOfScientificNotationButAllValuesAreEqual_returnsTrue() {
        NumericRestrictions restriction1 = new NumericRestrictions();
        NumericRestrictions restriction2 = new NumericRestrictions();

        restriction1.min = new NumericLimit<>(new BigDecimal(50), false);
        restriction2.min = new NumericLimit<>(new BigDecimal(5E1), false);
        restriction1.max = new NumericLimit<>(new BigDecimal(100), false);
        restriction2.max = new NumericLimit<>(new BigDecimal(100), false);

        boolean result = restriction1.equals(restriction2);

        Assert.assertTrue(result);
    }

    @Test
    void hashCode_whenNumericRestrictionsAreEqual_returnsEqualHashCode() {
        NumericRestrictions restriction1 = new NumericRestrictions();
        NumericRestrictions restriction2 = new NumericRestrictions();

        restriction1.min = new NumericLimit<>(new BigDecimal(10), false);
        restriction2.min = new NumericLimit<>(new BigDecimal(10), false);
        restriction1.max = new NumericLimit<>(new BigDecimal(30), false);
        restriction2.max = new NumericLimit<>(new BigDecimal(30), false);

        int hashCode1 = restriction1.hashCode();
        int hashCode2 = restriction2.hashCode();

        Assert.assertEquals(hashCode1, hashCode2);
    }

    @Test
    void hashCode_whenNumericRestrictionLimitsAreInverted_returnsFalse() {
        NumericRestrictions restriction1 = new NumericRestrictions();
        NumericRestrictions restriction2 = new NumericRestrictions();

        restriction1.min = new NumericLimit<>(new BigDecimal(10), false);
        restriction2.min = new NumericLimit<>(new BigDecimal(20), false);
        restriction1.max = new NumericLimit<>(new BigDecimal(20), false);
        restriction2.max = new NumericLimit<>(new BigDecimal(10), false);

        int hashCode1 = restriction1.hashCode();
        int hashCode2 = restriction2.hashCode();

        Assert.assertNotEquals(hashCode1, hashCode2);
    }

    @Test
    void hashCode_whenNumericRestrictionsAreEqual_returnsTrue() {
        NumericRestrictions restriction1 = new NumericRestrictions();
        NumericRestrictions restriction2 = new NumericRestrictions();

        restriction1.min = new NumericLimit<>(new BigDecimal(0), false);
        restriction2.min = new NumericLimit<>(new BigDecimal(0), false);
        restriction1.max = new NumericLimit<>(new BigDecimal(2), false);
        restriction2.max = new NumericLimit<>(new BigDecimal(2), false);

        int hashCode1 = restriction1.hashCode();
        int hashCode2 = restriction2.hashCode();

        Assert.assertEquals(hashCode1, hashCode2);
    }

    @Test
    void hashCode_whenNumericRestrictionsLimitMinValuesAreNotEqual_returnsFalse() {
        NumericRestrictions restriction1 = new NumericRestrictions();
        NumericRestrictions restriction2 = new NumericRestrictions();

        restriction1.min = new NumericLimit<>(new BigDecimal(1), false);
        restriction2.min = new NumericLimit<>(new BigDecimal(0), false);
        restriction1.max = new NumericLimit<>(new BigDecimal(2), false);
        restriction2.max = new NumericLimit<>(new BigDecimal(2), false);

        int hashCode1 = restriction1.hashCode();
        int hashCode2 = restriction2.hashCode();

        Assert.assertNotEquals(hashCode1, hashCode2);
    }

    @Test
    void hashCode_whenOneNumericRestrictionsLimitMinValueIsNull_returnsFalse() {
        NumericRestrictions restriction1 = new NumericRestrictions();
        NumericRestrictions restriction2 = new NumericRestrictions();

        restriction1.min = null;
        restriction2.min = new NumericLimit<>(new BigDecimal(0), false);
        restriction1.max = new NumericLimit<>(new BigDecimal(2), false);
        restriction2.max = new NumericLimit<>(new BigDecimal(2), false);

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
        restriction1.max = new NumericLimit<>(new BigDecimal(2), false);
        restriction2.max = new NumericLimit<>(new BigDecimal(2), false);

        int hashCode1 = restriction1.hashCode();
        int hashCode2 = restriction2.hashCode();

        Assert.assertEquals(hashCode1, hashCode2);
    }

    @Test
    void hashCode_whenNumericRestrictionsLimitMaxValuesAreNotEqual_returnsFalse() {
        NumericRestrictions restriction1 = new NumericRestrictions();
        NumericRestrictions restriction2 = new NumericRestrictions();

        restriction1.min = new NumericLimit<>(new BigDecimal(0), true);
        restriction2.min = new NumericLimit<>(new BigDecimal(0), true);
        restriction1.max = new NumericLimit<>(new BigDecimal(2), true);
        restriction2.max = new NumericLimit<>(new BigDecimal(3), true);

        int hashCode1 = restriction1.hashCode();
        int hashCode2 = restriction2.hashCode();

        Assert.assertNotEquals(hashCode1, hashCode2);
    }

    @Test
    void hashCode_whenOneNumericRestrictionsLimitMaxValueIsNull_returnsFalse() {
        NumericRestrictions restriction1 = new NumericRestrictions();
        NumericRestrictions restriction2 = new NumericRestrictions();

        restriction1.min = new NumericLimit<>(new BigDecimal(0), false);
        restriction2.min = new NumericLimit<>(new BigDecimal(0), false);
        restriction1.max = null;
        restriction2.max = new NumericLimit<>(new BigDecimal(2), false);

        int hashCode1 = restriction1.hashCode();
        int hashCode2 = restriction2.hashCode();

        Assert.assertNotEquals(hashCode1, hashCode2);
    }

    @Test
    void hashCode_whenNumericRestrictionsLimitMaxValuesAreNull_returnsTrue() {
        NumericRestrictions restriction1 = new NumericRestrictions();
        NumericRestrictions restriction2 = new NumericRestrictions();

        restriction1.min = new NumericLimit<>(new BigDecimal(0), false);
        restriction2.min = new NumericLimit<>(new BigDecimal(0), false);
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

        restriction1.min = new NumericLimit<>(new BigDecimal(0), true);
        restriction2.min = new NumericLimit<>(new BigDecimal(0), false);
        restriction1.max = new NumericLimit<>(new BigDecimal(2), false);
        restriction2.max = new NumericLimit<>(new BigDecimal(2), false);

        int hashCode1 = restriction1.hashCode();
        int hashCode2 = restriction2.hashCode();

        Assert.assertNotEquals(hashCode1, hashCode2);
    }

    @Test
    void hashCode_whenNumericRestrictionsLimitsMaxInclusiveValuesAreNotEqual_returnsFalse() {
        NumericRestrictions restriction1 = new NumericRestrictions();
        NumericRestrictions restriction2 = new NumericRestrictions();

        restriction1.min = new NumericLimit<>(new BigDecimal(0), true);
        restriction2.min = new NumericLimit<>(new BigDecimal(0), true);
        restriction1.max = new NumericLimit<>(new BigDecimal(2), false);
        restriction2.max = new NumericLimit<>(new BigDecimal(2), true);

        int hashCode1 = restriction1.hashCode();
        int hashCode2 = restriction2.hashCode();

        Assert.assertNotEquals(hashCode1, hashCode2);
    }


    @Test
    void hashCode_whenNumericRestrictionsLimitsAreEqualAndNegative_returnsTrue() {
        NumericRestrictions restriction1 = new NumericRestrictions();
        NumericRestrictions restriction2 = new NumericRestrictions();

        restriction1.min = new NumericLimit<>(new BigDecimal(-1), false);
        restriction2.min = new NumericLimit<>(new BigDecimal(-1), false);
        restriction1.max = new NumericLimit<>(new BigDecimal(-1), false);
        restriction2.max = new NumericLimit<>(new BigDecimal(-1), false);

        int hashCode1 = restriction1.hashCode();
        int hashCode2 = restriction2.hashCode();

        Assert.assertEquals(hashCode1, hashCode2);
    }

    @Test
    void hashCode_whenOneNumericRestrictionsLimitIsOfScientificNotationButAllValuesAreEqual_returnsTrue() {
        NumericRestrictions restriction1 = new NumericRestrictions();
        NumericRestrictions restriction2 = new NumericRestrictions();

        restriction1.min = new NumericLimit<>(new BigDecimal(50), false);
        restriction2.min = new NumericLimit<>(new BigDecimal(5E1), false);
        restriction1.max = new NumericLimit<>(new BigDecimal(100), false);
        restriction2.max = new NumericLimit<>(new BigDecimal(100), false);

        int hashCode1 = restriction1.hashCode();
        int hashCode2 = restriction2.hashCode();

        Assert.assertEquals(hashCode1, hashCode2);
    }

    @Test
    void numericValuesAreInteger_minAndMaxValueAreWithinIntegerRangeAndDoNotContainDecimalValues_returnsTrue() {
        NumericRestrictions restrictions = new NumericRestrictions();
        restrictions.min = new NumericLimit<>(new BigDecimal(10), false);
        restrictions.max = new NumericLimit<>(new BigDecimal(1000), false);

        boolean result = restrictions.numericValuesAreInteger();

        Assert.assertTrue(result);
    }

    @Test
    void numericValuesAreInteger_minAndMaxValuesHaveEdgeCaseValuesAndDoNotContainDecimalValues_returnsTrue() {
        NumericRestrictions restrictions = new NumericRestrictions();
        restrictions.min = new NumericLimit<>(new BigDecimal(-2147483648), false);
        restrictions.max = new NumericLimit<>(new BigDecimal(2147483647), false);

        boolean result = restrictions.numericValuesAreInteger();

        Assert.assertTrue(result);
    }

    @Test
    void numericValuesAreInteger_minValueIsBelowIntegerMinimumAndMaxValueIsIntegerAndBothDoNotContainDecimalValues_returnsFalse() {
        NumericRestrictions restrictions = new NumericRestrictions();
        restrictions.min = new NumericLimit<>(new BigDecimal("1E+38"), false);
        restrictions.max = new NumericLimit<>(new BigDecimal(10), false);

        boolean result = restrictions.numericValuesAreInteger();

        Assert.assertFalse(result);
    }

    @Test
    void numericValuesAreInteger_maxValueIsAboveIntegerMaximumAndMinValueIsIntegerAndBothDoNotContainDecimalValues_returnsFalse() {
        NumericRestrictions restrictions = new NumericRestrictions();
        restrictions.min = new NumericLimit<>(new BigDecimal(0), false);
        restrictions.max = new NumericLimit<>(new BigDecimal("1E+38"), false);

        boolean result = restrictions.numericValuesAreInteger();

        Assert.assertFalse(result);
    }

    @Test
    void numericValuesAreInteger_minAndMaxValuesAreWithinIntegerRangeAndMinValueContainsDecimalValue_returnsFalse() {
        NumericRestrictions restrictions = new NumericRestrictions();
        restrictions.min = new NumericLimit<>(new BigDecimal(15.5), false);
        restrictions.max = new NumericLimit<>(new BigDecimal(100), false);

        boolean result = restrictions.numericValuesAreInteger();

        Assert.assertFalse(result);
    }

    @Test
    void numericValuesAreInteger_minAndMaxValuesAreWithinIntegerRangeAndMaxValueContainsDecimalValue_returnsFalse() {
        NumericRestrictions restrictions = new NumericRestrictions();
        restrictions.min = new NumericLimit<>(new BigDecimal(0), false);
        restrictions.max = new NumericLimit<>(new BigDecimal(500.25), false);

        boolean result = restrictions.numericValuesAreInteger();

        Assert.assertFalse(result);
    }
}


