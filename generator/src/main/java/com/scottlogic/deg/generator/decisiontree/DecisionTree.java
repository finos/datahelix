package com.scottlogic.deg.generator.decisiontree;

import com.scottlogic.deg.generator.ProfileFields;
import com.scottlogic.deg.generator.constraints.IConstraint;

import java.util.Collection;
import java.util.stream.Collectors;

public class DecisionTree {
    public final ConstraintNode rootNode;
    public final ProfileFields fields;

    public DecisionTree(ConstraintNode rootNode, ProfileFields fields) {
        this.rootNode = rootNode;
        this.fields = fields;
    }

    public ConstraintNode getRootNode() {
        return rootNode;
    }

    public ProfileFields getFields() {
        return fields;
    }
}

