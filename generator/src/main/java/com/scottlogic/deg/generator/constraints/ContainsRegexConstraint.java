package com.scottlogic.deg.generator.constraints;

import com.scottlogic.deg.generator.Field;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ContainsRegexConstraint implements IConstraint {
    public final Field field;
    public final Pattern regex;

    public ContainsRegexConstraint(Field field, Pattern regex) {
        this.field = field;
        this.regex = regex;
    }

    @Override
    public String toDotLabel() {
        return String.format("%s contains /%s/", field.name, regex);
    }

    @Override
    public Collection<Field> getFields() {
        return Collections.singletonList(field);
    }

    @Override
    public boolean equals(Object o){
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ContainsRegexConstraint constraint = (ContainsRegexConstraint) o;
        return Objects.equals(field, constraint.field) && Objects.equals(regex.toString(), constraint.regex.toString());
    }

    @Override
    public int hashCode(){
        return Objects.hash(field, regex.toString());
    }

    @Override
    public String toString() {
        return String.format("`%s` contains /%s/", field.name, regex);
    }
}
