package com.scottlogic.deg.generator.inputs;

import java.util.Objects;

public class RuleInformation {
    private final String description;

    public RuleInformation(String rule) {
        this.description = rule != null ? rule : "Unnamed rule";
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
