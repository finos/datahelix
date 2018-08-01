package com.scottlogic.deg.generator.generation;

import com.scottlogic.deg.generator.generation.tmpReducerOutput.FieldSpec;
import com.scottlogic.deg.generator.generation.tmpReducerOutput.NullRestrictions;
import com.scottlogic.deg.generator.generation.tmpReducerOutput.RowSpec;
import com.scottlogic.deg.generator.outputs.TestCaseDataRow;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collection;

public class GeneratorTests {
    @Test
    public void shouldReturnSingleRowWithSingleNullObject_IfInputHasSingleFieldWhichMustBeNull() {
        FieldSpec fieldSpec0 = new FieldSpec("test");
        NullRestrictions nullRestrictions0 = new NullRestrictions();
        nullRestrictions0.nullness = NullRestrictions.Nullness.MustBeNull;
        fieldSpec0.setNullRestrictions(nullRestrictions0);
        RowSpec rowSpec = new RowSpec(Arrays.asList(fieldSpec0));
        Generator testObject = new Generator();

        Collection<TestCaseDataRow> testOutput = testObject.generateData(rowSpec);

        Assert.assertNotNull(testOutput);
        Assert.assertEquals(1, testOutput.size());
        TestCaseDataRow testRow = testOutput.iterator().next();
        Assert.assertNotNull(testRow);
        Assert.assertEquals(1, testRow.values.size());
        Object testField = testRow.values.iterator().next();
        Assert.assertNull(testField);
    }
}
