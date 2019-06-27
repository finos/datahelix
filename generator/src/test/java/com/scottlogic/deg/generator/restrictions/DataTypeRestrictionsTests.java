package com.scottlogic.deg.generator.restrictions;

import com.scottlogic.deg.common.profile.constraints.atomic.IsOfTypeConstraint;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

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

    @Test
    public void intersect_withAllPermittedTypes_shouldReturnSelf(){
        Collection<IsOfTypeConstraint.Types> allowedTypes = new ArrayList<>();
        allowedTypes.add(IsOfTypeConstraint.Types.STRING);
        allowedTypes.add(IsOfTypeConstraint.Types.DATETIME);
        DataTypeRestrictions self = new DataTypeRestrictions(allowedTypes);

        TypeRestrictions actual = self.intersect(DataTypeRestrictions.ALL_TYPES_PERMITTED);

        assertEquals(self, actual);
    }

    @Test
    public void intersect_withNoPermittedTypes_shouldReturnNull(){
        Collection<IsOfTypeConstraint.Types> allowedTypes = new ArrayList<>();
        allowedTypes.add(IsOfTypeConstraint.Types.STRING);
        allowedTypes.add(IsOfTypeConstraint.Types.DATETIME);
        DataTypeRestrictions self = new DataTypeRestrictions(allowedTypes);

        TypeRestrictions actual = self.intersect(DataTypeRestrictions.NO_TYPES_PERMITTED);

        assertNull(actual);
    }

    @Test
    public void intersect_withSomePermittedTypes_shouldReturnIntersection(){
        Collection<IsOfTypeConstraint.Types> allowedTypes = new ArrayList<>();
        allowedTypes.add(IsOfTypeConstraint.Types.STRING);
        allowedTypes.add(IsOfTypeConstraint.Types.DATETIME);
        DataTypeRestrictions self = new DataTypeRestrictions(allowedTypes);

        Collection<IsOfTypeConstraint.Types> otherAllowedTypes = new ArrayList<>();
        otherAllowedTypes.add(IsOfTypeConstraint.Types.NUMERIC);
        otherAllowedTypes.add(IsOfTypeConstraint.Types.STRING);
        DataTypeRestrictions other = new DataTypeRestrictions(otherAllowedTypes);

        TypeRestrictions actual = self.intersect(other);

        Collection<IsOfTypeConstraint.Types> expectedAllowedTypes = new ArrayList<>();
        expectedAllowedTypes.add(IsOfTypeConstraint.Types.STRING);
        DataTypeRestrictions expected = new DataTypeRestrictions(expectedAllowedTypes);
        assertEquals(expected, actual);
    }
}