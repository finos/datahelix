package com.scottlogic.deg.profile.reader.AtomicConstraintFactory;

import com.google.inject.Inject;
import com.scottlogic.deg.common.profile.*;
import com.scottlogic.deg.common.util.NumberUtils;
import com.scottlogic.deg.generator.fieldspecs.relations.InMapRelation;
import com.scottlogic.deg.generator.fieldspecs.whitelist.DistributedList;
import com.scottlogic.deg.generator.profile.constraints.Constraint;
import com.scottlogic.deg.generator.profile.constraints.atomic.*;
import com.scottlogic.deg.profile.dtos.constraints.*;
import com.scottlogic.deg.profile.reader.FileReader;
import com.scottlogic.deg.profile.reader.InvalidProfileException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.stream.Collectors;

abstract class AtomicConstraintFactory {

    private final FileReader fileReader;

    AtomicConstraintFactory(FileReader fileReader) {
        this.fileReader = fileReader;
    }

    Constraint readAtomicConstraintDto(AtomicConstraintDTO dto, ProfileFields profileFields) {
        Field field = profileFields.getByName(dto.field);
        switch (dto.getType()) {
            case EQUAL_TO:
                return getEqualToConstraint((EqualToConstraintDTO) dto, field);
            case IN_SET:
                return getInSetConstraint((InSetConstraintDTO) dto, field);
            case IN_MAP:
                return getInMapConstraint((InMapConstraintDTO) dto, profileFields, field);
            case MATCHES_REGEX:
                return getMatchesRegexConstraint((MatchesRegexConstraintDTO) dto, field);
            case CONTAINS_REGEX:
                return getContainsRegexConstraint((ContainsRegexConstraintDTO) dto, field);
            case OF_LENGTH:
                return getOfLengthConstraint((OfLengthConstraintDTO) dto, field);
            case SHORTER_THAN:
                return getShorterThanConstraint((ShorterThanConstraintDTO) dto, field);
            case LONGER_THAN:
                return getIsLongerThanConstraint((LongerThanConstraintDTO) dto, field);
            case GREATER_THAN:
                return getIsGreaterThanConstraint((GreaterThanConstraintDTO) dto, field);
            case GREATER_THAN_OR_EQUAL_TO:
                return getIsGreaterThanOrEqualToConstraint((GreaterThanOrEqualToConstraintDTO) dto, field);
            case LESS_THAN:
                return getIsLessThanConstraint((LessThanConstraintDTO) dto, field);
            case LESS_THAN_OR_EQUAL_TO:
                return getLessThanOrEqualToConstraint((LessThanOrEqualToConstraintDTO) dto, field);
            case AFTER:
                return getAfterConstraint((AfterConstraintDTO) dto, field);
            case AFTER_OR_AT:
                return getAfterOrAtConstraint((AfterOrAtConstraintDTO) dto, field);
            case BEFORE:
                return getBeforeConstraint((BeforeConstraintDTO) dto, field);
            case BEFORE_OR_AT:
                return getBeforeOrAtConstraint((BeforeOrAtConstraintDTO) dto, field);
            case GRANULAR_TO:
                return getGranularToConstraint((GranularToConstraintDTO) dto, field);
            case IS_NULL:
                return getIsNullConstraint((NullConstraintDTO) dto, profileFields);
            default:
                throw new InvalidProfileException("Atomic constraint type not found: " + dto);
        }
    }

    Constraint getIsNullConstraint(NullConstraintDTO dto, ProfileFields profileFields) {
        IsNullConstraint isNullConstraint = new IsNullConstraint(profileFields.getByName(dto.field));
        return dto.isNull
            ? isNullConstraint
            : isNullConstraint.negate();
    }

    @NotNull
    Constraint getInSetConstraint(InSetConstraintDTO dto, Field field) {
        return new IsInSetConstraint(field, prepareValuesForSet(dto, field));
    }

    EqualToConstraint getEqualToConstraint(EqualToConstraintDTO dto, Field field){
        return new EqualToConstraint(field, readAnyType(field, dto.value));
    }

    @NotNull
    Constraint getGranularToConstraint(GranularToConstraintDTO dto, Field field) {
        throw new UnsupportedOperationException("GranularTo Constraint is not supported with type " + field.getType());
    }

    @NotNull
    Constraint getBeforeOrAtConstraint(BeforeOrAtConstraintDTO dto, Field field) {
        throw new UnsupportedOperationException("beforeOrAt Constraint is not supported with type " + field.getType());
    }

    @NotNull
    Constraint getBeforeConstraint(BeforeConstraintDTO dto, Field field) {
        throw new UnsupportedOperationException("before Constraint is not supported with type " + field.getType());
    }

    @NotNull
    Constraint getAfterOrAtConstraint(AfterOrAtConstraintDTO dto, Field field) {
        throw new UnsupportedOperationException("afterOrAt Constraint is not supported with type " + field.getType());
    }

    @NotNull
    Constraint getAfterConstraint(AfterConstraintDTO dto, Field field) {
        throw new UnsupportedOperationException("after Constraint is not supported with type " + field.getType());
    }

    @NotNull
    Constraint getLessThanOrEqualToConstraint(LessThanOrEqualToConstraintDTO dto, Field field) {
        throw new UnsupportedOperationException("lessThanOrEqualTo Constraint is not supported with type " + field.getType());
    }

    @NotNull
    Constraint getIsLessThanConstraint(LessThanConstraintDTO dto, Field field) {
        throw new UnsupportedOperationException("lessThan Constraint is not supported with type " + field.getType());
    }

    @NotNull
    Constraint getIsGreaterThanOrEqualToConstraint(GreaterThanOrEqualToConstraintDTO dto, Field field) {
        throw new UnsupportedOperationException("isGreaterThanOrEqualTo Constraint is not supported with type " + field.getType());
    }

    @NotNull
    Constraint getIsGreaterThanConstraint(GreaterThanConstraintDTO dto, Field field) {
        throw new UnsupportedOperationException("greaterThan Constraint is not supported with type " + field.getType());
    }

    @NotNull
    Constraint getIsLongerThanConstraint(LongerThanConstraintDTO dto, Field field) {
        throw new UnsupportedOperationException("longerThan Constraint is not supported with type " + field.getType());
    }

    @NotNull
    Constraint getShorterThanConstraint(ShorterThanConstraintDTO dto, Field field) {
        throw new UnsupportedOperationException("shorterThan Constraint is not supported with type " + field.getType());
    }

    @NotNull
    Constraint getOfLengthConstraint(OfLengthConstraintDTO dto, Field field) {
        throw new UnsupportedOperationException("ofLength Constraint is not supported with type " + field.getType());
    }

    @NotNull
    Constraint getContainsRegexConstraint(ContainsRegexConstraintDTO dto, Field field) {
        throw new UnsupportedOperationException("containsRegex Constraint is not supported with type " + field.getType());
    }

    @NotNull
    Constraint getMatchesRegexConstraint(MatchesRegexConstraintDTO dto, Field field) {
        throw new UnsupportedOperationException("matchesRegex Constraint is not supported with type " + field.getType());
    }

    @NotNull
    Constraint getInMapConstraint(InMapConstraintDTO dto, ProfileFields profileFields, Field field) {
        return new InMapRelation(field, profileFields.getByName(dto.file),
            DistributedList.uniform(fileReader.listFromMapFile(dto.file, dto.key).stream()
                .map(value -> readAnyType(field, value))
                .collect(Collectors.toList())));
    }


    @Nullable
    Object readAnyType(Field field, Object value) {
        switch (field.getType()) {
            case DATETIME:
                return HelixDateTime.create((String) value).getValue();
            case NUMERIC:
                return NumberUtils.coerceToBigDecimal(value);
            default:
                return value;
        }
    }

    DistributedList<Object> prepareValuesForSet(InSetConstraintDTO inSetConstraintDTO, Field field) {
        return (inSetConstraintDTO instanceof InSetFromFileConstraintDTO
            ? fileReader.setFromFile(((InSetFromFileConstraintDTO) inSetConstraintDTO).file)
            : DistributedList.uniform(((InSetOfValuesConstraintDTO) inSetConstraintDTO).values.stream()
            .distinct()
            .map(o -> readAnyType(field, o))
            .collect(Collectors.toList())));
    }
}
