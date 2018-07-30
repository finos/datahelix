package com.scottlogic.deg.generator.inputs;

import com.scottlogic.deg.generator.Profile;
import com.scottlogic.deg.generator.constraints.IConstraint;
import com.scottlogic.deg.generator.constraints.IsOfTypeConstraint;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.Collection;
import java.util.function.Consumer;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;

public class ProfileReaderTests {
    @Test
    public void shouldDeserialiseSingleField() throws IOException, InvalidProfileException {
        // Arrange
        ProfileReader objectUnderTest = new ProfileReader();
        String profileJson =
            "{" +
            "    \"schemaVersion\": \"v3\"," +
            "    \"fields\": [ { \"name\": \"f1\" } ]," +
            "    \"rules\": []" +
            "}";

        // Act
        Profile actualResult =  objectUnderTest.read(profileJson);

        // Assert
        expectMany(actualResult.fields,
            field -> Assert.assertThat(
                field.name,
                equalTo("f1")));
    }

    @Test
    public void shouldDeserialiseMultipleFields() throws IOException, InvalidProfileException {
        // Arrange
        ProfileReader objectUnderTest = new ProfileReader();
        String profileJson =
            "{" +
            "    \"schemaVersion\": \"v3\"," +
            "    \"fields\": [ { \"name\": \"f1\" }, { \"name\": \"f2\" } ]," +
            "    \"rules\": []" +
            "}";

        // Act
        Profile actualResult =  objectUnderTest.read(profileJson);

        // Assert
        expectMany(actualResult.fields,
            field -> Assert.assertThat(
                field.name,
                equalTo("f1")),
            field -> Assert.assertThat(
                field.name,
                equalTo("f2")));
    }

    @Test
    public void shouldGiveDefaultNameToUnnamedRules() throws IOException, InvalidProfileException {
        // Arrange
        ProfileReader objectUnderTest = new ProfileReader();
        String profileJson =
            "{" +
            "    \"schemaVersion\": \"v3\"," +
            "    \"fields\": [ { \"name\": \"foo\" } ]," +
            "    \"rules\": [" +
            "        { \"field\": \"id\", \"type\": \"isNull\" }" +
            "    ]" +
            "}";

        // Act
        Profile actualResult =  objectUnderTest.read(profileJson);

        // Assert
        expectMany(actualResult.rules,
            rule -> Assert.assertThat(
                rule.description,
                equalTo("Unnamed rule")));
    }

    @Test
    public void shouldReadNameOfNamedRules() throws IOException, InvalidProfileException {
        // Arrange
        ProfileReader objectUnderTest = new ProfileReader();
        String profileJson =
            "{" +
            "    \"schemaVersion\": \"v3\"," +
            "    \"fields\": [ { \"name\": \"foo\" } ]," +
            "    \"rules\": [" +
            "        {" +
            "           \"rule\": \"Too rule for school\"," +
            "           \"constraints\": [" +
            "               { \"field\": \"id\", \"type\": \"isNull\" }" +
            "           ]" +
            "        }" +
            "    ]" +
            "}";

        // Act
        Profile actualResult =  objectUnderTest.read(profileJson);

        // Assert
        expectMany(actualResult.rules,
            rule -> Assert.assertThat(
                rule.description,
                equalTo("Too rule for school")));
    }

    @Test
    public void shouldDeserialiseIsOfTypeConstraint() throws IOException, InvalidProfileException {
        // Arrange
        ProfileReader objectUnderTest = new ProfileReader();
        String profileJson =
            "{" +
            "    \"schemaVersion\": \"v3\"," +
            "    \"fields\": [ { \"name\": \"foo\" } ]," +
            "    \"rules\": [" +
            "        { \"field\": \"id\", \"type\": \"isOfType\", \"value\": \"string\" }" +
            "    ]" +
            "}";

        // Act
        Profile actualResult =  objectUnderTest.read(profileJson);

        // Assert
        expectMany(actualResult.rules,
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
}
