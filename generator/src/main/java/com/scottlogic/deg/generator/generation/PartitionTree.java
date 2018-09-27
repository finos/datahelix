package com.scottlogic.deg.generator.generation;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.decisiontree.ConstraintNode;

import java.util.List;
import java.util.Set;

public class PartitionTree {
    public ConstraintNode rootNode;
    public Set<Field> fields;

    public PartitionTree(ConstraintNode rootNode, Set<Field> fields) {
        this.rootNode = rootNode;
        this.fields = fields;
    }
}
