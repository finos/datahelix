package com.scottlogic.deg.generator.restrictions;

import com.scottlogic.deg.generator.constraints.atomic.IsOfTypeConstraint;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

class DataTypeRestrictionsTests {
    @Test
    public void except_withAlreadyExcludedType_shouldReturnSameCollectionOfPermittedTypes(){
        TypeRestrictions exceptStrings = DataTypeRestrictions.createFromWhiteList(
            IsOfTypeConstraint.Types.NUMERIC,
            IsOfTypeConstraint.Types.TEMPORAL);

        TypeRestrictions result = exceptStrings.except(IsOfTypeConstraint.Types.STRING);

        Assert.assertThat(result.getAllowedTypes(), containsInAnyOrder(
            IsOfTypeConstraint.Types.NUMERIC,
            IsOfTypeConstraint.Types.TEMPORAL));
    }

    @Test
    public void except_withNoTypes_shouldReturnSameCollectionOfPermittedTypes(){
        TypeRestrictions exceptStrings = DataTypeRestrictions.createFromWhiteList(
            IsOfTypeConstraint.Types.NUMERIC,
            IsOfTypeConstraint.Types.TEMPORAL);

        TypeRestrictions result = exceptStrings.except();

        Assert.assertThat(result, sameInstance(exceptStrings));
    }

    @Test
    public void except_withPermittedType_shouldReturnSameCollectionExcludingGivenType(){
        TypeRestrictions exceptStrings = DataTypeRestrictions.createFromWhiteList(
            IsOfTypeConstraint.Types.NUMERIC,
            IsOfTypeConstraint.Types.TEMPORAL);

        TypeRestrictions result = exceptStrings.except(IsOfTypeConstraint.Types.NUMERIC);

        Assert.assertThat(result.getAllowedTypes(), containsInAnyOrder(
            IsOfTypeConstraint.Types.TEMPORAL));
    }

    @Test
    public void except_withLastPermittedType_shouldReturnNoTypesPermitted(){
        TypeRestrictions exceptStrings = DataTypeRestrictions.createFromWhiteList(
            IsOfTypeConstraint.Types.TEMPORAL);

        TypeRestrictions result = exceptStrings.except(IsOfTypeConstraint.Types.TEMPORAL);

        Assert.assertThat(result.getAllowedTypes(), empty());
    }
}