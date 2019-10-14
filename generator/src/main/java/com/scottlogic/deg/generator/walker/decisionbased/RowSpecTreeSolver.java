package com.scottlogic.deg.generator.walker.decisionbased;

import com.google.inject.Inject;
import com.scottlogic.deg.common.profile.Field;
import com.scottlogic.deg.common.profile.ProfileFields;
import com.scottlogic.deg.generator.fieldspecs.FieldSpecFactory;
import com.scottlogic.deg.generator.profile.constraints.atomic.AtomicConstraint;
import com.scottlogic.deg.generator.decisiontree.ConstraintNode;
import com.scottlogic.deg.generator.decisiontree.DecisionNode;
import com.scottlogic.deg.generator.decisiontree.DecisionTree;
import com.scottlogic.deg.generator.fieldspecs.FieldSpec;
import com.scottlogic.deg.generator.fieldspecs.RowSpec;
import com.scottlogic.deg.generator.reducer.ConstraintReducer;
import com.scottlogic.deg.generator.walker.pruner.Merged;
import com.scottlogic.deg.generator.walker.pruner.TreePruner;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.scottlogic.deg.common.util.FlatMappingSpliterator.flatMap;

public class RowSpecTreeSolver {

    private final ConstraintReducer constraintReducer;
    private final TreePruner treePruner;
    private final OptionPicker optionPicker;

    @Inject
    public RowSpecTreeSolver(ConstraintReducer constraintReducer,
                             TreePruner treePruner,
                             OptionPicker optionPicker) {
        this.constraintReducer = constraintReducer;
        this.treePruner = treePruner;
        this.optionPicker = optionPicker;
    }

    public Stream<RowSpec> createRowSpecs(DecisionTree tree) {
        return flatMap(reduceToRowNodes(tree.rootNode),
            rootNode -> toRowspec(tree.fields, rootNode));
    }

    private Stream<RowSpec> toRowspec(ProfileFields fields, ConstraintNode rootNode) {
        Optional<RowSpec> result = constraintReducer.reduceConstraintsToRowSpec(fields, rootNode);
        return result.map(Stream::of).orElseGet(Stream::empty);
    }

    /**
     * a row node is a constraint node with no further decisions
     */
    private Stream<ConstraintNode> reduceToRowNodes(ConstraintNode rootNode) {
        if (rootNode.getDecisions().isEmpty()) {
            return Stream.of(rootNode);
        }

        DecisionNode decisionNode = optionPicker.pickDecision(rootNode);
        ConstraintNode rootWithoutDecision = rootNode.builder().removeDecision(decisionNode).build();

        Stream<ConstraintNode> rootOnlyConstraintNodes = optionPicker.streamOptions(decisionNode)
            .map(option -> combineWithRootNode(rootWithoutDecision, option))
            .filter(newNode -> !newNode.isContradictory())
            .map(Merged::get);

        return flatMap(
            rootOnlyConstraintNodes,
            this::reduceToRowNodes);
    }

    private Merged<ConstraintNode> combineWithRootNode(ConstraintNode rootNode, ConstraintNode option) {
        ConstraintNode constraintNode = rootNode.builder()
            .addDecisions(option.getDecisions())
            .addAtomicConstraints(option.getAtomicConstraints())
            .addRelations(option.getRelations())
            .build();

        return treePruner.pruneConstraintNode(constraintNode, getFields(option));
    }

    private Map<Field, FieldSpec> getFields(ConstraintNode option) {
        return option.getAtomicConstraints().stream()
            .map(AtomicConstraint::getField)
            .distinct()
            .collect(Collectors.toMap(
                Function.identity(),
                field -> FieldSpecFactory.fromType(field.getType())));
    }

}
