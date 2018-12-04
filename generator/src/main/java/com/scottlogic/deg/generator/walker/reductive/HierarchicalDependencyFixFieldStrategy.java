package com.scottlogic.deg.generator.walker.reductive;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.Profile;
import com.scottlogic.deg.generator.constraints.ConditionalConstraint;
import com.scottlogic.deg.generator.constraints.IConstraint;
import com.scottlogic.deg.generator.constraints.IsInSetConstraint;
import com.scottlogic.deg.generator.decisiontree.reductive.ReductiveConstraintNode;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class HierarchicalDependencyFixFieldStrategy implements FixFieldStrategy {

    private final Profile profile;
    private final static int PRIMARY_METRIC_CONSTANT = 100;
    private final static int SECONDARY_METRIC_CONSTANT = 10;
    private List<FixFieldPriority> orderedFixFieldPriorities;

    public HierarchicalDependencyFixFieldStrategy(Profile profile) {
        this.profile = profile;
    }

    @Override
    public Field getNextFieldToFix(FieldCollection fieldCollection, ReductiveConstraintNode rootNode) {
        return getFieldFixingPriorityList()
            .stream()
            .map(FixFieldPriority::getField)
            .filter(field -> !fieldCollection.fieldIsFixed(field)
                && fieldCollection.getFields().stream().anyMatch(pf -> pf.equals(field)))
            .findFirst()
            .orElse(null);
    }

    List<FixFieldPriority> getFieldFixingPriorityList() {
        if (orderedFixFieldPriorities == null){
            orderedFixFieldPriorities =
            Collections.unmodifiableList(
                this.profile.fields
                .stream()
                .map(f -> new FixFieldPriority(f, getScore(f)))
                .sorted()
                .collect(Collectors.toList()));
        }
        return orderedFixFieldPriorities;
    }

    /**
     * Score is based on the following (in order of importance):
     * 1) The ratio of the number of other fields' values are affected TO the number of other fields that affect this field's value
     * 2) If the field is constrained by a set and the size of the set
     * @param field
     * @return field ranking
     */
    private double getScore(Field field) {
        // Metrics are multiplied by constants to give some weighting to each
        double primaryMetric = ((double) getScoreForDependentFields(field) / getScoreForAffectingFields(field)) * PRIMARY_METRIC_CONSTANT;
        double secondaryMetric = fieldConstrainedBySet(field) ? Math.pow(numValuesInSet(field), -1) * SECONDARY_METRIC_CONSTANT : 0;
        return primaryMetric + secondaryMetric;
    }

    private Stream<IConstraint> constraintsFromProfile(){
        return profile.rules.stream()
            .flatMap(rule -> rule.constraints.stream());
    }

    private Stream<ConditionalConstraint> conditionalConstraintsFromProfile(){
        return constraintsFromProfile()
            .filter(constraint -> constraint instanceof ConditionalConstraint)
            .map(constraint -> (ConditionalConstraint) constraint);
    }

    private Stream<ConditionalConstraint> findFieldInPredicate(Field field){
        return conditionalConstraintsFromProfile()
            .filter(constraint -> constraint.condition.getFields().contains(field));
    }

    private Stream<ConditionalConstraint> findFieldInConsequence(Field field){
        return conditionalConstraintsFromProfile()
            .filter(constraint -> (constraint.whenConditionIsTrue.getFields().contains(field))
                || (constraint.whenConditionIsFalse != null && constraint.whenConditionIsFalse.getFields().contains(field)));
    }

    private long getScoreForDependentFields(Field field) {
        // Number of conditionals containing field in predicate
        long occurrences = findFieldInPredicate(field).count();

        // Number of distinct fields affected by this field
        long uniqueFieldsAffected = findFieldInPredicate(field)
            .flatMap(condConstraint -> {
                Collection<Field> fields = condConstraint.whenConditionIsTrue.getFields();
                if (condConstraint.whenConditionIsFalse != null) {
                    return Stream.concat(fields.stream(), condConstraint.whenConditionIsFalse.getFields().stream());
                }
                return fields.stream();
            })
            .distinct()
            .count();

        return occurrences * uniqueFieldsAffected;
    }

    private long getScoreForAffectingFields(Field field) {
        // Number of times field is a consequence in a if statement
        long occurrences = findFieldInConsequence(field).count();

        // Number of distinct other fields that affect this one
        long uniqueImpactingFiles = findFieldInConsequence(field)
            .flatMap(cc -> cc.condition.getFields().stream())
            .distinct()
            .count();

        long score = occurrences * uniqueImpactingFiles;
        return score == 0 ? 1 : score;
    }

    private boolean fieldConstrainedBySet(Field field) {
        return constraintsFromProfile()
            .anyMatch(constraint -> constraint instanceof IsInSetConstraint
                && constraint.getFields().iterator().next().equals(field));
    }

    private int numValuesInSet(Field field) {
        return constraintsFromProfile()
            .filter(constraint -> constraint instanceof IsInSetConstraint
                && constraint.getFields().iterator().next().equals(field))
            .map(constraint -> ((IsInSetConstraint) constraint).legalValues)
            .max(Comparator.comparing(Set::size))
            .orElse(Collections.emptySet())
            .size();
    }

}
