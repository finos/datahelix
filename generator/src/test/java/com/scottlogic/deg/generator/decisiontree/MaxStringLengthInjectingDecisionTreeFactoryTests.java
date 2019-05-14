package com.scottlogic.deg.generator.decisiontree;

import com.scottlogic.deg.common.profile.Field;
import com.scottlogic.deg.common.profile.Profile;
import com.scottlogic.deg.common.profile.ProfileFields;
import com.scottlogic.deg.common.constraint.atomic.AtomicConstraint;
import com.scottlogic.deg.common.constraint.atomic.IsStringShorterThanConstraint;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Collections;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class MaxStringLengthInjectingDecisionTreeFactoryTests {
    @Test
    public void analyse_withMultipleFields_shouldAddMaxLengthConstraintToEveryField(){
        ProfileDecisionTreeFactory underlyingFactory = mock(ProfileDecisionTreeFactory.class);
        MaxStringLengthInjectingDecisionTreeFactory factory = new MaxStringLengthInjectingDecisionTreeFactory(
            underlyingFactory,
            1000);
        Profile profile = mock(Profile.class);
        ProfileFields fields = new ProfileFields(Collections.singletonList(new Field("field 1")));
        DecisionTree underlyingTree = new DecisionTree(
            new TreeConstraintNode(Collections.emptySet(), Collections.emptySet()),
            fields,
            "description"
        );
        when(underlyingFactory.analyse(profile)).thenReturn(underlyingTree);

        DecisionTree result = factory.analyse(profile);

        Collection<AtomicConstraint> atomicConstraints = result.getRootNode().getAtomicConstraints();
        Assert.assertThat(atomicConstraints, hasItem(instanceOf(IsStringShorterThanConstraint.class)));

        IsStringShorterThanConstraint shorterThan = (IsStringShorterThanConstraint)atomicConstraints
            .stream()
            .filter(ac -> ac instanceof IsStringShorterThanConstraint)
            .iterator().next();

        Assert.assertThat(shorterThan.referenceValue, equalTo(1001));
    }
}