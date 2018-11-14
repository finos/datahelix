package com.scottlogic.deg.generator.decisiontree;

import com.scottlogic.deg.generator.ProfileFields;

public class DecisionTree {
    public final ConstraintNode rootNode;
    public final ProfileFields fields;
    public final String description;

    public DecisionTree(ConstraintNode rootNode, ProfileFields fields, String description) {
        this.rootNode = rootNode;
        this.fields = fields;
        this.description = description;
    }

    public ConstraintNode getRootNode() {
        return rootNode;
    }

    public String getDescription(){
        return description;
    }

    public ProfileFields getFields() {
        return fields;
    }

    public String toString(){
        return description;
    }
}

