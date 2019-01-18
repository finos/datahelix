package com.scottlogic.deg.generator.constraints;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.constraints.atomic.*;
import com.scottlogic.deg.generator.constraints.grammatical.AndConstraint;
import com.scottlogic.deg.generator.constraints.grammatical.OrConstraint;
import com.scottlogic.deg.generator.inputs.RuleInformation;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/*
 * The following JsonTypeInfo is needed for the utility program GenTreeJson.java
 * (invoked via the mode of `genTreeJson`), which produces a JSON for the decision
 * tree in memory.
 */
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
        )
@JsonSubTypes({
    @JsonSubTypes.Type(value = IsInSetConstraint.class, name = "IsInSetConstraint"),
    @JsonSubTypes.Type(value = IsStringShorterThanConstraint.class, name = "IsStringShorterThanConstraint"),
    @JsonSubTypes.Type(value = IsOfTypeConstraint.class, name = "IsOfTypeConstraint"),
    @JsonSubTypes.Type(value = NotConstraint.class, name = "NotConstraint"),
    @JsonSubTypes.Type(value = IsNullConstraint.class, name = "IsNullConstraint"),
    @JsonSubTypes.Type(value = IsLessThanConstantConstraint.class, name = "IsLessThanConstantConstraint")
})
public interface Constraint
{
    static Collection<Constraint> combine(Constraint self, Constraint[] others)
    {
        return Stream
            .concat(
                Stream.of(self),
                Stream.of(others))
            .collect(Collectors.toList());
    }

    default Constraint or(Constraint... others)
    {
        return new OrConstraint(combine(this, others));
    }

    default Constraint and(Constraint... others)
    {
        return new AndConstraint(combine(this, others));
    }

    Constraint negate();

    Collection<Field> getFields();

    Set<RuleInformation> getRules();
}

