package com.scottlogic.deg.generator.decisiontree;

import com.scottlogic.deg.generator.Profile;

public interface DecisionTreeFactory {
    DecisionTreeCollection analyse(Profile profile);
}
