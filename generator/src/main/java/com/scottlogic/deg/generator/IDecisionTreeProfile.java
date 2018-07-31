package com.scottlogic.deg.generator;

import java.util.Collection;

public interface IDecisionTreeProfile {

    Collection<Field> getFields();

    Collection<IRuleDecisionTree> getDecisionTrees();
}
