package com.scottlogic.deg.generator.restrictions;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.nullValue;
import static org.hamcrest.core.IsSame.sameInstance;

class DateTimeRestrictionsMergerTests {
    @Test
    void merge_dateTimeRestrictionsAreBothNull_returnsNull() {
        DateTimeRestrictionsMerger merger = new DateTimeRestrictionsMerger();

        MergeResult<DateTimeRestrictions> result = merger.merge(null, null);

        Assert.assertThat(result, not(nullValue()));
        Assert.assertThat(result.successful, is(true));
        Assert.assertThat(result.restrictions, is(nullValue()));
    }

    @Test
    void merge_leftIsNullAndRightHasAValue_returnsRight() {
        DateTimeRestrictionsMerger merger = new DateTimeRestrictionsMerger();
        DateTimeRestrictions right = new DateTimeRestrictions();

        MergeResult<DateTimeRestrictions> result = merger.merge(null, right);

        Assert.assertThat(result, not(nullValue()));
        Assert.assertThat(result.successful, is(true));
        Assert.assertThat(result.restrictions, sameInstance(right));
    }

    @Test
    void merge_rightIsNullAndLeftHasAValue_returnsLeft() {
        DateTimeRestrictionsMerger merger = new DateTimeRestrictionsMerger();
        DateTimeRestrictions left = new DateTimeRestrictions();

        MergeResult<DateTimeRestrictions> result = merger.merge(left, null);

        Assert.assertThat(result, not(nullValue()));
        Assert.assertThat(result.successful, is(true));
        Assert.assertThat(result.restrictions, sameInstance(left));
    }

    @Test
    void merge_leftHasMinAndRightHasMax_returnsDateTimeRestrictionsWithMergedMinAndMax() {
        DateTimeRestrictionsMerger merger = new DateTimeRestrictionsMerger();

        DateTimeRestrictions.DateTimeLimit minDateTimeLimit = new DateTimeRestrictions.DateTimeLimit(
            OffsetDateTime.now().minusDays(7), false
        );
        DateTimeRestrictions.DateTimeLimit maxDateTimeLimit = new DateTimeRestrictions.DateTimeLimit(
            OffsetDateTime.now(), false
        );
        DateTimeRestrictions left = new DateTimeRestrictions() {{ min = minDateTimeLimit; }};
        DateTimeRestrictions right = new DateTimeRestrictions() {{ max = maxDateTimeLimit; }};

        MergeResult<DateTimeRestrictions> result = merger.merge(left, right);

        Assert.assertThat(result, not(nullValue()));
        Assert.assertThat(result.successful, is(true));
        Assert.assertThat(result.restrictions.min, equalTo(minDateTimeLimit));
        Assert.assertThat(result.restrictions.max, equalTo(maxDateTimeLimit));
    }

    @Test
    void merge_leftHasMaxAndRightHasMin_returnsDateTimeRestrictionsWithMergedMinAndMax() {
        DateTimeRestrictionsMerger merger = new DateTimeRestrictionsMerger();

        DateTimeRestrictions.DateTimeLimit minDateTimeLimit = new DateTimeRestrictions.DateTimeLimit(
            OffsetDateTime.now().minusDays(7), false
        );
        DateTimeRestrictions.DateTimeLimit maxDateTimeLimit = new DateTimeRestrictions.DateTimeLimit(
            OffsetDateTime.now(), false
        );
        DateTimeRestrictions left = new DateTimeRestrictions() {{ max = maxDateTimeLimit; }};
        DateTimeRestrictions right = new DateTimeRestrictions() {{ min = minDateTimeLimit; }};

        MergeResult<DateTimeRestrictions> result = merger.merge(left, right);

        Assert.assertThat(result, not(nullValue()));
        Assert.assertThat(result.successful, is(true));
        Assert.assertThat(result.restrictions.min, equalTo(minDateTimeLimit));
        Assert.assertThat(result.restrictions.max, equalTo(maxDateTimeLimit));
    }

    @Test
    void merge_leftHasMinGreaterThanRightMax_returnsNull() {
        DateTimeRestrictionsMerger merger = new DateTimeRestrictionsMerger();

        DateTimeRestrictions.DateTimeLimit minDateTimeLimit = new DateTimeRestrictions.DateTimeLimit(
            OffsetDateTime.now(), false
        );
        DateTimeRestrictions.DateTimeLimit maxDateTimeLimit = new DateTimeRestrictions.DateTimeLimit(
            OffsetDateTime.now().minusDays(10), false
        );
        DateTimeRestrictions left = new DateTimeRestrictions() {{ min = minDateTimeLimit; }};
        DateTimeRestrictions right = new DateTimeRestrictions() {{ max = maxDateTimeLimit; }};

        MergeResult<DateTimeRestrictions> result = merger.merge(left, right);

        Assert.assertThat(result, not(nullValue()));
        Assert.assertThat(result.successful, is(false));
    }

    @Test
    void merge_rightHasMinGreaterThanLeftMax_returnsNull() {
        DateTimeRestrictionsMerger merger = new DateTimeRestrictionsMerger();

        DateTimeRestrictions.DateTimeLimit minDateTimeLimit = new DateTimeRestrictions.DateTimeLimit(
            OffsetDateTime.now(), false
        );
        DateTimeRestrictions.DateTimeLimit maxDateTimeLimit = new DateTimeRestrictions.DateTimeLimit(
            OffsetDateTime.now().minusDays(10), false
        );
        DateTimeRestrictions left = new DateTimeRestrictions() {{ max = maxDateTimeLimit; }};
        DateTimeRestrictions right = new DateTimeRestrictions() {{ min = minDateTimeLimit; }};

        MergeResult<DateTimeRestrictions> result = merger.merge(left, right);

        Assert.assertThat(result, not(nullValue()));
        Assert.assertThat(result.successful, is(false));
    }

    @Test
    void merge_rightAndLeftHaveNoMax_shouldNotReturnNull() {
        DateTimeRestrictionsMerger merger = new DateTimeRestrictionsMerger();

        DateTimeRestrictions.DateTimeLimit minDateTimeLimit = new DateTimeRestrictions.DateTimeLimit(
            OffsetDateTime.now(), false
        );
        DateTimeRestrictions left = new DateTimeRestrictions() {{ min = minDateTimeLimit; }};
        DateTimeRestrictions right = new DateTimeRestrictions() {{ min = minDateTimeLimit; }};

        MergeResult<DateTimeRestrictions> result = merger.merge(left, right);

        Assert.assertThat(result, not(nullValue()));
        Assert.assertThat(result.successful, is(true));
        Assert.assertThat(result.restrictions.min, equalTo(minDateTimeLimit));
        Assert.assertThat(result.restrictions.max, is(nullValue()));
    }

    @Test
    void merge_rightAndLeftHaveNoMin_shouldNotReturnNull() {
        DateTimeRestrictionsMerger merger = new DateTimeRestrictionsMerger();

        DateTimeRestrictions.DateTimeLimit maxDateTimeLimit = new DateTimeRestrictions.DateTimeLimit(
            OffsetDateTime.now(), false
        );
        DateTimeRestrictions left = new DateTimeRestrictions() {{ max = maxDateTimeLimit; }};
        DateTimeRestrictions right = new DateTimeRestrictions() {{ max = maxDateTimeLimit; }};

        MergeResult<DateTimeRestrictions> result = merger.merge(left, right);

        Assert.assertThat(result, not(nullValue()));
        Assert.assertThat(result.successful, is(true));
        Assert.assertThat(result.restrictions.min, is(nullValue()));
        Assert.assertThat(result.restrictions.max, equalTo(maxDateTimeLimit));
    }

    @Test
    void merge_minExclusiveSameAsMaxInclusive_shouldReturnAsUnsuccessful() {
        DateTimeRestrictionsMerger merger = new DateTimeRestrictionsMerger();
        OffsetDateTime referenceTime = OffsetDateTime.now();

        DateTimeRestrictions.DateTimeLimit minDateTimeLimit = new DateTimeRestrictions.DateTimeLimit(
            referenceTime, false
        );
        DateTimeRestrictions.DateTimeLimit maxDateTimeLimit = new DateTimeRestrictions.DateTimeLimit(
            referenceTime, true
        );
        DateTimeRestrictions left = new DateTimeRestrictions() {{ max = maxDateTimeLimit; }};
        DateTimeRestrictions right = new DateTimeRestrictions() {{ min = minDateTimeLimit; }};

        MergeResult<DateTimeRestrictions> result = merger.merge(left, right);

        Assert.assertThat(result, not(nullValue()));
        Assert.assertThat(result.successful, is(false));
    }

    @Test
    void merge_minExclusiveSameAsMaxExclusive_shouldReturnAsUnsuccessful() {
        DateTimeRestrictionsMerger merger = new DateTimeRestrictionsMerger();
        OffsetDateTime referenceTime = OffsetDateTime.now();

        DateTimeRestrictions.DateTimeLimit minDateTimeLimit = new DateTimeRestrictions.DateTimeLimit(
            referenceTime, false
        );
        DateTimeRestrictions.DateTimeLimit maxDateTimeLimit = new DateTimeRestrictions.DateTimeLimit(
            referenceTime, false
        );
        DateTimeRestrictions left = new DateTimeRestrictions() {{ max = maxDateTimeLimit; }};
        DateTimeRestrictions right = new DateTimeRestrictions() {{ min = minDateTimeLimit; }};

        MergeResult<DateTimeRestrictions> result = merger.merge(left, right);

        Assert.assertThat(result, not(nullValue()));
        Assert.assertThat(result.successful, is(false));
    }

    @Test
    void merge_minInclusiveSameAsMaxExclusive_shouldReturnAsUnsuccessful() {
        DateTimeRestrictionsMerger merger = new DateTimeRestrictionsMerger();
        OffsetDateTime referenceTime = OffsetDateTime.now();

        DateTimeRestrictions.DateTimeLimit minDateTimeLimit = new DateTimeRestrictions.DateTimeLimit(
            referenceTime, true
        );
        DateTimeRestrictions.DateTimeLimit maxDateTimeLimit = new DateTimeRestrictions.DateTimeLimit(
            referenceTime, false
        );
        DateTimeRestrictions left = new DateTimeRestrictions() {{ max = maxDateTimeLimit; }};
        DateTimeRestrictions right = new DateTimeRestrictions() {{ min = minDateTimeLimit; }};

        MergeResult<DateTimeRestrictions> result = merger.merge(left, right);

        Assert.assertThat(result, not(nullValue()));
        Assert.assertThat(result.successful, is(false));
    }
}
