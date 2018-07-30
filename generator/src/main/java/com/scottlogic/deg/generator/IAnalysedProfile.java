package com.scottlogic.deg.generator;

import com.scottlogic.deg.generator.constraints.IConstraintTreeNode;

import java.util.Collection;

public interface IAnalysedProfile {

    Collection<Field> getFields();

    Collection<AnalysedRule> getAnalysedRules();
}
