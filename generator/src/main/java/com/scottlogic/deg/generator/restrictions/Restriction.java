package com.scottlogic.deg.generator.restrictions;

import java.util.Objects;
import java.util.function.BiFunction;

/**
 * A restriction that can be considered hard (from the profile) or soft (implied/default)
 *
 * @param <T>
 */
public class Restriction<T> {
    private final T value;
    private final boolean isSoftRestriction;

    public Restriction(T value, boolean isSoftRestriction) {
        this.value = value;
        this.isSoftRestriction = isSoftRestriction;
    }

    /**
     * Merge this restriction with another, using <mergeFunc> to resolve the merge if 2 restrictions are found
     *
     * @param other The other restriction to consider
     * @param mergeFunc A means of resolving the value from both restrictions
     * @return A restriction which has been merged appropriately
     */
    public Restriction<T> merge(Restriction<T> other, BiFunction<T, T, T> mergeFunc){
        if (other == null){
            return this;
        }

        return isSoftRestriction
            ? mergeAsSoftRestriction(other, mergeFunc)
            : mergeAsHardRestriction(other, mergeFunc);
    }

    /**
     * This restriction is a hard restriction, merge with the other restriction appropriately
     *
     * @param other
     * @param mergeFunc
     * @return this restriction if other is soft, otherwise a hard-restriction with a merged result
     */
    private Restriction<T> mergeAsHardRestriction(Restriction<T> other, BiFunction<T, T, T> mergeFunc) {
        if (other.isSoftRestriction){
            return this;
        }

        return new Restriction<>(mergeFunc.apply(this.value, other.value), false);
    }

    /**
     * This restriction is a soft restriction, merge with the other restriction appropriately
     *
     * @param other
     * @param mergeFunc
     * @return the other restriction if it is hard, otherwise a soft-restriction with a merged result
     */
    private Restriction<T> mergeAsSoftRestriction(Restriction<T> other, BiFunction<T, T, T> mergeFunc) {
        if (!other.isSoftRestriction){
            return other;
        }

        return new Restriction<>(mergeFunc.apply(this.value, other.value), true);
    }

    public T getValue() {
        return value;
    }

    public boolean equals(Object other){
        if (!(other instanceof Restriction)){
            return false;
        }

        Restriction otherRestriction = (Restriction) other;
        return ((value == null && otherRestriction.value == null) || (value != null && value.equals(otherRestriction.value)))
            && isSoftRestriction == otherRestriction.isSoftRestriction;
    }

    public int hashCode(){
        return Objects.hash(value, isSoftRestriction);
    }
}
