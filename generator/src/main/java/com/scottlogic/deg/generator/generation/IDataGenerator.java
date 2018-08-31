package com.scottlogic.deg.generator.generation;

import com.scottlogic.deg.generator.ProfileFields;
import com.scottlogic.deg.generator.decisiontree.ProfileDecisionTreeCollection;
import com.scottlogic.deg.generator.outputs.TestCaseDataRow;

public interface IDataGenerator {
    Iterable<TestCaseDataRow> generateData(
        ProfileFields profileFields,
        ProfileDecisionTreeCollection analysedProfile);
}
