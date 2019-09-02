package com.scottlogic.deg.generator.fieldspecs.relations;

import com.scottlogic.deg.common.date.TemporalAdjusterGenerator;
import com.scottlogic.deg.common.profile.Field;
import com.scottlogic.deg.generator.fieldspecs.FieldSpec;
import com.scottlogic.deg.generator.restrictions.DateTimeLimit;
import com.scottlogic.deg.generator.restrictions.DateTimeRestrictions;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.*;

class EqualToOffsetDateRelationTest {

    @Test
    public void reduceToRelatedFieldSpec_comparingTwoFields_givesEquivalentFieldSpec() {
        Field first = new Field("first");
        Field second = new Field("second");

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

        assertEquals(expectedSpec, newSpec);
    }



    @Test
    void reduceToRelatedFieldSpec_comparingTwoFieldsNegativeCase_givesEquivalentFieldSpec() {
        Field first = new Field("first");
        Field second = new Field("second");

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
        DateTimeLimit limit = new DateTimeLimit(time, true);

        DateTimeRestrictions restrictions = new DateTimeRestrictions();
        restrictions.min = limit;
        restrictions.max = limit;
        return FieldSpec.Empty.withDateTimeRestrictions(restrictions);
    }
}