package com.scottlogic.deg.generator;

import com.scottlogic.deg.generator.decisiontree.IDecisionTreeProfile;
import com.scottlogic.deg.generator.outputs.TestCaseDataRow;
import com.scottlogic.deg.generator.outputs.TestCaseDataSet;
import com.scottlogic.deg.generator.outputs.TestCaseGenerationResult;

import java.util.Arrays;
import java.util.Collections;

public class DummyDataGenerator implements IDataGenerator {
    @Override
    public TestCaseGenerationResult generateData(Profile profile, IDecisionTreeProfile analysedProfile) {
        int numFields = profile.fields.size();

        return new TestCaseGenerationResult(
            profile,
            Arrays.asList(
                new TestCaseDataSet(
                    null,
                    new TestCaseDataRow(Collections.nCopies(numFields, "aaa")),
                    new TestCaseDataRow(Collections.nCopies(numFields, "bbb")),
                    new TestCaseDataRow(Collections.nCopies(numFields, "ccc"))),
                new TestCaseDataSet(
                    "ID cannot be null",
                    new TestCaseDataRow(Collections.nCopies(numFields, "xxx")))));
    }
}
