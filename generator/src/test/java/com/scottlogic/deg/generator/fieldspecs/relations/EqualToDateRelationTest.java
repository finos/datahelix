package com.scottlogic.deg.generator.fieldspecs.relations;

import com.scottlogic.deg.common.profile.Field;
import com.scottlogic.deg.common.profile.FieldType;
import com.scottlogic.deg.generator.fieldspecs.FieldSpec;
import com.scottlogic.deg.generator.fieldspecs.FieldSpecFactory;
import com.scottlogic.deg.generator.fieldspecs.whitelist.DistributedList;
import com.scottlogic.deg.generator.generation.databags.DataBagValue;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import static com.shazam.shazamcrest.MatcherAssert.assertThat;
import static com.shazam.shazamcrest.matcher.Matchers.sameBeanAs;

class EqualToDateRelationTest {

    private final Field a = new Field("a", FieldType.DATETIME, false ,"", false);
    private final Field b = new Field("b", FieldType.DATETIME, false, "", false);
    private final FieldSpecRelations equalToDateRelations = new EqualToRelation(a, b);

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

        FieldSpec expected = FieldSpecFactory.fromList(DistributedList.singleton(value));
        assertThat(result, sameBeanAs(expected));
    }

    @Test
    public void testReduceToFieldSpec_withNull_reducesToSpec() {
        OffsetDateTime value = null;
        DataBagValue generatedValue = new DataBagValue(value);

        FieldSpec result = equalToDateRelations.reduceValueToFieldSpec(generatedValue);

        FieldSpec expected = FieldSpecFactory.nullOnly();
        assertThat(result, sameBeanAs(expected));
    }

}