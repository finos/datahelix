package com.scottlogic.deg.generator.walker.reductive.fieldselectionstrategy;

import com.scottlogic.deg.generator.constraints.atomic.AtomicConstraint;
import com.scottlogic.deg.generator.decisiontree.DecisionTree;
import com.scottlogic.deg.generator.decisiontree.visualisation.BaseVisitor;

import java.util.ArrayList;
import java.util.List;

public class AtomicConstraintCollector extends BaseVisitor {
    private final ArrayList<AtomicConstraint> atomicConstraints = new ArrayList<>();

    @Override
    public AtomicConstraint visit(AtomicConstraint atomicConstraint) {
        atomicConstraints.add(atomicConstraint);
        return super.visit(atomicConstraint);
    }

    public static List<AtomicConstraint> getAllAtomicConstraints(DecisionTree tree){
        AtomicConstraintCollector collector = new AtomicConstraintCollector();
        tree.getRootNode().accept(collector);

        return collector.atomicConstraints;
    }
}