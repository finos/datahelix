package com.scottlogic.deg.generator.decisiontree.testutils;

import java.util.Collections;
import java.util.List;

import com.scottlogic.deg.generator.decisiontree.DecisionTree;

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
