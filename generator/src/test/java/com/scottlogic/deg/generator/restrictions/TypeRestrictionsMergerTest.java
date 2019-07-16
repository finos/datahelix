package com.scottlogic.deg.generator.restrictions;

import com.scottlogic.deg.common.profile.constraints.atomic.IsOfTypeConstraint;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import static com.scottlogic.deg.common.profile.constraints.atomic.IsOfTypeConstraint.Types.DATETIME;
import static com.scottlogic.deg.common.profile.constraints.atomic.IsOfTypeConstraint.Types.STRING;
import static org.junit.jupiter.api.Assertions.*;

class TypeRestrictionsMergerTest {
    private TypeRestrictionsMerger typeRestrictionsMerger = new TypeRestrictionsMerger();

    @Test
    public void intersect_withAllPermittedTypes_shouldReturnSelf(){
        Collection<IsOfTypeConstraint.Types> allowedTypes = new ArrayList<>();
        allowedTypes.add(STRING);
        allowedTypes.add(DATETIME);
        TypeRestrictions self = new TypeRestrictions(allowedTypes);

        MergeResult<TypeRestrictions> actual = typeRestrictionsMerger.merge(self, TypeRestrictions.ALL_TYPES_PERMITTED);

        assertEquals(self, actual.restrictions);
    }

    @Test
    public void intersect_withNoOverlappingPermittedTypes_shouldBeUnsuccessful(){
        TypeRestrictions self = new TypeRestrictions(Collections.singleton(STRING));
        TypeRestrictions other = new TypeRestrictions(Collections.singleton(DATETIME));

        MergeResult<TypeRestrictions> actual = typeRestrictionsMerger.merge(self, other);

        assertEquals(MergeResult.unsuccessful(), actual);
    }

    @Test
    public void intersect_withSomePermittedTypes_shouldReturnIntersection(){
        Collection<IsOfTypeConstraint.Types> allowedTypes = new ArrayList<>();
        allowedTypes.add(STRING);
        allowedTypes.add(DATETIME);
        TypeRestrictions self = new TypeRestrictions(allowedTypes);

        Collection<IsOfTypeConstraint.Types> otherAllowedTypes = new ArrayList<>();
        otherAllowedTypes.add(IsOfTypeConstraint.Types.NUMERIC);
        otherAllowedTypes.add(STRING);
        TypeRestrictions other = new TypeRestrictions(otherAllowedTypes);

        MergeResult<TypeRestrictions> actual = typeRestrictionsMerger.merge(self, other);

        Collection<IsOfTypeConstraint.Types> expectedAllowedTypes = new ArrayList<>();
        expectedAllowedTypes.add(STRING);
        TypeRestrictions expected = new TypeRestrictions(expectedAllowedTypes);
        assertEquals(expected, actual.restrictions);
    }

}