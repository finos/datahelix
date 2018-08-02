package com.scottlogic.deg.generator.generation;

import com.scottlogic.deg.generator.generation.tmpReducerOutput.FieldSpec;
import com.scottlogic.deg.generator.generation.tmpReducerOutput.NullRestrictions;

import java.util.Iterator;
import java.util.Set;

class FieldSpecFulfiller implements Iterable<Object> {
    private final FieldSpec spec;
    private final GenerationStrategy strategy;

    FieldSpecFulfiller(FieldSpec spec, GenerationStrategy strategy) {
        this.spec = spec;
        this.strategy = strategy;
    }

    @Override
    public Iterator<Object> iterator() {
        if (spec.getNullRestrictions() != null &&
                spec.getNullRestrictions().nullness == NullRestrictions.Nullness.MustBeNull)
            return new NullFulfilmentIterator();

        if (spec.getSetRestrictions() != null) {
            Set<?> whitelist = spec.getSetRestrictions().getReconciledWhitelist();
            if (whitelist != null) {
                if (strategy == GenerationStrategy.Exhaustive)
                    return new SetMembershipIterator(whitelist.iterator());
                return new SingleObjectIterator(whitelist.iterator().next());
            }
        }

        if (spec.getNumericRestrictions() != null &&
                (spec.getNumericRestrictions().min != null || spec.getNumericRestrictions().max != null) &&
                (spec.getStringRestrictions() == null || spec.getStringRestrictions().automaton == null))
            return new NumericIterator(spec.getNumericRestrictions(), getBlacklist());

        if (spec.getStringRestrictions() != null && spec.getStringRestrictions().automaton != null &&
                (spec.getNumericRestrictions() == null ||
                        (spec.getNumericRestrictions().max == null && spec.getNumericRestrictions().min == null))) {
            StringIterator stringIterator = new StringIterator(spec.getStringRestrictions().automaton, getBlacklist());
            if (stringIterator.hasNext())
                return new SingleObjectIterator(stringIterator.next());
            else
                return new UnfulfillableIterator();
        }
        return new FieldSpecFulfilmentIterator(spec);
    }

    private Set<Object> getBlacklist() {
        if (spec.getSetRestrictions() != null)
            return spec.getSetRestrictions().blacklist;
        return null;
    }
}
