package com.scottlogic.deg.generator.decisiontree;

import com.scottlogic.deg.generator.constraints.IConstraint;
import com.scottlogic.deg.generator.constraints.NotConstraint;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class DecisionTreeOptimiser implements IDecisionTreeOptimiser {
    private final boolean simplify;
    private final int maxIterations;
    private final int maxDepth;
    private final boolean favourNegatedConstraints;

    public DecisionTreeOptimiser() {
        this(true, 50, 10000000, false);
    }

    public DecisionTreeOptimiser(boolean simplify, int maxIterations, int maxDepth, boolean favourNegatedConstraints) {
        this.simplify = simplify;
        this.maxIterations = maxIterations;
        this.maxDepth = maxDepth;
        this.favourNegatedConstraints = favourNegatedConstraints;
    }

    @Override
    public DecisionTree optimiseTree(DecisionTree tree){
        ConstraintNode rootNode = tree.getRootNode();
        Collection<DecisionNode> decisions = rootNode.getDecisions();
        if (decisions.size() <= 1)
            return tree; //not worth optimising

        optimiseDecisions(rootNode, 0);
        return tree;
    }

    private void optimiseDecisions(ConstraintNode rootNode, int depth){
        Collection<DecisionNode> decisions = rootNode.getDecisions();
        if (decisions.size() <= 1)
            return; //not worth optimising

        int iteration = 0;
        int prevDecisionCount = decisions.size();
        while (iteration < this.maxIterations && optimiseDecisions(rootNode, rootNode.getDecisions(), depth))
        {
            int newDecisionCount = rootNode.getDecisions().size();
            int changeInDecisionCount = newDecisionCount - prevDecisionCount;
            if (Math.abs(changeInDecisionCount) < 1) {
                break;
            }

            prevDecisionCount = newDecisionCount;
            iteration++;
        }
    }

    private boolean optimiseDecisions(ConstraintNode rootNode, Collection<DecisionNode> decisions, int depth){
        if (depth > this.maxDepth)
            return false;

        Optional<IConstraint> mostProlificAtomicConstraintOpt = getMostProlificConstraint(decisions);
        if (!mostProlificAtomicConstraintOpt.isPresent()){
            return false;
        }
        IConstraint mostProlificAtomicConstraint = mostProlificAtomicConstraintOpt.get();

        DecisionNode firstDecisionNodeContainingTheMostProlificAtomicConstraint =
            decisions
                .stream()
                .filter(d -> d.optionWithAtomicConstraintExists(mostProlificAtomicConstraint))
                .findFirst()
                .get();

        // Add new decision node
        DecisionNode factorisedDecisionNode = new DecisionNode(true);
        rootNode.insertDecisionNodeAfter(
            factorisedDecisionNode,
            firstDecisionNodeContainingTheMostProlificAtomicConstraint);

        // Add most prolific constraint to new decision node
        ConstraintNode factorisingConstraint = new ConstraintNode(true, mostProlificAtomicConstraint);
        factorisedDecisionNode.addOption(factorisingConstraint);

        // Add negation of most prolific constraint to new decision node
        IConstraint negatedMostProlificConstraint = NotConstraint.negate(mostProlificAtomicConstraint);
        ConstraintNode negatedFactorisingConstraint = new ConstraintNode(true, negatedMostProlificConstraint);
        factorisedDecisionNode.addOption(negatedFactorisingConstraint);

        for (DecisionNode decision : decisions) {
            // Sets to add constraints seen in conjunction with most prolific constraint
            Set<IConstraint> mfocConstraints = new HashSet<>();
            Set<IConstraint> notMfocConstraints = new HashSet<>();

            // Lists holding constraint nodes to be added throughout the factorisation process
            List<ConstraintNode> factorisedOptions = new ArrayList<>();
            List<ConstraintNode> negatedFactorisedOptions = new ArrayList<>();
            List<ConstraintNode> otherOptions = new ArrayList<>();

            processOptionsWithFactoringConstraint(decision, mostProlificAtomicConstraint, negatedMostProlificConstraint,
                mfocConstraints, notMfocConstraints, factorisedOptions, negatedFactorisedOptions);

            boolean shouldFactoriseDecisionNode = factorisedOptions.size() > 0;
            if (!shouldFactoriseDecisionNode) {
                continue;
            }

            /*
             * At this point we have removed the nodes with mfoc and nmfoc from the original tree. All that is left at this level are nodes
             * that need moving either to same level as factorising constraint or as decisions underneath
             */
            processOptionsWithoutFactorisingConstraint(decision, mfocConstraints, notMfocConstraints, factorisedOptions, negatedFactorisedOptions, otherOptions);

            // Perform movement of nodes
            addOptionsToFactorisedNode(factorisingConstraint, factorisedOptions);
            addOptionsToFactorisedNode(negatedFactorisingConstraint, negatedFactorisedOptions);
            otherOptions.forEach(factorisedDecisionNode::addOption);
            rootNode.removeDecision(decision);
        }

        if (this.simplify){
            factorisingConstraint.simplify();
            negatedFactorisingConstraint.simplify();
        }

        optimiseDecisions(factorisingConstraint, depth + 1);
        optimiseDecisions(negatedFactorisingConstraint, depth + 1);
        return true;
    }

    private void processOptionsWithoutFactorisingConstraint(
        DecisionNode decision,
        Set<IConstraint> mfocConstraints,
        Set<IConstraint> notMfocConstraints,
        List<ConstraintNode> factorisedOptions,
        List<ConstraintNode> negatedFactorisedOptions,
        List<ConstraintNode> otherOptions) {
        for (ConstraintNode remainingOption : decision.getOptions()){
            boolean nodeCanBeMovedUnderFactorised = constraintNodeContainsNegatedConstraints(remainingOption, mfocConstraints);
            boolean nodeCanBeMovedUnderNegatedFactorised = constraintNodeContainsNegatedConstraints(remainingOption, notMfocConstraints);
            if (nodeCanBeMovedUnderFactorised) {
                factorisedOptions.add(remainingOption);
            } else if (nodeCanBeMovedUnderNegatedFactorised) {
                negatedFactorisedOptions.add(remainingOption);
            } else {
                otherOptions.add(remainingOption);
            }
            decision.removeOption(remainingOption);
        }
    }

    private boolean constraintNodeContainsNegatedConstraints(ConstraintNode node, Set<IConstraint> constraints){
        return node.getAtomicConstraints().stream()
            .map(NotConstraint::negate)
            .allMatch(constraints::contains);
    }

    private void processOptionsWithFactoringConstraint(
        DecisionNode decision,
        IConstraint mostProlificAtomicConstraint,
        IConstraint negatedMostProlificConstraint,
        Set<IConstraint> mfocConstraints,
        Set<IConstraint> notMfocConstraints,
        List<ConstraintNode> factorisedOptions,
        List<ConstraintNode> negatedFactorisedOptions
    ) {

        for (ConstraintNode option : decision.getOptions()) {
            boolean nodeContainsProlificConstraint = option.atomicConstraintExists(mostProlificAtomicConstraint);
            boolean nodeContainsNegatedProlificConstraint = option.atomicConstraintExists(negatedMostProlificConstraint);

            if (nodeContainsProlificConstraint && nodeContainsNegatedProlificConstraint) {
                // OPTIMISATION - this node has contradictory constraints so delete entirely
                throw new RuntimeException("Contradictory constraint node");
            } else if (nodeContainsProlificConstraint) {
                factorOutConstraintNode(decision, option, mostProlificAtomicConstraint, mfocConstraints, factorisedOptions);
            } else if (nodeContainsNegatedProlificConstraint) {
                factorOutConstraintNode(decision, option, negatedMostProlificConstraint, notMfocConstraints, negatedFactorisedOptions);
            }
        }
    }

    private void factorOutConstraintNode(
        DecisionNode currentDecision,
        ConstraintNode option,
        IConstraint factoringConstraint,
        Set<IConstraint> atomicConstraints,
        List<ConstraintNode> factorisedOptions) {
        ConstraintNode positiveCaseNode = option.cloneWithoutAtomicConstraint(factoringConstraint);
        if (positiveCaseNode.getAtomicConstraints().size() > 0) {
            atomicConstraints.addAll(positiveCaseNode.getAtomicConstraints());
            factorisedOptions.add(positiveCaseNode);
        }
        currentDecision.removeOption(option);
    }

    private void addOptionsToFactorisedNode(ConstraintNode newNode, List<ConstraintNode> optionsToAdd) {
        if (optionsToAdd.size() > 0){
            DecisionNode decisionUnderFactorisedNode = new DecisionNode(true);
            optionsToAdd.forEach(decisionUnderFactorisedNode::addOption);
            newNode.addDecision(decisionUnderFactorisedNode);
        }
    }

    private int disfavourNotConstraints(Map.Entry<IConstraint, Long> entry){
        if (this.favourNegatedConstraints)
            return entry.getKey() instanceof NotConstraint ? 0 : 1;

        return entry.getKey() instanceof NotConstraint ? 1 : 0;
    }

    private Optional<IConstraint> getMostProlificConstraint(Collection<DecisionNode> decisions) {
        Map<IConstraint, Long> decisionConstraints = decisions
            .stream()
            .flatMap(dn -> dn.getOptions().stream())
            .flatMap(option -> option.getAtomicConstraints().stream())
            .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        Optional<Map.Entry<IConstraint, Long>> mostProlificConstraintOpt = decisionConstraints.entrySet()
            .stream()
            .sorted(Comparator
                .comparing(this::disfavourNotConstraints))
            .max(Comparator.comparing(Map.Entry::getValue));

        if (!mostProlificConstraintOpt.isPresent() || mostProlificConstraintOpt.get().getValue() <= 1){
            return Optional.empty();
        }
        return Optional.of(mostProlificConstraintOpt.get().getKey());
    }
}
