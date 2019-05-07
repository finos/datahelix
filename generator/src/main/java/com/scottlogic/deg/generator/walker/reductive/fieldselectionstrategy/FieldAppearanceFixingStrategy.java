package com.scottlogic.deg.generator.walker.reductive.fieldselectionstrategy;

import com.scottlogic.deg.common.profile.Field;
import com.scottlogic.deg.common.profile.ProfileFields;
import com.scottlogic.deg.generator.decisiontree.ConstraintNode;
import com.scottlogic.deg.generator.utils.FileUtils;
import com.scottlogic.deg.generator.walker.reductive.IterationVisualiser;
import com.scottlogic.deg.generator.walker.reductive.ReductiveIterationVisualiser;
import com.scottlogic.deg.generator.walker.reductive.ReductiveState;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class FieldAppearanceFixingStrategy implements FixFieldStrategy {
    final List<Field> fieldsInFixingOrder;

    public FieldAppearanceFixingStrategy(ConstraintNode rootNode) {
        FieldAppearanceAnalyser fieldAppearanceAnalyser = new FieldAppearanceAnalyser();
        rootNode.accept(fieldAppearanceAnalyser);

        fieldsInFixingOrder = fieldAppearanceAnalyser.fieldAppearances.entrySet().stream()
            .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
            .map(Map.Entry::getKey)
            .collect(Collectors.toList());
    }

    @Override
    public Field getNextFieldToFix(ReductiveState reductiveState) {
        return fieldsInFixingOrder.stream()
            .filter(field -> !reductiveState.isFieldFixed(field) && reductiveState.getFields().stream().anyMatch(pf -> pf.equals(field)))
            .findFirst()
            .orElse(null);
    }
}
