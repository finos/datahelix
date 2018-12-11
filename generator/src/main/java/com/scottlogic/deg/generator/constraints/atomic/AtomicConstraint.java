package com.scottlogic.deg.generator.constraints.atomic;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.constraints.Constraint;
import com.scottlogic.deg.generator.decisiontree.NodeVisitor;

import java.util.Collection;
import java.util.Collections;

public interface AtomicConstraint extends Constraint {

    Field getField();

    String toDotLabel();

    default AtomicConstraint negate() {
        return new NotConstraint(this);
    }

    default Collection<Field> getFields() {
        return Collections.singleton(getField());
    }

    default void accept(NodeVisitor visitor){
        visitor.visit(this);
    }
}
