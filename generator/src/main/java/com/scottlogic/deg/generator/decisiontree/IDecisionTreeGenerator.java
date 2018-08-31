package com.scottlogic.deg.generator.decisiontree;

import com.scottlogic.deg.generator.Rule;

public interface IDecisionTreeGenerator {
    DecisionTree generateTreeFor(Rule rule);
}
