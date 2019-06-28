package com.scottlogic.deg.generator.decisiontree;

import com.scottlogic.deg.common.profile.ProfileFields;

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

    public String toString(){
        return rootNode.toString();
    }
}

