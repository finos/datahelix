package com.scottlogic.deg.generator.fieldspecs.whitelist;

import java.util.Objects;

public class ElementFrequency<E> {

    private final E element;

    private final float frequency;

    public ElementFrequency(final E element, final float frequency) {
        this.element = element;
        this.frequency = frequency;
    }

    public E element() {
        return element;
    }

    public float frequency() {
        return frequency;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ElementFrequency<?> that = (ElementFrequency<?>) o;
        return Float.compare(that.frequency, frequency) == 0 &&
            Objects.equals(element, that.element);
    }

    @Override
    public int hashCode() {
        return Objects.hash(element, frequency);
    }
}
