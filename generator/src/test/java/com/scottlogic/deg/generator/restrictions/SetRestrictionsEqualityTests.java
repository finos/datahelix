package com.scottlogic.deg.generator.restrictions;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

public class SetRestrictionsEqualityTests {

    @Test
    void equals_whenOtherObjectIsNull_returnsFalse() {
        SetRestrictions restriction = new SetRestrictions(null, null);

        boolean result = restriction.equals(null);

        Assert.assertFalse(result);

    }

    @Test
    void equals_whenOtherObjectIsNotOfTheSameType_returnsTrue() {
        SetRestrictions restriction = new SetRestrictions(null, null);

        boolean result = restriction.equals("String");

        Assert.assertFalse(result);
    }

    @Test
    void equals_whenObjectsAreEqual_returnsTrue() {
        SetRestrictions restriction1 = new SetRestrictions(null, null);
        SetRestrictions restriction2 = new SetRestrictions(null, null);

        boolean result = restriction1.equals(restriction2);

        Assert.assertTrue(result);
    }


    @Test
    void equals_whenBlacklistsAreNotEqualAndOfSameType_returnsFalse() {
        Set<Object> blacklist1 = new HashSet<>();
        Set<Object> blacklist2 = new HashSet<>();
        blacklist1.add("Test");
        blacklist2.add("Parrot");

        SetRestrictions restriction1 = new SetRestrictions(null, blacklist1);
        SetRestrictions restriction2 = new SetRestrictions(null, blacklist2);

        boolean result = restriction1.equals(restriction2);

        Assert.assertFalse(result);
    }

    @Test
    void equals_whenBlacklistsAreNotEqualAndOfDifferentType_returnsFalse() {
        Set<Object> blacklist1 = new HashSet<>();
        Set<Object> blacklist2 = new HashSet<>();
        blacklist1.add("Test");
        blacklist2.add(1);

        SetRestrictions restriction1 = new SetRestrictions(null, blacklist1);
        SetRestrictions restriction2 = new SetRestrictions(null, blacklist2);

        boolean result = restriction1.equals(restriction2);

        Assert.assertFalse(result);
    }

    @Test
    void equals_whenBlacklistsAreEqual_returnsTrue() {
        Set<Object> blacklist1 = new HashSet<>();
        Set<Object> blacklist2 = new HashSet<>();
        blacklist1.add("Test");
        blacklist2.add("Test");

        SetRestrictions restriction1 = new SetRestrictions(null, blacklist1);
        SetRestrictions restriction2 = new SetRestrictions(null, blacklist2);

        boolean result = restriction1.equals(restriction2);

        Assert.assertTrue(result);
    }

    @Test
    void equals_whenWhitelistsAreNotEqualAndOfSameType_returnsFalse() {
        Set<Object> whitelist1 = new HashSet<>();
        Set<Object> whitelist2 = new HashSet<>();
        whitelist1.add("Test");
        whitelist2.add("Parrot");

        SetRestrictions restriction1 = new SetRestrictions(null, whitelist1);
        SetRestrictions restriction2 = new SetRestrictions(null, whitelist2);

        boolean result = restriction1.equals(restriction2);

        Assert.assertFalse(result);
    }

    @Test
    void equals_whenWhitelistsAreNotEqualAndOfDifferentType_returnsFalse() {
        Set<Object> whitelist1 = new HashSet<>();
        Set<Object> whitelist2 = new HashSet<>();
        whitelist1.add("Test");
        whitelist2.add(1);

        SetRestrictions restriction1 = new SetRestrictions(null, whitelist1);
        SetRestrictions restriction2 = new SetRestrictions(null, whitelist2);

        boolean result = restriction1.equals(restriction2);

        Assert.assertFalse(result);
    }

    @Test
    void equals_whenWhitelistsAreEqual_returnsTrue() {
        Set<Object> whitelist1 = new HashSet<>();
        Set<Object> whitelist2 = new HashSet<>();
        whitelist1.add("Test");
        whitelist2.add("Test");

        SetRestrictions restriction1 = new SetRestrictions(null, whitelist1);
        SetRestrictions restriction2 = new SetRestrictions(null, whitelist2);

        boolean result = restriction1.equals(restriction2);

        Assert.assertTrue(result);
    }

    @Test
    void hasCode_whenObjectsAreEqual_returnsTrue() {
        SetRestrictions restriction1 = new SetRestrictions(null, null);
        SetRestrictions restriction2 = new SetRestrictions(null, null);

        int hashCode1 = restriction1.hashCode();
        int hashCode2 = restriction2.hashCode();

        Assert.assertEquals(hashCode1, hashCode2);
    }


    @Test
    void hashCode_whenBlacklistsAreNotEqualAndOfSameType_returnsFalse() {
        Set<Object> blacklist1 = new HashSet<>();
        Set<Object> blacklist2 = new HashSet<>();
        blacklist1.add("Test");
        blacklist2.add("Parrot");

        SetRestrictions restriction1 = new SetRestrictions(null, blacklist1);
        SetRestrictions restriction2 = new SetRestrictions(null, blacklist2);

        int hashCode1 = restriction1.hashCode();
        int hashCode2 = restriction2.hashCode();

        Assert.assertNotEquals(hashCode1, hashCode2);
    }

    @Test
    void hashCode_whenBlacklistsAreNotEqualAndOfDifferentType_returnsFalse() {
        Set<Object> blacklist1 = new HashSet<>();
        Set<Object> blacklist2 = new HashSet<>();
        blacklist1.add("Test");
        blacklist2.add(1);

        SetRestrictions restriction1 = new SetRestrictions(null, blacklist1);
        SetRestrictions restriction2 = new SetRestrictions(null, blacklist2);

        int hashCode1 = restriction1.hashCode();
        int hashCode2 = restriction2.hashCode();

        Assert.assertNotEquals(hashCode1, hashCode2);
    }

    @Test
    void hashCode_whenBlacklistsAreEqual_returnsTrue() {
        Set<Object> blacklist1 = new HashSet<>();
        Set<Object> blacklist2 = new HashSet<>();
        blacklist1.add("Test");
        blacklist2.add("Test");

        SetRestrictions restriction1 = new SetRestrictions(null, blacklist1);
        SetRestrictions restriction2 = new SetRestrictions(null, blacklist2);

        int hashCode1 = restriction1.hashCode();
        int hashCode2 = restriction2.hashCode();

        Assert.assertEquals(hashCode1, hashCode2);
    }

    @Test
    void hashCode_whenWhitelistsAreNotEqualAndOfSameType_returnsFalse() {
        Set<Object> whitelist1 = new HashSet<>();
        Set<Object> whitelist2 = new HashSet<>();
        whitelist1.add("Test");
        whitelist2.add("Parrot");

        SetRestrictions restriction1 = new SetRestrictions(null, whitelist1);
        SetRestrictions restriction2 = new SetRestrictions(null, whitelist2);

        int hashCode1 = restriction1.hashCode();
        int hashCode2 = restriction2.hashCode();

        Assert.assertNotEquals(hashCode1, hashCode2);
    }

    @Test
    void hashCode_whenWhitelistsAreNotEqualAndOfDifferentType_returnsFalse() {
        Set<Object> whitelist1 = new HashSet<>();
        Set<Object> whitelist2 = new HashSet<>();
        whitelist1.add("Test");
        whitelist2.add(1);

        SetRestrictions restriction1 = new SetRestrictions(null, whitelist1);
        SetRestrictions restriction2 = new SetRestrictions(null, whitelist2);

        int hashCode1 = restriction1.hashCode();
        int hashCode2 = restriction2.hashCode();

        Assert.assertNotEquals(hashCode1, hashCode2);
    }

    @Test
    void hashCode_whenWhitelistsAreEqual_returnsTrue() {
        Set<Object> whitelist1 = new HashSet<>();
        Set<Object> whitelist2 = new HashSet<>();
        whitelist1.add("Test");
        whitelist2.add("Test");

        SetRestrictions restriction1 = new SetRestrictions(null, whitelist1);
        SetRestrictions restriction2 = new SetRestrictions(null, whitelist2);

        int hashCode1 = restriction1.hashCode();
        int hashCode2 = restriction2.hashCode();

        Assert.assertEquals(hashCode1, hashCode2);
    }

}

