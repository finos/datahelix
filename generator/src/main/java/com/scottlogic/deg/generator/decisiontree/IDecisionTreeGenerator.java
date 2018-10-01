package com.scottlogic.deg.generator.decisiontree;

import com.scottlogic.deg.generator.Profile;

public interface IDecisionTreeGenerator {
    DecisionTreeCollection analyse(Profile profile);
}
