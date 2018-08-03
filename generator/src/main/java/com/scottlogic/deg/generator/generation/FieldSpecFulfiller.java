package com.scottlogic.deg.generator.generation;

import com.scottlogic.deg.generator.generation.tmpReducerOutput.FieldSpec;
import com.scottlogic.deg.generator.generation.tmpReducerOutput.NullRestrictions;
import com.scottlogic.deg.generator.generation.tmpReducerOutput.NumericRestrictions;
import dk.brics.automaton.Automaton;
import dk.brics.automaton.RegExp;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
            return simplifyStringIterator(stringIterator);
        }

        if (spec.getStringRestrictions() != null && spec.getStringRestrictions().automaton != null &&
                spec.getNumericRestrictions() != null &&
                (spec.getNumericRestrictions().min != null || spec.getNumericRestrictions().max != null)) {
            Automaton combinedAutomaton = spec.getStringRestrictions().automaton
                    .intersection(getNumericAutomaton(spec.getNumericRestrictions()));
            StringIterator iterator = new StringIterator(combinedAutomaton, getBlacklist());
            return simplifyStringIterator(iterator);
        }

        return new FieldSpecFulfilmentIterator(spec);
    }

    private Set<Object> getBlacklist() {
        if (spec.getSetRestrictions() != null)
            return spec.getSetRestrictions().blacklist;
        return null;
    }

    private Iterator<Object> simplifyStringIterator(StringIterator stringIterator) {
        if (stringIterator.hasNext())
            return new SingleObjectIterator(stringIterator.next());
        return new UnfulfillableIterator();
    }

    private Automaton getNumericAutomaton(NumericRestrictions restrictions) {
        if (restrictions.min != null && restrictions.max != null) {
            return getNumericRangeAutomaton(restrictions);
        }
        if (restrictions.max != null) {
            return getNumericLessThanAutomaton(restrictions.max);
        }
        if (restrictions.min != null) {
            return getNumericGreaterThanAutomaton(restrictions.min);
        }
        return getNumericUnboundedAutomaton();
    }

    private Automaton getNumericDecimalAutomaton() {
        // matches either the empty string, or dot followed by one or more digits.
        return new RegExp("()|\\.[0-9]+").toAutomaton();
    }

    private Automaton getNumericUnboundedAutomaton() {
        return new RegExp("[1-9][0-9]*").toAutomaton().concatenate(getNumericDecimalAutomaton());
    }

    private Automaton getNumericLessThanAutomaton(NumericRestrictions.NumericLimit limit) {
        BigDecimal roundedLimit = limit.getLimit().setScale(0, RoundingMode.FLOOR);
        return Automaton.makeMaxInteger(Integer.toString(roundedLimit.intValue()))
                .concatenate(getNumericDecimalAutomaton());
    }

    private Automaton getNumericGreaterThanAutomaton(NumericRestrictions.NumericLimit limit) {
        BigDecimal roundedLimit = limit.getLimit().setScale(0, RoundingMode.CEILING);
        Automaton automaton = Automaton.makeMinInteger(Integer.toString(roundedLimit.intValue()))
                .concatenate(getNumericDecimalAutomaton());
        return automaton;
    }

    private Automaton getNumericRangeAutomaton(NumericRestrictions restrictions) {
        BigDecimal roundedMin = restrictions.min.getLimit().setScale(0, RoundingMode.CEILING);
        BigDecimal roundedMax = restrictions.max.getLimit().setScale(0, RoundingMode.FLOOR);
        return Automaton.makeInterval(roundedMin.intValue(), roundedMax.intValue(), 0)
                .concatenate(getNumericDecimalAutomaton());
    }
}
