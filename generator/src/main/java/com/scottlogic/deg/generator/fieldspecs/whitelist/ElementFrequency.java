package com.scottlogic.deg.generator.fieldspecs.whitelist;

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


}
