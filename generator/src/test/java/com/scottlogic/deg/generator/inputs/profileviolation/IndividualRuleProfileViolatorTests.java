package com.scottlogic.deg.generator.inputs.profileviolation;

import com.scottlogic.deg.common.profile.Field;
import com.scottlogic.deg.common.profile.Profile;
import com.scottlogic.deg.common.profile.ProfileFields;
import com.scottlogic.deg.common.profile.Rule;
import com.scottlogic.deg.common.profile.constraints.Constraint;
import com.scottlogic.deg.common.profile.constraints.atomic.IsGreaterThanConstantConstraint;
import com.scottlogic.deg.common.profile.constraints.atomic.IsLessThanConstantConstraint;
import com.scottlogic.deg.common.profile.RuleInformation;
import com.scottlogic.deg.generator.outputs.manifest.ManifestWriter;
import com.scottlogic.deg.generator.violations.ViolatedProfile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.scottlogic.deg.generator.inputs.profileviolation.TypeEqualityHelper.assertListProfileTypeEquality;
import static com.shazam.shazamcrest.MatcherAssert.assertThat;
import static com.shazam.shazamcrest.matcher.Matchers.sameBeanAs;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Mockito.*;

public class IndividualRuleProfileViolatorTests {

    private IndividualRuleProfileViolator target;

    @Mock private RuleViolator mockRuleViolator;
    @Mock private ManifestWriter mockManifestWriter;
    @Mock private Path mockPath;

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
            mockManifestWriter,
            mockPath,
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
        Profile inputProfile = new Profile(
            Arrays.asList(fooField, barField),
            Collections.singletonList(rule1),
            "Input profile description"
        );

        when(mockRuleViolator.violateRule(rule1)).thenReturn(violatedRule1);

        //Act
        List<Profile> outputProfileList = target.violate(inputProfile);

        //Assert
        List<Profile> expectedProfileList =
            Collections.singletonList(
                new ViolatedProfile(
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
        Profile inputProfile = new Profile(
            Arrays.asList(fooField, barField),
            Arrays.asList(rule1, rule2),
            "Input profile description"
        );

        when(mockRuleViolator.violateRule(rule1)).thenReturn(violatedRule1);
        when(mockRuleViolator.violateRule(rule2)).thenReturn(violatedRule2);

        //Act
        List<Profile> outputProfileList = target.violate(inputProfile);

        //Assert
        List<Profile> expectedProfileList =
            Arrays.asList(
                new ViolatedProfile(
                    rule1,
                    new ProfileFields(Arrays.asList(fooField, barField)),
                    Arrays.asList(violatedRule1, rule2),
                    "Input profile description -- Violating: Rule 1 description"
                ),
                new ViolatedProfile(
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

    /**
     * Violate with any profile should call the manifest writer.
     */
    @Test
    public void violate_withAnyProfile_callsManifestWriter() throws IOException {
        //Arrange
        Profile inputProfile = new Profile(
            Arrays.asList(fooField, barField),
            Collections.singletonList(rule1),
            "Input profile description"
        );

        doNothing()
            .when(mockManifestWriter)
            .writeManifest(anyListOf(ViolatedProfile.class), eq(mockPath));

        //Act
        target.violate(inputProfile);

        //Assert
        verify(mockManifestWriter, times(1))
            .writeManifest(anyListOf(ViolatedProfile.class), eq(mockPath));
    }

    /**
     * Violate with any profile that is unable to write the manifest continues execution.
     */
    @Test
    public void violate_withAnyProfileFailedManifestWriter_doesNotThrow() throws IOException {
        //Arrange
        Profile inputProfile = new Profile(
            Arrays.asList(fooField, barField),
            Collections.singletonList(rule1),
            "Input profile description"
        );

        doThrow(new IOException("Exception to be caught"))
            .when(mockManifestWriter)
            .writeManifest(anyListOf(ViolatedProfile.class), eq(mockPath));

        //Act
        IOException thrown =
            assertThrows(IOException.class,
                () -> target.violate(inputProfile),
                "Expected violate() to throw IOException, but it didn't");

        //Assert
        verify(mockManifestWriter, times(1))
            .writeManifest(anyListOf(ViolatedProfile.class), eq(mockPath));
    }

    private void initRules() {
        //Rule 1 consists of 2 constraints, "foo is greater than 100" and "bar is greater than 50"
        RuleInformation ruleInformation1 = new RuleInformation("Rule 1 description");
        fooField = new Field("foo");
        barField = new Field("bar");
        Constraint constraint1 = new IsGreaterThanConstantConstraint(
            fooField,
            100,
            Collections.singleton(ruleInformation1));
        Constraint constraint2 = new IsGreaterThanConstantConstraint(
            barField,
            50,
            Collections.singleton(ruleInformation1));
        rule1 = new Rule(ruleInformation1, Arrays.asList(constraint1, constraint2));

        //Violated Rule 1 consists of two constraints, "foo is less than to 101" and "bar is less than 51"
        Constraint constraint3 = new IsLessThanConstantConstraint(
            fooField,
            101,
            Collections.singleton(ruleInformation1));
        Constraint constraint4 = new IsLessThanConstantConstraint(
            barField,
            51,
            Collections.singleton(ruleInformation1));
        violatedRule1 = new Rule(ruleInformation1, Arrays.asList(constraint3, constraint4));

        RuleInformation ruleInformation2 = new RuleInformation("Rule 2 description");
        rule2 = new Rule(ruleInformation2, Arrays.asList(constraint1,constraint4));
        violatedRule2 = new Rule(ruleInformation2, Arrays.asList(constraint2,constraint3));
    }
}