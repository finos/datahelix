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
                c.type = "isOfType";
                c.value = "string";
            }),
            createConstraintAsRule(c -> {
                c.if_ = createConstraint(condition -> {
                    condition.anyOf = Arrays.asList(
                        createConstraint(c1 -> {
                            c1.field = "typecode";
                            c1.type = "not isNull";
                        }),
                        createConstraint(c1 -> {
                            c1.field = "typecode";
                            c1.type = "isEqualTo";
                            c1.value = "type_001";
                        }));
                });
                c.then = createConstraint(then -> {
                    then.field = "price";
                    then.type = "isGreaterThanOrEqualTo";
                    then.value = 42.1;
                });
                c.else_ = createConstraint(elseCondition -> {
                    elseCondition.field = "price";
                    elseCondition.type = "isLessThan";
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
                    "       \"type\" : \"isOfType\"," +
                    "       \"field\" : \"typecode\"," +
                    "       \"value\" : \"string\"" +
                    "   }," +
                    "   {" +
                    "       \"if\" : {" +
                    "           \"anyOf\" : [" +
                    "               {" +
                    "                   \"type\" : \"not isNull\"," +
                    "                   \"field\" : \"typecode\"" +
                    "               }," +
                    "               {" +
                    "                   \"type\" : \"isEqualTo\"," +
                    "                   \"field\" : \"typecode\"," +
                    "                   \"value\" : \"type_001\"" +
                    "               }" +
                    "           ]" +
                    "       }," +
                    "       \"then\" : {" +
                    "           \"type\" : \"isGreaterThanOrEqualTo\"," +
                    "           \"field\" : \"price\"," +
                    "           \"value\" : 42.1" +
                    "       }," +
                    "       \"else\" : {" +
                    "           \"type\" : \"isLessThan\"," +
                    "           \"field\" : \"price\"," +
                    "           \"value\" : 42.1" +
                    "       }" +
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

        newRule.description = description;
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
