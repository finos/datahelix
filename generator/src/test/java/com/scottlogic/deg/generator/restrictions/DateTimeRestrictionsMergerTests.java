package com.scottlogic.deg.generator.restrictions;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

class DateTimeRestrictionsMergerTests {
    @Test
    void merge_dateTimeRestrictionsAreBothNull_returnsNull() {
        DateTimeRestrictionsMerger merger = new DateTimeRestrictionsMerger();

        DateTimeRestrictions result = merger.merge(null, null);

        Assert.assertNull(result);
    }

    @Test
    void merge_leftIsNullAndRightHasAValue_returnsRight() {
        DateTimeRestrictionsMerger merger = new DateTimeRestrictionsMerger();
        DateTimeRestrictions right = new DateTimeRestrictions();

        DateTimeRestrictions result = merger.merge(null, right);

        Assert.assertSame(right, result);
    }

    @Test
    void merge_rightIsNullAndLeftHasAValue_returnsLeft() {
        DateTimeRestrictionsMerger merger = new DateTimeRestrictionsMerger();
        DateTimeRestrictions left = new DateTimeRestrictions();

        DateTimeRestrictions result = merger.merge(left, null);

        Assert.assertSame(left, result);
    }

    @Test
    void merge_leftHasMinAndRightHasMax_returnsDateTimeRestrictionsWithMergedMinAndMax() {
        DateTimeRestrictionsMerger merger = new DateTimeRestrictionsMerger();

        DateTimeRestrictions.DateTimeLimit minDateTimeLimit = new DateTimeRestrictions.DateTimeLimit(
            LocalDateTime.now().minusDays(7), false
        );
        DateTimeRestrictions.DateTimeLimit maxDateTimeLimit = new DateTimeRestrictions.DateTimeLimit(
            LocalDateTime.now(), false
        );
        DateTimeRestrictions left = new DateTimeRestrictions() {{ min = minDateTimeLimit; }};
        DateTimeRestrictions right = new DateTimeRestrictions() {{ max = maxDateTimeLimit; }};

        DateTimeRestrictions result = merger.merge(left, right);

        Assert.assertEquals(minDateTimeLimit, result.min);
        Assert.assertEquals(maxDateTimeLimit, result.max);
    }

    @Test
    void merge_leftHasMaxAndRightHasMin_returnsDateTimeRestrictionsWithMergedMinAndMax() {
        DateTimeRestrictionsMerger merger = new DateTimeRestrictionsMerger();

        DateTimeRestrictions.DateTimeLimit minDateTimeLimit = new DateTimeRestrictions.DateTimeLimit(
            LocalDateTime.now().minusDays(7), false
        );
        DateTimeRestrictions.DateTimeLimit maxDateTimeLimit = new DateTimeRestrictions.DateTimeLimit(
            LocalDateTime.now(), false
        );
        DateTimeRestrictions left = new DateTimeRestrictions() {{ max = maxDateTimeLimit; }};
        DateTimeRestrictions right = new DateTimeRestrictions() {{ min = minDateTimeLimit; }};

        DateTimeRestrictions result = merger.merge(left, right);

        Assert.assertEquals(minDateTimeLimit, result.min);
        Assert.assertEquals(maxDateTimeLimit, result.max);
    }

    @Test
    void merge_leftHasMinGreaterThanRightMax_returnsNull() {
        DateTimeRestrictionsMerger merger = new DateTimeRestrictionsMerger();

        DateTimeRestrictions.DateTimeLimit minDateTimeLimit = new DateTimeRestrictions.DateTimeLimit(
            LocalDateTime.now(), false
        );
        DateTimeRestrictions.DateTimeLimit maxDateTimeLimit = new DateTimeRestrictions.DateTimeLimit(
            LocalDateTime.now().minusDays(10), false
        );
        DateTimeRestrictions left = new DateTimeRestrictions() {{ min = minDateTimeLimit; }};
        DateTimeRestrictions right = new DateTimeRestrictions() {{ max = maxDateTimeLimit; }};

        DateTimeRestrictions result = merger.merge(left, right);

        Assert.assertNull(result);
    }

    @Test
    void merge_rightHasMinGreaterThanLeftMax_returnsNull() {
        DateTimeRestrictionsMerger merger = new DateTimeRestrictionsMerger();

        DateTimeRestrictions.DateTimeLimit minDateTimeLimit = new DateTimeRestrictions.DateTimeLimit(
            LocalDateTime.now(), false
        );
        DateTimeRestrictions.DateTimeLimit maxDateTimeLimit = new DateTimeRestrictions.DateTimeLimit(
            LocalDateTime.now().minusDays(10), false
        );
        DateTimeRestrictions left = new DateTimeRestrictions() {{ max = maxDateTimeLimit; }};
        DateTimeRestrictions right = new DateTimeRestrictions() {{ min = minDateTimeLimit; }};

        DateTimeRestrictions result = merger.merge(left, right);

        Assert.assertNull(result);
    }
}
