package com.scottlogic.deg.generator.decisiontree;

import com.scottlogic.deg.generator.ProfileFields;

import java.util.Collection;

public interface IDecisionTreeProfile {

    ProfileFields getFields();

    Collection<IRuleDecisionTree> getDecisionTrees();
}
