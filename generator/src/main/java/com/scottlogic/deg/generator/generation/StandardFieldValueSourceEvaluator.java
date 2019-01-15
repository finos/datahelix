package com.scottlogic.deg.generator.generation;

import com.scottlogic.deg.generator.constraints.atomic.IsOfTypeConstraint;
import com.scottlogic.deg.generator.fieldspecs.FieldSpec;
import com.scottlogic.deg.generator.generation.field_value_sources.*;
import com.scottlogic.deg.generator.restrictions.*;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StandardFieldValueSourceEvaluator implements FieldValueSourceEvaluator {
    private final MustContainRestrictionReducer mustContainRestrictionReducer = new MustContainRestrictionReducer();

    public Set<FieldValueSource> getFieldValueSources(FieldSpec fieldSpec){

        if (mustBeNull(fieldSpec)){
            return Collections.singleton(nullOnlySource());
        }

        if (fieldSpec.getSetRestrictions() != null && fieldSpec.getSetRestrictions().getWhitelist() != null) {
            return getWhitelistSources(fieldSpec);
        }

        Set<FieldValueSource> validSources = new LinkedHashSet<>();
        if (fieldSpec.getMustContainRestriction() != null) {
            applyMustConstrainRestrictionToValidSources(validSources, fieldSpec);
        }

        TypeRestrictions typeRestrictions = fieldSpec.getTypeRestrictions() != null
            ? fieldSpec.getTypeRestrictions()
            : DataTypeRestrictions.ALL_TYPES_PERMITTED;

        if (typeRestrictions.isTypeAllowed(IsOfTypeConstraint.Types.NUMERIC)) {
            NumericRestrictions restrictions = fieldSpec.getNumericRestrictions() == null
                ? new NumericRestrictions()
                : fieldSpec.getNumericRestrictions();

            int numericScale = fieldSpec.getGranularityRestrictions() != null
                ? fieldSpec.getGranularityRestrictions().getNumericScale()
                : 0;

            if (numericScale == 0) {
                validSources.add(
                    new IntegerFieldValueSource(
                        restrictions,
                        getBlacklist(fieldSpec)));
            } else {
                validSources.add(
                    new RealNumberFieldValueSource(
                        restrictions,
                        getBlacklist(fieldSpec),
                        numericScale));
            }
        }

        if (typeRestrictions.isTypeAllowed(IsOfTypeConstraint.Types.STRING)) {

            StringRestrictions stringRestrictions = fieldSpec.getStringRestrictions();
            if (stringRestrictions != null && (stringRestrictions.stringGenerator != null)) {
                Set<Object> blacklist = getBlacklist(fieldSpec);

                final StringGenerator generator;
                if (blacklist.size() > 0) {
                    RegexStringGenerator blacklistGenerator = RegexStringGenerator.createFromBlacklist(blacklist);

                    generator = stringRestrictions.stringGenerator.intersect(blacklistGenerator);
                } else {
                    generator = stringRestrictions.stringGenerator;
                }

                validSources.add(generator.asFieldValueSource());

            } else {
                // todo: move default interesting values into the string field value source
                validSources.add(CannedValuesFieldValueSource.of("Lorem Ipsum"));
            }
        }

        if (typeRestrictions.isTypeAllowed(IsOfTypeConstraint.Types.TEMPORAL)) {

            DateTimeRestrictions restrictions = fieldSpec.getDateTimeRestrictions();
            validSources.add(new TemporalFieldValueSource(
                restrictions != null ? restrictions : new DateTimeRestrictions(),
                getBlacklist(fieldSpec)));
        }

        if (mayBeNull(fieldSpec)){
            validSources.add(nullOnlySource());
        }

        return validSources;
    }

    private Set<FieldValueSource> getWhitelistSources(FieldSpec fieldSpec) {
        MustContainRestriction mustContainRestriction = fieldSpec.getMustContainRestriction();
        Stream<Object> whitelist = fieldSpec.getSetRestrictions().getWhitelist().stream();

        // If we have values that must be included we need to check that those values are included in the whitelist
        if (mustContainRestriction != null) {
            whitelist = Stream.concat(whitelist,
            getNotNullSetRestrictionFilterOnMustContainRestriction(mustContainRestriction)
                .flatMap(o -> o.getSetRestrictions().getWhitelist().stream()));
        }

        if (mayBeNull(fieldSpec)) {
            whitelist = Stream.concat(whitelist, Stream.of(null));
        }

        return Collections.singleton(new CannedValuesFieldValueSource(whitelist.collect(Collectors.toList())));
    }

    private boolean mayBeNull(FieldSpec fieldSpec) {
        return fieldSpec.getNullRestrictions() == null;
    }

    private boolean mustBeNull(FieldSpec fieldSpec) {
        return fieldSpec.getNullRestrictions() != null
            && fieldSpec.getNullRestrictions().nullness == Nullness.MUST_BE_NULL;
    }

    private CannedValuesFieldValueSource nullOnlySource() {
        return new CannedValuesFieldValueSource(Collections.singletonList(null));
    }

    private Set<Object> getBlacklist(FieldSpec fieldSpec) {
        if (fieldSpec.getSetRestrictions() == null)
            return Collections.emptySet();

        return new HashSet<>(fieldSpec.getSetRestrictions().getBlacklist());
    }

    private Stream<FieldSpec> getNotNullSetRestrictionFilterOnMustContainRestriction(MustContainRestriction restriction) {
        return restriction.getRequiredObjects()
            .stream()
            .filter(o -> o.getSetRestrictions() != null);
    }

    private void applyMustConstrainRestrictionToValidSources(Set<FieldValueSource> validSources, FieldSpec fieldSpec) {
        Set<FieldSpec> mustContainRestrictionFieldSpecs = fieldSpec.getMustContainRestriction().getRequiredObjects();
        if (mustContainRestrictionFieldSpecs.size() > 1) {
            mustContainRestrictionFieldSpecs = mustContainRestrictionReducer.getReducedMustContainRestriction(fieldSpec);
        }

        mustContainRestrictionFieldSpecs.forEach(fs -> validSources.addAll(getFieldValueSources(fs)));
    }
}
