package com.scottlogic.deg.generator.generation;

import com.scottlogic.deg.generator.constraints.atomic.IsOfTypeConstraint;
import com.scottlogic.deg.generator.fieldspecs.FieldSpec;
import com.scottlogic.deg.generator.generation.field_value_sources.*;
import com.scottlogic.deg.generator.restrictions.*;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StandardFieldValueSourceEvaluator implements FieldValueSourceEvaluator {
    private static final CannedValuesFieldValueSource nullOnlySource = new CannedValuesFieldValueSource(Collections.singletonList(null));

    private final MustContainRestrictionReducer mustContainRestrictionReducer = new MustContainRestrictionReducer();

    public List<FieldValueSource> getFieldValueSources(FieldSpec fieldSpec){

        if (mustBeNull(fieldSpec)){
            return Arrays.asList(nullOnlySource);
        }

        if (fieldSpec.getSetRestrictions() != null && fieldSpec.getSetRestrictions().getWhitelist() != null) {
            return getWhitelistSources(fieldSpec);
        }

        List<FieldValueSource> validSources = new ArrayList<>();

        if (fieldSpec.getMustContainRestriction() != null) {
            validSources.addAll(
                getMustContainRestrictionSources(fieldSpec));
        }

        TypeRestrictions typeRestrictions = fieldSpec.getTypeRestrictions() != null
            ? fieldSpec.getTypeRestrictions()
            : DataTypeRestrictions.ALL_TYPES_PERMITTED;

        if (typeRestrictions.isTypeAllowed(IsOfTypeConstraint.Types.NUMERIC)) {
            validSources.add(getNumericSource(fieldSpec));
        }

        if (typeRestrictions.isTypeAllowed(IsOfTypeConstraint.Types.STRING)) {
            validSources.add(getStringSource(fieldSpec));
        }

        if (typeRestrictions.isTypeAllowed(IsOfTypeConstraint.Types.TEMPORAL)) {
            validSources.add(getTemporalSource(fieldSpec));
        }

        if (mayBeNull(fieldSpec)){
            validSources.add(nullOnlySource);
        }

        return validSources;
    }

    private boolean mustBeNull(FieldSpec fieldSpec) {
        return fieldSpec.getNullRestrictions() != null
            && fieldSpec.getNullRestrictions().nullness == Nullness.MUST_BE_NULL;
    }

    private List<FieldValueSource> getWhitelistSources(FieldSpec fieldSpec) {
        MustContainRestriction mustContainRestriction = fieldSpec.getMustContainRestriction();
        Stream<Object> whitelist = fieldSpec.getSetRestrictions().getWhitelist().stream();

        // If we have values that must be included we need to check that those values are included in the whitelist
        if (mustContainRestriction != null) {
            whitelist = Stream.concat(whitelist,
            getNotNullSetRestrictionFilterOnMustContainRestriction(mustContainRestriction)
                .flatMap(o -> o.getSetRestrictions().getWhitelist().stream()));
        }

        if (mayBeNull(fieldSpec)) {
            return Arrays.asList(
                new CannedValuesFieldValueSource(whitelist.collect(Collectors.toList())),
                nullOnlySource);
        }

        return Arrays.asList(new CannedValuesFieldValueSource(whitelist.collect(Collectors.toList())));
    }

    private Stream<FieldSpec> getNotNullSetRestrictionFilterOnMustContainRestriction(MustContainRestriction restriction) {
        return restriction.getRequiredObjects()
            .stream()
            .filter(o -> o.getSetRestrictions() != null);
    }

    private boolean mayBeNull(FieldSpec fieldSpec) {
        return fieldSpec.getNullRestrictions() == null;
    }

    private List<FieldValueSource> getMustContainRestrictionSources(FieldSpec fieldSpec) {
        Set<FieldSpec> mustContainRestrictionFieldSpecs = fieldSpec.getMustContainRestriction().getRequiredObjects();
        if (mustContainRestrictionFieldSpecs.size() > 1) {
            mustContainRestrictionFieldSpecs = mustContainRestrictionReducer.getReducedMustContainRestriction(fieldSpec);
        }

        return mustContainRestrictionFieldSpecs.stream()
            .map(fs -> getFieldValueSources(fs))
            .flatMap(List::stream)
            .filter(x->!x.equals(nullOnlySource))
            .collect(Collectors.toList());
    }

    private FieldValueSource getNumericSource(FieldSpec fieldSpec) {
        NumericRestrictions restrictions = fieldSpec.getNumericRestrictions() == null
            ? new NumericRestrictions()
            : fieldSpec.getNumericRestrictions();

        int numericScale = fieldSpec.getGranularityRestrictions() != null
            ? fieldSpec.getGranularityRestrictions().getNumericScale()
            : 0;

        if (numericScale == 0) {
            return new IntegerFieldValueSource(
                restrictions,
                getBlacklist(fieldSpec));
        } else {
            return new RealNumberFieldValueSource(
                restrictions,
                getBlacklist(fieldSpec),
                numericScale);
        }
    }

    private Set<Object> getBlacklist(FieldSpec fieldSpec) {
        if (fieldSpec.getSetRestrictions() == null)
            return Collections.emptySet();

        return new HashSet<>(fieldSpec.getSetRestrictions().getBlacklist());
    }

    private FieldValueSource getStringSource(FieldSpec fieldSpec) {
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

            return generator.asFieldValueSource();

        } else {
            // todo: move default interesting values into the string field value source
            return CannedValuesFieldValueSource.of("Lorem Ipsum");
        }
    }

    private FieldValueSource getTemporalSource(FieldSpec fieldSpec) {
        DateTimeRestrictions restrictions = fieldSpec.getDateTimeRestrictions();

        return new TemporalFieldValueSource(
            restrictions != null ? restrictions : new DateTimeRestrictions(),
            getBlacklist(fieldSpec));
    }
}
