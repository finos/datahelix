package com.scottlogic.deg.generator.constraints;

import com.scottlogic.deg.generator.Field;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    Field getField();

    default public IConstraint or(IConstraint... others)
    {
        return new OrConstraint(combine(this, others));
    }

    default public IConstraint and(IConstraint... others)
    {
        return new AndConstraint(combine(this, others));
    }

    default public IConstraint isFalse()
    {
        return new NotConstraint(this);
    }
}
