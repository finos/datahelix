package com.scottlogic.deg.generator.decisiontree;

import com.scottlogic.deg.generator.Profile;

public class DecisionTree {
    private final ConstraintNode rootNode;
    private final Profile profile;
    private final String description;

    public DecisionTree(ConstraintNode rootNode, Profile profile, String description) {
        this.rootNode = rootNode;
        this.profile = profile;
        this.description = description;
    }

    public ConstraintNode getRootNode() {
        return rootNode;
    }

    public String getDescription(){
        return description;
    }

    public Profile getProfile() {
        return profile;
    }

    public String toString(){
        return description;
    }
}

