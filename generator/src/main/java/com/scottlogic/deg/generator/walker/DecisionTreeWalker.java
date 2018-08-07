package com.scottlogic.deg.generator.walker;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.ProfileFields;
import com.scottlogic.deg.generator.decisiontree.*;
import com.scottlogic.deg.generator.reducer.ConstraintReducer;
import com.scottlogic.deg.generator.restrictions.FieldSpec;
import com.scottlogic.deg.generator.restrictions.FieldSpecMerger;
import com.scottlogic.deg.generator.restrictions.RowSpec;

import java.util.*;
import java.util.function.Function;
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
        return helper.walk(
                new RuleDecision(
                        decisionTreeProfile
                                .getDecisionTrees()
                                .stream()
                                .map(IRuleDecisionTree::getRootOption)
                                .collect(Collectors.toList())
                )
        )
                .collect(Collectors.toList());
    }

    private class DecisionTreeWalkerHelper {
        private final ProfileFields profileFields;

        private DecisionTreeWalkerHelper(ProfileFields profileFields) {
            this.profileFields = profileFields;
        }

//        private Stream<RowSpec> walk(Collection<IRuleDecisionTree> ruleDecisionTrees) {
//            return ;
//        }

//        private Stream<RowSpec> walk(IRuleDecisionTree ruleDecisionTree) {
//            return walk(ruleDecisionTree.getRootOption());
//        }

        private Stream<RowSpec> walk(IRuleOption ruleOption) {
            final RowSpec nominalRowSpec = constraintReducer.reduceConstraintsToRowSpec(
                    profileFields,
                    ruleOption.getAtomicConstraints()
            );

            if (ruleOption.getDecisions().isEmpty()) {
                return Stream.of(nominalRowSpec);
            }

//            final Set<IRuleDecision> decisionsNotYetTaken = new HashSet<>(ruleOption.getDecisions());
//
//            for (IRuleDecision decision : decisionsNotYetTaken) {
//
//            }

//            final Map<Field, FieldSpec> fieldToFieldSpec = profileFields.stream()
//                    .collect(Collectors.toMap(Function.identity(), field -> new FieldSpec()));

//            Stream<RowSpec> cartesianProductsSoFar = Stream.of(new RowSpec(profileFields, fieldToFieldSpec));
            Stream<RowSpec> cartesianProductsSoFar = Stream.of(nominalRowSpec);
//            Stream<RowSpec> cartesianProductsSoFar = Stream.empty();
            // !A, A
            for (IRuleDecision decision : ruleOption.getDecisions()) {

//                if (cartesianProductsSoFar.is)

                // !B, B

                cartesianProductsSoFar = cartesianProductsSoFar
                        .flatMap(
                                rowSpecFromExistingProducts -> walk(decision, rowSpecFromExistingProducts)
                                        .map(
                                                rowSpecFromThisDecision -> RowSpec.merge(
                                                        fieldSpecMerger,
                                                        rowSpecFromExistingProducts,
                                                        rowSpecFromThisDecision
                                        )
                                )
                        );

            }

            return cartesianProductsSoFar;


//            final List<RowSpec> rowSpecs = new ArrayList<>();
//
//            for (IRuleDecision decision : ruleOption.getDecisions()) {
//                Stream<RowSpec> rowSpecsFromThisDecision = walk(decision, nominalRowSpec);
//            }


//                    .reduce(Arrays.<RowSpec>asList(nominalRowSpec), (acc, decision, ) -> rowspecs.)


//            return ruleOption
//                    .getDecisions()
//                    .stream()
//                    .flatMap(x -> walk(x, nominalRowSpec));
        }

        private RowSpec getIdentityRowSpec() {
            final Map<Field, FieldSpec> fieldToFieldSpec = profileFields.stream()
                    .collect(Collectors.toMap(Function.identity(), field -> new FieldSpec()));

            return new RowSpec(profileFields, fieldToFieldSpec);
        }

        public Stream<RowSpec> walk(IRuleDecision decision) {
            return walk(decision, getIdentityRowSpec());
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
