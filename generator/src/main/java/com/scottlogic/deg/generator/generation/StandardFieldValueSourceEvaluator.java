package com.scottlogic.deg.generator.generation;

import com.scottlogic.deg.generator.FlatMappingSpliterator;
import com.scottlogic.deg.generator.constraints.atomic.IsOfTypeConstraint;
import com.scottlogic.deg.generator.fieldspecs.FieldSpec;
import com.scottlogic.deg.generator.generation.fieldvaluesources.*;
import com.scottlogic.deg.generator.generation.fieldvaluesources.datetime.DateTimeFieldValueSource;
import com.scottlogic.deg.generator.restrictions.*;
import org.jetbrains.annotations.NotNull;

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
            List<FieldValueSource> setRestrictionSources = getSetRestrictionSources(fieldSpec.getSetRestrictions().getWhitelist());
            if (mayBeNull(fieldSpec)){
                return addNullSource(setRestrictionSources);
            }
            return setRestrictionSources;
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
            validSources.add(getDateTimeSource(fieldSpec));
        }

        if (mayBeNull(fieldSpec)){
            validSources.add(nullOnlySource);
        }

        return validSources;
    }

    private List<FieldValueSource> addNullSource(List<FieldValueSource> setRestrictionSources) {
        return Stream.concat(setRestrictionSources.stream(), Stream.of(nullOnlySource)).collect(Collectors.toList());
    }

    private boolean mustBeNull(FieldSpec fieldSpec) {
        return fieldSpec.getNullRestrictions() != null
            && fieldSpec.getNullRestrictions().nullness == Nullness.MUST_BE_NULL;
    }

    private List<FieldValueSource> getSetRestrictionSources(@NotNull Set<Object> whitelist) {
        if (whitelist.isEmpty()){
            return Collections.emptyList();
        }

        return Collections.singletonList(
            new CannedValuesFieldValueSource(
                new ArrayList<>(whitelist)));
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

        return new RealNumberFieldValueSource(
            restrictions,
            getBlacklist(fieldSpec));
    }

    private Set<Object> getBlacklist(FieldSpec fieldSpec) {
        if (fieldSpec.getSetRestrictions() == null)
            return Collections.emptySet();

        return new HashSet<>(fieldSpec.getSetRestrictions().getBlacklist());
    }

    private FieldValueSource getStringSource(FieldSpec fieldSpec) {
        StringRestrictions stringRestrictions = fieldSpec.getStringRestrictions();

        if (stringRestrictions == null) {
            return new CannedValuesFieldValueSource(Collections.emptyList());
        }

        Set<Object> blacklist = getBlacklist(fieldSpec);

        StringGenerator generator = stringRestrictions.createGenerator();
        if (blacklist.size() > 0) {
            RegexStringGenerator blacklistGenerator = RegexStringGenerator.createFromBlacklist(blacklist);

            generator = generator.intersect(blacklistGenerator);
        }

        return generator.asFieldValueSource();
    }

    private FieldValueSource getDateTimeSource(FieldSpec fieldSpec) {
        DateTimeRestrictions restrictions = fieldSpec.getDateTimeRestrictions();

        return new DateTimeFieldValueSource(
            restrictions != null ? restrictions : new DateTimeRestrictions(),
            getBlacklist(fieldSpec));
    }
}
