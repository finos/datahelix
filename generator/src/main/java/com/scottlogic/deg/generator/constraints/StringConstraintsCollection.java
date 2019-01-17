package com.scottlogic.deg.generator.constraints;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.constraints.atomic.*;
import com.scottlogic.deg.generator.inputs.RuleInformation;

import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StringConstraintsCollection {
    private final Set<AtomicConstraint> constraints;

    public StringConstraintsCollection(Set<AtomicConstraint> constraints) {
        this.constraints = constraints
            .stream()
            .filter(StringConstraintsCollection::isUnderstoodConstraintType)
            .collect(Collectors.toSet());
    }

    public StringConstraintsCollection(AtomicConstraint constraint){
        this(Collections.singleton(constraint));
    }

    private static boolean isUnderstoodConstraintType(AtomicConstraint constraint) {
        return constraint instanceof StringHasLengthConstraint
            || constraint instanceof IsStringLongerThanConstraint
            || constraint instanceof IsStringShorterThanConstraint
            || (constraint instanceof NotConstraint && isUnderstoodConstraintType(((NotConstraint) constraint).negatedConstraint));
    }

    public StringConstraintsCollection union(StringConstraintsCollection otherConstraint){
        return new StringConstraintsCollection(
            Stream.concat(
                this.constraints.stream(),
                otherConstraint.constraints.stream()
            ).collect(Collectors.toSet()));
    }

    public boolean isContradictory() {
        if (this.constraints.isEmpty() || this.constraints.size() == 1){
            return false;
        }

        return this.constraints
            .stream()
            .anyMatch(this::isContradictory);
    }

    private boolean isContradictory(AtomicConstraint constraint) {
        Set<AtomicConstraint> otherConstraints = this.constraints.stream().filter(c -> c != constraint).collect(Collectors.toSet());

        if (constraint instanceof NotConstraint){
            AtomicConstraint complementOfConstraint = getComplementOfConstraint(((NotConstraint) constraint).negatedConstraint); // field1 < 10 -> field1 > 9
            return complementOfConstraint != null
                ? otherConstraints
                    .stream()
                    .anyMatch(otherConstraint -> isContradictory(complementOfConstraint, otherConstraint))
                : false; //we cannot prove it is contradictory
        }

        return otherConstraints
            .stream()
            .anyMatch(otherConstraint -> isContradictory(constraint, otherConstraint));
    }

    private boolean isContradictory(AtomicConstraint constraint, AtomicConstraint otherConstraint) {
        if (otherConstraint instanceof NotConstraint){
            AtomicConstraint negatedConstraint = ((NotConstraint) otherConstraint).negatedConstraint;
            if (constraint.equals(negatedConstraint)){
                return true; //e.g field1 < 10, not(field1 < 10); //fully contradictory
            }

            AtomicConstraint complementOfOtherConstraint = getComplementOfConstraint(negatedConstraint); // field1 < 10 -> field1 > 9
            return complementOfOtherConstraint != null
                ? isContradictory(constraint, complementOfOtherConstraint)
                : false; //we cannot prove it is contradictory
        }

        if (constraint.getClass().equals(otherConstraint.getClass())){
            if (constraint instanceof StringHasLengthConstraint){
                return areLengthConstraintsContradictory((StringHasLengthConstraint)constraint, (StringHasLengthConstraint)otherConstraint);
            }

            if (constraint instanceof StringHasDifferentLengthConstraint) {
                return areLengthConstraintsContradictory((StringHasDifferentLengthConstraint)constraint, (StringHasDifferentLengthConstraint)otherConstraint);
            }

            return false; //same type constraints cannot contradict
        }

        if (constraint instanceof IsStringShorterThanConstraint){
            return isContradictoryToMaxLength(((IsStringShorterThanConstraint) constraint).referenceValue, otherConstraint);
        }

        if (constraint instanceof IsStringLongerThanConstraint){
            return isContradictoryToMinLength(((IsStringLongerThanConstraint) constraint).referenceValue, otherConstraint);
        }

        if (constraint instanceof StringHasLengthConstraint) {
            return isContradictoryToOfLengthConstraint(((StringHasLengthConstraint) constraint).referenceValue, otherConstraint);
        }

        if (constraint instanceof StringHasDifferentLengthConstraint) {
            return isContradictoryToDifferentLengthConstraint(((StringHasDifferentLengthConstraint) constraint).getLength(), otherConstraint);
        }

        return false; //we cannot prove it is contradictory
    }

    private boolean areLengthConstraintsContradictory(
        StringHasDifferentLengthConstraint constraint,
        StringHasDifferentLengthConstraint otherConstraint) {
        return constraint.getLength() != otherConstraint.getLength(); //TODO: should this be == or !=?
    }

    private boolean areLengthConstraintsContradictory(
        StringHasLengthConstraint constraint,
        StringHasLengthConstraint otherConstraint) {
        return constraint.referenceValue != otherConstraint.referenceValue;
    }

    private boolean isContradictoryToMaxLength(int shorterThan, AtomicConstraint otherConstraint) {
        if (otherConstraint instanceof IsStringLongerThanConstraint){
            int longerThan = ((IsStringLongerThanConstraint) otherConstraint).referenceValue;
            if (longerThan >= shorterThan - 1){
                return true; //field1 < 10 & field1 > 20 | field1 < 10 & field1 > 10
            }
        }

        return false;
    }

    private boolean isContradictoryToMinLength(int longerThan, AtomicConstraint otherConstraint) {
        if (otherConstraint instanceof IsStringShorterThanConstraint){
            int shorterThan = ((IsStringShorterThanConstraint) otherConstraint).referenceValue;
            if (shorterThan <= longerThan){
                return true; //field1 > 20 & field1 < 20 | field1 > 10 & field1 < 10
            }
        }

        return false;
    }

    private boolean isContradictoryToOfLengthConstraint(int requiredLength, AtomicConstraint otherConstraint) {
        if (otherConstraint instanceof IsStringLongerThanConstraint){
            int longerThan = ((IsStringLongerThanConstraint) otherConstraint).referenceValue;
            if (requiredLength <= longerThan){
                return true; // field1 ofLength 10 & field1 > 10 (i.e field1 >= 11)
            }

            return false; //technically non-contradictory
        }

        if (otherConstraint instanceof IsStringShorterThanConstraint){
            int shorterThan = ((IsStringShorterThanConstraint) otherConstraint).referenceValue;
            if (requiredLength >= shorterThan){
                return true; // field1 ofLength 10 & field1 < 10 (i.e field1 <= 9)
            }

            return false; //technically non-contradictory
        }

        if (otherConstraint instanceof StringHasDifferentLengthConstraint){
            int notOfLength = ((StringHasDifferentLengthConstraint) otherConstraint).getLength();
            return notOfLength == requiredLength;
        }

        return false;
    }

    private boolean isContradictoryToDifferentLengthConstraint(int notOfLength, AtomicConstraint otherConstraint) {
        if (otherConstraint instanceof StringHasLengthConstraint){
            int requiredLength = ((StringHasLengthConstraint) otherConstraint).referenceValue;
            return notOfLength == requiredLength;
        }

        return false;
    }

    private AtomicConstraint getComplementOfConstraint(AtomicConstraint negatedConstraint) {
        if (negatedConstraint instanceof IsStringShorterThanConstraint){
            IsStringShorterThanConstraint shorterThan = (IsStringShorterThanConstraint) negatedConstraint;
            return new IsStringLongerThanConstraint(
                shorterThan.field,
                shorterThan.referenceValue - 1, //field1 < 10 --> field1 > 9
                shorterThan.getRules());
        }

        if (negatedConstraint instanceof IsStringLongerThanConstraint){
            IsStringLongerThanConstraint longerThan = (IsStringLongerThanConstraint) negatedConstraint;
            return new IsStringShorterThanConstraint(
                longerThan.field,
                longerThan.referenceValue + 1, //field1 > 9 --> field1 < 10
                longerThan.getRules());
        }

        if (negatedConstraint instanceof StringHasLengthConstraint){
            StringHasLengthConstraint ofLength = (StringHasLengthConstraint) negatedConstraint;

            return new StringHasDifferentLengthConstraint(ofLength);
        }

        return null;
    }

    private class StringHasDifferentLengthConstraint implements AtomicConstraint {
        final StringHasLengthConstraint underlyingConstraint;

        public StringHasDifferentLengthConstraint(StringHasLengthConstraint underlyingConstraint) {
            this.underlyingConstraint = underlyingConstraint;
        }

        @Override
        public String toDotLabel() {
            return this.underlyingConstraint.toDotLabel();
        }

        @Override
        public Field getField() {
            return this.underlyingConstraint.getField();
        }

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof StringHasDifferentLengthConstraint)) {
                return false;
            }

            StringHasDifferentLengthConstraint other = (StringHasDifferentLengthConstraint) o;
            return this.equals(other);
        }

        @Override
        public int hashCode() {
            return Objects.hash(this.underlyingConstraint, StringHasDifferentLengthConstraint.class);
        }

        @Override
        public String toString() {
            return String.format("`%s` != %d", this.underlyingConstraint.getField().name, this.underlyingConstraint.referenceValue);
        }

        @Override
        public Set<RuleInformation> getRules() {
            return this.underlyingConstraint.getRules();
        }

        @Override
        public AtomicConstraint withRules(Set<RuleInformation> rules) {
            return new StringHasDifferentLengthConstraint(
                (StringHasLengthConstraint) this.underlyingConstraint.withRules(rules));
        }

        public int getLength(){
            return this.underlyingConstraint.referenceValue;
        }
    }
}
