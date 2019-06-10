package com.scottlogic.deg.generator.inputs.profileviolation;

import com.scottlogic.deg.common.profile.*;
import com.scottlogic.deg.common.profile.constraintdetail.ParsedGranularity;
import com.scottlogic.deg.common.profile.constraints.Constraint;
import com.scottlogic.deg.common.profile.constraints.atomic.*;
import com.scottlogic.deg.common.profile.constraints.grammatical.AndConstraint;
import com.scottlogic.deg.common.profile.constraints.grammatical.ConditionalConstraint;
import com.scottlogic.deg.common.profile.constraints.grammatical.OrConstraint;
import com.scottlogic.deg.generator.builders.*;
import com.scottlogic.deg.generator.violations.filters.ConstraintTypeViolationFilter;
import com.scottlogic.deg.generator.violations.filters.ViolationFilter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.scottlogic.deg.generator.inputs.profileviolation.TypeEqualityHelper.assertProfileListsAreEquivalent;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Defines tests for all business logic involved in Profile Violation.
 */
public class ProfileViolationTests {
    private IndividualRuleProfileViolator profileViolator;
    private ArrayList<ViolationFilter> constraintsToNotViolate;

    private Field field1;
    private Field field2;
    private Field field3;
    private Field field4;
    private Field field5;
    private static final Field STATIC_FIELD = new Field("static field");

    private ConstraintChainBuilder<Constraint> A;
    private ConstraintChainBuilder<Constraint> B;
    private ConstraintChainBuilder<Constraint> C;
    private ConstraintChainBuilder<Constraint> D;
    private ConstraintChainBuilder<Constraint> E;


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

            Arguments.of(IsGranularToNumericConstraint.class, new ParsedGranularity(BigDecimal.ONE)),
            Arguments.of(IsGreaterThanConstantConstraint.class, 100),
            Arguments.of(IsGreaterThanOrEqualToConstantConstraint.class, 200),
            Arguments.of(IsLessThanConstantConstraint.class, 300),
            Arguments.of(IsLessThanOrEqualToConstantConstraint.class, 400)
        );
    }

    private static Stream<Arguments> nestingConstraints() {
        BaseConstraintBuilder<Constraint> notConstraintBuilder = new SingleConstraintBuilder()
            .withEqualToConstraint(STATIC_FIELD, "hello").negate();

        BaseConstraintBuilder<Constraint> violatedNotConstraintBuilder = new SingleConstraintBuilder()
            .withEqualToConstraint(STATIC_FIELD, "hello");

        BaseConstraintBuilder<AndConstraint> andConstraintBuilder = new AndBuilder()
            .withGreaterThanConstraint(STATIC_FIELD, 100)
            .withLessThanConstraint(STATIC_FIELD, 200);

        BaseConstraintBuilder<OrConstraint> violatedAndConstraintBuilder = new OrBuilder()
            .withAndConstraint(new AndBuilder()
                .withGreaterThanConstraint(STATIC_FIELD, 100).negate()
                .withLessThanConstraint(STATIC_FIELD, 200)
            )
            .withAndConstraint(new AndBuilder()
                .withGreaterThanConstraint(STATIC_FIELD, 100)
                .withLessThanConstraint(STATIC_FIELD, 200).negate()
            );

        BaseConstraintBuilder<OrConstraint> orConstraintBuilder = new OrBuilder()
            .withAfterConstraint(STATIC_FIELD, OffsetDateTime.of(2018, 1, 15, 12, 0, 0, 0, ZoneOffset.UTC))
            .withBeforeConstraint(STATIC_FIELD, OffsetDateTime.of(2019, 1, 15, 12, 0, 0, 0, ZoneOffset.UTC));

        BaseConstraintBuilder<AndConstraint> violatedOrConstraintBuilder = new AndBuilder()
            .withAfterConstraint(STATIC_FIELD, OffsetDateTime.of(2018, 1, 15, 12, 0, 0, 0, ZoneOffset.UTC)).negate()
            .withBeforeConstraint(STATIC_FIELD, OffsetDateTime.of(2019, 1, 15, 12, 0, 0, 0, ZoneOffset.UTC)).negate();

        BaseConstraintBuilder<ConditionalConstraint> ifThenConstraintBuilder = new IfBuilder()
            .withIf(new SingleConstraintBuilder().withOfTypeConstraint(STATIC_FIELD, IsOfTypeConstraint.Types.NUMERIC))
            .withThen(new SingleConstraintBuilder().withInSetConstraint(STATIC_FIELD, new Object[]{10, 100}));

        BaseConstraintBuilder<AndConstraint> violatedIfThenConstraintBuilder = new AndBuilder()
            .withOfTypeConstraint(STATIC_FIELD, IsOfTypeConstraint.Types.NUMERIC)
            .withInSetConstraint(STATIC_FIELD, new Object[]{10, 100}).negate();

        BaseConstraintBuilder<ConditionalConstraint> ifThenElseConstraintBuilder = new IfBuilder()
            .withIf(new SingleConstraintBuilder().withOfLengthConstraint(STATIC_FIELD, 5))
            .withThen(new SingleConstraintBuilder().withMatchesRegexConstraint(STATIC_FIELD, Pattern.compile("abcde")))
            .withElse(new SingleConstraintBuilder().withContainsRegexConstraint(STATIC_FIELD, Pattern.compile("z")));

        BaseConstraintBuilder<OrConstraint> violatedIfThenElseConstraintBuilder = new OrBuilder()
            .withAndConstraint(new AndBuilder()
                .withOfLengthConstraint(STATIC_FIELD, 5)
                .withMatchesRegexConstraint(STATIC_FIELD, Pattern.compile("abcde")).negate()
            )
            .withAndConstraint(new AndBuilder()
                .withOfLengthConstraint(STATIC_FIELD, 5).negate()
                .withContainsRegexConstraint(STATIC_FIELD, Pattern.compile("z")).negate()
            );

        return Stream.of(
            Arguments.of(notConstraintBuilder, violatedNotConstraintBuilder),
            Arguments.of(andConstraintBuilder, violatedAndConstraintBuilder),
            Arguments.of(orConstraintBuilder, violatedOrConstraintBuilder),
            Arguments.of(ifThenConstraintBuilder, violatedIfThenConstraintBuilder),
            Arguments.of(ifThenElseConstraintBuilder, violatedIfThenElseConstraintBuilder)
        );
    }

    @BeforeEach
    public void setUp() throws IOException {
        MockitoAnnotations.initMocks(this);

        constraintsToNotViolate = new ArrayList<>();
        IndividualConstraintRuleViolator ruleViolator = new IndividualConstraintRuleViolator(constraintsToNotViolate);
        profileViolator = new IndividualRuleProfileViolator(ruleViolator);
        field1 = new Field("field1");
        field2 = new Field("field2");
        field3 = new Field("field3");
        field4 = new Field("field4");
        field5 = new Field("field5");


        A = new SingleConstraintBuilder().withEqualToConstraint(field1, "A");
        B = new SingleConstraintBuilder().withGreaterThanConstraint(field2, 100);
        C = new SingleConstraintBuilder().withOfLengthConstraint(field3, 10);
        D = new SingleConstraintBuilder().withOfTypeConstraint(field4, IsOfTypeConstraint.Types.NUMERIC);
        E = new SingleConstraintBuilder().withLessThanConstraint(field5, 200);
    }

    @Test
    public void violate_withNoConstraints_producesViolatedEmptyProfile() throws IOException {
        //Arrange
        Rule rule = new RuleBuilder("Empty rule")
            .build();

        TestProfiles testProfiles = createTestProfiles(
            "Empty rule profile",
            Collections.singletonList(field1),
            Collections.singletonList(new RuleViolatedRulePair(rule, rule))
        );

        //Act
        List<Profile> violatedProfiles = (List<Profile>)(List<?>) profileViolator.violate(testProfiles.inputProfile);

        //Assert
        assertProfileListsAreEquivalent(violatedProfiles, testProfiles.expectedViolatedProfiles);
    }

    @ParameterizedTest
    @MethodSource("allAtomicConstraints")
    public void violate_withLinearProfileSingleRuleSingleConstraint_producesViolatedProfile(
        Class<? extends AtomicConstraint> atomicConstraint, Object value
    ) throws IOException {
        //Arrange
        Rule rule = new RuleBuilder("Input Rule")
            .withAtomicConstraint(field1, atomicConstraint, value)
            .build();

        Rule violatedRule = new RuleBuilder("Input Rule")
            .withAtomicConstraint(field1, atomicConstraint, value).negate()
            .build();

        TestProfiles testProfiles = createTestProfiles(
            "Input Profile",
            Collections.singletonList(field1),
            Collections.singletonList(new RuleViolatedRulePair(rule, violatedRule))
        );

        //Act
        List<Profile> violatedProfiles = (List<Profile>)(List<?>) profileViolator.violate(testProfiles.inputProfile);

        //Assert
        assertProfileListsAreEquivalent(violatedProfiles, testProfiles.expectedViolatedProfiles);
    }

    @Test
    public void violate_withFilteredConstraintType_producesViolatedProfile() throws IOException {
        //Arrange
        Rule rule = new RuleBuilder("Input Rule")
            .withGreaterThanConstraint(field1, 100)
            .build();

        constraintsToNotViolate.add(new ConstraintTypeViolationFilter(IsGreaterThanConstantConstraint.class));

        TestProfiles testProfiles = createTestProfiles(
            "Input Profile",
            Collections.singletonList(field1),
            Collections.singletonList(new RuleViolatedRulePair(rule, rule))
        );

        //Act
        List<Profile> violatedProfiles = (List<Profile>)(List<?>) profileViolator.violate(testProfiles.inputProfile);

        //Assert
        assertProfileListsAreEquivalent(violatedProfiles, testProfiles.expectedViolatedProfiles);
    }

    /**
     * Tests that the violator can take a profile with a single rule with two different types of constraints for two
     * fields and return the correct violated profile.
     */
    @Test
    public void violate_withLinearProfileSingleRule_producesViolatedProfile() throws IOException {
        //Arrange
        Rule rule1 = new RuleBuilder("Rule 1")
            .withLessThanConstraint(field1, 100)
            .withGreaterThanConstraint(field2, 200)
            .build();

        Rule violatedRule1 = new RuleBuilder("Rule 1")
            .withAndConstraint(new AndBuilder()
                .withLessThanConstraint(field1, 100)
                .withGreaterThanConstraint(field2, 200)
            ).negate()
            .build();

        TestProfiles testProfiles = createTestProfiles(
            "Profile 1",
            Arrays.asList(field1, field2),
            Collections.singletonList(new RuleViolatedRulePair(rule1, violatedRule1))
        );

        //Act
        List<Profile> violatedProfiles = (List<Profile>)(List<?>) profileViolator.violate(testProfiles.inputProfile);

        //Assert
        assertProfileListsAreEquivalent(violatedProfiles, testProfiles.expectedViolatedProfiles);
    }

    /**
     * Tests that the violator can take a profile with a single rule with two different types of constraints including a
     * not for two fields and return the correct violated profile.
     */
    @Test
    public void violate_withLinearProfileSingleRuleIncludingNot_producesViolatedProfile() throws IOException {
        //Arrange
        Rule rule = new RuleBuilder("Rule 1")
            .withLessThanConstraint(field1, 100).negate()
            .withGreaterThanConstraint(field2, 200)
            .build();

        Rule violatedRule = new RuleBuilder("Rule 1")
            .withAndConstraint(new AndBuilder()
                .withLessThanConstraint(field1, 100).negate()
                .withGreaterThanConstraint(field2, 200)
            ).negate()
            .build();

        TestProfiles testProfiles = createTestProfiles(
            "Nested if then else profile",
            Arrays.asList(field1, field2),
            Collections.singletonList(new RuleViolatedRulePair(rule, violatedRule))
        );

        //Act
        List<Profile> violatedProfiles = (List<Profile>)(List<?>) profileViolator.violate(testProfiles.inputProfile);

        assertProfileListsAreEquivalent(violatedProfiles, testProfiles.expectedViolatedProfiles);
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
        Rule rule1 = new RuleBuilder("Rule 1")
            .withLessThanConstraint(field1, 100)
            .withLessThanConstraint(field1, 200)
            .build();

        Rule rule2 = new RuleBuilder("Rule 2")
            .withGreaterThanConstraint(field1, 10)
            .withGreaterThanConstraint(field1, 15)
            .build();

        Rule violatedRule1 = new RuleBuilder("Rule 1")
            .withAndConstraint(new AndBuilder()
                .withLessThanConstraint(field1, 100)
                .withLessThanConstraint(field1, 200)
            ).negate()
            .build();

        Rule violatedRule2 = new RuleBuilder("Rule 2")
            .withAndConstraint(new AndBuilder()
                .withLessThanConstraint(field1, 10)
                .withLessThanConstraint(field1, 15)
            ).negate()
            .build();

        TestProfiles testProfiles = createTestProfiles(
            "Profile 1",
            Collections.singletonList(field1),
            Arrays.asList(new RuleViolatedRulePair(rule1, violatedRule1), new RuleViolatedRulePair(rule2, violatedRule2))
        );

        //Act
        List<Profile> violatedProfiles = (List<Profile>)(List<?>) profileViolator.violate(testProfiles.inputProfile);

        //Assert
        assertProfileListsAreEquivalent(violatedProfiles, testProfiles.expectedViolatedProfiles);
    }

    @Test
    public void violate_callsViolateRuleForInputtedRule() throws IOException {
        //Arrange
        RuleViolator ruleViolator = Mockito.mock(RuleViolator.class);
        profileViolator = new IndividualRuleProfileViolator(ruleViolator);
        RuleInformation ruleInformation = Mockito.mock(RuleInformation.class);
        Mockito.when(ruleInformation.getDescription()).thenReturn("Mock Description");
        Rule rule = Mockito.mock(Rule.class);
        Mockito.when(rule.getRuleInformation()).thenReturn(ruleInformation);
        List<Rule> rules = new ArrayList<>();
        rules.add(rule);
        Profile profile = Mockito.mock(Profile.class);
        Mockito.when(profile.getRules()).thenReturn(rules);
        ArgumentCaptor<Rule> argument = ArgumentCaptor.forClass(Rule.class);

        //Act
        profileViolator.violate(profile);

        //Assert
        Mockito.verify(ruleViolator, Mockito.times(1)).violateRule(argument.capture());
        assertEquals(rule, argument.getValue());
    }

    private TestProfiles createTestProfiles(String description, List<Field> fields, List<RuleViolatedRulePair> ruleViolationHolders) {
        Profile inputProfile = new Profile(fields, getRulesFromPair(ruleViolationHolders), description);
        List<Profile> violatedProfiles = createViolatedProfiles(description, fields, ruleViolationHolders);

        return new TestProfiles(inputProfile, violatedProfiles);
    }

    private List<Profile> createViolatedProfiles(String description,
                                                 List<Field> fields,
                                                 List<RuleViolatedRulePair> ruleViolationHolders) {
        return ruleViolationHolders
            .stream()
            .map(p -> createViolatedProfile(description, fields, ruleViolationHolders, p.getRule()))
            .collect(Collectors.toList());
    }

    private Profile createViolatedProfile(String description,
                                          List<Field> fields,
                                          List<RuleViolatedRulePair> ruleViolationHolders,
                                          Rule rule) {
        List<Rule> newRuleList = ruleViolationHolders
            .stream()
            .map(h -> h.getRule().equals(rule) ? h.getViolatedRule() : h.getRule())
            .collect(Collectors.toList());

        String processedDescription = description + " -- Violating: " + rule.getRuleInformation().getDescription();

        return new ViolatedProfile(rule, new ProfileFields(fields), newRuleList, processedDescription);
    }

    private List<Rule> getRulesFromPair(List<RuleViolatedRulePair> pair) {
        return pair
            .stream()
            .map(RuleViolatedRulePair::getRule)
            .collect(Collectors.toList());
    }


    private class RuleViolatedRulePair {

        private final Rule rule;
        private final Rule violatedRule;

        private RuleViolatedRulePair(Rule rule, Rule violatedRule) {
            this.rule = rule;
            this.violatedRule = violatedRule;
        }

        private Rule getRule() {
            return rule;
        }

        private Rule getViolatedRule() {
            return violatedRule;
        }
    }

    private class TestProfiles {
        final Profile inputProfile;
        final List<Profile> expectedViolatedProfiles;

        TestProfiles(Profile inputProfile, List<Profile> expectedViolatedProfiles) {
            this.inputProfile = inputProfile;
            this.expectedViolatedProfiles = expectedViolatedProfiles;
        }
    }
}
