package com.scottlogic.deg.generator.decisiontree;

import com.scottlogic.deg.common.profile.Profile;

public interface DecisionTreeFactory {
    DecisionTree analyse(Profile profile);
}
