package com.scottlogic.deg.generator.constraints;

import com.scottlogic.deg.schemas.v3.RuleDTO;

import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;

public class ConstraintRule{
    private final String description;
    private final boolean violated;

    public ConstraintRule(RuleDTO rule) {
        this.description = rule.rule;
        this.violated = false;
    }

    private ConstraintRule(String description, boolean violated) {
        this.description = description;
        this.violated = violated;
    }

    private static ConstraintRule fromRules(Collection<ConstraintRule> rules, String delimiter){
        return new ConstraintRule(
            String.join(
                delimiter,
                rules.stream().map(r -> r.description).collect(Collectors.toList())),
            rules.stream().anyMatch(r -> r.violated)
        );
    }

    public static ConstraintRule fromConstraints(Collection<Constraint> constraints, String delimiter){
        return fromRules(constraints.stream().map(Constraint::getRule).collect(Collectors.toList()), delimiter);
    }

    public static ConstraintRule fromDescription(String rule) {
        return new ConstraintRule(rule, false);
    }

    public ConstraintRule violate(){
        return new ConstraintRule(this.description, true);
    }

    public String getDescription() {
        return description;
    }

    public boolean isViolated() {
        return violated;
    }

    @Override
    public String toString() {
        return this.violated
            ? String.format("Violated: %s", this.description)
            : String.format("Valid: %s", this.description);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConstraintRule that = (ConstraintRule) o;
        return violated == that.violated &&
            Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(description, violated);
    }
}
