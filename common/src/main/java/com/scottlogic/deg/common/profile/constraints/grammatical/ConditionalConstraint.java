package com.scottlogic.deg.common.profile.constraints.grammatical;

import com.scottlogic.deg.common.profile.constraints.Constraint;
import com.scottlogic.deg.common.profile.RuleInformation;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ConditionalConstraint implements GrammaticalConstraint
{
    public final Constraint condition;
    public final Constraint whenConditionIsTrue;
    public final Constraint whenConditionIsFalse;

    public ConditionalConstraint(
        Constraint condition,
        Constraint whenConditionIsTrue) {
        this(condition, whenConditionIsTrue, null);
    }

    public ConditionalConstraint(
        Constraint condition,
        Constraint whenConditionIsTrue,
        Constraint whenConditionIsFalse) {
        this.condition = condition;
        this.whenConditionIsTrue = whenConditionIsTrue;
        this.whenConditionIsFalse = whenConditionIsFalse;
    }

    @Override
    public Set<RuleInformation> getRules() {
        return Stream.of(condition, whenConditionIsTrue, whenConditionIsFalse)
                .filter(Objects::nonNull)
                .flatMap(c -> c.getRules().stream())
                .collect(Collectors.toSet());
    }

    @Override
    public String toString() {
        return String.format(
            "if (%s) then %s%s",
            condition,
            whenConditionIsTrue.toString(),
            whenConditionIsFalse != null ? " else " + whenConditionIsFalse.toString() : ""
        );
    }
}
