package com.scottlogic.deg.generator.walker.reductive;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Consumer;

public class Combined<T> {

    private final T value;


    /**
     * Returns an {@code Unptional} with the specified present non-null value.
     *
     * @param <T> the class of the value
     * @param value the value to be present, which must be non-null
     * @return an {@code Unptional} with the value present
     * @throws NullPointerException if value is null
     */
    public static <T> Combined<T> of(T value) {
        return new Combined<>(value);
    }


    /**
     * Return {@code true} if there is no value present, otherwise {@code false}.
     *
     * @return {@code true} if there is no value present, otherwise {@code false}
     */
    public boolean isContradictory() {
        return value == null;
    }

    /**
     * If a value not Contradictory in this {@code Unptional}, returns the value,
     * otherwise throws {@code NoSuchElementException}.
     *
     * @return the non-null value held by this {@code Optional}
     * @throws NoSuchElementException if is Contradictory
     *
     * @see Combined#isContradictory() ()
     */
    public T get() {
        if (value == null) {
            throw new NoSuchElementException("No value present");
        }
        return value;
    }

    /**
     * Returns a Contradictory {@code Unptional} instance.  No value is present for this
     * Unptional.
     *
     * @param <T> Type of the non-existent value
     * @return a Contradictory {@code Unptional}
     */
    public static<T> Combined<T> contradictory() {
        @SuppressWarnings("unchecked")
        Combined<T> t = (Combined<T>) CONTRADICTORY;
        return t;
    }

    /**
     * If a value is present, invoke the specified consumer with the value,
     * otherwise do nothing.
     *
     * @param consumer block to be executed if a value is present
     * @throws NullPointerException if value is present and {@code consumer} is
     * null
     */
    public void ifPresent(Consumer<? super T> consumer) {
        if (value != null)
            consumer.accept(value);
    }

    private Combined(T value) {
        this.value = Objects.requireNonNull(value);
    }

    private static final Combined<?> CONTRADICTORY = new Combined<>();
    private Combined() {
        this.value = null;
    }
}
