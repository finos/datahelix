package com.scottlogic.deg.generator.constraints.atomic;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.inputs.validation.ProfileVisitor;
import com.scottlogic.deg.generator.inputs.validation.VisitableProfileElement;
import com.scottlogic.deg.generator.restrictions.NullRestrictions;
import com.scottlogic.deg.generator.restrictions.Nullness;

import java.util.Objects;

public class IsNullConstraint implements AtomicConstraint, VisitableProfileElement
{
    public final Field field;

    public IsNullConstraint(Field field) {
        this.field = field;
    }

    @Override
    public String toDotLabel() {
        return String.format("%s is null", field.name);
    }

    public String toString(){
        return String.format(
                "`%s`: %s",
                Nullness.MUST_BE_NULL,
                field.toString());
    }

    @Override
    public Field getField() {
        return field;
    }

    @Override
    public boolean equals(Object o){
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IsNullConstraint constraint = (IsNullConstraint) o;
        return Objects.equals(field, constraint.field);
    }

    @Override
    public int hashCode(){
        return Objects.hash(field);
    }

    @Override
    public void accept(ProfileVisitor visitor) {
        visitor.visit(this  );
    }
}
