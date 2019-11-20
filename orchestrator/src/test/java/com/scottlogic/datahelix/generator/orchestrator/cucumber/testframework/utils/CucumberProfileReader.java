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

package com.scottlogic.datahelix.generator.orchestrator.cucumber.testframework.utils;

import com.google.inject.Inject;
import com.scottlogic.datahelix.generator.common.commands.CommandBus;
import com.scottlogic.datahelix.generator.common.util.FileUtils;
import com.scottlogic.datahelix.generator.core.profile.Profile;
import com.scottlogic.datahelix.generator.profile.dtos.ProfileDTO;
import com.scottlogic.datahelix.generator.profile.reader.JsonProfileReader;
import com.scottlogic.datahelix.generator.profile.serialisation.ProfileSerialiser;
import com.scottlogic.datahelix.generator.profile.validators.ConfigValidator;

public class CucumberProfileReader extends JsonProfileReader {

    private final CucumberTestState state;

    @Inject
    public CucumberProfileReader(CucumberTestState state, CommandBus commandBus) {
        super(null, new ConfigValidator(new FileUtils()), new CucumberFileReader(state), commandBus);
        this.state = state;
    }

    @Override
    public Profile read() {
        return super.read(createJson());
    }

    private String createJson() {
        ProfileDTO profileDTO = new ProfileDTO();
        profileDTO.schemaVersion = "0.10";
        profileDTO.fields = state.profileFields;
        profileDTO.constraints = state.constraints;
        return new ProfileSerialiser().serialise(profileDTO);
    }


}
