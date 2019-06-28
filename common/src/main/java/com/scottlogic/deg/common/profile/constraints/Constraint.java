package com.scottlogic.deg.common.profile.constraints;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.scottlogic.deg.common.profile.constraints.atomic.*;
import com.scottlogic.deg.common.profile.RuleInformation;

import java.util.Set;
/*
 * The following JsonTypeInfo is needed for the utility program GenTreeJson.java
 * (invoked via the mode of `genTreeJson`), which produces a JSON for the decision
 * tree in memory.
 */
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "type"
        )
@JsonSubTypes({
    @JsonSubTypes.Type(value = IsInSetConstraint.class, name = "IsInSetConstraint"),
    @JsonSubTypes.Type(value = IsStringShorterThanConstraint.class, name = "IsStringShorterThanConstraint"),
    @JsonSubTypes.Type(value = IsOfTypeConstraint.class, name = "IsOfTypeConstraint"),
    @JsonSubTypes.Type(value = NotConstraint.class, name = "NotConstraint"),
    @JsonSubTypes.Type(value = IsNullConstraint.class, name = "IsNullConstraint"),
    @JsonSubTypes.Type(value = IsLessThanConstantConstraint.class, name = "IsLessThanConstantConstraint")
})
public interface Constraint
{
    Constraint negate();

    Set<RuleInformation> getRules();
}

