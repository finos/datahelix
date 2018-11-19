package com.scottlogic.deg.generator.decisiontree.test_utils;

import com.scottlogic.deg.generator.constraints.IConstraint;
import com.scottlogic.deg.generator.decisiontree.DecisionNode;

import java.util.Collection;
import java.util.stream.Collectors;

public class DecisionComparer implements EqualityComparer {
    private final static AnyOrderCollectionEqualityComparer constraintAnyOrderComparer = new AnyOrderCollectionEqualityComparer();

    @Override
    public int getHashCode(Object decision){
        return getHashCode((DecisionNode)decision);
    }

    public int getHashCode(DecisionNode decision){
        return decision
            .getOptions()
            .stream()
            .reduce(
                0,
                (prev, option) -> prev * option.hashCode(),
                (prevHash, optionHash) -> prevHash * optionHash);
    }

    @Override
    public boolean equals(Object item1, Object item2) {
        return equals((DecisionNode)item1, (DecisionNode)item2);
    }

    public boolean equals(DecisionNode decision1, DecisionNode decision2){
        if (decision1 == null && decision2 == null)
            return true;

        if (decision1 == null || decision2 == null)
            return false; //either decision1 XOR decision2 is null

        Collection<IConstraint> decision1AtomicConstraints = decision1
            .getOptions()
            .stream()
            .flatMap(o -> o.getAtomicConstraints().stream())
            .collect(Collectors.toList());

        Collection<IConstraint> decision2AtomicConstraints = decision2
            .getOptions()
            .stream()
            .flatMap(o -> o.getAtomicConstraints().stream())
            .collect(Collectors.toList());

        return constraintAnyOrderComparer.equals(decision1AtomicConstraints, decision2AtomicConstraints);
    }
}
