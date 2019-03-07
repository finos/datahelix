package com.scottlogic.deg.generator.generation;

public class FilterAfterFirst<T> {

    private boolean previouslyFound = false;
    private T filterValue;

    public FilterAfterFirst(T filterValue) {
        this.filterValue = filterValue;
    }

    public boolean getPredicate(T field) {
        if (!field.equals(filterValue)) {
            return true;
        }

        if (previouslyFound) {
            return false;
        }

        previouslyFound = true;
        return true;
    }
}
