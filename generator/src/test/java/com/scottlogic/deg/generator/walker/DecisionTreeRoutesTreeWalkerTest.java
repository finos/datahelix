package com.scottlogic.deg.generator.walker;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.ProfileFields;
import com.scottlogic.deg.generator.constraints.IConstraint;
import com.scottlogic.deg.generator.constraints.IsEqualToConstantConstraint;
import com.scottlogic.deg.generator.decisiontree.*;
import com.scottlogic.deg.generator.reducer.ConstraintReducer;
import com.scottlogic.deg.generator.restrictions.FieldSpecFactory;
import com.scottlogic.deg.generator.restrictions.FieldSpecMerger;
import com.scottlogic.deg.generator.restrictions.RowSpec;
import com.scottlogic.deg.generator.restrictions.RowSpecMerger;
import com.scottlogic.deg.generator.walker.routes.RowSpecRoute;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class DecisionTreeRoutesTreeWalkerTest {

    @Test
    void shouldEnumerateRoutes() {
        ConstraintReducer reducer = new TestConstraintReducer();
        RowSpecMerger rowSpecMerger = new TestRowSpecMerger();
        TestRowSpecRouteProducer routeProducer = new TestRowSpecRouteProducer();
        DecisionTreeRoutesTreeWalker walker = new DecisionTreeRoutesTreeWalker(
            reducer,
            rowSpecMerger,
            routeProducer);
        DecisionTree tree = new DecisionTree(new TreeConstraintNode(), getFields(), "Test tree");

        walker.walk(tree);

        Assert.assertSame(routeProducer.actualDecisionTree, tree);
    }

    @Test
    void shouldReturnAccumulatedRowSpecForSingleDecision() {
        /*       [ rootNode ]
                       |
                       v
                       |
            [ singleDecisionOption ] */

        RowSpecMerger rowSpecMerger = new TestRowSpecMerger();
        RowSpecRoute route1 = route(-1, route(0));
        ConstraintNode singleDecisionOption = constraint("decision");
        ConstraintNode rootNode = constraint(
            "root",
            new OrderedDecisionNode(singleDecisionOption));
        DecisionTree tree = new DecisionTree(rootNode, getFields(), "Test tree");
        RowSpec expectedRowSpec = rowSpec("single decision");
        ConstraintReducer reducer = new TestConstraintReducer(
            new ConstraintNodeToRowSpecMap(rootNode, rowSpec("root")),
            new ConstraintNodeToRowSpecMap(singleDecisionOption, expectedRowSpec)
        );
        TestRowSpecRouteProducer routeProducer = new TestRowSpecRouteProducer(route1);
        DecisionTreeRoutesTreeWalker walker = new DecisionTreeRoutesTreeWalker(
            reducer,
            rowSpecMerger,
            routeProducer);

        Stream<RowSpec> routes = walker.walk(tree);

        List<RowSpec> routesList = routes.collect(Collectors.toList());
        Assert.assertEquals(routesList.size(), 1);
        Assert.assertEquals(routesList.get(0).toString(), "single decision<root");
    }

    @Test
    void shouldReturnAccumulatedRowSpecForMultipleDecisions() {
        /*       [ rootNode ]
                   /      \
                  /        \
                 v          v
                 |          |
[ leftDecisionOption ]  [ rightDecisionOption ] */

        RowSpecMerger rowSpecMerger = new TestRowSpecMerger();
        RowSpecRoute route1 = route(-1, route(0), route(0));
        ConstraintNode leftDecisionOption = constraint("left");
        ConstraintNode rightDecisionOption = constraint("right");
        ConstraintNode rootNode = constraint(
            "root",
            new OrderedDecisionNode(leftDecisionOption),
            new OrderedDecisionNode(rightDecisionOption)
        );
        DecisionTree tree = new DecisionTree(rootNode, getFields(), "Test tree");
        ConstraintReducer reducer = new TestConstraintReducer(
            new ConstraintNodeToRowSpecMap(rootNode, rowSpec("root")),
            new ConstraintNodeToRowSpecMap(leftDecisionOption, rowSpec("left decision")),
            new ConstraintNodeToRowSpecMap(rightDecisionOption, rowSpec("right decision"))
        );
        TestRowSpecRouteProducer routeProducer = new TestRowSpecRouteProducer(route1);
        DecisionTreeRoutesTreeWalker walker = new DecisionTreeRoutesTreeWalker(
            reducer,
            rowSpecMerger,
            routeProducer);

        Stream<RowSpec> routes = walker.walk(tree);

        List<RowSpec> routesList = routes.collect(Collectors.toList());
        Assert.assertEquals(routesList.get(0).toString(), "right decision<left decision<root");
    }

    @Test
    void shouldNotReturnARowSpecIfUnableToMerge() {
        /*       [ rootNode ]
                   /      \
                  /        \
                 v          v
                 |          |
[ leftDecisionOption ]  [ conflictingOption ] */

        RowSpecMerger rowSpecMerger = new TestRowSpecMerger();
        RowSpecRoute route1 = route(-1, route(0), route(0));
        ConstraintNode leftDecisionOption = constraint("left");
        ConstraintNode conflictingOption = constraint("conflicting");
        ConstraintNode rootNode = constraint(
            "root",
            new OrderedDecisionNode(leftDecisionOption),
            new OrderedDecisionNode(conflictingOption)
        );
        DecisionTree tree = new DecisionTree(rootNode, getFields(), "Test tree");
        ConstraintReducer reducer = new TestConstraintReducer(
            new ConstraintNodeToRowSpecMap(rootNode, rowSpec("root")),
            new ConstraintNodeToRowSpecMap(leftDecisionOption, rowSpec("left decision")),
            new ConstraintNodeToRowSpecMap(conflictingOption, null)
        );
        TestRowSpecRouteProducer routeProducer = new TestRowSpecRouteProducer(route1);
        DecisionTreeRoutesTreeWalker walker = new DecisionTreeRoutesTreeWalker(
            reducer,
            rowSpecMerger,
            routeProducer);

        Stream<RowSpec> routes = walker.walk(tree);

        List<RowSpec> routesList = routes.collect(Collectors.toList());
        Assert.assertEquals(routesList.size(), 0);
    }

    @Test
    void shouldPermuteAllPathsThroughADecision() {
        /*       [ rootNode ]
                      |
                      v
                     / \
                    /   \
[ leftDecisionOption ]  [ rightDecisionOption ] */

        RowSpecMerger rowSpecMerger = new TestRowSpecMerger();
        RowSpecRoute route1 = route(-1, route(0));
        RowSpecRoute route2 = route(-1, route(1));
        ConstraintNode leftDecisionOption = constraint("left");
        ConstraintNode rightDecisionOption = constraint("right");
        ConstraintNode rootNode = constraint(
            "root",
            new OrderedDecisionNode(leftDecisionOption, rightDecisionOption)
        );
        DecisionTree tree = new DecisionTree(rootNode, getFields(), "Test tree");
        ConstraintReducer reducer = new TestConstraintReducer(
            new ConstraintNodeToRowSpecMap(rootNode, rowSpec("root")),
            new ConstraintNodeToRowSpecMap(leftDecisionOption, rowSpec("left decision")),
            new ConstraintNodeToRowSpecMap(rightDecisionOption, rowSpec("right decision"))
        );
        TestRowSpecRouteProducer routeProducer = new TestRowSpecRouteProducer(route1, route2);
        DecisionTreeRoutesTreeWalker walker = new DecisionTreeRoutesTreeWalker(
            reducer,
            rowSpecMerger,
            routeProducer);

        Stream<RowSpec> routes = walker.walk(tree);

        List<RowSpec> routesList = routes.collect(Collectors.toList());
        Assert.assertEquals(routesList.size(), 2);
        List<String> routePaths = routesList.stream().map(RowSpec::toString).collect(Collectors.toList());
        Assert.assertThat(routePaths, Matchers.containsInAnyOrder("left decision<root", "right decision<root"));
    }

    @Test
    void shouldOnlyTraverseDecisionPathIfToldTo() {
        /*       [ rootNode ]
                      |
                      v
                     / \
                    /   \
[ leftDecisionOption ]  [ rightDecisionOption ] */

        RowSpecMerger rowSpecMerger = new TestRowSpecMerger();
        RowSpecRoute route1 = route(-1, route(0));
        ConstraintNode leftDecisionOption = constraint("left");
        ConstraintNode rightDecisionOption = constraint("right");
        ConstraintNode rootNode = constraint(
            "root",
            new OrderedDecisionNode(leftDecisionOption, rightDecisionOption)
        );
        DecisionTree tree = new DecisionTree(rootNode, getFields(), "Test tree");
        ConstraintReducer reducer = new TestConstraintReducer(
            new ConstraintNodeToRowSpecMap(rootNode, rowSpec("root")),
            new ConstraintNodeToRowSpecMap(leftDecisionOption, rowSpec("left decision")),
            new ConstraintNodeToRowSpecMap(rightDecisionOption, rowSpec("right decision"))
        );
        TestRowSpecRouteProducer routeProducer = new TestRowSpecRouteProducer(route1);
        DecisionTreeRoutesTreeWalker walker = new DecisionTreeRoutesTreeWalker(
            reducer,
            rowSpecMerger,
            routeProducer);

        Stream<RowSpec> routes = walker.walk(tree);

        List<RowSpec> routesList = routes.collect(Collectors.toList());
        Assert.assertEquals(routesList.size(), 1);
        List<String> routePaths = routesList.stream().map(RowSpec::toString).collect(Collectors.toList());
        Assert.assertThat(routePaths, Matchers.containsInAnyOrder("left decision<root"));
        Assert.assertThat(routePaths, Matchers.not(Matchers.containsInAnyOrder("right decision<root")));
    }

    private RowSpec rowSpec(String name){
        return new TestRowSpec(name);
    }

    private static ProfileFields getFields(){
        return new ProfileFields(Arrays.asList(new Field[0]));
    }

    private static RowSpecRoute route(int decisionOptionIndex, RowSpecRoute... subRoutes){
        RowSpecRoute route = new RowSpecRoute();
        route.decisionIndex = decisionOptionIndex;
        route.subRoutes = subRoutes;

        return route;
    }

    private static ConstraintNode constraint(String name, OrderedDecisionNode... decisions){
        return new OrderedConstraintNode(
            Collections.singletonList(new IsEqualToConstantConstraint(new Field(name), name)),
            Arrays.asList(decisions));
    }

    private class TestConstraintReducer extends ConstraintReducer {
        private final ConstraintNodeToRowSpecMap[] maps;

        TestConstraintReducer(ConstraintNodeToRowSpecMap... maps) {
            super(new FieldSpecFactory(), new FieldSpecMerger());
            this.maps = maps;
        }

        @Override
        public Optional<RowSpec> reduceConstraintsToRowSpec(ProfileFields fields, Iterable<IConstraint> atomicConstraints) {
            for (ConstraintNodeToRowSpecMap map : maps){
                if (constraintsMatch(map.constraint, atomicConstraints.iterator())){
                    return map.rowSpec == null
                        ? Optional.empty()
                        : Optional.of(map.rowSpec);
                }
            }

            return Optional.empty();
        }

        private boolean constraintsMatch(ConstraintNode constraint, Iterator<IConstraint> atomicConstraints){
            Iterator<IConstraint> compareAtomicConstraints = constraint.getAtomicConstraints().iterator();

            while (atomicConstraints.hasNext()){
                if (!compareAtomicConstraints.hasNext())
                    return false;

                IConstraint compareNext = compareAtomicConstraints.next();
                IConstraint next = atomicConstraints.next();

                if (!next.equals(compareNext))
                    return false;
            }

            return compareAtomicConstraints.hasNext() == atomicConstraints.hasNext();
        }
    }

    private class TestRowSpecMerger extends RowSpecMerger {
        final ArrayList<Collection<RowSpec>> rowSpecsMerged = new ArrayList<>();

        TestRowSpecMerger() {
            super(new FieldSpecMerger());
        }

        @Override
        public Optional<RowSpec> merge(Collection<RowSpec> rowSpecs) {
            rowSpecsMerged.add(rowSpecs);

            String path = String.join(
                "<",
                rowSpecs.stream().map(RowSpec::toString).collect(Collectors.toList()));
            return Optional.of(new TestRowSpec(path));
        }
    }

    private class ConstraintNodeToRowSpecMap{
        public final ConstraintNode constraint;
        final RowSpec rowSpec;

        ConstraintNodeToRowSpecMap(ConstraintNode constraint, RowSpec rowSpec) {
            this.constraint = constraint;
            this.rowSpec = rowSpec;
        }
    }

    private class TestRowSpec extends RowSpec {
        private final String name;

        TestRowSpec(String name) {
            super(new ProfileFields(Arrays.asList(new Field[0])), new HashMap<>());
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    private static class OrderedConstraintNode implements ConstraintNode {
        private final List<IConstraint> atomicConstraints;
        private final List<DecisionNode> decisions;

        public OrderedConstraintNode(List<IConstraint> atomicConstraints, List<DecisionNode> decisions) {
            this.atomicConstraints = atomicConstraints;
            this.decisions = decisions;
        }

        @Override
        public Collection<IConstraint> getAtomicConstraints() {
            return atomicConstraints;
        }

        @Override
        public Collection<DecisionNode> getDecisions() {
            return decisions;
        }

        @Override
        public Optional<RowSpec> getOrCreateRowSpec(Supplier<Optional<RowSpec>> createRowSpecFunc) {
            throw new UnsupportedOperationException("Not supported");
        }

        @Override
        public ConstraintNode removeDecisions(Collection<DecisionNode> decisionsToRemove) {
            throw new UnsupportedOperationException("Not supported");
        }

        @Override
        public ConstraintNode cloneWithoutAtomicConstraint(IConstraint excludeAtomicConstraint) {
            throw new UnsupportedOperationException("Not supported");
        }

        @Override
        public boolean atomicConstraintExists(IConstraint constraint) {
            throw new UnsupportedOperationException("Not supported");
        }

        @Override
        public ConstraintNode addAtomicConstraints(Collection<IConstraint> constraints) {
            throw new UnsupportedOperationException("Not supported");
        }

        @Override
        public ConstraintNode addDecisions(Collection<DecisionNode> decisions) {
            throw new UnsupportedOperationException("Not supported");
        }

        @Override
        public ConstraintNode setDecisions(Collection<DecisionNode> decisions) {
            throw new UnsupportedOperationException("Not supported");
        }
    }

    private static class OrderedDecisionNode implements DecisionNode {
        private final List<ConstraintNode> options;

        public OrderedDecisionNode(ConstraintNode... options) {
            this.options = Arrays.asList(options);
        }

        @Override
        public Collection<ConstraintNode> getOptions() {
            return options;
        }

        @Override
        public DecisionNode setOptions(Collection<ConstraintNode> options) {
            throw new UnsupportedOperationException("Not supported");
        }
    }
}