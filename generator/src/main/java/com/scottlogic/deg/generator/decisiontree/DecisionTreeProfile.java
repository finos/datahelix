package com.scottlogic.deg.generator.decisiontree;

import com.scottlogic.deg.generator.ProfileFields;
import com.sun.xml.internal.bind.v2.runtime.reflect.opt.Const;

import java.util.ArrayList;
import java.util.Collection;

public class DecisionTreeProfile {
    private final ProfileFields fields;
    private final ConstraintNode rootNode;

    DecisionTreeProfile(ProfileFields fields, ConstraintNode rootNode) {
        this.fields = fields;
        this.rootNode = rootNode;
    }

    public ProfileFields getFields() {
        return fields;
    }

    public ConstraintNode getRootNode() {
        return rootNode;
    }
}
