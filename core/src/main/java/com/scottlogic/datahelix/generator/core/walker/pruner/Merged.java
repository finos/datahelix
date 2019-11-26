/*
 * Copyright 2019 Scott Logic Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.scottlogic.datahelix.generator.core.walker.pruner;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * Generic merged object that may be contradictory.
 * This would be nicer if it extended optional, but optional is final
 */
public class Merged<T> {
    private final T value;


    /**
     * Returns an {@code Merged} with the specified present non-null value.
     *
     * @param <T> the class of the value
     * @param value the value to be present, which must be non-null
     * @return an {@code Merged} with the value present
     * @throws NullPointerException if value is null
     */
    public static <T> Merged<T> of(T value) {
        return new Merged<>(value);
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
     * If a value not Contradictory in this {@code Merged}, returns the value,
     * otherwise throws {@code NoSuchElementException}.
     *
     * @return the non-null value held by this {@code Optional}
     * @throws NoSuchElementException if is Contradictory
     *
     * @see Merged#isContradictory() ()
     */
    public T get() {
        if (value == null) {
            throw new NoSuchElementException("No value present");
        }
        return value;
    }

    /**
     * Returns a Contradictory {@code Merged} instance.  No value is present for this
     * Merged.
     *
     * @param <T> Type of the non-existent value
     * @return a Contradictory {@code Merged}
     */
    public static<T> Merged<T> contradictory() {
        return new Merged<>();
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

    private Merged(T value) {
        this.value = Objects.requireNonNull(value);
    }
    private Merged() {
        this.value = null;
    }
}
