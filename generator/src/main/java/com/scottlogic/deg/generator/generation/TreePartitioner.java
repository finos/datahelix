package com.scottlogic.deg.generator.generation;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.decisiontree.ConstraintNode;
import com.scottlogic.deg.generator.decisiontree.DecisionTreeProfile;
import com.scottlogic.deg.generator.decisiontree.RuleDecisionTree;

import java.util.List;
import java.util.Map;

/**
 * Given a rule or list of rules it finds which rules act on which fields and returns a map of rules to fields
 */
public class TreePartitioner {
    public List<DecisionTreeProfile> splitTreeIntoPartitions(DecisionTreeProfile profile, Map<RuleDecisionTree, Field> ruleFieldMapping) {
        return null;
    }
}
