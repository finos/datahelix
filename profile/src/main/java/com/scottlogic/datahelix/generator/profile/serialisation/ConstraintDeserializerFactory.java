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

package com.scottlogic.datahelix.generator.profile.serialisation;

import com.fasterxml.jackson.databind.JsonDeserializer;
import com.google.inject.Inject;
import com.scottlogic.datahelix.generator.profile.dtos.constraints.ConstraintDTO;
import com.scottlogic.datahelix.generator.profile.reader.FileReader;

import java.nio.file.Path;

public class ConstraintDeserializerFactory {
    private final FileReader fileReader;

    @Inject
    public ConstraintDeserializerFactory(FileReader fileReader) {
        this.fileReader = fileReader;
    }

    public JsonDeserializer<ConstraintDTO> createDeserialiser(Path profileDirectory) {
        if (profileDirectory == null) {
            throw new IllegalArgumentException("`profileDirectory` must be supplied");
        }

        return new ConstraintDeserializer(fileReader, profileDirectory);
    }
}
