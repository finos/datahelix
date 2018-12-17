package com.scottlogic.deg.generator.constraints.atomic;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.inputs.RuleInformation;

import java.util.Objects;
import java.util.Set;
import java.util.regex.Pattern;

public class MatchesRegexConstraint implements AtomicConstraint {
    public final Field field;
    public final Pattern regex;
    private final Set<RuleInformation> rules;

    public MatchesRegexConstraint(Field field, Pattern regex, Set<RuleInformation> rules) {
        this.field = field;
        this.regex = regex;
        this.rules = rules;
    }

    @Override
    public String toDotLabel(){
        return String.format("%s matches /%s/", field.name, regex);
    }

    @Override
    public Field getField() {
        return field;
    }

    @Override
    public boolean equals(Object o){
        if (this == o) return true;
        if (o instanceof ViolatedAtomicConstraint) {
            return o.equals(this);
        }
        if (o == null || getClass() != o.getClass()) return false;
        MatchesRegexConstraint constraint = (MatchesRegexConstraint) o;
        return Objects.equals(field, constraint.field) && Objects.equals(regex.toString(), constraint.regex.toString());
    }

    @Override
    public int hashCode(){
        return Objects.hash(field, regex.toString());
    }

    @Override
    public String toString(){ return String.format("`%s` matches /%s/", field.name, regex); }

    @Override
    public Set<RuleInformation> getRules() {
        return rules;
    }
}
