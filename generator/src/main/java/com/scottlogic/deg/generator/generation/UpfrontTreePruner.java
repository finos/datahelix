package com.scottlogic.deg.generator.generation;

import com.google.inject.Inject;
import com.scottlogic.deg.common.profile.Field;
import com.scottlogic.deg.generator.decisiontree.ConstraintNode;
import com.scottlogic.deg.generator.decisiontree.DecisionTree;
import com.scottlogic.deg.generator.fieldspecs.FieldSpec;
import com.scottlogic.deg.generator.walker.reductive.Merged;
import com.scottlogic.deg.generator.walker.reductive.ReductiveTreePruner;

import java.util.HashMap;
import java.util.Map;

public class UpfrontTreePruner {
    private ReductiveTreePruner treePruner;
    @Inject
    public UpfrontTreePruner(ReductiveTreePruner treePruner) {
        this.treePruner = treePruner;
    }

    public DecisionTree runUpfrontPrune(DecisionTree tree) {
        Map<Field, FieldSpec> fieldSpecs = new HashMap<>();

        for (Field field : tree.getFields().getFields()) {
            fieldSpecs.put(field, FieldSpec.Empty);
        }
        Merged<ConstraintNode> prunedNode = treePruner.pruneConstraintNode(tree.getRootNode(), fieldSpecs);
        if (prunedNode.isContradictory()) {
            return new DecisionTree(null, tree.getFields(), tree.getDescription());
        }
        return new DecisionTree(prunedNode.get(), tree.getFields(), tree.getDescription());
    }
}
