package com.scottlogic.deg.generator.fieldspecs.relations;

import com.scottlogic.deg.common.profile.Field;
import com.scottlogic.deg.common.profile.FieldType;
import com.scottlogic.deg.generator.fieldspecs.FieldSpec;
import com.scottlogic.deg.generator.fieldspecs.FieldSpecFactory;
import com.scottlogic.deg.generator.fieldspecs.whitelist.DistributedList;
import com.scottlogic.deg.generator.restrictions.StringRestrictionsFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static com.shazam.shazamcrest.MatcherAssert.assertThat;
import static com.shazam.shazamcrest.matcher.Matchers.sameBeanAs;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

import static com.scottlogic.deg.common.profile.FieldBuilder.createField;

class InMapIndexRelationTest {
    private static InMapIndexRelation testInstance;

    @BeforeAll
    static void before() {
        Field f1 = createField("field1");
        Field f2 = createField("field1");
        List<String> values = Arrays.asList("foo", "bar");

        testInstance = new InMapIndexRelation(f1, f2, DistributedList.uniform(values));
    }

    @Test
    void reduceToRelatedFieldSpec_whenAllValid_returnCompleteWhiteList() {
        FieldSpec parameter = FieldSpecFactory.fromType(FieldType.STRING);

        FieldSpec expected = FieldSpecFactory.fromList(DistributedList.uniform(Arrays.asList(0, 1))).withNotNull();
        FieldSpec actual = testInstance.reduceToRelatedFieldSpec(parameter);

        assertThat(actual, sameBeanAs(expected));
    }

    @Test
    void reduceToRelatedFieldSpec_whenSomeValid_returnReducedWhiteList() {
        FieldSpec parameter = FieldSpecFactory.fromRestriction(StringRestrictionsFactory.forStringContaining(Pattern.compile("^f.*"), false));

        FieldSpec expected = FieldSpecFactory.fromList(DistributedList.uniform(Collections.singletonList(0))).withNotNull();
        FieldSpec actual = testInstance.reduceToRelatedFieldSpec(parameter);

        assertThat(actual, sameBeanAs(expected));
    }
}