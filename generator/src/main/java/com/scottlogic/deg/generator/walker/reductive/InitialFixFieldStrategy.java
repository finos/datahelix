package com.scottlogic.deg.generator.walker.reductive;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.constraints.IConstraint;
import com.scottlogic.deg.generator.decisiontree.reductive.ReductiveConstraintNode;
import com.scottlogic.deg.generator.reducer.ConstraintFieldSniffer;

import java.util.stream.Stream;

public class InitialFixFieldStrategy implements FixFieldStrategy {

    private final ConstraintFieldSniffer fieldSniffer;

    public InitialFixFieldStrategy(ConstraintFieldSniffer fieldSniffer) {
        this.fieldSniffer = fieldSniffer;
    }

    private ConstraintCollection fromConstraints(Stream<IConstraint> constraints){
        ConstraintCollection coll = new ConstraintCollection();

        constraints
            .forEach(atomicConstraint -> {
                Field field = fieldSniffer.detectField(atomicConstraint);
                coll.add(field, atomicConstraint);
            });

        return coll;
    }

    @Override
    public FieldAndConstraintMapping getFieldAndConstraintMapToFixNext(ReductiveConstraintNode rootNode) {
        ConstraintCollection collection = fromConstraints(rootNode
            .getAllIncludedConstraints()
            .stream());

        return collection.getFieldAndConstraintMapToFixNext();
    }
}
