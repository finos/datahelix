package com.scottlogic.deg.generator.fieldspecs.whitelist;

import java.util.Objects;

public class WeightedElement<E> {

    private final E element;

    private final float weight;

    public WeightedElement(final E element, final float weight) {
        this.element = element;
        this.weight = weight;
    }

    public E element() {
        return element;
    }

    public float weight() {
        return weight;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WeightedElement<?> that = (WeightedElement<?>) o;
        return Float.compare(that.weight, weight) == 0 &&
            Objects.equals(element, that.element);
    }

    @Override
    public int hashCode() {
        return Objects.hash(element, weight);
    }

    @Override
    public String toString() {
        return "WeightedElement{" +
            "element=" + element +
            ", weight=" + weight +
            '}';
    }
}
