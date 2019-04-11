package com.scottlogic.deg.generator.guice;

import com.scottlogic.deg.generator.constraints.atomic.*;
import com.scottlogic.deg.schemas.v0_1.AtomicConstraintType;

public class AtomicConstraintTypeMapper {
    public Class toConstraintClass(AtomicConstraintType type) {
        switch (type) {
            case ISNULL:
                return IsNullConstraint.class;
            case ISOFTYPE:
                return IsOfTypeConstraint.class;
            case MATCHESREGEX:
                return MatchesRegexConstraint.class;
            case CONTAINSREGEX:
                return ContainsRegexConstraint.class;
            case FORMATTEDAS:
                return FormatConstraint.class;
            case AVALID:
                return MatchesStandardConstraint.class;
            case HASLENGTH:
                return StringHasLengthConstraint.class;
            case ISSTRINGLONGERTHAN:
                return IsStringLongerThanConstraint.class;
            case ISSTRINGSHORTERTHAN:
                return IsStringShorterThanConstraint.class;
            case ISGREATERTHANCONSTANT:
                return IsGreaterThanConstantConstraint.class;
            case ISGREATERTHANOREQUALTOCONSTANT:
                return IsGreaterThanOrEqualToConstantConstraint.class;
            case ISLESSTHANCONSTANT:
                return IsLessThanConstantConstraint.class;
            case ISLESSTHANOREQUALTOCONSTANT:
                return IsLessThanOrEqualToConstantConstraint.class;
            case ISAFTERCONSTANTDATETIME:
                return IsAfterConstantDateTimeConstraint.class;
            case ISAFTEROREQUALTOCONSTANTDATETIME:
                return IsAfterOrEqualToConstantDateTimeConstraint.class;
            case ISBEFORECONSTANTDATETIME:
                return IsBeforeConstantDateTimeConstraint.class;
            case ISBEFOREOREQUALTOCONSTANTDATETIME:
                return IsBeforeOrEqualToConstantDateTimeConstraint.class;
            case ISGRANULARTO:
                return IsGranularToConstraint.class;
            case ISEQUALTOCONSTANT:
            case ISINSET:
                return IsInSetConstraint.class;
            default:
                throw new UnsupportedOperationException();
        }
    }

    public AtomicConstraintType fromConstraintClass(Class<? extends AtomicConstraint> atomicConstraint) {
        if (atomicConstraint == IsNullConstraint.class) {
            return AtomicConstraintType.ISNULL;
        }
        if (atomicConstraint == IsOfTypeConstraint.class) {
            return AtomicConstraintType.ISOFTYPE;
        }
        if (atomicConstraint == MatchesRegexConstraint.class) {
            return AtomicConstraintType.MATCHESREGEX;
        }
        if (atomicConstraint == ContainsRegexConstraint.class) {
            return AtomicConstraintType.CONTAINSREGEX;
        }
        if (atomicConstraint == FormatConstraint.class) {
            return AtomicConstraintType.FORMATTEDAS;
        }
        if (atomicConstraint == MatchesStandardConstraint.class) {
            return AtomicConstraintType.AVALID;
        }
        if (atomicConstraint == StringHasLengthConstraint.class) {
            return AtomicConstraintType.HASLENGTH;
        }
        if (atomicConstraint == IsStringLongerThanConstraint.class) {
            return AtomicConstraintType.ISSTRINGLONGERTHAN;
        }
        if (atomicConstraint == IsStringShorterThanConstraint.class) {
            return AtomicConstraintType.ISSTRINGSHORTERTHAN;
        }
        if (atomicConstraint == IsGreaterThanConstantConstraint.class) {
            return AtomicConstraintType.ISGREATERTHANCONSTANT;
        }
        if (atomicConstraint == IsGreaterThanOrEqualToConstantConstraint.class) {
            return AtomicConstraintType.ISGREATERTHANOREQUALTOCONSTANT;
        }
        if (atomicConstraint == IsLessThanConstantConstraint.class) {
            return AtomicConstraintType.ISLESSTHANCONSTANT;
        }
        if (atomicConstraint == IsLessThanOrEqualToConstantConstraint.class) {
            return AtomicConstraintType.ISLESSTHANOREQUALTOCONSTANT;
        }
        if (atomicConstraint == IsAfterConstantDateTimeConstraint.class) {
            return AtomicConstraintType.ISAFTERCONSTANTDATETIME;
        }
        if (atomicConstraint == IsAfterOrEqualToConstantDateTimeConstraint.class) {
            return AtomicConstraintType.ISAFTEROREQUALTOCONSTANTDATETIME;
        }
        if (atomicConstraint == IsBeforeConstantDateTimeConstraint.class) {
            return AtomicConstraintType.ISBEFORECONSTANTDATETIME;
        }
        if (atomicConstraint == IsBeforeOrEqualToConstantDateTimeConstraint.class) {
            return AtomicConstraintType.ISBEFOREOREQUALTOCONSTANTDATETIME;
        }
        if (atomicConstraint == IsGranularToConstraint.class) {
            return AtomicConstraintType.ISGRANULARTO;
        }
        if (atomicConstraint == IsInSetConstraint.class) {
            return AtomicConstraintType.ISINSET;
        }
        throw new UnsupportedOperationException();
    }
}

