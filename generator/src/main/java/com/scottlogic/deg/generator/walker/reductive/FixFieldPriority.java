package com.scottlogic.deg.generator.walker.reductive;

import com.scottlogic.deg.generator.Field;

public class FixFieldPriority implements Comparable<FixFieldPriority> {

    private Field field;
    private double priority;

    FixFieldPriority(Field field, double priority) {
        this.field = field;
        this.priority = priority;
    }

    public Field getField() {
        return field;
    }

    public double getPriority() {
        return priority;
    }

    @Override
    public int compareTo(FixFieldPriority ffp) {
        int priorityComparison = Double.compare(ffp.priority, this.priority);
        return priorityComparison == 0 ? this.field.name.compareTo(ffp.field.name) : priorityComparison;
    }

    @Override
    public String toString() {
        return String.format("%s : %.2f", field, priority);
    }
}
