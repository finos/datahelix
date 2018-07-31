package com.scottlogic.deg.generator.inputs;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.Profile;
import com.scottlogic.deg.generator.Rule;
import com.scottlogic.deg.generator.constraints.IConstraint;
import com.scottlogic.deg.generator.constraints.IsOfTypeConstraint;
import com.scottlogic.deg.generator.constraints.NotConstraint;
import org.junit.Assert;
import org.junit.Test;
import org.junit.Before;

import java.io.IOException;
import java.util.Collection;
import java.util.function.Consumer;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;

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

    private void expectFields(Consumer<Field>... fieldAssertions) throws IOException, InvalidProfileException {
        expectMany(this.getResultingProfile().fields, fieldAssertions);
    }

    private <T> void expectMany(
        Collection<T> assertionTargets,
        Consumer<T>... perItemAssertions) {
        Assert.assertThat(
            assertionTargets.size(),
            equalTo(perItemAssertions.length));

        int i = 0;
        for (T item : assertionTargets) {
            perItemAssertions[i++].accept(item);
        }
    }

    private <T extends IConstraint> void expectTyped(
        IConstraint constraint,
        Class<T> classRef,
        Consumer<T> assertFunc) {

        Assert.assertThat(constraint, instanceOf(classRef));

        assertFunc.accept((T)constraint);
    }

    @Test
    public void shouldDeserialiseNotWrapper() throws IOException, InvalidProfileException {
        // Arrange
        givenJson(
            "{" +
            "    \"schemaVersion\": \"v3\"," +
            "    \"fields\": [ { \"name\": \"foo\" } ]," +
            "    \"rules\": [" +
            "        { \"not\": { \"field\": \"id\", \"is\": \"ofType\", \"value\": \"string\" } }" +
            "    ]" +
            "}");

        expectRules(
            rule -> {
                expectMany(rule.constraints,
                    constraint -> expectTyped(
                        constraint,
                        NotConstraint.class,
                        c -> {
                            Assert.assertThat(
                                c.negatedConstraint,
                                instanceOf(IsOfTypeConstraint.class));
                        }));
            });
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
            field -> Assert.assertThat(
                field.name,
                equalTo("f1")));
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
            field -> Assert.assertThat(
                field.name,
                equalTo("f1")),
            field -> Assert.assertThat(
                field.name,
                equalTo("f2")));
    }

    @Test
    public void shouldGiveDefaultNameToUnnamedRules() throws IOException, InvalidProfileException {
        givenJson(
            "{" +
            "    \"schemaVersion\": \"v3\"," +
            "    \"fields\": [ { \"name\": \"foo\" } ]," +
            "    \"rules\": [" +
            "        { \"field\": \"id\", \"is\": \"null\" }" +
            "    ]" +
            "}");

        expectRules(
            rule -> Assert.assertThat(
                rule.description,
                equalTo("Unnamed rule")));
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
            "               { \"field\": \"id\", \"is\": \"null\" }" +
            "           ]" +
            "        }" +
            "    ]" +
            "}");

        expectRules(
            rule -> Assert.assertThat(
                rule.description,
                equalTo("Too rule for school")));
    }

    @Test
    public void shouldDeserialiseIsOfTypeConstraint() throws IOException, InvalidProfileException {
        givenJson(
            "{" +
            "    \"schemaVersion\": \"v3\"," +
            "    \"fields\": [ { \"name\": \"foo\" } ]," +
            "    \"rules\": [" +
            "        { \"field\": \"id\", \"is\": \"ofType\", \"value\": \"string\" }" +
            "    ]" +
            "}");

        expectRules(
            rule -> {
                Assert.assertThat(rule.description, equalTo("Unnamed rule"));

                expectMany(rule.constraints,
                    constraint -> expectTyped(
                        constraint,
                        IsOfTypeConstraint.class,
                        c -> Assert.assertThat(
                            c.requiredType,
                            equalTo(IsOfTypeConstraint.Types.String))));
            });
    }
}
