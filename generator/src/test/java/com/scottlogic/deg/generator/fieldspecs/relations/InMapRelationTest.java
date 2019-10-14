package com.scottlogic.deg.generator.fieldspecs.relations;

import com.scottlogic.deg.common.profile.Field;
import com.scottlogic.deg.generator.fieldspecs.FieldSpec;
import com.scottlogic.deg.generator.fieldspecs.FieldSpecFactory;
import com.scottlogic.deg.generator.fieldspecs.whitelist.DistributedList;
import com.scottlogic.deg.generator.generation.databags.DataBagValue;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static com.scottlogic.deg.common.profile.FieldBuilder.createField;
import static org.junit.jupiter.api.Assertions.assertThrows;

class InMapRelationTest {
    private static InMapRelation testInstance;

    @BeforeAll
    static void before() {
        Field f1 = createField("field1");
        Field f2 = createField("field1");
        List<String> values = Arrays.asList("foo", "bar");

        testInstance = new InMapRelation(f1, f2, DistributedList.uniform(values));
    }

    @Test
    void reduceValueToFieldSpec_whenValidIndex_returnFieldSpec() {
        FieldSpec expected = FieldSpecFactory.fromList(DistributedList.singleton("bar"));
        FieldSpec actual = testInstance.reduceValueToFieldSpec(new DataBagValue(BigDecimal.valueOf(1)));

        Assert.assertEquals(expected, actual);
    }

    @Test
    void reduceValueToFieldSpec_whenInvalidIndex_throws() {
        assertThrows(IndexOutOfBoundsException.class, () -> testInstance.reduceValueToFieldSpec(new DataBagValue(BigDecimal.valueOf(3))));
    }

    @Test
    void reduceValueToFieldSpec_whenInvalidIndexType_throws() {
        assertThrows(ClassCastException.class, () -> testInstance.reduceValueToFieldSpec(new DataBagValue("bar")));
    }
}