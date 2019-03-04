package com.scottlogic.deg.generator.generation;

import com.scottlogic.deg.generator.FlatMappingSpliterator;
import com.scottlogic.deg.generator.constraints.atomic.IsOfTypeConstraint;
import com.scottlogic.deg.generator.fieldspecs.FieldSpec;
import com.scottlogic.deg.generator.generation.fieldvaluesources.*;
import com.scottlogic.deg.generator.restrictions.*;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StandardFieldValueSourceEvaluator implements FieldValueSourceEvaluator {
    private static final CannedValuesFieldValueSource nullOnlySource = new CannedValuesFieldValueSource(Collections.singletonList(null));

    private final MustContainRestrictionReducer mustContainRestrictionReducer = new MustContainRestrictionReducer();

    public List<FieldValueSource> getFieldValueSources(FieldSpec fieldSpec){

        if (mustBeNull(fieldSpec)){
            return Collections.singletonList(nullOnlySource);
        }

        if (fieldSpec.getSetRestrictions() != null && fieldSpec.getSetRestrictions().getWhitelist() != null) {
            return getWhitelistSources(fieldSpec);
        }

        List<FieldValueSource> validSources = new ArrayList<>();

        if (fieldSpec.getMustContainRestriction() != null && !fieldSpec.getMustContainRestriction().getRequiredObjects().isEmpty()) {
            List<FieldValueSource> mustContainRestrictionSources = getMustContainRestrictionSources(fieldSpec);
            if (!mustContainRestrictionSources.isEmpty()){
                return mustContainRestrictionSources;
            }
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
                FlatMappingSpliterator.flatMap(getNotNullSetRestrictionFilterOnMustContainRestriction(mustContainRestriction),
                    o -> o.getSetRestrictions().getWhitelist().stream()));
        }

        if (mayBeNull(fieldSpec)) {
            return Arrays.asList(
                new CannedValuesFieldValueSource(whitelist.collect(Collectors.toList())),
                nullOnlySource);
        }

        return Collections.singletonList(new CannedValuesFieldValueSource(whitelist.collect(Collectors.toList())));
    }

    private Stream<FieldSpec> getNotNullSetRestrictionFilterOnMustContainRestriction(MustContainRestriction restriction) {
        return restriction.getRequiredObjects()
            .stream()
            .filter(o -> o.getSetRestrictions() != null
                && o.getSetRestrictions().getWhitelist() != null);
    }

    private boolean mayBeNull(FieldSpec fieldSpec) {
        return fieldSpec.getNullRestrictions() == null;
    }

    private List<FieldValueSource> getMustContainRestrictionSources(FieldSpec fieldSpec) {
        Set<FieldSpec> mustContainRestrictionFieldSpecs = fieldSpec.getMustContainRestriction().getRequiredObjects();
        if (mustContainRestrictionFieldSpecs.size() > 1) {
            //mustContainRestrictionFieldSpecs = mustContainRestrictionReducer.getReducedMustContainRestriction(fieldSpec);
            //TODO paul WHAT AND WHY
        }

        return FlatMappingSpliterator.flatMap(mustContainRestrictionFieldSpecs.stream()
            .map(this::getFieldValueSources),
            List::stream)
            .filter(x->!x.equals(nullOnlySource))
            .collect(Collectors.toList());
    }

    private FieldValueSource getNumericSource(FieldSpec fieldSpec) {
        NumericRestrictions restrictions = fieldSpec.getNumericRestrictions() == null
            ? new NumericRestrictions()
            : fieldSpec.getNumericRestrictions();

        int numericScale = getNumericScale(restrictions, fieldSpec.getGranularityRestrictions());

        if ((restrictions.min == null && restrictions.max == null) || isFieldValueAnInteger(restrictions, numericScale)) {
            return new IntegerFieldValueSource(
                restrictions,
                getBlacklist(fieldSpec));
        }

        return new RealNumberFieldValueSource(
            restrictions,
            getBlacklist(fieldSpec),
            numericScale);
    }

    private int getNumericScale(NumericRestrictions numericRestrictions, GranularityRestrictions granularityRestrictions) {
        if (granularityRestrictions != null) {
            return granularityRestrictions.getNumericScale();
        }

        if (numericRestrictions.min != null && numericRestrictions.max != null) {
            return Math.max(numericRestrictions.min.getLimit().scale(), numericRestrictions.max.getLimit().scale());
        }

        NumericLimit<BigDecimal> numericLimit = numericRestrictions.min != null
            ? numericRestrictions.min
            : numericRestrictions.max;

        return numericLimit != null ? numericLimit.getLimit().scale() : 0;
    }

    private boolean isFieldValueAnInteger(NumericRestrictions numericRestrictions, int numericScale) {
        if (numericScale > 0) {
            return false;
        }

        return numericRestrictions.areLimitValuesInteger();
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
