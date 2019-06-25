package com.scottlogic.deg.generator.generation;

import com.scottlogic.deg.common.profile.constraints.atomic.IsOfTypeConstraint;
import com.scottlogic.deg.generator.fieldspecs.FieldSpec;
import com.scottlogic.deg.generator.generation.fieldvaluesources.*;
import com.scottlogic.deg.generator.generation.fieldvaluesources.datetime.DateTimeFieldValueSource;
import com.scottlogic.deg.generator.generation.string.RegexStringGenerator;
import com.scottlogic.deg.generator.generation.string.StringGenerator;
import com.scottlogic.deg.generator.restrictions.*;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StandardFieldValueSourceEvaluator implements FieldValueSourceEvaluator {
    private static final CannedValuesFieldValueSource nullOnlySource = new CannedValuesFieldValueSource(Collections.singletonList(null));

    public List<FieldValueSource> getFieldValueSources(FieldSpec fieldSpec){

        if (fieldSpec.getWhitelist() != null && fieldSpec.getWhitelist() != null) {

            List<FieldValueSource> setRestrictionSources =
                getSetRestrictionSources(fieldSpec.getWhitelist());
            if (fieldSpec.isNullable()) {
                return addNullSource(setRestrictionSources);
            }
            return setRestrictionSources;
        }

        List<FieldValueSource> validSources = new ArrayList<>();

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

        if (fieldSpec.isNullable()) {
            validSources.add(nullOnlySource);
        }

        return validSources;
    }

    private List<FieldValueSource> addNullSource(List<FieldValueSource> setRestrictionSources) {
        return Stream.concat(setRestrictionSources.stream(), Stream.of(nullOnlySource)).collect(Collectors.toList());
    }

    private List<FieldValueSource> getSetRestrictionSources(@NotNull Set<Object> whitelist) {
        if (whitelist.isEmpty()){
            return Collections.emptyList();
        }

        return Collections.singletonList(
            new CannedValuesFieldValueSource(
                new ArrayList<>(whitelist)));
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
        if (fieldSpec.getBlacklistRestrictions() == null)
            return Collections.emptySet();

        return new HashSet<>(fieldSpec.getBlacklistRestrictions().getBlacklist());
    }

    private FieldValueSource getStringSource(FieldSpec fieldSpec) {
        StringRestrictions stringRestrictions = fieldSpec.getStringRestrictions();

        if (stringRestrictions == null) {
            return new CannedValuesFieldValueSource(Collections.emptyList());
        }

        Set<Object> blacklist = getBlacklist(fieldSpec);

        StringGenerator generator = stringRestrictions.createGenerator();
        if (!blacklist.isEmpty()) {
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
