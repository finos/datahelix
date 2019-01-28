package com.scottlogic.deg.schemas;

import com.scottlogic.deg.schemas.common.ProfileSerialiser;
import com.scottlogic.deg.schemas.v3.ConstraintDTO;
import com.scottlogic.deg.schemas.v3.FieldDTO;
import com.scottlogic.deg.schemas.v3.RuleDTO;
import com.scottlogic.deg.schemas.v3.V3ProfileDTO;
import org.hamcrest.core.Is;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.function.Consumer;
import java.util.function.Function;

public class ProfileSerialiserTests {
    @Test
    public void shouldSerialiseExampleProfile() throws IOException {
        // Arrange
        final V3ProfileDTO profile = new V3ProfileDTO();
        profile.fields = Arrays.asList(
            createField(f -> f.name = "typecode"),
            createField(f -> f.name = "price"));

        profile.rules = Arrays.asList(
            createConstraintAsRule(c -> {
                c.field = "typecode";
                c.is = "ofType";
                c.value = "string";
            }),
            createConstraintAsRule(c -> {
                c.if_ = createConstraint(condition -> {
                    condition.anyOf = Arrays.asList(
                        createConstraint(cNot -> {
                            cNot.not = createConstraint(c1 -> {
                                c1.field = "typecode";
                                c1.is = "null";
                            });
                        }),
                        createConstraint(c1 -> {
                            c1.field = "typecode";
                            c1.is = "equalTo";
                            c1.value = "type_001";
                        }));
                });
                c.then = createConstraint(then -> {
                    then.field = "price";
                    then.is = "greaterThanOrEqualTo";
                    then.value = 42.1;
                });
                c.else_ = createConstraint(elseCondition -> {
                    elseCondition.field = "price";
                    elseCondition.is = "lessThan";
                    elseCondition.value = 42.1;
                });
            }));

        Function<String, String> normalise = str -> str.replaceAll("[\r\n\\s]", ""); // normalise the whitespace for comparison

        final String expectedJson =
            normalise.apply(
                "{" +
                    "\"schemaVersion\" : \"v3\"," +
                    "\"fields\" : [" +
                    "   { \"name\" : \"typecode\" }," +
                    "   { \"name\" : \"price\" }" +
                    "]," +
                    "\"rules\" : [" +
                    "   {" +
                    "       \"field\" : \"typecode\"," +
                    "       \"is\" : \"ofType\"," +
                    "       \"value\" : \"string\"" +
                    "   }," +
                    "   {" +
                    "       \"if\" : {" +
                    "           \"anyOf\" : [" +
                    "               { \"not\": { \"field\" : \"typecode\", \"is\" : \"null\" } }," +
                    "               { \"field\": \"typecode\", \"is\": \"equalTo\", \"value\" : \"type_001\" }" +
                    "           ]" +
                    "       }," +
                    "       \"then\" : { \"field\": \"price\", \"is\": \"greaterThanOrEqualTo\", \"value\" : 42.1 }," +
                    "       \"else\" : { \"field\" : \"price\", \"is\" : \"lessThan\", \"value\" : 42.1 }" +
                    "   }" +
                    "]" +
                "}"); // normalise the line endings for comparison;

        // Act
        final String actualJson = normalise.apply(
                new ProfileSerialiser()
                    .serialise(profile));

        // Assert
        Assert.assertThat(actualJson, Is.is(expectedJson));
    }

    @Test
    public void shouldSerialiseExampleProfileWithIsConstraintMissing() throws IOException {
        // Arrange
        final V3ProfileDTO profile = new V3ProfileDTO();
        profile.fields = Arrays.asList(
            createField(f -> f.name = "typecode"),
            createField(f -> f.name = "price"));

        profile.rules = Collections.singletonList(
            createConstraintAsRule(c -> {
                c.field = "typecode";
                c.value = "string";
            }));

        Function<String, String> normalise = str -> str.replaceAll("[\r\n\\s]", ""); // normalise the whitespace for comparison

        final String expectedJson =
            normalise.apply(
                "{" +
                    "\"schemaVersion\" : \"v3\"," +
                    "\"fields\" : [" +
                    "   { \"name\" : \"typecode\" }," +
                    "   { \"name\" : \"price\" }" +
                    "]," +
                    "\"rules\" : [" +
                    "   {" +
                    "       \"field\" : \"typecode\"," +
                    "       \"value\" : \"string\"" +
                    "   }" +
                    "]" +
                    "}"); // normalise the line endings for comparison;

        // Act
        final String actualJson = normalise.apply(
            new ProfileSerialiser()
                .serialise(profile));

        // Assert
        Assert.assertThat(actualJson, Is.is(expectedJson));
    }

    private static FieldDTO createField(Consumer<FieldDTO> setupField) {
        FieldDTO newField = new FieldDTO();
        setupField.accept(newField);
        return newField;
    }

    private static RuleDTO createRule(
        String description,
        ConstraintDTO... constraints) {
        RuleDTO newRule = new RuleDTO();

        newRule.rule = description;
        newRule.constraints = Arrays.asList(constraints);

        return newRule;
    }

    private static ConstraintDTO createConstraint(Consumer<ConstraintDTO> setupConstraint) {
        ConstraintDTO newConstraint = new ConstraintDTO();
        setupConstraint.accept(newConstraint);
        return newConstraint;
    }

    private static RuleDTO createConstraintAsRule(Consumer<ConstraintDTO> setupConstraint) {
        ConstraintDTO newConstraint = createConstraint(setupConstraint);

        return createRule(null, newConstraint);
    }
}
