package com.scottlogic.deg.generator.generation;

import com.scottlogic.deg.generator.Profile;
import com.scottlogic.deg.generator.decisiontree.IDecisionTreeProfile;
import com.scottlogic.deg.generator.outputs.TestCaseGenerationResult;

public interface IDataGenerator {
    TestCaseGenerationResult generateData(Profile profile, IDecisionTreeProfile analysedProfile);
}
