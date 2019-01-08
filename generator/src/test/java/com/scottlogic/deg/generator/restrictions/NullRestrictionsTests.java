package com.scottlogic.deg.generator.restrictions;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

public class NullRestrictionsTests {

    @Test
    void equals_whenOtherObjectIsNull_returnsFalse () {
        NullRestrictions restriction = new NullRestrictions();

        boolean result = restriction.equals(null);

        Assert.assertFalse(result);
    }

    @Test
    void equals_whenOtherObjectIsOfDifferentType_returnsFalse () {
        NullRestrictions restriction = new NullRestrictions();

        boolean result = restriction.equals("String");

        Assert.assertFalse(result);

    }

    @Test
    void equals_whenBothObjectsAreEqualToNull_returnsTrue () {
        NullRestrictions restriction1 = new NullRestrictions(Nullness.MUST_BE_NULL);
        NullRestrictions restriction2 = new NullRestrictions(Nullness.MUST_BE_NULL);


        boolean result = restriction1.equals(restriction2);

        Assert.assertTrue(result);

    }

    @Test
    void equals_whenBothObjectsAreEqualToNotNull_returnsTrue () {
        NullRestrictions restriction1 = new NullRestrictions(Nullness.MUST_NOT_BE_NULL);
        NullRestrictions restriction2 = new NullRestrictions(Nullness.MUST_NOT_BE_NULL);


        boolean result = restriction1.equals(restriction2);

        Assert.assertTrue(result);

    }

    @Test
    void equals_whenBothObjectsAreNotEqual_returnsFalse () {
        NullRestrictions restriction1 = new NullRestrictions(Nullness.MUST_NOT_BE_NULL);
        NullRestrictions restriction2 = new NullRestrictions(Nullness.MUST_BE_NULL);


        boolean result = restriction1.equals(restriction2);

        Assert.assertFalse(result);

    }


    @Test
    void hashCode_whenBothObjectsAreEqualToNull_returnsTrue () {
        NullRestrictions restriction1 = new NullRestrictions(Nullness.MUST_BE_NULL);
        NullRestrictions restriction2 = new NullRestrictions(Nullness.MUST_BE_NULL);

        int hashCode1 = restriction1.hashCode();
        int hashCode2 = restriction2.hashCode();

        Assert.assertEquals(hashCode1, hashCode2);

    }

    @Test
    void hashCode_whenBothObjectsAreEqualToNotNull_returnsTrue () {
        NullRestrictions restriction1 = new NullRestrictions(Nullness.MUST_NOT_BE_NULL);
        NullRestrictions restriction2 = new NullRestrictions(Nullness.MUST_NOT_BE_NULL);

        int hashCode1 = restriction1.hashCode();
        int hashCode2 = restriction2.hashCode();

        Assert.assertEquals(hashCode1, hashCode2);

    }

    @Test
    void hashCode_whenBothObjectsAreNotEqual_returnsFalse () {
        NullRestrictions restriction1 = new NullRestrictions(Nullness.MUST_NOT_BE_NULL);
        NullRestrictions restriction2 = new NullRestrictions(Nullness.MUST_BE_NULL);

        int hashCode1 = restriction1.hashCode();
        int hashCode2 = restriction2.hashCode();

        Assert.assertNotEquals(hashCode1, hashCode2);

    }

}
