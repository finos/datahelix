package com.scottlogic.deg.generator.walker;

import com.google.inject.Inject;
import com.scottlogic.deg.generator.ProfileFields;
import com.scottlogic.deg.generator.decisiontree.ConstraintNode;
import com.scottlogic.deg.generator.decisiontree.DecisionTree;
import com.scottlogic.deg.generator.fieldspecs.RowSpec;
import com.scottlogic.deg.generator.fieldspecs.RowSpecMerger;
import com.scottlogic.deg.generator.reducer.ConstraintReducer;
import com.scottlogic.deg.generator.walker.reductive.fieldselectionstrategy.FixFieldStrategy;
import com.scottlogic.deg.generator.walker.routes.RowSpecRoute;
import com.scottlogic.deg.generator.walker.routes.RowSpecRouteProducer;

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Stream;

public class DecisionTreeRoutesTreeWalker implements DecisionTreeWalker {
    private final ConstraintReducer constraintReducer;
    private final RowSpecMerger rowSpecMerger;
    private final RowSpecRouteProducer producer;

    @Inject
    public DecisionTreeRoutesTreeWalker(
        ConstraintReducer constraintReducer,
        RowSpecMerger rowSpecMerger,
        RowSpecRouteProducer producer) {
        this.constraintReducer = constraintReducer;
        this.rowSpecMerger = rowSpecMerger;
        this.producer = producer;
    }

    @Override
    public Stream<RowSpec> walk(DecisionTree tree){
        ConstraintNode rootNode = tree.getRootNode();
        ProfileFields fields = tree.getFields();
        Stream<RowSpecRoute> routes = this.producer.produceRoutes(tree);

        return routes
            .map(route -> {
                RowSpec accumulatedSpec = getRootRowSpec(fields, rootNode);
                return getRowSpec(fields, accumulatedSpec, route.subRoutes);
            })
            .filter(rowSpec -> rowSpec != null);
    }

    private RowSpec getRowSpec(ProfileFields fields, RowSpec accumulatedSpec, Collection<RowSpecRoute> routes) {
        for (RowSpecRoute route : routes) {
            ConstraintNode decisionOption = route.chosenOption;

            accumulatedSpec = getMergedRowSpec(fields, accumulatedSpec, decisionOption, route);

            if (accumulatedSpec == null) {
                return null; /* in case of contradicting decisions? */
            }
        }

        return accumulatedSpec;
    }

    private RowSpec getMergedRowSpec(ProfileFields fields, RowSpec accumulatedSpec, ConstraintNode decisionOption, RowSpecRoute route) {
        Optional<RowSpec> nominalRowSpec = constraintReducer.reduceConstraintsToRowSpec(
            fields,
            decisionOption.getAtomicConstraints());

        if (!nominalRowSpec.isPresent()) {
            return null;
        }

        final Optional<RowSpec> mergedRowSpecOpt = rowSpecMerger.merge(
            Arrays.asList(
                nominalRowSpec.get(),
                accumulatedSpec
            )
        );

        if (!mergedRowSpecOpt.isPresent()) {
            return null;
        }

        RowSpec rowSpec = mergedRowSpecOpt.get();

        boolean hasSubRoutes = route.subRoutes != null && !route.subRoutes.isEmpty();
        if (!hasSubRoutes && decisionOption.getDecisions().isEmpty()) {
            return rowSpec; //at a leaf node; return
        }

        return getRowSpec(fields, rowSpec, route.subRoutes);
    }

    private RowSpec getRootRowSpec(ProfileFields fields, ConstraintNode rootNode) {
        return constraintReducer.reduceConstraintsToRowSpec(
            fields,
            rootNode.getAtomicConstraints()).get();
    }
}
