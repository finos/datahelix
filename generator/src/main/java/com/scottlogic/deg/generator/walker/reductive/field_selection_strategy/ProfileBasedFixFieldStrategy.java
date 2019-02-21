package com.scottlogic.deg.generator.walker.reductive.field_selection_strategy;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.Guice.CurrentProfileCache;
import com.scottlogic.deg.generator.ProfileFields;
import com.scottlogic.deg.generator.decisiontree.reductive.ReductiveConstraintNode;
import com.scottlogic.deg.generator.walker.reductive.ReductiveState;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public abstract class ProfileBasedFixFieldStrategy implements FixFieldStrategy {
    private List<Field> fieldsInFixingOrder;

    @Override
    public Field getNextFieldToFix(ReductiveState reductiveState, ReductiveConstraintNode rootNode) {
        return getFieldFixingPriorityList(reductiveState.getFields()).stream()
            .filter(field -> !reductiveState.isFieldFixed(field) && reductiveState.getFields().stream().anyMatch(pf -> pf.equals(field)))
            .findFirst()
            .orElse(null);
    }

    List<Field> getFieldFixingPriorityList(ProfileFields fields) {
        if (fieldsInFixingOrder == null) {
            fieldsInFixingOrder = Collections.unmodifiableList(fields.stream()
                .sorted(getFieldOrderingStrategy())
                .collect(Collectors.toList()));
        }
        return fieldsInFixingOrder;
    }

    abstract Comparator<Field> getFieldOrderingStrategy();
}
