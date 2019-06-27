package com.scottlogic.deg.generator.walker;

import com.scottlogic.deg.generator.decisiontree.DecisionTree;
import com.scottlogic.deg.generator.generation.databags.DataBag;

import java.util.stream.Stream;

public interface DecisionTreeWalker {
    Stream<DataBag> walk(DecisionTree tree);
}
