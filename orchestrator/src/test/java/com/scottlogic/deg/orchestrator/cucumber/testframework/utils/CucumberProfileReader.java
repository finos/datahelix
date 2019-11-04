/*
 * Copyright 2019 Scott Logic Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.scottlogic.deg.orchestrator.cucumber.testframework.utils;

import com.google.inject.Inject;
import com.scottlogic.deg.common.commands.CommandBus;
import com.scottlogic.deg.generator.profile.Profile;
import com.scottlogic.deg.profile.creation.dtos.ProfileDTO;
import com.scottlogic.deg.profile.creation.dtos.RuleDTO;
import com.scottlogic.deg.profile.creation.serialisation.ProfileDeserialiser;
import com.scottlogic.deg.profile.creation.serialisation.ProfileSerialiser;
import com.scottlogic.deg.profile.reader.JsonProfileReader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class CucumberProfileReader extends JsonProfileReader {

    private final CucumberTestState state;

    @Inject
    public CucumberProfileReader(CucumberTestState state, CommandBus commandBus) {
        super(null, new ProfileDeserialiser(new CucumberFileReader(state      )), commandBus);
        this.state = state;
    }

    @Override
    public Profile read() throws IOException {
        return super.read(createJson());
    }

    private String createJson() throws IOException {
        ProfileDTO profileDTO = new ProfileDTO();
        profileDTO.schemaVersion = "0.10";
        profileDTO.fields = state.profileFields;

        RuleDTO ruleDTO = new RuleDTO();
        ruleDTO.constraints = state.constraints;
        profileDTO.rules = state.constraints.isEmpty() ? new ArrayList<>() : Collections.singletonList(ruleDTO);

        return new ProfileSerialiser().serialise(profileDTO);
    }


}
