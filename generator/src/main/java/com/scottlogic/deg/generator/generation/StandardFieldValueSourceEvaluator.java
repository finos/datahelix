package com.scottlogic.deg.generator.generation;

import com.scottlogic.deg.generator.FlatMappingSpliterator;
import com.scottlogic.deg.generator.constraints.atomic.IsOfTypeConstraint;
import com.scottlogic.deg.generator.fieldspecs.FieldSpec;
import com.scottlogic.deg.generator.generation.fieldvaluesources.*;
import com.scottlogic.deg.generator.generation.fieldvaluesources.DateTime.DatetimeFieldValueSource;
import com.scottlogic.deg.generator.restrictions.*;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StandardFieldValueSourceEvaluator implements FieldValueSourceEvaluator {
    private static final CannedValuesFieldValueSource nullOnlySource = new CannedValuesFieldValueSource(Collections.singletonList(null));

    public List<FieldValueSource> getFieldValueSources(FieldSpec fieldSpec){

        if (mustBeNull(fieldSpec)){
            return Collections.singletonList(nullOnlySource);
        }

        if (fieldSpec.getSetRestrictions() != null && fieldSpec.getSetRestrictions().getWhitelist() != null) {
            return getSetRestrictionSources(fieldSpec);
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

        if (typeRestrictions.isTypeAllowed(IsOfTypeConstraint.Types.DATETIME)) {
            validSources.add(getDatetimeSource(fieldSpec));
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

    private List<FieldValueSource> getSetRestrictionSources(FieldSpec fieldSpec) {
        Stream<Object> whitelist = fieldSpec.getSetRestrictions().getWhitelist().stream();

        if (mayBeNull(fieldSpec)) {
            return Arrays.asList(
                new CannedValuesFieldValueSource(whitelist.collect(Collectors.toList())),
                nullOnlySource);
        }

        return Collections.singletonList(new CannedValuesFieldValueSource(whitelist.collect(Collectors.toList())));
    }

    private boolean mayBeNull(FieldSpec fieldSpec) {
        return fieldSpec.getNullRestrictions() == null;
    }

    private List<FieldValueSource> getMustContainRestrictionSources(FieldSpec fieldSpec) {
        Set<FieldSpec> mustContainRestrictionFieldSpecs = fieldSpec.getMustContainRestriction().getRequiredObjects();

        return FlatMappingSpliterator.flatMap(mustContainRestrictionFieldSpecs.stream()
            .map(this::getFieldValueSources),
            List::stream)
            .distinct()
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

    private FieldValueSource getDatetimeSource(FieldSpec fieldSpec) {
        DateTimeRestrictions restrictions = fieldSpec.getDateTimeRestrictions();

        return new DatetimeFieldValueSource(
            restrictions != null ? restrictions : new DateTimeRestrictions(),
            getBlacklist(fieldSpec));
    }
}
