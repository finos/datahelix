package com.scottlogic.deg.generator.walker.rowspec;

import com.scottlogic.deg.generator.decisiontree.DecisionTree;
import com.scottlogic.deg.generator.fieldspecs.RowSpec;

import java.util.stream.Stream;

public interface RowSpecTreeSolver {
    Stream<RowSpec> createRowSpecs(DecisionTree tree);
}
