package com.scottlogic.deg.generator.cucumber.steps;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.scottlogic.deg.generator.ProfileFields;
import com.scottlogic.deg.generator.cucumber.utils.ConstraintHolder;
import com.scottlogic.deg.generator.inputs.IConstraintReader;
import com.scottlogic.deg.generator.inputs.InvalidProfileException;
import com.scottlogic.deg.generator.inputs.MainConstraintReader;
import com.scottlogic.deg.schemas.v3.ConstraintDTO;
import cucumber.api.java.en.And;

import java.io.IOException;

public class JSONTestStep {

    private DegTestState state;

    public JSONTestStep(DegTestState state){
        this.state = state;
    }

    @And("^there is a constraint:$")
    public void jsonConstraint(String jsonConstraint) throws IOException, InvalidProfileException {
        String constraintProfile = "[" + jsonConstraint + "]";
        this.jsonConstraints(constraintProfile);
    }

    @And("^there are constraints:$")
    public void jsonConstraints(String jsonConstraints) throws IOException, InvalidProfileException {
        String constraintProfile = "{ \"constraints\" : " + jsonConstraints + "}";

        IConstraintReader reader = new MainConstraintReader();
        ProfileFields pf = new ProfileFields(this.state.profileFields);
        ConstraintHolder holder = this.deserialise(constraintProfile);
        for(ConstraintDTO cd : holder.constraints){
            this.state.constraints.add(reader.apply(cd, pf));
        }
    }

    private ConstraintHolder deserialise(String json) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS);

        ConstraintHolder constraints = mapper.readerFor(ConstraintHolder.class).readValue(json);
        return constraints;
    }
}
