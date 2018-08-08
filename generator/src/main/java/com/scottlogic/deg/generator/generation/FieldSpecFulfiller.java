package com.scottlogic.deg.generator.generation;

import com.scottlogic.deg.generator.restrictions.FieldSpec;
import com.scottlogic.deg.generator.restrictions.NullRestrictions;
import com.scottlogic.deg.generator.restrictions.NumericRestrictions;
import com.scottlogic.deg.generator.utils.LimitingIteratorDecorator;
import dk.brics.automaton.Automaton;
import dk.brics.automaton.RegExp;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;


interface IDataPointSource {
    Iterator<Object> iterator(GenerationConfig config);
}

class FieldSpecFulfiller implements IDataPointSource {
    private final FieldSpec spec;

    FieldSpecFulfiller(FieldSpec spec) {
        this.spec = spec;
    }

    @Override
    public Iterator<Object> iterator(GenerationConfig config) {
        IFieldSpecIterator internalIterator = getSpecialisedInternalIterator(config);

        if (internalIterator.isInfinite() && config.shouldChooseFiniteSampling())
            return new LimitingIteratorDecorator<>(internalIterator, 1);

        return internalIterator;
    }

    private IFieldSpecIterator getSpecialisedInternalIterator(GenerationConfig config) {
        if (spec.getNullRestrictions() != null &&
                spec.getNullRestrictions().nullness == NullRestrictions.Nullness.MustBeNull)
            return new NullFulfilmentIterator();

        if (spec.getSetRestrictions() != null) {
            Set<?> whitelist = spec.getSetRestrictions().getReconciledWhitelist();
            if (whitelist != null) {
                return config.shouldEnumerateSetsExhaustively()
                    ? new SetMembershipIterator(whitelist.iterator())
                    : new SingleObjectIterator(whitelist.iterator().next());
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

        // no restrictions, just output some random bits of data
        return new SpecificDataPointsIterator(null, "string", 123, true);
    }

    private Set<Object> getBlacklist() {
        if (spec.getSetRestrictions() != null)
            return new HashSet<>(spec.getSetRestrictions().blacklist);
        return null;
    }

    private IFieldSpecIterator simplifyStringIterator(StringIterator stringIterator) {
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

