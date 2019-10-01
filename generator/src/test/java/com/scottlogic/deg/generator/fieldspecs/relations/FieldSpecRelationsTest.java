package com.scottlogic.deg.generator.fieldspecs.relations;

import com.scottlogic.deg.common.profile.Field;
import com.scottlogic.deg.common.profile.Types;
import com.scottlogic.deg.common.profile.constraintdetail.Timescale;
import com.scottlogic.deg.generator.fieldspecs.FieldSpec;
import com.scottlogic.deg.generator.restrictions.linear.Limit;
import com.scottlogic.deg.generator.restrictions.linear.LinearRestrictions;
import com.scottlogic.deg.generator.restrictions.linear.LinearRestrictionsFactory;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import static com.scottlogic.deg.common.profile.FieldBuilder.createField;
import static com.scottlogic.deg.common.util.Defaults.ISO_MAX_DATE;
import static com.scottlogic.deg.common.util.Defaults.ISO_MIN_DATE;
import static com.scottlogic.deg.generator.restrictions.linear.LinearRestrictionsFactory.createDateTimeRestrictions;
import static com.shazam.shazamcrest.MatcherAssert.assertThat;
import static com.shazam.shazamcrest.matcher.Matchers.sameBeanAs;

class FieldSpecRelationsTest {
    private Field main = createField("main", Types.DATETIME);
    private Field other = createField("other", Types.DATETIME);

    @Test
    public void equalTo_exactValue_returnsSame(){
        FieldSpec fieldSpec = forYears(2018, 2018);
        EqualToDateRelation relation = new EqualToDateRelation(main, other);

        FieldSpec actual = relation.reduceToRelatedFieldSpec(fieldSpec);
        FieldSpec expected = fieldSpec;

        assertThat(actual, sameBeanAs(expected));
    }

    @Test
    public void equalTo_range_returnsSame(){
        FieldSpec fieldSpec = forYears(2018, 2020);
        EqualToDateRelation relation = new EqualToDateRelation(main, other);

        FieldSpec actual = relation.reduceToRelatedFieldSpec(fieldSpec);
        FieldSpec expected = fieldSpec;

        assertThat(actual, sameBeanAs(expected));
    }

    @Test
    public void afterOrAt_exactValue_returnsBetween(){
        FieldSpec fieldSpec = forYears(2018, 2018);
        AfterDateRelation relation = new AfterDateRelation(main, other, true);

        FieldSpec actual = relation.reduceToRelatedFieldSpec(fieldSpec);
        FieldSpec expected = fromMin(2018, true);

        assertThat(actual, sameBeanAs(expected));
    }

    @Test
    public void afterOrAt_range_returnsFromMin(){
        FieldSpec fieldSpec = forYears(2018, 2020);
        AfterDateRelation relation = new AfterDateRelation(main, other, true);

        FieldSpec actual = relation.reduceToRelatedFieldSpec(fieldSpec);
        FieldSpec expected = fromMin(2018, true);

        assertThat(actual, sameBeanAs(expected));
    }

    @Test
    public void beforeOrAt_exactValue_returnsBetween(){
        FieldSpec fieldSpec = forYears(2018, 2018);
        BeforeDateRelation relation = new BeforeDateRelation(main, other, true);

        FieldSpec actual = relation.reduceToRelatedFieldSpec(fieldSpec);
        FieldSpec expected = fromMax(2018, true);

        assertThat(actual, sameBeanAs(expected));
    }

    @Test
    public void beforeOrAt_range_returnsFromMin(){
        FieldSpec fieldSpec = forYears(2018, 2020);
        BeforeDateRelation relation = new BeforeDateRelation(main, other, true);

        FieldSpec actual = relation.reduceToRelatedFieldSpec(fieldSpec);
        FieldSpec expected = fromMax(2020, true);

        assertThat(actual, sameBeanAs(expected));
    }

    private FieldSpec fromMin(int year, boolean inclusive) {
        OffsetDateTime min = OffsetDateTime.of(year, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC);
        LinearRestrictions restrictions = createDateTimeRestrictions(new Limit<>(min, inclusive), new Limit(ISO_MAX_DATE, true));
        return FieldSpec.fromRestriction(restrictions);
    }

    private FieldSpec fromMax(int year, boolean inclusive) {
        OffsetDateTime max = OffsetDateTime.of(year, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC);
        LinearRestrictions restrictions = createDateTimeRestrictions(new Limit(ISO_MIN_DATE, true), new Limit<>(max, inclusive));
        return FieldSpec.fromRestriction(restrictions);
    }

    private FieldSpec forYears(int minYear, int maxYear) {
        OffsetDateTime min = OffsetDateTime.of(minYear, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC);
        OffsetDateTime max = OffsetDateTime.of(maxYear, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC);
        LinearRestrictions<OffsetDateTime> restrictions = new LinearRestrictions<>(new Limit<>(min, true), new Limit<>(max, true), Timescale.YEARS);
        return FieldSpec.fromRestriction(restrictions).withNotNull();
    }
}