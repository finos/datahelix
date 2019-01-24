package com.scottlogic.deg.generator.Guice;

import com.scottlogic.deg.generator.constraints.atomic.*;
import com.scottlogic.deg.schemas.v3.AtomicConstraintType;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

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
                throw new NotImplementedException();
        }
    }
}

