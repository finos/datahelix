package com.scottlogic.deg.generator.fieldspecs.relations;

import com.scottlogic.deg.common.date.TemporalAdjusterGenerator;
import com.scottlogic.deg.common.profile.Field;
import com.scottlogic.deg.generator.fieldspecs.FieldSpec;
import com.scottlogic.deg.generator.restrictions.linear.Limit;
import com.scottlogic.deg.generator.restrictions.linear.LinearRestrictions;
import com.scottlogic.deg.generator.restrictions.linear.LinearRestrictionsFactory;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.*;
import static com.scottlogic.deg.common.profile.FieldBuilder.createField;
import static com.shazam.shazamcrest.MatcherAssert.assertThat;
import static com.shazam.shazamcrest.matcher.Matchers.sameBeanAs;

class EqualToOffsetDateRelationTest {

    @Test
    public void reduceToRelatedFieldSpec_comparingTwoFields_givesEquivalentFieldSpec() {
        Field first = createField("first");
        Field second = createField("second");

        TemporalAdjusterGenerator wrapper = new TemporalAdjusterGenerator(ChronoUnit.DAYS, false);
        int days = 3;

        FieldSpecRelations relation = new EqualToOffsetDateRelation(first, second, wrapper, 3);

        OffsetDateTime exactTime = OffsetDateTime.of(
            2005,
            3,
            4,
            5,
            6,
            7,
            0,
            ZoneOffset.UTC);

        FieldSpec initialSpec = specEqualToTime(exactTime);

        FieldSpec expectedSpec = specEqualToTime(exactTime.minusDays(days));

        FieldSpec newSpec = relation.reduceToRelatedFieldSpec(initialSpec);

        assertThat(expectedSpec, sameBeanAs(newSpec));
    }



    @Test
    void reduceToRelatedFieldSpec_comparingTwoFieldsNegativeCase_givesEquivalentFieldSpec() {
        Field first = createField("first");
        Field second = createField("second");

        int days = -3;

        TemporalAdjusterGenerator wrapper = new TemporalAdjusterGenerator(ChronoUnit.DAYS, false);

        FieldSpecRelations relation = new EqualToOffsetDateRelation(first, second, wrapper, days);

        OffsetDateTime exactTime = OffsetDateTime.of(
            2005,
            3,
            5,
            5,
            6,
            7,
            0,
            ZoneOffset.UTC);

        FieldSpec initialSpec = specEqualToTime(exactTime);

        FieldSpec expectedSpec = specEqualToTime(exactTime.plusDays(Math.abs(days)));

        FieldSpec newSpec = relation.reduceToRelatedFieldSpec(initialSpec);

        assertEquals(expectedSpec, newSpec);
    }

    private static FieldSpec specEqualToTime(OffsetDateTime time) {
        Limit<OffsetDateTime> limit = new Limit<>(time, true);

        LinearRestrictions<OffsetDateTime> restrictions = LinearRestrictionsFactory.createDateTimeRestrictions(limit, limit);
        return FieldSpec.fromRestriction(restrictions);
    }
}