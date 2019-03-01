package com.scottlogic.deg.generator.inputs.profileviolation;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.Profile;
import com.scottlogic.deg.generator.ProfileFields;
import com.scottlogic.deg.generator.Rule;
import com.scottlogic.deg.generator.builders.AndBuilder;
import com.scottlogic.deg.generator.builders.OrBuilder;
import com.scottlogic.deg.generator.builders.RuleBuilder;
import com.scottlogic.deg.generator.outputs.manifest.ManifestWriter;
import com.scottlogic.deg.generator.violations.ViolatedProfile;
import com.scottlogic.deg.generator.violations.filters.ViolationFilter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.nio.file.Path;
import java.util.*;

import static com.scottlogic.deg.generator.inputs.profileviolation.TypeEqualityHelper.assertProfileListsAreEquivalent;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Mockito.*;

/**
 * Defines tests for all business logic involved in Profile Violation.
 */
public class ProfileViolationTests {
    private IndividualRuleProfileViolator target;
    private ArrayList<ViolationFilter> constraintsToNotViolate;

    @Mock private ManifestWriter mockManifestWriter;
    @Mock private Path mockPath;

    @BeforeEach
    public void setUp() throws IOException {
        MockitoAnnotations.initMocks(this);
        doNothing()
            .when(mockManifestWriter)
            .writeManifest(anyListOf(ViolatedProfile.class), same(mockPath));

        constraintsToNotViolate = new ArrayList<>();
        IndividualConstraintRuleViolator ruleViolator = new IndividualConstraintRuleViolator(constraintsToNotViolate);
        target = new IndividualRuleProfileViolator(mockManifestWriter, mockPath, ruleViolator);
    }

    /**
     * Tests that the violator can take a profile of two simple rules and return two violated profiles with the correct
     * negation combinations.
     *  Input: Profile with 2 fields foo and bar, 2 single atomic constraint rules affecting foo and bar
     *  Output: 2 Profiles, one with rule 1 negated and rule 2 unaffected, one with rule 1 unaffected and rule 2 negated
     */
    @Test
    public void violate_withTwoSimpleRuleProfile_producesTwoViolatedProfiles() throws IOException {
        //Arrange
        Field fooField = new Field("foo");
        Rule rule1 = new RuleBuilder("Rule 1")
            .withLessThanConstraint(fooField, 100)
            .withLessThanConstraint(fooField, 200)
            .build();

        Rule rule2 = new RuleBuilder("Rule 2")
            .withGreaterThanConstraint(fooField, 10)
            .withGreaterThanConstraint(fooField, 15)
            .build();

        Profile inputProfile = new Profile(
            Collections.singletonList(fooField),
            Arrays.asList(rule1, rule2),
            "Profile 1");

        //Act
        List<Profile> violatedProfiles = target.violate(inputProfile);

        //Assert
        Rule violatedRule1 = new RuleBuilder("Rule 1")
            .withOrConstraint(new OrBuilder()
                .withAndConstraint(new AndBuilder()
                    .withLessThanConstraint(fooField, 100).violate()
                    .withLessThanConstraint(fooField, 200)
                )
                .withAndConstraint(new AndBuilder()
                    .withLessThanConstraint(fooField, 100)
                    .withLessThanConstraint(fooField, 200).violate()
                )
            )
            .build();

        Rule violatedRule2 = new RuleBuilder("Rule 2")
            .withOrConstraint(new OrBuilder()
                .withAndConstraint(new AndBuilder()
                    .withGreaterThanConstraint(fooField, 10).violate()
                    .withGreaterThanConstraint(fooField, 15)
                )
                .withAndConstraint(new AndBuilder()
                    .withGreaterThanConstraint(fooField, 10)
                    .withGreaterThanConstraint(fooField, 15).violate()
                )
            )
            .build();

        Profile violatedProfile1 = new ViolatedProfile(
            rule1,
            new ProfileFields(Collections.singletonList(fooField)),
            Arrays.asList(violatedRule1, rule2),
            "Profile 1 -- Violating: Rule 1"
        );
        Profile violatedProfile2 = new ViolatedProfile(
            rule2,
            new ProfileFields(Collections.singletonList(fooField)),
            Arrays.asList(rule1, violatedRule2),
            "Profile 1 -- Violating: Rule 2"
        );
        List<Profile> expectedViolatedProfiles = Arrays.asList(violatedProfile1, violatedProfile2);

        assertProfileListsAreEquivalent(violatedProfiles, expectedViolatedProfiles);
        verify(mockManifestWriter, times(1))
            .writeManifest(anyListOf(ViolatedProfile.class), same(mockPath));
    }
}
