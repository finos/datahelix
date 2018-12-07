package com.scottlogic.deg.generator.constraints;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.inputs.InvalidProfileException;
import com.scottlogic.deg.generator.inputs.visitor.IConstraintValidatorVisitor;
import com.scottlogic.deg.generator.inputs.visitor.ValidationAlert;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class IsOfTypeConstraint implements IConstraint {
    public final Field field;
    public final Types requiredType;

    public IsOfTypeConstraint(Field field, Types requiredType) {
        this.field = field;
        this.requiredType = requiredType;
    }

    public enum Types {
        NUMERIC,
        STRING,
        TEMPORAL
    }

    @Override
    public String toDotLabel() {
        return String.format("%s is %s", field.name, requiredType.name());
    }

    @Override
    public Collection<Field> getFields() {
        return Collections.singletonList(field);
    }

    @Override
    public List<ValidationAlert> accept(IConstraintValidatorVisitor visitor) {
       return visitor.visit(this);
    }

    @Override
    public boolean equals(Object o){
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IsOfTypeConstraint constraint = (IsOfTypeConstraint) o;
        return Objects.equals(field, constraint.field) && Objects.equals(requiredType, constraint.requiredType);
    }

    @Override
    public int hashCode(){
        return Objects.hash(field, requiredType);
    }

    @Override
    public String toString() { return String.format("`%s` is %s", field.name, requiredType.name()); }
}
