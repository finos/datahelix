package com.scottlogic.deg.generator;

import com.scottlogic.deg.generator.decisiontree.IDecisionTreeProfile;
import com.scottlogic.deg.generator.outputs.TestCaseGenerationResult;

public interface IDataGenerator {
    TestCaseGenerationResult generateData(Profile profile, IDecisionTreeProfile analysedProfile);
}
