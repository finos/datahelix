package com.scottlogic.deg.generator.generation;

import com.scottlogic.deg.generator.generation.iterators.*;
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

public class FieldSpecFulfiller implements IDataPointSource {
    private final FieldSpec spec;

    public FieldSpecFulfiller(FieldSpec spec) {
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
}
