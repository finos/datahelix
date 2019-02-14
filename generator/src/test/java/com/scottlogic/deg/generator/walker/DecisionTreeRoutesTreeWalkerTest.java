package com.scottlogic.deg.generator.walker;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.ProfileFields;
import com.scottlogic.deg.generator.constraints.atomic.IsInSetConstraint;
import com.scottlogic.deg.generator.generation.TestGenerationConfig;
import com.scottlogic.deg.generator.inputs.RuleInformation;
import com.scottlogic.deg.generator.constraints.atomic.AtomicConstraint;
import com.scottlogic.deg.generator.decisiontree.*;
import com.scottlogic.deg.generator.reducer.ConstraintReducer;
import com.scottlogic.deg.generator.fieldspecs.FieldSpecFactory;
import com.scottlogic.deg.generator.fieldspecs.FieldSpecMerger;
import com.scottlogic.deg.generator.fieldspecs.RowSpec;
import com.scottlogic.deg.generator.fieldspecs.RowSpecMerger;
import com.scottlogic.deg.generator.walker.routes.RowSpecRoute;
import com.scottlogic.deg.schemas.v3.RuleDTO;
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
        ConstraintNode singleDecisionOption = constraint("decision");
        ConstraintNode rootNode = constraint(
            "root",
            new OrderedDecisionNode(singleDecisionOption));
        DecisionTree tree = new DecisionTree(rootNode, getFields(), "Test tree");

        RowSpecRoute route1 = route(null, route(singleDecisionOption));
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
        ConstraintNode leftDecisionOption = constraint("left");
        ConstraintNode rightDecisionOption = constraint("right");
        ConstraintNode rootNode = constraint(
            "root",
            new OrderedDecisionNode(leftDecisionOption),
            new OrderedDecisionNode(rightDecisionOption)
        );
        DecisionTree tree = new DecisionTree(rootNode, getFields(), "Test tree");

        RowSpecRoute route1 = route(null, route(leftDecisionOption), route(rightDecisionOption));
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
        ConstraintNode leftDecisionOption = constraint("left");
        ConstraintNode conflictingOption = constraint("conflicting");
        ConstraintNode rootNode = constraint(
            "root",
            new OrderedDecisionNode(leftDecisionOption),
            new OrderedDecisionNode(conflictingOption)
        );
        DecisionTree tree = new DecisionTree(rootNode, getFields(), "Test tree");

        RowSpecRoute route1 = route(null, route(leftDecisionOption), route(conflictingOption));
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
        ConstraintNode leftDecisionOption = constraint("left");
        ConstraintNode rightDecisionOption = constraint("right");
        ConstraintNode rootNode = constraint(
            "root",
            new OrderedDecisionNode(leftDecisionOption, rightDecisionOption)
        );
        DecisionTree tree = new DecisionTree(rootNode, getFields(), "Test tree");

        RowSpecRoute route1 = route(null, route(leftDecisionOption));
        RowSpecRoute route2 = route(null, route(rightDecisionOption));
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
        ConstraintNode leftDecisionOption = constraint("left");
        ConstraintNode rightDecisionOption = constraint("right");
        ConstraintNode rootNode = constraint(
            "root",
            new OrderedDecisionNode(leftDecisionOption, rightDecisionOption)
        );
        RowSpecRoute route1 = route(null, route(leftDecisionOption));
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

    private static RowSpecRoute route(ConstraintNode decisionOption, RowSpecRoute... subRoutes){
        return new RowSpecRoute(decisionOption, Arrays.asList(subRoutes));
    }

    private static ConstraintNode constraint(String name, OrderedDecisionNode... decisions){
        return new OrderedConstraintNode(
            Collections.singletonList(new IsInSetConstraint(new Field(name), Collections.singleton(name), rules())),
            Arrays.asList(decisions));
    }

    private class TestConstraintReducer extends ConstraintReducer {
        private final ConstraintNodeToRowSpecMap[] maps;

        TestConstraintReducer(ConstraintNodeToRowSpecMap... maps) {
            super(new FieldSpecFactory(), new FieldSpecMerger());
            this.maps = maps;
        }

        @Override
        public Optional<RowSpec> reduceConstraintsToRowSpec(ProfileFields fields, Iterable<AtomicConstraint> atomicConstraints) {
            for (ConstraintNodeToRowSpecMap map : maps){
                if (constraintsMatch(map.constraint, atomicConstraints.iterator())){
                    return map.rowSpec == null
                        ? Optional.empty()
                        : Optional.of(map.rowSpec);
                }
            }

            return Optional.empty();
        }

        private boolean constraintsMatch(ConstraintNode constraint, Iterator<AtomicConstraint> atomicConstraints){
            Iterator<AtomicConstraint> compareAtomicConstraints = constraint.getAtomicConstraints().iterator();

            while (atomicConstraints.hasNext()){
                if (!compareAtomicConstraints.hasNext())
                    return false;

                AtomicConstraint compareNext = compareAtomicConstraints.next();
                AtomicConstraint next = atomicConstraints.next();

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
        private final List<AtomicConstraint> atomicConstraints;
        private final List<DecisionNode> decisions;

        public OrderedConstraintNode(List<AtomicConstraint> atomicConstraints, List<DecisionNode> decisions) {
            this.atomicConstraints = atomicConstraints;
            this.decisions = decisions;
        }

        @Override
        public Collection<AtomicConstraint> getAtomicConstraints() {
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
        public ConstraintNode cloneWithoutAtomicConstraint(AtomicConstraint excludeAtomicConstraint) {
            throw new UnsupportedOperationException("Not supported");
        }

        @Override
        public boolean atomicConstraintExists(AtomicConstraint constraint) {
            throw new UnsupportedOperationException("Not supported");
        }

        @Override
        public ConstraintNode addAtomicConstraints(Collection<AtomicConstraint> constraints) {
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

        @Override
        public ConstraintNode markNode(NodeMarking marking) {
            throw new UnsupportedOperationException("Not supported");
        }

        @Override
        public boolean hasMarking(NodeMarking detail) {
            throw new UnsupportedOperationException("Not supported");
        }

        @Override
        public ConstraintNode accept(NodeVisitor visitor){
            Stream<AtomicConstraint> atomicConstraintStream = getAtomicConstraints().stream().map(a -> a.accept(visitor));
            Stream<DecisionNode> decisionNodeStream = getDecisions().stream().map(d -> d.accept(visitor));

            return visitor.visit(
                new OrderedConstraintNode(
                    atomicConstraintStream.collect(Collectors.toList()),
                    decisionNodeStream.collect(Collectors.toList())));
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

        @Override
        public DecisionNode markNode(NodeMarking marking) {
            throw new UnsupportedOperationException("Not supported");
        }

        @Override
        public boolean hasMarking(NodeMarking detail) {
            throw new UnsupportedOperationException("Not supported");
        }

        @Override
        public DecisionNode accept(NodeVisitor visitor){
            Stream<ConstraintNode> options = getOptions().stream().map(c->c.accept(visitor));
            return visitor.visit(
                new TreeDecisionNode(
                    options.collect(Collectors.toList())
                )
            );
        }
    }

    private static Set<RuleInformation> rules(){
        RuleDTO rule = new RuleDTO();
        rule.rule = "rules";
        return Collections.singleton(new RuleInformation(rule));
    }
}