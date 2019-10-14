package com.scottlogic.deg.generator.fieldspecs.relations;

import com.scottlogic.deg.common.profile.Field;
import com.scottlogic.deg.common.profile.FieldType;
import com.scottlogic.deg.common.profile.constraintdetail.DateTimeGranularity;
import com.scottlogic.deg.common.util.defaults.DateTimeDefaults;
import com.scottlogic.deg.generator.fieldspecs.FieldSpec;
import com.scottlogic.deg.generator.fieldspecs.FieldSpecFactory;
import com.scottlogic.deg.generator.restrictions.linear.LinearRestrictions;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import static com.scottlogic.deg.common.profile.FieldBuilder.createField;
import static com.scottlogic.deg.common.util.Defaults.ISO_MAX_DATE;
import static com.scottlogic.deg.common.util.Defaults.ISO_MIN_DATE;
import static com.scottlogic.deg.generator.restrictions.linear.LinearRestrictionsFactory.createDateTimeRestrictions;
import static com.shazam.shazamcrest.MatcherAssert.assertThat;
import static com.shazam.shazamcrest.matcher.Matchers.sameBeanAs;
import static java.time.temporal.ChronoUnit.MILLIS;
import static java.time.temporal.ChronoUnit.YEARS;

class FieldSpecRelationsTest {
    private Field main = createField("main", FieldType.DATETIME);
    private Field other = createField("other", FieldType.DATETIME);

    @Test
    public void equalTo_exactValue_returnsSame(){
        FieldSpec fieldSpec = forYears(2018, 2018);
        EqualToRelation relation = new EqualToRelation(main, other);

        FieldSpec actual = relation.reduceToRelatedFieldSpec(fieldSpec);
        FieldSpec expected = fieldSpec;

        assertThat(actual, sameBeanAs(expected));
    }

    @Test
    public void equalTo_range_returnsSame(){
        FieldSpec fieldSpec = forYears(2018, 2020);
        EqualToRelation relation = new EqualToRelation(main, other);

        FieldSpec actual = relation.reduceToRelatedFieldSpec(fieldSpec);
        FieldSpec expected = fieldSpec;

        assertThat(actual, sameBeanAs(expected));
    }

    @Test
    public void afterOrAt_exactValue_returnsBetween(){
        FieldSpec fieldSpec = forYears(2018, 2018);
        AfterRelation relation = new AfterRelation(main, other, true, DateTimeDefaults.get());

        FieldSpec actual = relation.reduceToRelatedFieldSpec(fieldSpec);
        FieldSpec expected = fromMin(2018);

        assertThat(actual, sameBeanAs(expected));
    }

    @Test
    public void afterOrAt_range_returnsFromMin(){
        FieldSpec fieldSpec = forYears(2018, 2020);
        AfterRelation relation = new AfterRelation(main, other, true, DateTimeDefaults.get());

        FieldSpec actual = relation.reduceToRelatedFieldSpec(fieldSpec);
        FieldSpec expected = fromMin(2018);

        assertThat(actual, sameBeanAs(expected));
    }

    @Test
    public void after_range_returnsFromMin(){
        FieldSpec fieldSpec = forYears(2018, 2021);
        AfterRelation relation = new AfterRelation(main, other, false, DateTimeDefaults.get());

        FieldSpec actual = relation.reduceToRelatedFieldSpec(fieldSpec);
        FieldSpec expected = fromMin(2019);

        assertThat(actual, sameBeanAs(expected));
    }

    @Test
    public void beforeOrAt_exactValue_returnsBetween(){
        FieldSpec fieldSpec = forYears(2018, 2018);
        BeforeRelation relation = new BeforeRelation(main, other, true, DateTimeDefaults.get());

        FieldSpec actual = relation.reduceToRelatedFieldSpec(fieldSpec);
        FieldSpec expected = fromMax(2018);

        assertThat(actual, sameBeanAs(expected));
    }

    @Test
    public void beforeOrAt_range_returnsFromMin(){
        FieldSpec fieldSpec = forYears(2018, 2020);
        BeforeRelation relation = new BeforeRelation(main, other, true, DateTimeDefaults.get());

        FieldSpec actual = relation.reduceToRelatedFieldSpec(fieldSpec);
        FieldSpec expected = fromMax(2020);

        assertThat(actual, sameBeanAs(expected));
    }

    @Test
    public void before_range_returnsFromMin(){
        FieldSpec fieldSpec = forYears(2017, 2020);
        BeforeRelation relation = new BeforeRelation(main, other, false, DateTimeDefaults.get());

        FieldSpec actual = relation.reduceToRelatedFieldSpec(fieldSpec);
        FieldSpec expected = fromMax(2019);

        assertThat(actual, sameBeanAs(expected));
    }

    private FieldSpec fromMin(int year) {
        OffsetDateTime min = OffsetDateTime.of(year, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC);
        LinearRestrictions restrictions = new LinearRestrictions(min, ISO_MAX_DATE, new DateTimeGranularity(MILLIS));
        return FieldSpecFactory.fromRestriction(restrictions);
    }

    private FieldSpec fromMax(int year) {
        OffsetDateTime max = OffsetDateTime.of(year, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC);
        LinearRestrictions restrictions = new LinearRestrictions(ISO_MIN_DATE, max, new DateTimeGranularity(MILLIS));
        return FieldSpecFactory.fromRestriction(restrictions);
    }

    private FieldSpec forYears(int minYear, int maxYear) {
        OffsetDateTime min = OffsetDateTime.of(minYear, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC);
        OffsetDateTime max = OffsetDateTime.of(maxYear, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC);
        LinearRestrictions<OffsetDateTime> restrictions = new LinearRestrictions(min, max, new DateTimeGranularity(YEARS));
        return FieldSpecFactory.fromRestriction(restrictions).withNotNull();
    }
}