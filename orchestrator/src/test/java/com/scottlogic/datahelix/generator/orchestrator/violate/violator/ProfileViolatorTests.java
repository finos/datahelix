/*
 * Copyright 2019 Scott Logic Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.scottlogic.datahelix.generator.orchestrator.violate.violator;

import com.scottlogic.datahelix.generator.common.profile.Field;
import com.scottlogic.datahelix.generator.common.profile.Fields;
import com.scottlogic.datahelix.generator.common.util.NumberUtils;
import com.scottlogic.datahelix.generator.core.profile.Profile;
import com.scottlogic.datahelix.generator.core.profile.constraints.Constraint;
import com.scottlogic.datahelix.generator.core.profile.constraints.atomic.GreaterThanConstraint;
import com.scottlogic.datahelix.generator.core.profile.constraints.atomic.LessThanConstraint;
import com.scottlogic.datahelix.generator.core.profile.constraints.grammatical.AndConstraint;
import com.scottlogic.datahelix.generator.orchestrator.violate.ViolatedProfile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.scottlogic.datahelix.generator.common.profile.FieldBuilder.createField;
import static com.scottlogic.datahelix.generator.orchestrator.violate.violator.TypeEqualityHelper.assertListProfileTypeEquality;
import static com.shazam.shazamcrest.MatcherAssert.assertThat;
import static com.shazam.shazamcrest.matcher.Matchers.sameBeanAs;
import static org.mockito.Mockito.when;

public class ProfileViolatorTests {

    private ProfileViolator target;

    @Mock private ConstraintViolator mockConstraintViolator;

    private Field fooField;
    private Field barField;
    private Constraint constraint1;
    private Constraint constraint2;
    private Constraint violatedConstraint1;
    private Constraint violatedConstraint2;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        target = new ProfileViolator(
            mockConstraintViolator
        );

        initRules();
    }

    /**
     * Violate with a profile with a single rule returns a single violated profile.
     */
    @Test
    public void violate_withSingleRuleProfile_returnsSingleViolatedProfile() throws IOException {
        //Arrange
        Profile inputProfile = new Profile(
            Arrays.asList(fooField, barField),
            Collections.singletonList(constraint1),
            "Input profile description"
        );

        when(mockConstraintViolator.violateConstraint(constraint1)).thenReturn(violatedConstraint1);

        //Act
        List<Profile> outputProfileList = (List<Profile>)(List<?>) target.violate(inputProfile);

        //Assert
        List<Profile> expectedProfileList =
            Collections.singletonList(
                new ViolatedProfile(
                    constraint1,
                    new Fields(Arrays.asList(fooField, barField)),
                    Collections.singletonList(violatedConstraint1),
                    "Input profile description -- Violating: Rule 1 description"
                )
            );

        assertThat(
            "The violate method should have returned the correct shaped profile",
            outputProfileList,
            sameBeanAs(expectedProfileList)
        );
        assertListProfileTypeEquality(outputProfileList, expectedProfileList);
    }

    /**
     * Violate with a profile with a multiple rules returns a 2 violated profiles.
     */
    @Test
    public void violate_withMultipleRuleProfile_returnsMultipleViolatedProfile() throws IOException {
        //Arrange
        Profile inputProfile = new Profile(
            Arrays.asList(fooField, barField),
            Arrays.asList(constraint1, constraint2),
            "Input profile description"
        );

        when(mockConstraintViolator.violateConstraint(constraint1)).thenReturn(violatedConstraint1);
        when(mockConstraintViolator.violateConstraint(constraint2)).thenReturn(violatedConstraint2);

        //Act
        List<Profile> outputProfileList = (List<Profile>)(List<?>) target.violate(inputProfile);

        //Assert
        List<Profile> expectedProfileList =
            Arrays.asList(
                new ViolatedProfile(
                    constraint1,
                    new Fields(Arrays.asList(fooField, barField)),
                    Arrays.asList(violatedConstraint1, constraint2),
                    "Input profile description -- Violating: Rule 1 description"
                ),
                new ViolatedProfile(
                    constraint2,
                    new Fields(Arrays.asList(fooField, barField)),
                    Arrays.asList(constraint1, violatedConstraint2),
                    "Input profile description -- Violating: Rule 2 description"
                )
            );

        assertThat(
            "The violate method should have returned the correct shaped profile",
            outputProfileList,
            sameBeanAs(expectedProfileList)
        );
        assertListProfileTypeEquality(outputProfileList, expectedProfileList);
    }

    private void initRules() {
        //Rule 1 consists of 2 constraints, "foo is greater than 100" and "bar is greater than 50"
        fooField = createField("foo");
        barField = createField("bar");
        Constraint constraint1 = new GreaterThanConstraint(
            fooField,
            NumberUtils.coerceToBigDecimal(100)
        );
        Constraint constraint2 = new GreaterThanConstraint(
            barField,
            NumberUtils.coerceToBigDecimal(50)
        );
        this.constraint1 = new AndConstraint(constraint1, constraint2);

        //Violated Rule 1 consists of two constraints, "foo is less than to 101" and "bar is less than 51"
        Constraint constraint3 = new LessThanConstraint(
            fooField,
            NumberUtils.coerceToBigDecimal(101)
        );
        Constraint constraint4 = new LessThanConstraint(
            barField,
            NumberUtils.coerceToBigDecimal(51)
        );
        violatedConstraint1 = new AndConstraint(constraint3, constraint4);

        this.constraint2 = new AndConstraint(constraint1,constraint4);
        violatedConstraint2 = new AndConstraint(constraint2,constraint3);
    }
}