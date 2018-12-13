package com.scottlogic.deg.generator.inputs;

import com.scottlogic.deg.generator.constraints.Constraint;
import com.scottlogic.deg.schemas.v3.RuleDTO;

import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;

public class RuleInformation {
    private final String description;

    public RuleInformation(RuleDTO rule) {
        this.description = rule.rule;
    }

    private RuleInformation(String description) {
        this.description = description;
    }

    private static RuleInformation fromRules(Collection<RuleInformation> rules, String delimiter){
        return new RuleInformation(
            String.join(
                delimiter,
                rules.stream().map(r -> r.description).collect(Collectors.toList()))
        );
    }

    public static RuleInformation fromConstraints(Collection<Constraint> constraints, String delimiter){
        return fromRules(constraints.stream().map(Constraint::getRule).collect(Collectors.toList()), delimiter);
    }

    public static RuleInformation fromDescription(String rule) {
        return new RuleInformation(rule);
    }

    public String getDescription() {
        return this.description;
    }

    @Override
    public String toString() {
        return this.description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RuleInformation that = (RuleInformation) o;
        return Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return description.hashCode();
    }
}
