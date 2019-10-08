package com.scottlogic.deg.generator.fieldspecs.relations;

import com.scottlogic.deg.common.profile.Field;
import com.scottlogic.deg.common.profile.Types;
import com.scottlogic.deg.common.profile.constraintdetail.DateTimeGranularity;
import com.scottlogic.deg.generator.fieldspecs.FieldSpec;
import com.scottlogic.deg.generator.generation.databags.DataBagValue;
import com.scottlogic.deg.generator.restrictions.linear.LinearRestrictions;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.*;

class EqualToDateRelationTest {

    private final Field a = new Field("a", Types.DATETIME, false ,"", false);
    private final Field b = new Field("b", Types.DATETIME, false, "", false);
    private final FieldSpecRelations equalToDateRelations = new EqualToDateRelation(a, b);

    @Test
    public void testReduceToFieldSpec_withNotNull_reducesToSpec() {
        OffsetDateTime value = OffsetDateTime.of(2000,
            1,
            1,
            0,
            0,
            0,
            0,
            ZoneOffset.UTC);
        DataBagValue generatedValue = new DataBagValue(value);

        FieldSpec result = equalToDateRelations.reduceValueToFieldSpec(generatedValue);

        FieldSpec expected = FieldSpec.fromRestriction(new LinearRestrictions<>(value, value, new DateTimeGranularity(ChronoUnit.MILLIS)));
        assertEquals(expected, result);
    }

    @Test
    public void testReduceToFieldSpec_withNull_reducesToSpec() {
        OffsetDateTime value = null;
        DataBagValue generatedValue = new DataBagValue(value);

        FieldSpec result = equalToDateRelations.reduceValueToFieldSpec(generatedValue);

        FieldSpec expected = FieldSpec.empty();
        assertEquals(expected, result);
    }

}