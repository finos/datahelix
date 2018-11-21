package com.scottlogic.deg.generator.validators;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.ProfileFields;
import com.scottlogic.deg.generator.decisiontree.*;
import com.scottlogic.deg.generator.reducer.ConstraintReducer;
import com.scottlogic.deg.generator.restrictions.*;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Check whether there are any breaking contradictions for leaf nodes that
 */
public class StaticDecisionTreeValidator {

    private final ProfileFields profileFields;
    private final RowSpecMerger rowSpecMerger;
    private final ConstraintReducer constraintReducer;

    public StaticDecisionTreeValidator(ProfileFields profileFields, RowSpecMerger rowSpecMerger, ConstraintReducer constraintReducer){
        this.profileFields = profileFields;
        this.rowSpecMerger = rowSpecMerger;
        this.constraintReducer = constraintReducer;
    }

    private RowSpec getIdentityRowSpec() {
        final Map<Field, FieldSpec> fieldToFieldSpec = profileFields.stream()
            .collect(Collectors.toMap(Function.identity(), field -> new FieldSpec()));

        return new RowSpec(profileFields, fieldToFieldSpec);
    }

    public DecisionTree validate(DecisionTree tree) {
        return new DecisionTree(validate(tree.rootNode), tree.fields, tree.description);
    }

    public ConstraintNode validate(ConstraintNode node){
        return validate(node, getIdentityRowSpec());
    }

    public ConstraintNode validate(ConstraintNode node, RowSpec accumulatedSpec){
        final Optional<RowSpec> nominalRowSpec = node.getOrCreateRowSpec(() -> constraintReducer.reduceConstraintsToRowSpec(
            profileFields,
            node.getAtomicConstraints()
        ));

        if (!nominalRowSpec.isPresent()) {
            return new ContradictoryConstraintNode(node);
        }

        final Optional<RowSpec> mergedRowSpecOpt = rowSpecMerger.merge(
            Arrays.asList(
                nominalRowSpec.get(),
                accumulatedSpec
            )
        );

        if (!mergedRowSpecOpt.isPresent()) {
            return new ContradictoryConstraintNode(node);
        }

        if (node.getDecisions().isEmpty()) {
            return node;
        } else {
            Collection<DecisionNode> decisions = node.getDecisions()
                .stream()
                .map(d -> validate(d, mergedRowSpecOpt.get()))
                .collect(Collectors.toList());
            boolean nodeIsContradictory = decisions.stream().allMatch(this::isNodeContradictory);
            ConstraintNode transformed = node.setDecisions(decisions);
            return nodeIsContradictory ? new ContradictoryConstraintNode(transformed) : transformed;
        }

    }

    public DecisionNode validate(DecisionNode node, RowSpec accumulatedSpec){
        if (node.getOptions().isEmpty()){
            return node;
        }
        Collection<ConstraintNode> options = node.getOptions().stream()
            .map(c -> validate(c, accumulatedSpec))
            .collect(Collectors.toList());

        boolean decisionIsContradictory = options.stream().allMatch(this::isNodeContradictory);
        DecisionNode transformed = node.setOptions(options);
        if (decisionIsContradictory) {
            return new ContradictoryDecisionNode(transformed);
        }
        return transformed;
    }

    private boolean isNodeContradictory(Node node){
        return node instanceof ContradictoryNode;
    }


}
