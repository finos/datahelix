package com.scottlogic.deg.generator.generation;

import com.scottlogic.deg.generator.generation.iterators.*;
import com.scottlogic.deg.generator.restrictions.FieldSpec;
import com.scottlogic.deg.generator.restrictions.NullRestrictions;
import com.scottlogic.deg.generator.utils.FilteringIterator;
import com.scottlogic.deg.generator.utils.LimitingIteratorDecorator;
import com.scottlogic.deg.generator.utils.ValuePrependingIterator;

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

        Iterator<Object> potentiallyLimitedIterator =
            internalIterator.isInfinite() && config.shouldChooseFiniteSampling()
            ? // the field's infinite and we're configured to sample from infinite sequences
                new LimitingIteratorDecorator<>(internalIterator, 1)
            : internalIterator;

        return this.spec.getNullRestrictions() == null || this.spec.getNullRestrictions().nullness == null
            ? // the field could be null; output one at the start of the sequence and filter any out from later
                new ValuePrependingIterator<>(
                    new FilteringIterator<>(potentiallyLimitedIterator, null), null)
            : potentiallyLimitedIterator;
    }

    private IFieldSpecIterator getSpecialisedInternalIterator(GenerationConfig config) {
        if (spec.getNullRestrictions() != null &&
                spec.getNullRestrictions().nullness == NullRestrictions.Nullness.MustBeNull) {
            return new SpecificDataPointsIterator(null);
        }

        if (spec.getSetRestrictions() != null) {
            Set<?> whitelist = spec.getSetRestrictions().getReconciledWhitelist();
            if (whitelist != null) {
                return config.shouldEnumerateSetsExhaustively()
                    ? new SetMembershipIterator(whitelist.iterator())
                    : new SpecificDataPointsIterator(whitelist.iterator().next());
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
        return new SpecificDataPointsIterator("string", 123, true);
    }

    private Set<Object> getBlacklist() {
        if (spec.getSetRestrictions() != null)
            return new HashSet<>(spec.getSetRestrictions().blacklist);
        return null;
    }

    private IFieldSpecIterator simplifyStringIterator(StringIterator stringIterator) {
        if (stringIterator.hasNext())
            return new SpecificDataPointsIterator(stringIterator.next());
        return new UnfulfillableIterator();
    }
}
