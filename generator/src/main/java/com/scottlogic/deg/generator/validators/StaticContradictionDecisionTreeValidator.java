package com.scottlogic.deg.generator.validators;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.ProfileFields;
import com.scottlogic.deg.generator.decisiontree.*;
import com.scottlogic.deg.generator.fieldspecs.FieldSpec;
import com.scottlogic.deg.generator.fieldspecs.RowSpec;
import com.scottlogic.deg.generator.fieldspecs.RowSpecMerger;
import com.scottlogic.deg.generator.reducer.ConstraintReducer;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class StaticContradictionDecisionTreeValidator {

    private final ProfileFields profileFields;
    private final RowSpecMerger rowSpecMerger;
    private final ConstraintReducer constraintReducer;

    public StaticContradictionDecisionTreeValidator(ProfileFields profileFields, RowSpecMerger rowSpecMerger, ConstraintReducer constraintReducer){
        this.profileFields = profileFields;
        this.rowSpecMerger = rowSpecMerger;
        this.constraintReducer = constraintReducer;
    }

    public DecisionTree markContradictions(DecisionTree tree) {
        return new DecisionTree(markContradictions(tree.getRootNode()), tree.getProfile(), tree.getDescription());
    }

    public ConstraintNode markContradictions(ConstraintNode node){
        return markContradictions(node, getIdentityRowSpec());
    }

    public ConstraintNode markContradictions(ConstraintNode node, RowSpec accumulatedSpec){
        final Optional<RowSpec> nominalRowSpec = node.getOrCreateRowSpec(() -> constraintReducer.reduceConstraintsToRowSpec(
            profileFields,
            node.getAtomicConstraints()
        ));

        if (!nominalRowSpec.isPresent()) {
            return node.markNode(NodeMarking.STATICALLY_CONTRADICTORY);
        }

        final Optional<RowSpec> mergedRowSpecOpt = rowSpecMerger.merge(
            Arrays.asList(
                nominalRowSpec.get(),
                accumulatedSpec
            )
        );

        if (!mergedRowSpecOpt.isPresent()) {
            return node.markNode(NodeMarking.STATICALLY_CONTRADICTORY);
        }

        if (node.getDecisions().isEmpty()) {
            return node;
        } else {
            Collection<DecisionNode> decisions = node.getDecisions()
                .stream()
                .map(d -> markContradictions(d, mergedRowSpecOpt.get()))
                .collect(Collectors.toList());
            boolean nodeIsContradictory = decisions.stream().allMatch(this::isNodeContradictory);
            ConstraintNode transformed = node.setDecisions(decisions);
            return nodeIsContradictory ? transformed.markNode(NodeMarking.STATICALLY_CONTRADICTORY) : transformed;
        }
    }

    public DecisionNode markContradictions(DecisionNode node, RowSpec accumulatedSpec){
        if (node.getOptions().isEmpty()){
            return node;
        }
        Collection<ConstraintNode> options = node.getOptions().stream()
            .map(c -> markContradictions(c, accumulatedSpec))
            .collect(Collectors.toList());

        boolean decisionIsContradictory = options.stream().allMatch(this::isNodeContradictory);
        DecisionNode transformed = node.setOptions(options);
        if (decisionIsContradictory) {
            return transformed.markNode(NodeMarking.STATICALLY_CONTRADICTORY);
        }
        return transformed;
    }

    private RowSpec getIdentityRowSpec() {
        final Map<Field, FieldSpec> fieldToFieldSpec = profileFields.stream()
            .collect(Collectors.toMap(Function.identity(), field -> FieldSpec.Empty));

        return new RowSpec(profileFields, fieldToFieldSpec);
    }

    private boolean isNodeContradictory(Node node){
        return node.hasMarking(NodeMarking.STATICALLY_CONTRADICTORY);
    }


}
