package com.scottlogic.deg.generator.inputs.profileviolation;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.Profile;
import com.scottlogic.deg.generator.ProfileFields;
import com.scottlogic.deg.generator.Rule;
import com.scottlogic.deg.generator.builders.*;
import com.scottlogic.deg.generator.constraints.Constraint;
import com.scottlogic.deg.generator.constraints.atomic.IsOfTypeConstraint;
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

    /**
     * Tests that an if-then constraint nested inside the conditional of another if-then constraint violates as
     * expected.
     * In shorthand where A,B,C are atomic constraints: VIOLATE(IF(IF A THEN B) THEN C) -> (IF A THEN B) AND VIOLATE(C)
     * @throws IOException if the manifest writer fails to write.
     */
    @Test
    public void violate_nestedIfThen_producesViolatedProfiles() throws IOException {
        //Arrange
        Field fooField = new Field("foo");
        Field barField = new Field("bar");
        String ruleName = "Nested if rule";

        final ConstraintChainBuilder<Constraint> builderA =
            new SingleConstraintBuilder().withInSetConstraint(fooField, new Object[]{1, 2, "hello"});
        final ConstraintChainBuilder<Constraint> builderB =
            new SingleConstraintBuilder().withInSetConstraint(barField, new Object[]{"A", "B"});
        final ConstraintChainBuilder<Constraint> builderC
            = new SingleConstraintBuilder().withEqualToConstraint(barField, "A");

        Rule nestedIfRule = new RuleBuilder(ruleName)
            .withIfConstraint(new IfBuilder()
                .withIf(new IfBuilder()
                    .withIf(builderA)
                    .withThen(builderB)
                )
                .withThen(builderC)
            )
            .build();

        Profile inputProfile = new Profile(
            Arrays.asList(fooField, barField),
            Collections.singletonList(nestedIfRule),
            "Nested if profile"
        );

        //Act
        List<Profile> violatedProfiles = target.violate(inputProfile);

        //Assert
        Rule violatedIfRule = new RuleBuilder(ruleName)
            .withAndConstraint(new AndBuilder()
                .withIfConstraint(new IfBuilder()
                    .withIf(builderA)
                    .withThen(builderB)
                )
                .appendBuilder(builderC.violate())
            )
            .build();

        List<Profile> expectedViolatedProfiles = Collections.singletonList(
            new ViolatedProfile(
                nestedIfRule,
                new ProfileFields(Arrays.asList(fooField, barField)),
                Collections.singletonList(violatedIfRule),
                "Nested if profile -- Violating: " + ruleName
            )
        );

        assertProfileListsAreEquivalent(violatedProfiles, expectedViolatedProfiles);
        verify(mockManifestWriter, times(1))
            .writeManifest(anyListOf(ViolatedProfile.class), same(mockPath));
    }

    /**
     * Tests that an if-then-else constraint nested inside the conditional of another if-then-else constraint violates
     * as expected.
     * In shorthand where A,B,C,D,E are atomic constraints:
     * VIOLATE(IF(IF A THEN B ELSE C) THEN D ELSE E)
     *   ->   ((IF A THEN B ELSE C) AND VIOLATE(D)) OR (VIOLATE(IF A THEN B ELSE C) AND VIOLATE(E))
     *   ->   ((IF A THEN B ELSE C) AND VIOLATE(D)) OR (((A AND VIOLATE(B)) OR (VIOLATE(A) AND VIOLATE(C))) AND VIOLATE(E))
     * @throws IOException if the manifest writer fails to write.
     */
    @Test
    public void violate_nestedIfThenElseInsideIf_producesViolatedProfiles() throws IOException {
        //Arrange
        Field fooField = new Field("foo");
        Field barField = new Field("bar");
        String ruleName = "Nested if rule";

        ConstraintChainBuilder<Constraint> builderA = new SingleConstraintBuilder().withEqualToConstraint(fooField, "A");
        ConstraintChainBuilder<Constraint> builderB = new SingleConstraintBuilder().withEqualToConstraint(barField, "B");
        ConstraintChainBuilder<Constraint> builderC = new SingleConstraintBuilder().withOfLengthConstraint(fooField, 1);
        ConstraintChainBuilder<Constraint> builderD = new SingleConstraintBuilder().withOfLengthConstraint(barField, 1);
        ConstraintChainBuilder<Constraint> builderE = new SingleConstraintBuilder().withOfTypeConstraint(fooField, IsOfTypeConstraint.Types.STRING);

        Rule nestedIfThenElseRule = new RuleBuilder(ruleName)
            .withIfConstraint(new IfBuilder()
                .withIf(new IfBuilder()
                    .withIf(builderA)
                    .withThen(builderB)
                    .withElse(builderC)
                )
                .withThen(builderD)
                .withElse(builderE)
            )
            .build();

        Profile inputProfile = new Profile(
            Arrays.asList(fooField, barField),
            Collections.singletonList(nestedIfThenElseRule),
            "Nested if then else profile"
        );

        //Act
        List<Profile> violatedProfiles = target.violate(inputProfile);

        //Assert
        //((IF A THEN B ELSE C) AND VIOLATE(D)) OR (((A AND VIOLATE(B)) OR (VIOLATE(A) AND VIOLATE(C))) AND VIOLATE(E))
        Rule violatedIfRule = new RuleBuilder(ruleName)
            .withOrConstraint(new OrBuilder()
                .withAndConstraint(new AndBuilder()
                    .withIfConstraint(new IfBuilder()
                        .withIf(builderA)
                        .withThen(builderB)
                        .withElse(builderC)
                    )
                    .appendBuilder(builderD.violate())
                )
                .withAndConstraint(new AndBuilder()
                    .withOrConstraint(new OrBuilder()
                        .withAndConstraint(new AndBuilder()
                            .appendBuilder(builderA)
                            .appendBuilder(builderB.violate())
                        )
                        .withAndConstraint(new AndBuilder()
                            .appendBuilder(builderA.violate())
                            .appendBuilder(builderC.violate())
                        )
                    )
                    .appendBuilder(builderE.violate())
                )
            )
            .build();

        List<Profile> expectedViolatedProfiles = Collections.singletonList(
            new ViolatedProfile(
                nestedIfThenElseRule,
                new ProfileFields(Arrays.asList(fooField, barField)),
                Collections.singletonList(violatedIfRule),
                "Nested if then else profile -- Violating: " + ruleName
            )
        );

        assertProfileListsAreEquivalent(violatedProfiles, expectedViolatedProfiles);
        verify(mockManifestWriter, times(1))
            .writeManifest(anyListOf(ViolatedProfile.class), same(mockPath));
    }

    /**
     * Tests that an if-then-else constraint nested inside the then clause of another if-then-else constraint violates
     * as expected.
     * In shorthand where A,B,C,D,E are atomic constraints:
     * VIOLATE(IF A THEN (IF B THEN C ELSE D) ELSE E)
     *   ->   (A AND VIOLATE(IF B THEN C ELSE D)) OR (VIOLATE(A) AND VIOLATE(E))
     *   ->   (A AND ((B AND VIOLATE(C)) OR (VIOLATE(B) AND VIOLATE(D))) OR (VIOLATE(A) AND VIOLATE(E))
     * @throws IOException if the manifest writer fails to write.
     */
    @Test
    public void violate_nestedIfThenElseInsideThen_producesViolatedProfiles() throws IOException {
        //Arrange
        Field fooField = new Field("foo");
        Field barField = new Field("bar");
        String ruleName = "Nested if rule";

        ConstraintChainBuilder<Constraint> builderA = new SingleConstraintBuilder().withEqualToConstraint(fooField, "A");
        ConstraintChainBuilder<Constraint> builderB = new SingleConstraintBuilder().withEqualToConstraint(barField, "B");
        ConstraintChainBuilder<Constraint> builderC = new SingleConstraintBuilder().withOfLengthConstraint(fooField, 1);
        ConstraintChainBuilder<Constraint> builderD = new SingleConstraintBuilder().withOfLengthConstraint(barField, 1);
        ConstraintChainBuilder<Constraint> builderE = new SingleConstraintBuilder().withOfTypeConstraint(fooField, IsOfTypeConstraint.Types.STRING);

        Rule nestedIfThenElseRule = new RuleBuilder(ruleName)
            .withIfConstraint(new IfBuilder()
                .withIf(builderA)
                .withThen(new IfBuilder()
                    .withIf(builderB)
                    .withThen(builderC)
                    .withElse(builderD)
                )
                .withElse(builderE)
            )
            .build();

        Profile inputProfile = new Profile(
            Arrays.asList(fooField, barField),
            Collections.singletonList(nestedIfThenElseRule),
            "Nested if then else profile"
        );

        //Act
        List<Profile> violatedProfiles = target.violate(inputProfile);

        //Assert
        //(A AND ((B AND VIOLATE(C)) OR (VIOLATE(B) AND VIOLATE(D))) OR (VIOLATE(A) AND VIOLATE(E))
        Rule violatedIfRule = new RuleBuilder(ruleName)
            .withOrConstraint(new OrBuilder()
                .withAndConstraint(new AndBuilder()
                    .appendBuilder(builderA)
                    .withOrConstraint(new OrBuilder()
                        .withAndConstraint(new AndBuilder()
                            .appendBuilder(builderB)
                            .appendBuilder(builderC.violate())
                        )
                        .withAndConstraint(new AndBuilder()
                            .appendBuilder(builderB.violate())
                            .appendBuilder(builderD.violate())
                        )
                    )
                )
                .withAndConstraint(new AndBuilder()
                    .appendBuilder(builderA.violate())
                    .appendBuilder(builderE.violate())
                )
            )
            .build();

        List<Profile> expectedViolatedProfiles = Collections.singletonList(
            new ViolatedProfile(
                nestedIfThenElseRule,
                new ProfileFields(Arrays.asList(fooField, barField)),
                Collections.singletonList(violatedIfRule),
                "Nested if then else profile -- Violating: " + ruleName
            )
        );

        assertProfileListsAreEquivalent(violatedProfiles, expectedViolatedProfiles);
        verify(mockManifestWriter, times(1))
            .writeManifest(anyListOf(ViolatedProfile.class), same(mockPath));
    }

    /**
     * Tests that an if-then-else constraint nested inside the else clause of another if-then-else constraint violates
     * as expected.
     * In shorthand where A,B,C,D,E are atomic constraints:
     * VIOLATE(IF A THEN B ELSE (IF C THEN D ELSE E))
     *   ->   (A AND VIOLATE(B)) OR (VIOLATE(A) AND VIOLATE(IF C THEN D ELSE E))
     *   ->   (A AND VIOLATE(B)) OR (VIOLATE(A) AND ((C AND VIOLATE(D)) OR (VIOLATE(C) AND VIOLATE(E))))
     * @throws IOException if the manifest writer fails to write.
     */
    @Test
    public void violate_nestedIfThenElseInsideElse_producesViolatedProfiles() throws IOException {
        //Arrange
        Field fooField = new Field("foo");
        Field barField = new Field("bar");
        String ruleName = "Nested if rule";

        ConstraintChainBuilder<Constraint> builderA = new SingleConstraintBuilder().withEqualToConstraint(fooField, "A");
        ConstraintChainBuilder<Constraint> builderB = new SingleConstraintBuilder().withEqualToConstraint(barField, "B");
        ConstraintChainBuilder<Constraint> builderC = new SingleConstraintBuilder().withOfLengthConstraint(fooField, 1);
        ConstraintChainBuilder<Constraint> builderD = new SingleConstraintBuilder().withOfLengthConstraint(barField, 1);
        ConstraintChainBuilder<Constraint> builderE = new SingleConstraintBuilder().withOfTypeConstraint(fooField, IsOfTypeConstraint.Types.STRING);

        Rule nestedIfThenElseRule = new RuleBuilder(ruleName)
            .withIfConstraint(new IfBuilder()
                .withIf(builderA)
                .withThen(builderB)
                .withElse(new IfBuilder()
                    .withIf(builderC)
                    .withThen(builderD)
                    .withElse(builderE)
                )
            )
            .build();

        Profile inputProfile = new Profile(
            Arrays.asList(fooField, barField),
            Collections.singletonList(nestedIfThenElseRule),
            "Nested if then else profile"
        );

        //Act
        List<Profile> violatedProfiles = target.violate(inputProfile);

        //Assert
        //(A AND VIOLATE(B)) OR (VIOLATE(A) AND ((C AND VIOLATE(D)) OR (VIOLATE(C) AND VIOLATE(E))))
        Rule violatedIfRule = new RuleBuilder(ruleName)
            .withOrConstraint(new OrBuilder()
                .withAndConstraint(new AndBuilder()
                    .appendBuilder(builderA)
                    .appendBuilder(builderB.violate())
                )
                .withAndConstraint(new AndBuilder()
                    .appendBuilder(builderA.violate())
                    .withOrConstraint(new OrBuilder()
                        .withAndConstraint(new AndBuilder()
                            .appendBuilder(builderC)
                            .appendBuilder(builderD.violate())
                        )
                        .withAndConstraint(new AndBuilder()
                            .appendBuilder(builderC.violate())
                            .appendBuilder(builderE.violate())
                        )
                    )
                )
            )
            .build();

        List<Profile> expectedViolatedProfiles = Collections.singletonList(
            new ViolatedProfile(
                nestedIfThenElseRule,
                new ProfileFields(Arrays.asList(fooField, barField)),
                Collections.singletonList(violatedIfRule),
                "Nested if then else profile -- Violating: " + ruleName
            )
        );

        assertProfileListsAreEquivalent(violatedProfiles, expectedViolatedProfiles);
        verify(mockManifestWriter, times(1))
            .writeManifest(anyListOf(ViolatedProfile.class), same(mockPath));
    }
}
