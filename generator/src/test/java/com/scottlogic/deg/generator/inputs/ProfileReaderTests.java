package com.scottlogic.deg.generator.inputs;

import com.scottlogic.deg.generator.AssertUtils;
import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.Profile;
import com.scottlogic.deg.generator.Rule;
import com.scottlogic.deg.generator.constraints.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.*;
import java.util.function.Consumer;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.core.IsNull.nullValue;

public class ProfileReaderTests {
    private String json;
    private Profile profile;

    @Before
    public void Setup()
    {
        this.json = null;
        this.profile = null;
    }

    private void givenJson(String json)
    {
        this.json = json;
    }

    private Profile getResultingProfile() throws IOException, InvalidProfileException {
        if (this.profile == null) {
            ProfileReader objectUnderTest = new ProfileReader();
            this.profile = objectUnderTest.read(this.json);
        }

        return this.profile;
    }

    private void expectRules(Consumer<Rule>... ruleAssertions) throws IOException, InvalidProfileException {
        expectMany(this.getResultingProfile().rules, ruleAssertions);
    }

    private Consumer<Rule> ruleWithDescription(String expectedDescription) {
        return rule -> Assert.assertThat(rule.description, equalTo(expectedDescription));
    }

    private Consumer<Rule> ruleWithConstraints(Consumer<IConstraint>... constraintAsserters) {
        return rule -> expectMany(rule.constraints, constraintAsserters);
    }

    private <T> Consumer<IConstraint> typedConstraint(Class<T> constraintType, Consumer<T> asserter) {
        return constraint -> {
            Assert.assertThat(constraint, instanceOf(constraintType));

            asserter.accept((T)constraint);
        };
    }

    private Consumer<Field> fieldWithName(String expectedName) {
        return field -> Assert.assertThat(field.name, equalTo(expectedName));
    }

    private void expectFields(Consumer<Field>... fieldAssertions) throws IOException, InvalidProfileException {
        expectMany(this.getResultingProfile().fields, fieldAssertions);
    }

    /**
     * Given a set I1, I2, I3... and some consumers A1, A2, A3..., run A1(I1), A2(I2), A3(I3)...
     * This lets us make assertions about each entry in a sequence
     * */
    private <T> void expectMany(
        Iterable<T> assertionTargets,
        Consumer<T>... perItemAssertions) {

        AssertUtils.pairwiseAssert(
            assertionTargets,
            Arrays.asList(perItemAssertions), // because arrays aren't iterable?
            (assertionTarget, asserter) -> asserter.accept(assertionTarget));
    }

    @Test
    public void shouldDeserialiseSingleField() throws IOException, InvalidProfileException {
        givenJson(
            "{" +
            "    \"schemaVersion\": \"v3\"," +
            "    \"fields\": [ { \"name\": \"f1\" } ]," +
            "    \"rules\": []" +
            "}");

        expectFields(
            fieldWithName("f1"));
    }

    @Test
    public void shouldDeserialiseMultipleFields() throws IOException, InvalidProfileException {
        givenJson(
            "{" +
            "    \"schemaVersion\": \"v3\"," +
            "    \"fields\": [ { \"name\": \"f1\" }, { \"name\": \"f2\" } ]," +
            "    \"rules\": []" +
            "}");

        expectFields(
            fieldWithName("f1"),
            fieldWithName("f2"));
    }

    @Test
    public void shouldGiveDefaultNameToUnnamedRules() throws IOException, InvalidProfileException {
        givenJson(
            "{" +
            "    \"schemaVersion\": \"v3\"," +
            "    \"fields\": [ { \"name\": \"foo\" } ]," +
            "    \"rules\": [" +
            "        { \"field\": \"foo\", \"is\": \"null\" }" +
            "    ]" +
            "}");

        expectRules(
            ruleWithDescription("Unnamed rule"));
    }

    @Test
    public void shouldReadNameOfNamedRules() throws IOException, InvalidProfileException {
        givenJson(
            "{" +
            "    \"schemaVersion\": \"v3\"," +
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
    public void shouldDeserialiseIsOfTypeConstraint() throws IOException, InvalidProfileException {
        givenJson(
            "{" +
            "    \"schemaVersion\": \"v3\"," +
            "    \"fields\": [ { \"name\": \"foo\" } ]," +
            "    \"rules\": [" +
            "        { \"field\": \"foo\", \"is\": \"ofType\", \"value\": \"string\" }" +
            "    ]" +
            "}");

        expectRules(
            ruleWithConstraints(
                typedConstraint(
                    IsOfTypeConstraint.class,
                    c -> Assert.assertThat(
                        c.requiredType,
                        equalTo(IsOfTypeConstraint.Types.String)))));
    }

    @Test
    public void shouldDeserialiseIsOfLengthConstraint() throws IOException, InvalidProfileException {
        givenJson(
                "{" +
                        "    \"schemaVersion\": \"v3\"," +
                        "    \"fields\": [ { \"name\": \"foo\" } ]," +
                        "    \"rules\": [" +
                        "        { \"field\": \"id\", \"is\": \"hasLength\", \"value\": 5 }" +
                        "    ]" +
                        "}");

        expectRules(
                ruleWithConstraints(
                        typedConstraint(
                                StringHasLengthConstraint.class,
                                c -> Assert.assertThat(c.referenceValue, equalTo(5)))));
    }

    @Test
    public void shouldDeserialiseNotWrapper() throws IOException, InvalidProfileException {
        // Arrange
        givenJson(
            "{" +
                "    \"schemaVersion\": \"v3\"," +
                "    \"fields\": [ { \"name\": \"foo\" } ]," +
                "    \"rules\": [" +
                "        { \"not\": { \"field\": \"foo\", \"is\": \"ofType\", \"value\": \"string\" } }" +
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
    public void shouldDeserialiseOrConstraint() throws IOException, InvalidProfileException {
        givenJson(
            "{" +
            "    \"schemaVersion\": \"v3\"," +
            "    \"fields\": [ { \"name\": \"foo\" } ]," +
            "    \"rules\": [{" +
            "        \"anyOf\": [" +
            "          { \"field\": \"foo\", \"is\": \"equalTo\", \"value\": \"1\" }," +
            "          { \"field\": \"foo\", \"is\": \"null\" }" +
            "        ]" +
            "    }]" +
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
    public void shouldDeserialiseAndConstraint() throws IOException, InvalidProfileException {
        givenJson(
            "{" +
            "    \"schemaVersion\": \"v3\"," +
            "    \"fields\": [ { \"name\": \"foo\" } ]," +
            "    \"rules\": [{" +
            "        \"allOf\": [" +
            "          { \"field\": \"foo\", \"is\": \"equalTo\", \"value\": \"1\" }," +
            "          { \"field\": \"foo\", \"is\": \"null\" }" +
            "        ]" +
            "    }]" +
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
    public void shouldDeserialiseIfConstraint() throws IOException, InvalidProfileException {
        givenJson(
            "{" +
            "    \"schemaVersion\": \"v3\"," +
            "    \"fields\": [ { \"name\": \"foo\" } ]," +
            "    \"rules\": [{" +
            "        \"if\": { \"field\": \"foo\", \"is\": \"ofType\", \"value\": \"string\" }," +
            "        \"then\": { \"field\": \"foo\", \"is\": \"equalTo\", \"value\": \"str!\" }," +
            "        \"else\": { \"field\": \"foo\", \"is\": \"greaterThan\", \"value\": 3 }" +
            "    }]" +
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
                            instanceOf(IsEqualToConstantConstraint.class));

                        Assert.assertThat(
                            c.whenConditionIsFalse,
                            instanceOf(IsGreaterThanConstantConstraint.class));
                    })));
    }

    @Test
    public void shouldDeserialiseIfConstraintWithoutElse() throws IOException, InvalidProfileException {
        givenJson(
            "{" +
                "    \"schemaVersion\": \"v3\"," +
                "    \"fields\": [ { \"name\": \"foo\" } ]," +
                "    \"rules\": [{" +
                "        \"if\": { \"field\": \"foo\", \"is\": \"ofType\", \"value\": \"string\" }," +
                "        \"then\": { \"field\": \"foo\", \"is\": \"equalTo\", \"value\": \"str!\" }" +
                "    }]" +
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
                            instanceOf(IsEqualToConstantConstraint.class));

                        Assert.assertThat(
                            c.whenConditionIsFalse,
                            nullValue());
                    })));
    }
}
