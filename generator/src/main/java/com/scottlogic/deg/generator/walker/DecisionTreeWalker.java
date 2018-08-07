package com.scottlogic.deg.generator.walker;

import com.scottlogic.deg.generator.ProfileFields;
import com.scottlogic.deg.generator.decisiontree.IDecisionTreeProfile;
import com.scottlogic.deg.generator.decisiontree.IRuleDecision;
import com.scottlogic.deg.generator.decisiontree.IRuleDecisionTree;
import com.scottlogic.deg.generator.decisiontree.IRuleOption;
import com.scottlogic.deg.generator.reducer.ConstraintReducer;
import com.scottlogic.deg.generator.restrictions.FieldSpecMerger;
import com.scottlogic.deg.generator.restrictions.RowSpec;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DecisionTreeWalker {
    private final ConstraintReducer constraintReducer;
    private final FieldSpecMerger fieldSpecMerger;

    public DecisionTreeWalker(
            ConstraintReducer constraintReducer,
            FieldSpecMerger fieldSpecMerger
    ) {
        this.constraintReducer = constraintReducer;
        this.fieldSpecMerger = fieldSpecMerger;
    }

    public Iterable<RowSpec> walk(IDecisionTreeProfile decisionTreeProfile) {
        final DecisionTreeWalkerHelper helper = new DecisionTreeWalkerHelper(decisionTreeProfile.getFields());
        return helper.walk(decisionTreeProfile.getDecisionTrees())
                .collect(Collectors.toList());
    }

    private class DecisionTreeWalkerHelper {
        private final ProfileFields profileFields;

        private DecisionTreeWalkerHelper(ProfileFields profileFields) {
            this.profileFields = profileFields;
        }

        private Stream<RowSpec> walk(Collection<IRuleDecisionTree> ruleDecisionTrees) {
            return ruleDecisionTrees.stream()
                    .flatMap(this::walk);
        }

        private Stream<RowSpec> walk(IRuleDecisionTree ruleDecisionTree) {
            return walk(ruleDecisionTree.getRootOption());
        }

        private Stream<RowSpec> walk(IRuleOption ruleOption) {
            final RowSpec nominalRowSpec = constraintReducer.reduceConstraintsToRowSpec(
                    profileFields,
                    ruleOption.getAtomicConstraints()
            );

            return ruleOption
                    .getDecisions()
                    .stream()
                    .flatMap(x -> walk(x, nominalRowSpec));
        }

        private Stream<RowSpec> walk(IRuleDecision decision, RowSpec accumulatedSpec) {
            return decision
                    .getOptions()
                    .stream()
                    .flatMap(this::walk)
                    .map(x -> RowSpec.merge(fieldSpecMerger, accumulatedSpec, x));
        }
    }
}
