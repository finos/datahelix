package com.scottlogic.deg.generator.inputs.profileviolation;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.Profile;
import com.scottlogic.deg.generator.ProfileFields;
import com.scottlogic.deg.generator.Rule;
import com.scottlogic.deg.generator.builders.*;
import com.scottlogic.deg.generator.constraints.Constraint;
import com.scottlogic.deg.generator.constraints.atomic.*;
import com.scottlogic.deg.generator.outputs.manifest.ManifestWriter;
import com.scottlogic.deg.generator.restrictions.ParsedGranularity;
import com.scottlogic.deg.generator.violations.ViolatedProfile;
import com.scottlogic.deg.generator.violations.filters.ViolationFilter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static com.scottlogic.deg.generator.inputs.profileviolation.TypeEqualityHelper.assertProfileListsAreEquivalent;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Mockito.*;

/**
 * Defines tests for all business logic involved in Profile Violation.
 */
public class ProfileViolationTests {
    private IndividualRuleProfileViolator target;
    private ArrayList<ViolationFilter> constraintsToNotViolate;

    @Mock
    private ManifestWriter mockManifestWriter;
    @Mock
    private Path mockPath;

    private static Stream<Arguments> allAtomicConstraints() {
        OffsetDateTime sampleDate = OffsetDateTime.of(2019, 1, 15, 12, 0, 0, 0, ZoneOffset.UTC);
        final HashSet<Object> sampleSet = new HashSet<>(Arrays.asList("hello", 10));

        return Stream.of(
            Arguments.of(FormatConstraint.class, "%d"),

            Arguments.of(IsInSetConstraint.class, sampleSet),
            Arguments.of(IsNullConstraint.class, null),
            Arguments.of(IsOfTypeConstraint.class, IsOfTypeConstraint.Types.STRING),
            Arguments.of(MatchesStandardConstraint.class, StandardConstraintTypes.ISIN),

            Arguments.of(ContainsRegexConstraint.class, Pattern.compile("\\w+")),
            Arguments.of(MatchesRegexConstraint.class, Pattern.compile("\\d+")),
            Arguments.of(IsStringLongerThanConstraint.class, 10),
            Arguments.of(IsStringShorterThanConstraint.class, 20),
            Arguments.of(StringHasLengthConstraint.class, 15),

            Arguments.of(IsAfterConstantDateTimeConstraint.class, sampleDate),
            Arguments.of(IsAfterOrEqualToConstantDateTimeConstraint.class, sampleDate.plusDays(1)),
            Arguments.of(IsBeforeConstantDateTimeConstraint.class, sampleDate.minusDays(1)),
            Arguments.of(IsBeforeOrEqualToConstantDateTimeConstraint.class, sampleDate.plusDays(2)),

            Arguments.of(IsGranularToConstraint.class, new ParsedGranularity(BigDecimal.ONE)),
            Arguments.of(IsGreaterThanConstantConstraint.class, 100),
            Arguments.of(IsGreaterThanOrEqualToConstantConstraint.class, 200),
            Arguments.of(IsLessThanConstantConstraint.class, 300),
            Arguments.of(IsLessThanOrEqualToConstantConstraint.class, 400)
        );
    }

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


    @ParameterizedTest
    @MethodSource("allAtomicConstraints")
    public void violate_withLinearProfileSingleRuleSingleConstraint_producesViolatedProfile(
        Class<? extends AtomicConstraint> atomicConstraint, Object value
    ) throws IOException {
        //Arrange
        Field fooField = new Field("foo");

        ConstraintChainBuilder<Rule> inputRuleBuilder = new RuleBuilder("Input Rule")
            .withAtomicConstraint(fooField, atomicConstraint, value);

        Profile inputProfile = new Profile(
            Collections.singletonList(fooField),
            Collections.singletonList(inputRuleBuilder.build()),
            "Input Profile");

        //Act
        List<Profile> violatedProfiles = target.violate(inputProfile);

        //Assert
        Profile violatedProfile1 = new ViolatedProfile(
            inputRuleBuilder.build(),
            new ProfileFields(Collections.singletonList(fooField)),
            Arrays.asList(inputRuleBuilder.negate().violate().build()),
            "Input Profile -- Violating: Input Rule"
        );

        List<Profile> expectedViolatedProfiles = Collections.singletonList(violatedProfile1);

        assertProfileListsAreEquivalent(violatedProfiles, expectedViolatedProfiles);
        verify(mockManifestWriter, times(1))
            .writeManifest(anyListOf(ViolatedProfile.class), same(mockPath));
    }

    /**
     * Tests that the violator can take a profile with a single rule with two different types of constraints for two
     * fields and return the correct violated profile.
     */
    @Test
    public void violate_withLinearProfileSingleRule_producesViolatedProfile() throws IOException {
        //Arrange
        Field fooField = new Field("foo");
        Field barField = new Field("bar");
        Rule rule1 = new RuleBuilder("Rule 1")
            .withLessThanConstraint(fooField, 100)
            .withGreaterThanConstraint(barField, 200)
            .build();

        Profile inputProfile = new Profile(
            Collections.singletonList(fooField),
            Collections.singletonList(rule1),
            "Profile 1");

        //Act
        List<Profile> violatedProfiles = target.violate(inputProfile);

        //Assert
        Rule violatedRule1 = new RuleBuilder("Rule 1")
            .withOrConstraint(new OrBuilder()
                .withAndConstraint(new AndBuilder()
                    .withLessThanConstraint(fooField, 100).negate().violate()
                    .withGreaterThanConstraint(barField, 200)
                )
                .withAndConstraint(new AndBuilder()
                    .withLessThanConstraint(fooField, 100)
                    .withGreaterThanConstraint(barField, 200).negate().violate()
                )
            )
            .build();

        Profile violatedProfile1 = new ViolatedProfile(
            rule1,
            new ProfileFields(Collections.singletonList(fooField)),
            Collections.singletonList(violatedRule1),
            "Profile 1 -- Violating: Rule 1"
        );
        List<Profile> expectedViolatedProfiles = Collections.singletonList(violatedProfile1);

        assertProfileListsAreEquivalent(violatedProfiles, expectedViolatedProfiles);
        verify(mockManifestWriter, times(1))
            .writeManifest(anyListOf(ViolatedProfile.class), same(mockPath));
    }

    /**
     * Tests that the violator can take a profile with a single rule with two different types of constraints including a
     * not for two fields and return the correct violated profile.
     */
    @Test
    public void violate_withLinearProfileSingleRuleIncludingNot_producesViolatedProfile() throws IOException {
        //Arrange
        Field fooField = new Field("foo");
        Field barField = new Field("bar");
        Rule rule1 = new RuleBuilder("Rule 1")
            .withLessThanConstraint(fooField, 100).negate()
            .withGreaterThanConstraint(barField, 200)
            .build();

        Profile inputProfile = new Profile(
            Collections.singletonList(fooField),
            Collections.singletonList(rule1),
            "Profile 1");

        //Act
        List<Profile> violatedProfiles = target.violate(inputProfile);

        //Assert
        Rule violatedRule1 = new RuleBuilder("Rule 1")
            .withOrConstraint(new OrBuilder()
                .withAndConstraint(new AndBuilder()
                    .withLessThanConstraint(fooField, 100).violate()
                    .withGreaterThanConstraint(barField, 200)
                )
                .withAndConstraint(new AndBuilder()
                    .withLessThanConstraint(fooField, 100).negate()
                    .withGreaterThanConstraint(barField, 200).negate().violate()
                )
            )
            .build();

        Profile violatedProfile1 = new ViolatedProfile(
            rule1,
            new ProfileFields(Collections.singletonList(fooField)),
            Collections.singletonList(violatedRule1),
            "Profile 1 -- Violating: Rule 1"
        );
        List<Profile> expectedViolatedProfiles = Collections.singletonList(violatedProfile1);

        assertProfileListsAreEquivalent(violatedProfiles, expectedViolatedProfiles);
        verify(mockManifestWriter, times(1))
            .writeManifest(anyListOf(ViolatedProfile.class), same(mockPath));
    }

    /**
     * Tests that the violator can take a profile of two simple rules and return two violated profiles with the correct
     * negation combinations.
     * Input: Profile with 2 fields foo and bar, 2 single atomic constraint rules affecting foo and bar
     * Output: 2 Profiles, one with rule 1 negated and rule 2 unaffected, one with rule 1 unaffected and rule 2 negated
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
                    .withLessThanConstraint(fooField, 100).negate().violate()
                    .withLessThanConstraint(fooField, 200)
                )
                .withAndConstraint(new AndBuilder()
                    .withLessThanConstraint(fooField, 100)
                    .withLessThanConstraint(fooField, 200).negate().violate()
                )
            )
            .build();

        Rule violatedRule2 = new RuleBuilder("Rule 2")
            .withOrConstraint(new OrBuilder()
                .withAndConstraint(new AndBuilder()
                    .withGreaterThanConstraint(fooField, 10).negate().violate()
                    .withGreaterThanConstraint(fooField, 15)
                )
                .withAndConstraint(new AndBuilder()
                    .withGreaterThanConstraint(fooField, 10)
                    .withGreaterThanConstraint(fooField, 15).negate().violate()
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
     *
     * @throws IOException if the manifest writer fails to write.
     */
    @Test
    public void violate_nestedIfThenInsideIf_producesViolatedProfiles() throws IOException {
        //Arrange
        Field fooField = new Field("foo");
        Field barField = new Field("bar");
        String ruleName = "Nested if rule";

        final ConstraintChainBuilder<Constraint> A =
            new SingleConstraintBuilder().withInSetConstraint(fooField, new Object[] {1, 2, "hello"});
        final ConstraintChainBuilder<Constraint> B =
            new SingleConstraintBuilder().withInSetConstraint(barField, new Object[] {"A", "B"});
        final ConstraintChainBuilder<Constraint> C
            = new SingleConstraintBuilder().withEqualToConstraint(barField, "A");

        Rule nestedIfRule = new RuleBuilder(ruleName)
            .withIfConstraint(new IfBuilder()
                .withIf(new IfBuilder()
                    .withIf(A)
                    .withThen(B)
                )
                .withThen(C)
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
                    .withIf(A)
                    .withThen(B)
                )
                .appendBuilder(C.negate().violate())
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
     * ->   ((IF A THEN B ELSE C) AND VIOLATE(D)) OR (VIOLATE(IF A THEN B ELSE C) AND VIOLATE(E))
     * ->   ((IF A THEN B ELSE C) AND VIOLATE(D)) OR (((A AND VIOLATE(B)) OR (VIOLATE(A) AND VIOLATE(C))) AND VIOLATE(E))
     *
     * @throws IOException if the manifest writer fails to write.
     */
    @Test
    public void violate_nestedIfThenElseInsideIf_producesViolatedProfiles() throws IOException {
        //Arrange
        Field fooField = new Field("foo");
        Field barField = new Field("bar");
        String ruleName = "Nested if rule";

        ConstraintChainBuilder<Constraint> A = new SingleConstraintBuilder().withEqualToConstraint(fooField, "A");
        ConstraintChainBuilder<Constraint> B = new SingleConstraintBuilder().withEqualToConstraint(barField, "B");
        ConstraintChainBuilder<Constraint> C = new SingleConstraintBuilder().withOfLengthConstraint(fooField, 1);
        ConstraintChainBuilder<Constraint> D = new SingleConstraintBuilder().withOfLengthConstraint(barField, 1);
        ConstraintChainBuilder<Constraint> E = new SingleConstraintBuilder().withOfTypeConstraint(fooField, IsOfTypeConstraint.Types.STRING);

        Rule nestedIfThenElseRule = new RuleBuilder(ruleName)
            .withIfConstraint(new IfBuilder()
                .withIf(new IfBuilder()
                    .withIf(A)
                    .withThen(B)
                    .withElse(C)
                )
                .withThen(D)
                .withElse(E)
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
                        .withIf(A)
                        .withThen(B)
                        .withElse(C)
                    )
                    .appendBuilder(D.negate().violate())
                )
                .withAndConstraint(new AndBuilder()
                    .withOrConstraint(new OrBuilder()
                        .withAndConstraint(new AndBuilder()
                            .appendBuilder(A)
                            .appendBuilder(B.negate().violate())
                        )
                        .withAndConstraint(new AndBuilder()
                            .appendBuilder(A.negate().violate())
                            .appendBuilder(C.negate().violate())
                        )
                    )
                    .appendBuilder(E.negate().violate())
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
     * ->   (A AND VIOLATE(IF B THEN C ELSE D)) OR (VIOLATE(A) AND VIOLATE(E))
     * ->   (A AND ((B AND VIOLATE(C)) OR (VIOLATE(B) AND VIOLATE(D))) OR (VIOLATE(A) AND VIOLATE(E))
     *
     * @throws IOException if the manifest writer fails to write.
     */
    @Test
    public void violate_nestedIfThenElseInsideThen_producesViolatedProfiles() throws IOException {
        //Arrange
        Field fooField = new Field("foo");
        Field barField = new Field("bar");
        String ruleName = "Nested if rule";

        ConstraintChainBuilder<Constraint> A = new SingleConstraintBuilder().withEqualToConstraint(fooField, "A");
        ConstraintChainBuilder<Constraint> B = new SingleConstraintBuilder().withEqualToConstraint(barField, "B");
        ConstraintChainBuilder<Constraint> C = new SingleConstraintBuilder().withOfLengthConstraint(fooField, 1);
        ConstraintChainBuilder<Constraint> D = new SingleConstraintBuilder().withOfLengthConstraint(barField, 1);
        ConstraintChainBuilder<Constraint> E = new SingleConstraintBuilder().withOfTypeConstraint(fooField, IsOfTypeConstraint.Types.STRING);

        Rule nestedIfThenElseRule = new RuleBuilder(ruleName)
            .withIfConstraint(new IfBuilder()
                .withIf(A)
                .withThen(new IfBuilder()
                    .withIf(B)
                    .withThen(C)
                    .withElse(D)
                )
                .withElse(E)
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
                    .appendBuilder(A)
                    .withOrConstraint(new OrBuilder()
                        .withAndConstraint(new AndBuilder()
                            .appendBuilder(B)
                            .appendBuilder(C.negate().violate())
                        )
                        .withAndConstraint(new AndBuilder()
                            .appendBuilder(B.negate().violate())
                            .appendBuilder(D.negate().violate())
                        )
                    )
                )
                .withAndConstraint(new AndBuilder()
                    .appendBuilder(A.negate().violate())
                    .appendBuilder(E.negate().violate())
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
     * ->   (A AND VIOLATE(B)) OR (VIOLATE(A) AND VIOLATE(IF C THEN D ELSE E))
     * ->   (A AND VIOLATE(B)) OR (VIOLATE(A) AND ((C AND VIOLATE(D)) OR (VIOLATE(C) AND VIOLATE(E))))
     *
     * @throws IOException if the manifest writer fails to write.
     */
    @Test
    public void violate_nestedIfThenElseInsideElse_producesViolatedProfiles() throws IOException {
        //Arrange
        Field fooField = new Field("foo");
        Field barField = new Field("bar");
        String ruleName = "Nested if rule";

        ConstraintChainBuilder<Constraint> A = new SingleConstraintBuilder().withEqualToConstraint(fooField, "A");
        ConstraintChainBuilder<Constraint> B = new SingleConstraintBuilder().withEqualToConstraint(barField, "B");
        ConstraintChainBuilder<Constraint> C = new SingleConstraintBuilder().withOfLengthConstraint(fooField, 1);
        ConstraintChainBuilder<Constraint> D = new SingleConstraintBuilder().withOfLengthConstraint(barField, 1);
        ConstraintChainBuilder<Constraint> E = new SingleConstraintBuilder().withOfTypeConstraint(fooField, IsOfTypeConstraint.Types.STRING);

        Rule nestedIfThenElseRule = new RuleBuilder(ruleName)
            .withIfConstraint(new IfBuilder()
                .withIf(A)
                .withThen(B)
                .withElse(new IfBuilder()
                    .withIf(C)
                    .withThen(D)
                    .withElse(E)
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
                    .appendBuilder(A)
                    .appendBuilder(B.negate().violate())
                )
                .withAndConstraint(new AndBuilder()
                    .appendBuilder(A.negate().violate())
                    .withOrConstraint(new OrBuilder()
                        .withAndConstraint(new AndBuilder()
                            .appendBuilder(C)
                            .appendBuilder(D.negate().violate())
                        )
                        .withAndConstraint(new AndBuilder()
                            .appendBuilder(C.negate().violate())
                            .appendBuilder(E.negate().violate())
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
