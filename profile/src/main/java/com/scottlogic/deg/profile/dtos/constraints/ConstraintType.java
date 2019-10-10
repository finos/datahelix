package com.scottlogic.deg.profile.dtos.constraints;

import com.fasterxml.jackson.annotation.JsonValue;
import com.scottlogic.deg.profile.dtos.constraints.atomic.chronological.AfterConstraintDTO;
import com.scottlogic.deg.profile.dtos.constraints.atomic.chronological.AfterOrAtConstraintDTO;
import com.scottlogic.deg.profile.dtos.constraints.atomic.chronological.BeforeConstraintDTO;
import com.scottlogic.deg.profile.dtos.constraints.atomic.chronological.BeforeOrAtConstraintDTO;
import com.scottlogic.deg.profile.dtos.constraints.atomic.general.EqualToConstraintDTO;
import com.scottlogic.deg.profile.dtos.constraints.atomic.general.GranularToConstraintDTO;
import com.scottlogic.deg.profile.dtos.constraints.atomic.general.InMapConstraintDTO;
import com.scottlogic.deg.profile.dtos.constraints.atomic.general.InSetConstraintDTO;
import com.scottlogic.deg.profile.dtos.constraints.atomic.numerical.GreaterThanConstraintDTO;
import com.scottlogic.deg.profile.dtos.constraints.atomic.numerical.GreaterThanOrEqualToConstraintDTO;
import com.scottlogic.deg.profile.dtos.constraints.atomic.numerical.LessThanConstraintDTO;
import com.scottlogic.deg.profile.dtos.constraints.atomic.numerical.LessThanOrEqualToConstraintDTO;
import com.scottlogic.deg.profile.dtos.constraints.atomic.texual.*;
import com.scottlogic.deg.profile.dtos.constraints.grammatical.*;

import java.util.Arrays;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum ConstraintType
{
    EQUAL_TO(EqualToConstraintDTO.PROPERTY_NAME),
    IN_SET(InSetConstraintDTO.PROPERTY_NAME),
    IN_MAP(InMapConstraintDTO.PROPERTY_NAME),
    NULL(NullConstraintDTO.PROPERTY_NAME),
    GRANULAR_TO(GranularToConstraintDTO.PROPERTY_NAME),
    MATCHES_REGEX(MatchesRegexConstraintDTO.PROPERTY_NAME),
    CONTAINS_REGEX(ContainsRegexConstraintDTO.PROPERTY_NAME),
    OF_LENGTH(OfLengthConstraintDTO.PROPERTY_NAME),
    LONGER_THAN(LongerThanConstraintDTO.PROPERTY_NAME),
    SHORTER_THAN(ShorterThanConstraintDTO.PROPERTY_NAME),
    GREATER_THAN(GreaterThanConstraintDTO.PROPERTY_NAME),
    GREATER_THAN_OR_EQUAL_TO(GreaterThanOrEqualToConstraintDTO.PROPERTY_NAME),
    LESS_THAN(LessThanConstraintDTO.PROPERTY_NAME),
    LESS_THAN_OR_EQUAL_TO(LessThanOrEqualToConstraintDTO.PROPERTY_NAME),
    AFTER(AfterConstraintDTO.PROPERTY_NAME),
    AFTER_OR_AT(AfterOrAtConstraintDTO.PROPERTY_NAME),
    BEFORE(BeforeConstraintDTO.PROPERTY_NAME),
    BEFORE_OR_AT(BeforeOrAtConstraintDTO.PROPERTY_NAME),
    NOT(NotConstraintDTO.PROPERTY_NAME),
    ANY_OF(AnyOfConstraintDTO.PROPERTY_NAME),
    ALL_OF(AllOfConstraintDTO.PROPERTY_NAME),
    IF(IfConstraintDTO.PROPERTY_NAME);

    @JsonValue
    public final String propertyName;

    ConstraintType(String propertyName)
    {
        this.propertyName = propertyName;
    }

    public static ConstraintType fromPropertyName(String propertyName)
    {
        return Arrays.stream(values()).collect(Collectors.toMap(o -> propertyName, Function.identity())).get(propertyName);
    }
}

