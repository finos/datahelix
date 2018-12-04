package com.scottlogic.deg.generator.constraints;

import com.scottlogic.deg.generator.Field;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

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
    @JsonSubTypes.Type(value = IsEqualToConstantConstraint.class, name = "IsEqualToConstantConstraint"),
    @JsonSubTypes.Type(value = IsStringShorterThanConstraint.class, name = "IsStringShorterThanConstraint"),
    @JsonSubTypes.Type(value = IsOfTypeConstraint.class, name = "IsOfTypeConstraint"),
    @JsonSubTypes.Type(value = NotConstraint.class, name = "NotConstraint"),
    @JsonSubTypes.Type(value = IsNullConstraint.class, name = "IsNullConstraint"),
    @JsonSubTypes.Type(value = IsLessThanConstantConstraint.class, name = "IsLessThanConstantConstraint")
})
public interface IConstraint
{
    static Collection<IConstraint> combine(IConstraint self, IConstraint[] others)
    {
        return Stream
            .concat(
                Stream.of(self),
                Stream.of(others))
            .collect(Collectors.toList());
    }

    String toDotLabel();

    Collection<Field> getFields();

    default IConstraint or(IConstraint... others)
    {
        return new OrConstraint(combine(this, others));
    }

    default IConstraint and(IConstraint... others)
    {
        return new AndConstraint(combine(this, others));
    }

    default IConstraint not()
    {
        if (this instanceof AtomicConstraint)
            return new AtomicNotConstraint((AtomicConstraint) this);

        return new NotConstraint(this);
    }
}
