package com.scottlogic.deg.generator.restrictions;

import com.scottlogic.deg.generator.generation.fieldvaluesources.datetime.Timescale;
import org.assertj.core.api.AutoCloseableSoftAssertions;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.nullValue;
import static org.hamcrest.core.IsSame.sameInstance;

class DateTimeRestrictionsMergerTests {

    private DateTimeRestrictionsMerger merger;

    private static final OffsetDateTime REFERENCE_TIME = OffsetDateTime.of(2000, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC);

    @BeforeEach
    void setUp() {
        merger = new DateTimeRestrictionsMerger();
    }

    @Test
    void merge_dateTimeRestrictionsAreBothNull_returnsNull() {
        MergeResult<DateTimeRestrictions> result = merger.merge(null, null);

        Assert.assertThat(result, not(nullValue()));
        Assert.assertThat(result.successful, is(true));
        Assert.assertThat(result.restrictions, is(nullValue()));
    }

    @Test
    void merge_leftIsNullAndRightHasAValue_returnsRight() {
        DateTimeRestrictions right = new DateTimeRestrictions();

        MergeResult<DateTimeRestrictions> result = merger.merge(null, right);

        Assert.assertThat(result, not(nullValue()));
        Assert.assertThat(result.successful, is(true));
        Assert.assertThat(result.restrictions, sameInstance(right));
    }

    @Test
    void merge_rightIsNullAndLeftHasAValue_returnsLeft() {
        DateTimeRestrictions left = new DateTimeRestrictions();

        MergeResult<DateTimeRestrictions> result = merger.merge(left, null);

        Assert.assertThat(result, not(nullValue()));
        Assert.assertThat(result.successful, is(true));
        Assert.assertThat(result.restrictions, sameInstance(left));
    }

    @Test
    void merge_leftHasMinAndRightHasMax_returnsDateTimeRestrictionsWithMergedMinAndMax() {
        DateTimeRestrictions.DateTimeLimit minDateTimeLimit = new DateTimeRestrictions.DateTimeLimit(
            REFERENCE_TIME.minusDays(7), false
        );
        DateTimeRestrictions.DateTimeLimit maxDateTimeLimit = new DateTimeRestrictions.DateTimeLimit(
            REFERENCE_TIME, false
        );
        DateTimeRestrictions left = new DateTimeRestrictions() {{
            min = minDateTimeLimit;
        }};
        DateTimeRestrictions right = new DateTimeRestrictions() {{
            max = maxDateTimeLimit;
        }};

        MergeResult<DateTimeRestrictions> result = merger.merge(left, right);

        Assert.assertThat(result, not(nullValue()));
        Assert.assertThat(result.successful, is(true));
        Assert.assertThat(result.restrictions.min, equalTo(minDateTimeLimit));
        Assert.assertThat(result.restrictions.max, equalTo(maxDateTimeLimit));
    }

    @Test
    void merge_leftHasMaxAndRightHasMin_returnsDateTimeRestrictionsWithMergedMinAndMax() {
        DateTimeRestrictions.DateTimeLimit minDateTimeLimit = new DateTimeRestrictions.DateTimeLimit(
            REFERENCE_TIME.minusDays(7), false
        );
        DateTimeRestrictions.DateTimeLimit maxDateTimeLimit = new DateTimeRestrictions.DateTimeLimit(
            REFERENCE_TIME, false
        );
        DateTimeRestrictions left = new DateTimeRestrictions() {{
            max = maxDateTimeLimit;
        }};
        DateTimeRestrictions right = new DateTimeRestrictions() {{
            min = minDateTimeLimit;
        }};

        MergeResult<DateTimeRestrictions> result = merger.merge(left, right);

        Assert.assertThat(result, not(nullValue()));
        Assert.assertThat(result.successful, is(true));
        Assert.assertThat(result.restrictions.min, equalTo(minDateTimeLimit));
        Assert.assertThat(result.restrictions.max, equalTo(maxDateTimeLimit));
    }

    @Test
    void merge_leftHasMinGreaterThanRightMax_returnsNull() {
        DateTimeRestrictions.DateTimeLimit minDateTimeLimit = new DateTimeRestrictions.DateTimeLimit(
            OffsetDateTime.now(), false
        );
        DateTimeRestrictions.DateTimeLimit maxDateTimeLimit = new DateTimeRestrictions.DateTimeLimit(
            OffsetDateTime.now().minusDays(10), false
        );
        DateTimeRestrictions left = new DateTimeRestrictions() {{
            min = minDateTimeLimit;
        }};
        DateTimeRestrictions right = new DateTimeRestrictions() {{
            max = maxDateTimeLimit;
        }};

        MergeResult<DateTimeRestrictions> result = merger.merge(left, right);

        Assert.assertThat(result, not(nullValue()));
        Assert.assertThat(result.successful, is(false));
    }

    @Test
    void merge_rightHasMinGreaterThanLeftMax_returnsNull() {
        DateTimeRestrictions.DateTimeLimit minDateTimeLimit = new DateTimeRestrictions.DateTimeLimit(
            OffsetDateTime.now(), false
        );
        DateTimeRestrictions.DateTimeLimit maxDateTimeLimit = new DateTimeRestrictions.DateTimeLimit(
            OffsetDateTime.now().minusDays(10), false
        );
        DateTimeRestrictions left = new DateTimeRestrictions() {{
            max = maxDateTimeLimit;
        }};
        DateTimeRestrictions right = new DateTimeRestrictions() {{
            min = minDateTimeLimit;
        }};

        MergeResult<DateTimeRestrictions> result = merger.merge(left, right);

        Assert.assertThat(result, not(nullValue()));
        Assert.assertThat(result.successful, is(false));
    }

    @Test
    void merge_rightAndLeftHaveNoMax_shouldNotReturnNull() {
        DateTimeRestrictions.DateTimeLimit minDateTimeLimit = new DateTimeRestrictions.DateTimeLimit(
            REFERENCE_TIME, false
        );
        DateTimeRestrictions left = new DateTimeRestrictions() {{
            min = minDateTimeLimit;
        }};
        DateTimeRestrictions right = new DateTimeRestrictions() {{
            min = minDateTimeLimit;
        }};

        MergeResult<DateTimeRestrictions> result = merger.merge(left, right);

        Assert.assertThat(result, not(nullValue()));
        Assert.assertThat(result.successful, is(true));
        Assert.assertThat(result.restrictions.min, equalTo(minDateTimeLimit));
        Assert.assertThat(result.restrictions.max, is(nullValue()));
    }

    @Test
    void merge_rightAndLeftHaveNoMin_shouldNotReturnNull() {
        DateTimeRestrictions.DateTimeLimit maxDateTimeLimit = new DateTimeRestrictions.DateTimeLimit(
            REFERENCE_TIME, false
        );
        DateTimeRestrictions left = new DateTimeRestrictions() {{
            max = maxDateTimeLimit;
        }};
        DateTimeRestrictions right = new DateTimeRestrictions() {{
            max = maxDateTimeLimit;
        }};

        MergeResult<DateTimeRestrictions> result = merger.merge(left, right);

        assertThat(result).isNotNull()
            .extracting(r -> r.successful)
            .isEqualTo(true);

        DateTimeRestrictions restrictions = result.restrictions;
        assertThat(restrictions).isNotNull();

        try (AutoCloseableSoftAssertions softly = new AutoCloseableSoftAssertions()) {
            softly.assertThat(restrictions.min).isNull();
            softly.assertThat(restrictions.max).isEqualTo(maxDateTimeLimit);
        }
    }

    @Test
    void merge_minExclusiveSameAsMaxInclusive_shouldReturnAsUnsuccessful() {
        OffsetDateTime referenceTime = OffsetDateTime.now();

        DateTimeRestrictions.DateTimeLimit minDateTimeLimit = new DateTimeRestrictions.DateTimeLimit(
            referenceTime, false
        );
        DateTimeRestrictions.DateTimeLimit maxDateTimeLimit = new DateTimeRestrictions.DateTimeLimit(
            referenceTime, true
        );
        DateTimeRestrictions left = new DateTimeRestrictions() {{
            max = maxDateTimeLimit;
        }};
        DateTimeRestrictions right = new DateTimeRestrictions() {{
            min = minDateTimeLimit;
        }};

        MergeResult<DateTimeRestrictions> result = merger.merge(left, right);

        Assert.assertThat(result, not(nullValue()));
        Assert.assertThat(result.successful, is(false));
    }

    @Test
    void merge_minExclusiveSameAsMaxExclusive_shouldReturnAsUnsuccessful() {
        OffsetDateTime referenceTime = OffsetDateTime.now();

        DateTimeRestrictions.DateTimeLimit minDateTimeLimit = new DateTimeRestrictions.DateTimeLimit(
            referenceTime, false
        );
        DateTimeRestrictions.DateTimeLimit maxDateTimeLimit = new DateTimeRestrictions.DateTimeLimit(
            referenceTime, false
        );
        DateTimeRestrictions left = new DateTimeRestrictions() {{
            max = maxDateTimeLimit;
        }};
        DateTimeRestrictions right = new DateTimeRestrictions() {{
            min = minDateTimeLimit;
        }};

        MergeResult<DateTimeRestrictions> result = merger.merge(left, right);

        Assert.assertThat(result, not(nullValue()));
        Assert.assertThat(result.successful, is(false));
    }

    @Test
    void merge_minInclusiveSameAsMaxExclusive_shouldReturnAsUnsuccessful() {
        OffsetDateTime referenceTime = OffsetDateTime.now();

        DateTimeRestrictions.DateTimeLimit minDateTimeLimit = new DateTimeRestrictions.DateTimeLimit(
            referenceTime, true
        );
        DateTimeRestrictions.DateTimeLimit maxDateTimeLimit = new DateTimeRestrictions.DateTimeLimit(
            referenceTime, false
        );
        DateTimeRestrictions left = new DateTimeRestrictions() {{
            max = maxDateTimeLimit;
        }};
        DateTimeRestrictions right = new DateTimeRestrictions() {{
            min = minDateTimeLimit;
        }};

        MergeResult<DateTimeRestrictions> result = merger.merge(left, right);

        Assert.assertThat(result, not(nullValue()));
        Assert.assertThat(result.successful, is(false));
    }

    @Test
    void merge_minOfDifferentGranularity_shouldReturnMostCoarse() {
        OffsetDateTime laterTime = REFERENCE_TIME.plusSeconds(1);

        DateTimeRestrictions.DateTimeLimit lowerDateTimeLimit = new DateTimeRestrictions.DateTimeLimit(
            REFERENCE_TIME, true
        );
        DateTimeRestrictions.DateTimeLimit upperDateTimeLimit = new DateTimeRestrictions.DateTimeLimit(
            laterTime, false
        );
        DateTimeRestrictions left = new DateTimeRestrictions(Timescale.HOURS) {{
            min = lowerDateTimeLimit;
        }};
        DateTimeRestrictions right = new DateTimeRestrictions(Timescale.MILLIS) {{
            min = upperDateTimeLimit;
        }};

        MergeResult<DateTimeRestrictions> result = merger.merge(left, right);
        DateTimeRestrictions restrictions = result.restrictions;

        assertThat(result)
            .isNotNull()
            .extracting(e -> e.successful)
            .isEqualTo(true);

        try (AutoCloseableSoftAssertions softly = new AutoCloseableSoftAssertions()) {
            softly.assertThat(restrictions)
                .isNotNull()
                .extracting(DateTimeRestrictions::getGranularity)
                .isEqualTo(Timescale.HOURS);
            softly.assertThat(restrictions.min)
                .isEqualTo(new DateTimeRestrictions.DateTimeLimit(REFERENCE_TIME.plusHours(1), false));
        }
    }
}
