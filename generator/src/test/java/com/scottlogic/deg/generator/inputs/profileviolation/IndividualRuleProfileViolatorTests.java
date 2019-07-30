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

package com.scottlogic.deg.generator.inputs.profileviolation;

import com.scottlogic.deg.common.profile.Field;
import com.scottlogic.deg.common.profile.Profile;
import com.scottlogic.deg.common.profile.ProfileFields;
import com.scottlogic.deg.common.profile.Rule;
import com.scottlogic.deg.common.profile.constraints.Constraint;
import com.scottlogic.deg.common.profile.constraints.atomic.IsGreaterThanConstantConstraint;
import com.scottlogic.deg.common.profile.constraints.atomic.IsLessThanConstantConstraint;
import com.scottlogic.deg.common.profile.RuleInformation;
import com.scottlogic.deg.common.profile.ViolatedProfile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.scottlogic.deg.generator.inputs.profileviolation.TypeEqualityHelper.assertListProfileTypeEquality;
import static com.shazam.shazamcrest.MatcherAssert.assertThat;
import static com.shazam.shazamcrest.matcher.Matchers.sameBeanAs;
import static org.mockito.Mockito.*;

public class IndividualRuleProfileViolatorTests {

    private IndividualRuleProfileViolator target;

    @Mock private RuleViolator mockRuleViolator;

    private Field fooField;
    private Field barField;
    private Rule rule1;
    private Rule rule2;
    private Rule violatedRule1;
    private Rule violatedRule2;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        target = new IndividualRuleProfileViolator(
            mockRuleViolator
        );

        initRules();
    }

    /**
     * Violate with a profile with a single rule returns a single violated profile.
     */
    @Test
    public void violate_withSingleRuleProfile_returnsSingleViolatedProfile() throws IOException {
        //Arrange
        String schemaVersion = "0.1";
        Profile inputProfile = new Profile(
            schemaVersion,
            Arrays.asList(fooField, barField),
            Collections.singletonList(rule1),
            "Input profile description"
        );

        when(mockRuleViolator.violateRule(rule1)).thenReturn(violatedRule1);

        //Act
        List<Profile> outputProfileList = (List<Profile>)(List<?>) target.violate(inputProfile);

        //Assert
        List<Profile> expectedProfileList =
            Collections.singletonList(
                new ViolatedProfile(
                    schemaVersion,
                    rule1,
                    new ProfileFields(Arrays.asList(fooField, barField)),
                    Collections.singletonList(violatedRule1),
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
        String schemaVersion = "0.1";
        Profile inputProfile = new Profile(
            schemaVersion,
            Arrays.asList(fooField, barField),
            Arrays.asList(rule1, rule2),
            "Input profile description"
        );

        when(mockRuleViolator.violateRule(rule1)).thenReturn(violatedRule1);
        when(mockRuleViolator.violateRule(rule2)).thenReturn(violatedRule2);

        //Act
        List<Profile> outputProfileList = (List<Profile>)(List<?>) target.violate(inputProfile);

        //Assert
        List<Profile> expectedProfileList =
            Arrays.asList(
                new ViolatedProfile(
                    schemaVersion,
                    rule1,
                    new ProfileFields(Arrays.asList(fooField, barField)),
                    Arrays.asList(violatedRule1, rule2),
                    "Input profile description -- Violating: Rule 1 description"
                ),
                new ViolatedProfile(
                    schemaVersion,
                    rule2,
                    new ProfileFields(Arrays.asList(fooField, barField)),
                    Arrays.asList(rule1, violatedRule2),
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
        RuleInformation ruleInformation1 = new RuleInformation("Rule 1 description");
        fooField = new Field("foo");
        barField = new Field("bar");
        Constraint constraint1 = new IsGreaterThanConstantConstraint(
            fooField,
            100
        );
        Constraint constraint2 = new IsGreaterThanConstantConstraint(
            barField,
            50
        );
        rule1 = new Rule(ruleInformation1, Arrays.asList(constraint1, constraint2));

        //Violated Rule 1 consists of two constraints, "foo is less than to 101" and "bar is less than 51"
        Constraint constraint3 = new IsLessThanConstantConstraint(
            fooField,
            101
        );
        Constraint constraint4 = new IsLessThanConstantConstraint(
            barField,
            51
        );
        violatedRule1 = new Rule(ruleInformation1, Arrays.asList(constraint3, constraint4));

        RuleInformation ruleInformation2 = new RuleInformation("Rule 2 description");
        rule2 = new Rule(ruleInformation2, Arrays.asList(constraint1,constraint4));
        violatedRule2 = new Rule(ruleInformation2, Arrays.asList(constraint2,constraint3));
    }
}