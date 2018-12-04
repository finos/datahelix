package com.scottlogic.deg.generator.decisiontree.test_utils;

import java.util.Collections;
import java.util.List;

import com.scottlogic.deg.generator.decisiontree.DecisionTree;
import com.scottlogic.deg.generator.decisiontree.DecisionTreeOptimiser;
import com.scottlogic.deg.generator.decisiontree.IDecisionTreeOptimiser;

public class SerialisationOnlyTestStrategy implements TreeTransformationTestStrategy {

    @Override
    public String getTestsDirName() {
        return "serialisation-only-tests";
    }

    @Override
    public List<DecisionTree> transformTree(DecisionTree beforeTree) {
        // No op -- just test serialisation only
        return Collections.singletonList(beforeTree);
    }
}
