package com.scottlogic.deg.generator.decisiontree;

import com.scottlogic.deg.generator.ProfileFields;

public class DecisionTree {
    private final ConstraintNode rootNode;
    private final ProfileFields fields;

    DecisionTree(ConstraintNode rootNode, ProfileFields fields) {
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
