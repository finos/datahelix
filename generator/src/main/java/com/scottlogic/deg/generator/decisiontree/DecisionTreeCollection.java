package com.scottlogic.deg.generator.decisiontree;

import com.scottlogic.deg.generator.Profile;
import com.scottlogic.deg.generator.ProfileFields;

import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;


public class DecisionTreeCollection {
    private final Profile profile;
    private final Collection<DecisionTree> decisionTrees;

    DecisionTreeCollection(Profile profile, Collection<DecisionTree> decisionTrees) {
        this.profile = profile;
        this.decisionTrees = decisionTrees;
    }

    public Profile getProfile() {
        return profile;
    }

    public Collection<DecisionTree> getDecisionTrees() {
        return decisionTrees;
    }

    public DecisionTree getMergedTree() {
        return new DecisionTree(
            ConstraintNode.merge(decisionTrees
                .stream()
                .map(DecisionTree::getRootNode)
                .iterator()),
            profile,
            decisionTrees.isEmpty() ? null : decisionTrees.iterator().next().getDescription());
    }
}
