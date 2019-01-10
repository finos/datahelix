package com.scottlogic.deg.generator.restrictions;

import com.scottlogic.deg.generator.constraints.atomic.*;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StringConstraints{
    private final Set<AtomicConstraint> constraints;

    public StringConstraints(Set<AtomicConstraint> constraints) {
        this.constraints = constraints;
    }

    public StringConstraints(AtomicConstraint constraint){
        this(Collections.singleton(constraint));
    }

    public StringConstraints intersect(StringConstraints otherConstraint){
        return new StringConstraints(
            Stream.concat(
                this.constraints.stream(),
                otherConstraint.constraints.stream()
            ).collect(Collectors.toSet()));
    }

    boolean isContradictory() {
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
            AtomicConstraint complementOfConstraint = complementOfConstraint(((NotConstraint) constraint).negatedConstraint); // field1 < 10 -> field1 > 9
            return complementOfConstraint != null
                ? isContradictory(constraint, complementOfConstraint)
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

            AtomicConstraint complementOfOtherConstraint = complementOfConstraint(negatedConstraint); // field1 < 10 -> field1 > 9
            return complementOfOtherConstraint != null
                ? isContradictory(constraint, complementOfOtherConstraint)
                : false; //we cannot prove it is contradictory
        }

        if (constraint.getClass().equals(otherConstraint.getClass())){
            return false; //same type constraints cannot contradict
        }

        if (constraint instanceof IsStringShorterThanConstraint){
            return isContradictoryToMaxLength(((IsStringShorterThanConstraint) constraint).referenceValue, otherConstraint)
                .orElse(false);
        }

        if (constraint instanceof IsStringLongerThanConstraint){
            return isContradictoryToMinLength(((IsStringLongerThanConstraint) constraint).referenceValue, otherConstraint)
                .orElse(false);
        }

        if (constraint instanceof StringHasLengthConstraint) {
            return isContradictoryToOfLengthConstraint(((StringHasLengthConstraint) constraint).referenceValue, otherConstraint)
                .orElse(false);
        }

        return false; //we cannot prove it is contradictory
    }

    private Optional<Boolean> isContradictoryToMaxLength(int shorterThan, AtomicConstraint otherConstraint) {
        if (otherConstraint instanceof IsStringLongerThanConstraint){
            int longerThan = ((IsStringLongerThanConstraint) otherConstraint).referenceValue;
            if (longerThan >= shorterThan){
                return Optional.of(true); //field1 < 10 & field1 > 20 | field1 < 10 & field1 > 10
            }

            return Optional.of(false);
        }

        return Optional.empty();
    }

    private Optional<Boolean> isContradictoryToMinLength(int longerThan, AtomicConstraint otherConstraint) {
        if (otherConstraint instanceof IsStringShorterThanConstraint){
            int shorterThan = ((IsStringShorterThanConstraint) otherConstraint).referenceValue;
            if (shorterThan <= longerThan){
                return Optional.of(true); //field1 > 20 & field1 < 20 | field1 > 10 & field1 < 10
            }

            return Optional.of(false);
        }

        return Optional.empty();
    }

    private Optional<Boolean> isContradictoryToOfLengthConstraint(int requiredLength, AtomicConstraint otherConstraint) {
        if (otherConstraint instanceof IsStringLongerThanConstraint){
            int longerThan = ((IsStringLongerThanConstraint) otherConstraint).referenceValue;
            if (longerThan >= requiredLength){
                return Optional.of(true); // field1 ofLength 10 & field1 > 10 (i.e field1 >= 11)
            }

            return Optional.of(false); //technically non-contradictory
        }

        if (otherConstraint instanceof IsStringShorterThanConstraint){
            int shorterThan = ((IsStringShorterThanConstraint) otherConstraint).referenceValue;
            if (requiredLength <= shorterThan){
                return Optional.of(true); // field1 ofLength 10 & field1 < 10 (i.e field1 <= 9)
            }

            return Optional.of(false); //technically non-contradictory
        }

        return Optional.empty();
    }

    private AtomicConstraint complementOfConstraint(AtomicConstraint negatedConstraint) {
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

            //can use either longerThan <length> or shorterThan <length>
            return new IsStringShorterThanConstraint(
                ofLength.field,
                ofLength.referenceValue,
                ofLength.getRules()
            );
        }

        return null;
    }
}
