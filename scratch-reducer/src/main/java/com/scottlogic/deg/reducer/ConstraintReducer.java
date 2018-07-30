package com.scottlogic.deg.reducer;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class ConstraintReducer {
    private final FieldRestrictionFactory fieldRestrictionFactory = new FieldRestrictionFactory();

    public List<IFieldRestriction> getReducedConstraints(Iterable<IConstraint> constraints) {
        final List<IFieldRestriction> fieldRestrictions = new ArrayList<>();
        final Map<Field, List<IConstraint>> fieldConstraints = StreamSupport
                .stream(constraints.spliterator(), false)
                .collect(Collectors.groupingBy(IConstraint::getField));
        return fieldConstraints.entrySet().stream().map(x -> getReducedConstraints(x.getKey(), x.getValue())).collect(Collectors.toList());
    }

    public IFieldRestriction getReducedConstraints(Field field, Iterable<IConstraint> constraints) {
        IFieldRestriction fieldRestriction = null;
        for (IConstraint constraint : constraints) {
            if (fieldRestriction == null) {
                fieldRestriction = fieldRestrictionFactory.getForConstraint(field, constraint);
            }
        }
        return fieldRestriction;
    }

}

class Field {}

class NumericConstraintTypeClassifier {
    final Map<Class<? extends Number>, NumericConstraintType> typeMapping = new HashMap<>();
    {
        typeMapping.put(Integer.class, NumericConstraintType.Integer);
        typeMapping.put(Double.class, NumericConstraintType.Double);
    }

    public NumericConstraintType classify(IConstraint constraint) {
        if (!(constraint instanceof IHasNumericTypeToken)) {
            throw new IllegalStateException();
        }
        final IHasNumericTypeToken<?> hasNumericTypeToken = (IHasNumericTypeToken) constraint;
        if (!typeMapping.containsKey(hasNumericTypeToken.getTypeToken())) {
            throw new IllegalStateException();
        }
        return typeMapping.get(hasNumericTypeToken.getTypeToken());
    }
}

class ConstraintTypeClassifier {
    final Map<Class<? extends IConstraint>, ConstraintType> typeMapping = new HashMap<>();
    {
        typeMapping.put(NumericLimitConstConstraint.class, ConstraintType.Numeric);
    }

    public ConstraintType classify(IConstraint constraint) {
        if (!typeMapping.containsKey(constraint.getClass())) {
            throw new IllegalStateException();
        }
        return typeMapping.get(constraint);
    }
}

class FieldRestrictionFactory {
    private final ConstraintTypeClassifier constraintTypeClassifier = new ConstraintTypeClassifier();
    private final NumericFieldRestrictionFactory numericFieldRestrictionFactory = new NumericFieldRestrictionFactory();

    public IFieldRestriction getForConstraint(Field field, IConstraint constraint) {
        final ConstraintType constraintType = constraintTypeClassifier.classify(constraint);
        switch(constraintType) {
            case Numeric:
                return numericFieldRestrictionFactory.getForConstraint(field, constraint);
            case Abstract:
//                TODO
            default:
                throw new IllegalStateException();
        }
    }
}

class NumericFieldRestrictionFactory {
    private final NumericConstraintTypeClassifier numericConstraintTypeClassifier = new NumericConstraintTypeClassifier();

    public IFieldRestriction getForConstraint(Field field, IConstraint constraint) {
        final NumericConstraintType constraintType = numericConstraintTypeClassifier.classify(constraint);
        switch(constraintType) {
            case Integer:
                return new NumericFieldRestriction<Integer>(field);
            case Double:
                return new NumericFieldRestriction<Double>(field);
            default:
                throw new IllegalStateException();
        }
    }
}

enum ConstraintType {
    Numeric,
    Abstract
}

enum NumericConstraintType {
    Integer,
    Double
}

interface IFieldRestriction {}
interface IHasTypeToken<T> {
    Class<T> getTypeToken();
}
interface IHasNumericTypeToken<T extends Number> extends IHasTypeToken<T> {}
class NumericFieldRestriction<T extends Number> implements IFieldRestriction {
    private final Field field;

    public NumericFieldRestriction(Field field) {
        this.field = field;
    }

    public T min;
    public T max;
    public Set<T> among;
}

interface IConstraint {
    Field getField();
}
class NumericLimitConstConstraint<T extends Number> implements IConstraint, IHasNumericTypeToken<T> {
    private final Field field;
    private final T limit;
    private final LimitType limitType;
    private final Class<T> typeToken;

    enum LimitType { Min, Max }

    public NumericLimitConstConstraint(Field field, T limit, LimitType limitType, Class<T> typeToken) {
        this.field = field;
        this.limit = limit;
        this.limitType = limitType;
        this.typeToken = typeToken;
    }

    public Field getField() { return field; }

    public Class<T> getTypeToken() { return typeToken; }
}
class AmongConstraint<T> implements IConstraint, IHasTypeToken<T> {
    private final Field field;
    private final T limit;
    private final Set<T> among;
    private final Class<T> typeToken;

    public AmongConstraint(Field field, T limit, Set<T> among, Class<T> typeToken) {
        this.field = field;
        this.limit = limit;
        this.among = among;
        this.typeToken = typeToken;
    }

    public Field getField() { return field; }

    public Set<T> getAmong() {
        return among;
    }

    public Class<T> getTypeToken() { return typeToken; }
}