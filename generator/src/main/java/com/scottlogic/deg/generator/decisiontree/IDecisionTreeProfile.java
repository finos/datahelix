package com.scottlogic.deg.generator.decisiontree;

import com.scottlogic.deg.generator.Field;

import java.util.Collection;

public interface IDecisionTreeProfile {

    Collection<Field> getFields();

    Collection<IRuleDecisionTree> getDecisionTrees();
}
