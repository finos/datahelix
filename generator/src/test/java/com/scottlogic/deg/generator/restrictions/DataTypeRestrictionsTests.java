package com.scottlogic.deg.generator.restrictions;

import com.scottlogic.deg.common.constraint.atomic.IsOfTypeConstraint;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.*;

class DataTypeRestrictionsTests {
    @Test
    public void except_withAlreadyExcludedType_shouldReturnSameCollectionOfPermittedTypes(){
        TypeRestrictions exceptStrings = DataTypeRestrictions.createFromWhiteList(
            IsOfTypeConstraint.Types.NUMERIC,
            IsOfTypeConstraint.Types.DATETIME);

        TypeRestrictions result = exceptStrings.except(IsOfTypeConstraint.Types.STRING);

        Assert.assertThat(result.getAllowedTypes(), containsInAnyOrder(
            IsOfTypeConstraint.Types.NUMERIC,
            IsOfTypeConstraint.Types.DATETIME));
    }

    @Test
    public void except_withNoTypes_shouldReturnSameCollectionOfPermittedTypes(){
        TypeRestrictions exceptStrings = DataTypeRestrictions.createFromWhiteList(
            IsOfTypeConstraint.Types.NUMERIC,
            IsOfTypeConstraint.Types.DATETIME);

        TypeRestrictions result = exceptStrings.except();

        Assert.assertThat(result, sameInstance(exceptStrings));
    }

    @Test
    public void except_withPermittedType_shouldReturnSameCollectionExcludingGivenType(){
        TypeRestrictions exceptStrings = DataTypeRestrictions.createFromWhiteList(
            IsOfTypeConstraint.Types.NUMERIC,
            IsOfTypeConstraint.Types.DATETIME);

        TypeRestrictions result = exceptStrings.except(IsOfTypeConstraint.Types.NUMERIC);

        Assert.assertThat(result.getAllowedTypes(), containsInAnyOrder(
            IsOfTypeConstraint.Types.DATETIME));
    }

    @Test
    public void except_withLastPermittedType_shouldReturnNoTypesPermitted(){
        TypeRestrictions exceptStrings = DataTypeRestrictions.createFromWhiteList(
            IsOfTypeConstraint.Types.DATETIME);

        TypeRestrictions result = exceptStrings.except(IsOfTypeConstraint.Types.DATETIME);

        Assert.assertThat(result.getAllowedTypes(), empty());
    }
}