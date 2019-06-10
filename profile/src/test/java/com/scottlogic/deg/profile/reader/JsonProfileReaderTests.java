package com.scottlogic.deg.profile.reader;

import com.scottlogic.deg.common.profile.constraints.atomic.*;
import com.scottlogic.deg.common.profile.Field;
import com.scottlogic.deg.common.profile.Profile;
import com.scottlogic.deg.common.profile.Rule;
import com.scottlogic.deg.common.profile.constraints.Constraint;
import com.scottlogic.deg.common.profile.constraints.grammatical.AndConstraint;
import com.scottlogic.deg.common.profile.constraints.grammatical.ConditionalConstraint;
import com.scottlogic.deg.common.profile.constraints.grammatical.OrConstraint;
import com.scottlogic.deg.profile.reader.InvalidProfileException;
import com.scottlogic.deg.profile.reader.JsonProfileReader;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.Iterator;
import java.util.function.Consumer;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.core.IsNull.nullValue;

public class JsonProfileReaderTests {
    private String json;
    private Profile profile;

    @BeforeEach
    public void Setup() {
        this.json = null;
        this.profile = null;
    }

    private void givenJson(String json) {
        this.json = json;
    }

    private Profile getResultingProfile() throws IOException {
        if (this.profile == null) {
            JsonProfileReader objectUnderTest = new JsonProfileReader();
            this.profile = objectUnderTest.read(this.json);
        }

        return this.profile;
    }

    private void expectException() {
        Assertions.assertThrows(Exception.class, this::getResultingProfile);
    }

    private void expectInvalidProfileException() {
        Assertions.assertThrows(InvalidProfileException.class, this::getResultingProfile);
    }

    private void expectRules(Consumer<Rule>... ruleAssertions) throws IOException {
        expectMany(this.getResultingProfile().getRules(), ruleAssertions);
    }

    private Consumer<Rule> ruleWithDescription(String expectedDescription) {
        return rule -> Assert.assertThat(rule.ruleInformation.getDescription(), equalTo(expectedDescription));
    }

    private Consumer<Rule> ruleWithConstraints(Consumer<Constraint>... constraintAsserters) {
        return rule -> expectMany(rule.constraints, constraintAsserters);
    }

    private <T> Consumer<Constraint> typedConstraint(Class<T> constraintType, Consumer<T> asserter) {
        return constraint -> {
            Assert.assertThat(constraint, instanceOf(constraintType));

            asserter.accept((T) constraint);
        };
    }

    private Consumer<Field> fieldWithName(String expectedName) {
        return field -> Assert.assertThat(field.name, equalTo(expectedName));
    }

    private void expectFields(Consumer<Field>... fieldAssertions) throws IOException {
        expectMany(this.getResultingProfile().getFields(), fieldAssertions);
    }

    /**
     * Given a set I1, I2, I3... and some consumers A1, A2, A3..., run A1(I1), A2(I2), A3(I3)...
     * This lets us make assertions about each entry in a sequence
     */
    private <T> void expectMany(
            Iterable<T> assertionTargets,
            Consumer<T>... perItemAssertions) {

        Iterator<T> aIterator = assertionTargets.iterator();
        Iterator<Consumer<T>> bIterator = Arrays.asList(perItemAssertions).iterator();

        while (aIterator.hasNext() && bIterator.hasNext()) {
            bIterator.next().accept(aIterator.next());
        }

        if (aIterator.hasNext() || bIterator.hasNext())
            Assert.fail("Sequences had different numbers of elements");
    }


    @Test
    public void shouldDeserialiseSingleField() throws IOException {
        givenJson(
                "{" +
                        "    \"schemaVersion\": \"0.1\"," +
                        "    \"fields\": [ { \"name\": \"f1\" } ]," +
                        "    \"rules\": []" +
                        "}");

        expectFields(
                fieldWithName("f1"));
    }

    @Test
    public void shouldDeserialiseMultipleFields() throws IOException {
        givenJson(
                "{" +
                        "    \"schemaVersion\": \"0.1\"," +
                        "    \"fields\": [ { \"name\": \"f1\" }, { \"name\": \"f2\" } ]," +
                        "    \"rules\": []" +
                        "}");

        expectFields(
                fieldWithName("f1"),
                fieldWithName("f2"));
    }

    @Test
    public void shouldDeserialiseInvalidProfileAsEmptyRule() throws IOException {
        givenJson(
            "{" +
                "    \"schemaVersion\": \"0.1\"," +
                "    \"fields\": [ { \"name\": \"foo\" } ]," +
                "    \"rules\": [" +
                "       { \"field\": \"foo\", \"is\": \"null\" } " +
                "    ]" +
                "}");

        expectInvalidProfileException();
    }

    @Test
    public void shouldGiveDefaultNameToUnnamedRules() throws IOException {
        givenJson(
                "{" +
                        "    \"schemaVersion\": \"0.1\"," +
                        "    \"fields\": [ { \"name\": \"foo\" } ]," +
                        "    \"rules\": [" +
                        "      {" +
                        "        \"constraints\": [" +
                        "            { \"field\": \"foo\", \"is\": \"null\" } " +
                        "        ]" +
                        "      }" +
                        "    ]" +
                        "}");

        expectRules(
                ruleWithDescription("Unnamed rule"));
    }

    @Test
    public void shouldReadNameOfNamedRules() throws IOException {
        givenJson(
                "{" +
                        "    \"schemaVersion\": \"0.1\"," +
                        "    \"fields\": [ { \"name\": \"foo\" } ]," +
                        "    \"rules\": [" +
                        "        {" +
                        "           \"rule\": \"Too rule for school\"," +
                        "           \"constraints\": [" +
                        "               { \"field\": \"foo\", \"is\": \"null\" }" +
                        "           ]" +
                        "        }" +
                        "    ]" +
                        "}");

        expectRules(
                ruleWithDescription("Too rule for school"));
    }

    @Test
    public void shouldNotThrowIsNullWithValueNull() {
        givenJson(
            "{" +
                "    \"schemaVersion\": \"0.1\"," +
                "    \"fields\": [ { \"name\": \"foo\" } ]," +
                "    \"rules\": [" +
                "        {" +
                "           \"rule\": \"Too rule for school\"," +
                "           \"constraints\": [" +
                "               { \"field\": \"foo\", \"is\": \"null\", \"value\": null }" +
                "           ]" +
                "        }" +
                "    ]" +
                "}");

        Assertions.assertDoesNotThrow(
            () -> getResultingProfile());
    }

    @Test
    public void shouldNotThrowIsNullWithValuesNull() {
        givenJson(
            "{" +
                "    \"schemaVersion\": \"0.1\"," +
                "    \"fields\": [ { \"name\": \"foo\" } ]," +
                "    \"rules\": [" +
                "        {" +
                "           \"rule\": \"Too rule for school\"," +
                "           \"constraints\": [" +
                "               { \"field\": \"foo\", \"is\": \"null\", \"values\": null }" +
                "           ]" +
                "        }" +
                "    ]" +
                "}");

        Assertions.assertDoesNotThrow(
            () -> getResultingProfile());
    }

    @Test
    public void shouldDeserialiseIsOfTypeConstraint() throws IOException {
        givenJson(
                "{" +
                        "    \"schemaVersion\": \"0.1\"," +
                        "    \"fields\": [ { \"name\": \"foo\" } ]," +
                        "    \"rules\": [" +
                        "      {" +
                        "        \"constraints\": [" +
                        "        { \"field\": \"foo\", \"is\": \"ofType\", \"value\": \"string\" }" +
                        "        ]" +
                        "      }" +
                        "    ]" +
                        "}");

        expectRules(
                ruleWithConstraints(
                        typedConstraint(
                                IsOfTypeConstraint.class,
                                c -> Assert.assertThat(
                                        c.requiredType,
                                        equalTo(IsOfTypeConstraint.Types.STRING)))));
    }

    @Test
    public void shouldDeserialiseFormatConstraint() throws IOException {
        givenJson(
                "{" +
                        "    \"schemaVersion\": \"0.1\"," +
                        "    \"fields\": [ { \"name\": \"foo\" } ]," +
                        "    \"rules\": [" +
                        "      {" +
                        "        \"constraints\": [" +
                        "        { \"field\": \"foo\", \"is\": \"formattedAs\", \"value\": \"%.5s\" }" +
                        "        ]" +
                        "      }" +
                        "    ]" +
                        "}");

        expectRules(
                ruleWithConstraints(
                        typedConstraint(
                                FormatConstraint.class,
                                c -> Assert.assertThat(
                                        c.format,
                                        equalTo("%.5s")))));
    }

    @Test
    public void shouldDeserialiseIsOfLengthConstraint() throws IOException {
        givenJson(
                "{" +
                        "    \"schemaVersion\": \"0.1\"," +
                        "    \"fields\": [ { \"name\": \"id\" } ]," +
                        "    \"rules\": [" +
                        "      {" +
                        "        \"constraints\": [" +
                        "        { \"field\": \"id\", \"is\": \"ofLength\", \"value\": 5 }" +
                        "        ]" +
                        "      }" +
                        "    ]" +
                        "}");

        expectRules(
                ruleWithConstraints(
                        typedConstraint(
                                StringHasLengthConstraint.class,
                                c -> Assert.assertThat(c.referenceValue, equalTo(5)))));
    }

    @Test
    public void shouldDeserialiseNotWrapper() throws IOException {
        // Arrange
        givenJson(
                "{" +
                        "    \"schemaVersion\": \"0.1\"," +
                        "    \"fields\": [ { \"name\": \"foo\" } ]," +
                        "    \"rules\": [" +
                        "      {" +
                        "        \"constraints\": [" +
                        "        { \"not\": { \"field\": \"foo\", \"is\": \"ofType\", \"value\": \"string\" } }" +
                        "        ]" +
                        "      }" +
                        "    ]" +
                        "}");

        expectRules(
                ruleWithConstraints(
                        typedConstraint(
                                NotConstraint.class,
                                c -> {
                                    Assert.assertThat(
                                            c.negatedConstraint,
                                            instanceOf(IsOfTypeConstraint.class));
                                })));
    }

    @Test
    public void shouldDeserialiseOrConstraint() throws IOException {
        givenJson(
                "{" +
                        "    \"schemaVersion\": \"0.1\"," +
                        "    \"fields\": [ { \"name\": \"foo\" } ]," +
                        "    \"rules\": [" +
                        "      {" +
                        "        \"constraints\": [" +
                        "          {" +
                        "            \"anyOf\": [" +
                        "              { \"field\": \"foo\", \"is\": \"equalTo\", \"value\": \"1\" }," +
                        "              { \"field\": \"foo\", \"is\": \"null\" }" +
                        "            ]" +
                        "          }" +
                        "        ]" +
                        "      }" +
                        "   ]" +
                        "}");

        expectRules(
                ruleWithConstraints(
                        typedConstraint(
                                OrConstraint.class,
                                c -> Assert.assertThat(
                                        c.subConstraints.size(),
                                        equalTo(2)))));
    }

    @Test
    public void shouldDeserialiseAndConstraint() throws IOException {
        givenJson(
                "{" +
                        "    \"schemaVersion\": \"0.1\"," +
                        "    \"fields\": [ { \"name\": \"foo\" } ]," +
                        "    \"rules\": [" +
                        "      {" +
                        "        \"constraints\": [" +
                        "          {" +
                        "           \"allOf\": [" +
                        "             { \"field\": \"foo\", \"is\": \"equalTo\", \"value\": \"1\" }," +
                        "             { \"field\": \"foo\", \"is\": \"null\" }" +
                        "            ]" +
                        "          }" +
                        "        ]" +
                        "      }" +
                        "    ]" +
                        "}");

        expectRules(
                ruleWithConstraints(
                        typedConstraint(
                                AndConstraint.class,
                                c -> Assert.assertThat(
                                        c.subConstraints.size(),
                                        equalTo(2)))));
    }

    @Test
    public void shouldDeserialiseIfConstraint() throws IOException {
        givenJson(
                "{" +
                        "    \"schemaVersion\": \"0.1\"," +
                        "    \"fields\": [ { \"name\": \"foo\" } ]," +
                        "    \"rules\": [" +
                        "      {" +
                        "        \"constraints\": [" +
                        "          {" +
                        "            \"if\": { \"field\": \"foo\", \"is\": \"ofType\", \"value\": \"string\" }," +
                        "            \"then\": { \"field\": \"foo\", \"is\": \"equalTo\", \"value\": \"str!\" }," +
                        "            \"else\": { \"field\": \"foo\", \"is\": \"greaterThan\", \"value\": 3 }" +
                        "          }" +
                        "        ]" +
                        "      }" +
                        "   ]" +
                        "}");

        expectRules(
                ruleWithConstraints(
                        typedConstraint(
                                ConditionalConstraint.class,
                                c -> {
                                    Assert.assertThat(
                                            c.condition,
                                            instanceOf(IsOfTypeConstraint.class));

                                    Assert.assertThat(
                                            c.whenConditionIsTrue,
                                            instanceOf(IsInSetConstraint.class));

                                    Assert.assertThat(
                                            c.whenConditionIsFalse,
                                            instanceOf(IsGreaterThanConstantConstraint.class));
                                })));
    }

    @Test
    public void shouldDeserialiseIfConstraintWithoutElse() throws IOException {
        givenJson(
                "{" +
                        "    \"schemaVersion\": \"0.1\"," +
                        "    \"fields\": [ { \"name\": \"foo\" } ]," +
                        "    \"rules\": [" +
                        "      {" +
                        "        \"constraints\": [" +
                        "          {" +
                        "            \"if\": { \"field\": \"foo\", \"is\": \"ofType\", \"value\": \"string\" }," +
                        "            \"then\": { \"field\": \"foo\", \"is\": \"equalTo\", \"value\": \"str!\" }" +
                        "          }" +
                        "        ]" +
                        "      }" +
                        "    ]" +
                        "}");

        expectRules(
                ruleWithConstraints(
                        typedConstraint(
                                ConditionalConstraint.class,
                                c -> {
                                    Assert.assertThat(
                                            c.condition,
                                            instanceOf(IsOfTypeConstraint.class));

                                    Assert.assertThat(
                                            c.whenConditionIsTrue,
                                            instanceOf(IsInSetConstraint.class));

                                    Assert.assertThat(
                                            c.whenConditionIsFalse,
                                            nullValue());
                                })));
    }

    @Test
    public void shouldDeserialiseOneAsNumericGranularToConstraint() throws IOException {
        givenJson(
            "{" +
            "    \"schemaVersion\": \"0.1\"," +
            "    \"fields\": [ { \"name\": \"foo\" } ]," +
            "    \"rules\": [" +
            "      {" +
            "        \"constraints\": [" +
            "        { \"field\": \"foo\", \"is\": \"granularTo\", \"value\": 1 }" +
            "        ]" +
            "      }" +
            "    ]" +
            "}");

        expectRules(
            ruleWithConstraints(
                typedConstraint(
                    IsGranularToNumericConstraint.class,
                    c -> {
                        Assert.assertThat(
                            c.granularity.getNumericGranularity(),
                            equalTo(new BigDecimal(1)));
                    })));
    }

    @Test
    public void shouldDeserialiseTenthAsNumericGranularToConstraint() throws IOException {
        givenJson(
            "{" +
                "    \"schemaVersion\": \"0.1\"," +
                "    \"fields\": [ { \"name\": \"foo\" } ]," +
                "    \"rules\": [" +
                "      {" +
                "        \"constraints\": [" +
                "        { \"field\": \"foo\", \"is\": \"granularTo\", \"value\": 0.1 }" +
                "        ]" +
                "      }" +
                "    ]" +
                "}");

        expectRules(
            ruleWithConstraints(
                typedConstraint(
                    IsGranularToNumericConstraint.class,
                    c -> {
                        Assert.assertThat(
                            c.granularity.getNumericGranularity(),
                            equalTo(BigDecimal.valueOf(0.1)));
                    })));
    }

    @Test
    public void shouldDisregardTrailingZeroesInNumericGranularities() throws IOException {
        givenJson(
            "{" +
                "    \"schemaVersion\": \"0.1\"," +
                "    \"fields\": [ { \"name\": \"foo\" } ]," +
                "    \"rules\": [" +
                "      {" +
                "        \"constraints\": [" +
                "        { \"field\": \"foo\", \"is\": \"granularTo\", \"value\": 0.100000000 }" +
                "        ]" +
                "      }" +
                "    ]" +
                "}");

        expectRules(
            ruleWithConstraints(
                typedConstraint(
                    IsGranularToNumericConstraint.class,
                    c -> {
                        Assert.assertThat(
                            c.granularity.getNumericGranularity(),
                            equalTo(BigDecimal.valueOf(0.1)));
                    })));
    }

    @Test
    public void shouldAllowValidISO8601DateTime() throws IOException {
        givenJson(
            "{" +
                "    \"schemaVersion\": \"0.1\"," +
                "    \"fields\": [ { \"name\": \"foo\" } ]," +
                "    \"rules\": [" +
                "      {" +
                "        \"constraints\": [" +
                "        { \"field\": \"foo\", \"is\": \"afterOrAt\", \"value\": { \"date\": \"2019-01-01T00:00:00.000\" } }," +
                "        { \"field\": \"foo\", \"is\": \"before\", \"value\": { \"date\": \"2019-01-03T00:00:00.000\" } }" +
                "        ]" +
                "      }" +
                "    ]" +
                "}");

        expectRules(
            ruleWithConstraints(
                typedConstraint(
                    IsAfterOrEqualToConstantDateTimeConstraint.class,
                    c -> {
                        Assert.assertThat(
                            c.referenceValue,
                            equalTo(OffsetDateTime.parse("2019-01-01T00:00:00.000Z")));
                    }),
                typedConstraint(
                    IsBeforeConstantDateTimeConstraint.class,
                    c -> {
                        Assert.assertThat(
                            c.referenceValue,
                            equalTo(OffsetDateTime.parse("2019-01-03T00:00:00.000Z")));
                    })
                )
        );
    }

    @Test
    public void shouldRejectGreaterThanOneNumericGranularityConstraint() {
        givenJson(
            "{" +
            "    \"schemaVersion\": \"0.1\"," +
            "    \"fields\": [ { \"name\": \"foo\" } ]," +
            "    \"rules\": [" +
            "      {" +
            "        \"constraints\": [" +
            "        { \"field\": \"foo\", \"is\": \"granularTo\", \"value\": 2 }" +
            "        ]" +
            "      }" +
            "    ]" +
            "}");

        expectException();
    }

    @Test
    public void shouldRejectNonPowerOfTenNumericGranularityConstraint() {
        givenJson(
            "{" +
            "    \"schemaVersion\": \"0.1\"," +
            "    \"fields\": [ { \"name\": \"foo\" } ]," +
            "    \"rules\": [" +
            "      {" +
            "        \"constraints\": [" +
            "        { \"field\": \"foo\", \"is\": \"granularTo\", \"value\": 0.15 }" +
            "        ]" +
            "      }" +
            "    ]" +
            "}");

        expectException();
    }

    @Test
    public void shouldRejectNonISO8601DateTime() {
        givenJson(
            "{" +
                "    \"schemaVersion\": \"0.1\"," +
                "    \"fields\": [ { \"name\": \"foo\" } ]," +
                "    \"rules\": [" +
                "      {" +
                "        \"constraints\": [" +
                "        { \"field\": \"foo\", \"is\": \"after\", \"value\": \"2018-01-12\" }" +
                "        ]" +
                "      }" +
                "    ]" +
                "}");

        expectException();
    }

    @Test
    public void shouldRejectEqualToWithNullValue() {
        givenJson(
            "{" +
                "    \"schemaVersion\": \"0.1\"," +
                "    \"fields\": [ { \"name\": \"foo\" } ]," +
                "    \"rules\": [" +
                "      {" +
                "        \"constraints\": [" +
                "        { \"field\": \"foo\", \"is\": \"equalTo\", \"value\": null }" +
                "        ]" +
                "      }" +
                "    ]" +
                "}");

        expectInvalidProfileException();
    }

    @Test
    public void shouldRejectLessThanWithNullValue() {
        givenJson(
            "{" +
                "    \"schemaVersion\": \"0.1\"," +
                "    \"fields\": [ { \"name\": \"foo\" } ]," +
                "    \"rules\": [" +
                "      {" +
                "        \"constraints\": [" +
                "        { \"field\": \"foo\", \"is\": \"lessThan\", \"value\": null }" +
                "        ]" +
                "      }" +
                "    ]" +
                "}");

        expectInvalidProfileException();
    }

    @Test
    public void shouldRejectInSetWithANullValue() {
        givenJson(
            "{" +
                "    \"schemaVersion\": \"0.1\"," +
                "    \"fields\": [ { \"name\": \"foo\" } ]," +
                "    \"rules\": [" +
                "      {" +
                "        \"constraints\": [" +
                "        { \"field\": \"foo\", \"is\": \"inSet\", \"values\": [ null ] }" +
                "        ]" +
                "      }" +
                "    ]" +
                "}");

        expectInvalidProfileException();
    }

    @Test
    public void shouldRejectInSetSetToNull() {
        givenJson(
            "{" +
                "    \"schemaVersion\": \"0.1\"," +
                "    \"fields\": [ { \"name\": \"foo\" } ]," +
                "    \"rules\": [" +
                "      {" +
                "        \"constraints\": [" +
                "        { \"field\": \"foo\", \"is\": \"inSet\", \"values\": null }" +
                "        ]" +
                "      }" +
                "    ]" +
                "}");

        expectInvalidProfileException();
    }

    @Test
    public void shouldRejectAllOfWithEmptySet() {
        givenJson("{" +
            "    \"schemaVersion\": \"0.1\"," +
            "    \"fields\": [ { \"name\": \"foo\" } ]," +
            "    \"rules\": [" +
            "      {" +
            "        \"constraints\": [" +
            "        { \"allOf\": [] }" +
            "        ]" +
            "      }" +
            "    ]" +
            "}");

        this.expectInvalidProfileException();
    }

    @Test
    public void shouldRejectAllOfWithEmptySetWithExplicitConstraint() {
        givenJson("{" +
            "    \"schemaVersion\": \"0.1\"," +
            "    \"fields\": [ { \"name\": \"foo\" } ]," +
            "    \"rules\": [{" +
            "        \"rule\": \"foo rule\"," +
            "        \"constraints\": [{" +
            "           \"allOf\": []" +
            "        }]" +
            "    }]" +
            "}");

        this.expectInvalidProfileException();
    }

    @Test
    public void shouldRejectIsConstraintSetToNull() {
        givenJson(
            "{" +
                "    \"schemaVersion\": \"0.1\"," +
                "    \"fields\": [ { \"name\": \"foo\" } ]," +
                "    \"rules\": [" +
                "      {" +
                "        \"constraints\": [" +
                "        { \"field\": \"foo\", \"is\": null }" +
                "        ]" +
                "      }" +
                "    ]" +
                "}");

        expectInvalidProfileException();
    }

    @Test
    public void shouldRejectIsConstraintSetToNullWithRuleAndConstraintFormat() {
        givenJson(
            "{" +
                "    \"schemaVersion\": \"0.1\"," +
                "    \"fields\": [ { \"name\": \"foo\" } ]," +
                "    \"rules\": [" +
                "       {" +
                "        \"rule\": \"fooRule\"," +
                "        \"constraints\": [{ \"field\": \"foo\", \"is\": null }]" +
                "       }" +
                "    ]" +
                "}");

        expectInvalidProfileException();
    }

    @Test
    public void shouldRejectIsConstraintSetToNullForNot() {
        givenJson(
            "{" +
                "    \"schemaVersion\": \"0.1\"," +
                "    \"fields\": [ { \"name\": \"foo\" } ]," +
                "    \"rules\": [" +
                "      {" +
                "        \"constraints\": [" +
                "        { \"not\": { \"field\": \"foo\", \"is\": null } }" +
                "        ]" +
                "      }" +
                "    ]" +
                "}");

        expectInvalidProfileException();
    }

    @Test
    public void shouldRejectIsConstraintSetToNullForNotWithRuleAndConstraintFormat() {
        givenJson(
            "{" +
                "    \"schemaVersion\": \"0.1\"," +
                "    \"fields\": [ { \"name\": \"foo\" } ]," +
                "    \"rules\": [" +
                "       {" +
                "        \"rule\": \"fooRule\"," +
                "        \"constraints\": [{ \"not\": { \"field\": \"foo\", \"is\": null } }]" +
                "       }" +
                "    ]" +
                "}");

        expectInvalidProfileException();
    }
}
