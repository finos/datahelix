package com.scottlogic.deg.generator.decisiontree;

import java.util.Collection;
public interface DecisionNode extends Node {
    Collection<ConstraintNode> getOptions();
    DecisionNode setOptions(Collection<ConstraintNode> options);
    DecisionNode markNode(NodeMarking marking);

    default void accept(NodeVisitor visitor){
        visitor.visit(this);
        getOptions().forEach(c->c.accept(visitor));
    }
}

