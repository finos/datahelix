package com.scottlogic.deg.generator.restrictions;

import org.junit.Assert;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.Matchers.equalTo;

class SetRestrictionsTests {
    @Nested
    class fromWhitelist {
        @Test
        void copiesWhitelistAndUsesEmptyBlacklist() {
            SetRestrictions objectUnderTest = SetRestrictions.fromWhitelist(set(1, 2, 3));

            Assert.assertThat(objectUnderTest.getWhitelist(), equalTo(set(1, 2, 3)));
        }
    }

    @Nested
    class allowNoValues {
        @Test
        void isSameAsEmptyWhitelist() {
            assertEqual(
                SetRestrictions.allowNoValues(),
                SetRestrictions.fromWhitelist(Collections.emptySet()));
        }
    }

    @Nested
    class equality {
        @Test
        void isNotEqualToNull() {
            assertNotEqual(
                SetRestrictions.fromWhitelist(set("Test")),
                null);
        }

        @Test
        void isNotEqualToNonSetRestrictionsObject() {
            assertNotEqual(
                SetRestrictions.fromWhitelist(set("Test")),
                "String");
        }

        @Test
        void twoEmptyWhitelistsAreEqual() {
            assertEqual(
                SetRestrictions.fromWhitelist(Collections.emptySet()),
                SetRestrictions.fromWhitelist(Collections.emptySet()));
        }

        @Test
        void twoDifferentWhitelistsAreNotEqual() {
            assertNotEqual( // same types
                SetRestrictions.fromWhitelist(set("Test")),
                SetRestrictions.fromWhitelist(set("Parrot")));

            assertNotEqual( // different types
                SetRestrictions.fromWhitelist(set("Test")),
                SetRestrictions.fromWhitelist(set(1)));
        }

        @Test
        void twoIdenticalWhitelistsAreEqual() {
            assertEqual(
                SetRestrictions.fromWhitelist(set("Test")),
                SetRestrictions.fromWhitelist(set("Test")));
        }
    }

    private static void assertEqual(Object a, Object b) {
        Assert.assertEquals(a, b);
        Assert.assertEquals(b, a);

        if (a != null && b != null) Assert.assertEquals(a.hashCode(), b.hashCode());
    }

    private static void assertNotEqual(Object a, Object b) {
        Assert.assertNotEquals(a, b);
        Assert.assertNotEquals(b, a);

        // not sure about this; hash collisions aren't intrinsically functional errors
        if (a != null && b != null) Assert.assertNotEquals(a.hashCode(), b.hashCode());
    }

    private static Set<Object> set(Object... values) {
        return new HashSet<>(Arrays.asList(values));
    }
}
