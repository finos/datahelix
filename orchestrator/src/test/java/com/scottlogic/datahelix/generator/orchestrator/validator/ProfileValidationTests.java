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

package com.scottlogic.datahelix.generator.orchestrator.validator;

import com.scottlogic.datahelix.generator.common.commands.CommandBus;
import com.scottlogic.datahelix.generator.common.util.FileUtils;
import com.scottlogic.datahelix.generator.core.profile.Profile;
import com.scottlogic.datahelix.generator.custom.CustomGeneratorList;
import com.scottlogic.datahelix.generator.profile.custom.CustomConstraintFactory;
import com.scottlogic.datahelix.generator.profile.reader.CsvInputStreamReaderFactory;
import com.scottlogic.datahelix.generator.profile.reader.FileReader;
import com.scottlogic.datahelix.generator.profile.reader.JsonProfileReader;
import com.scottlogic.datahelix.generator.profile.reader.ProfileCommandBus;
import com.scottlogic.datahelix.generator.profile.serialisation.ConstraintDeserializerFactory;
import com.scottlogic.datahelix.generator.profile.serialisation.ProfileDeserialiser;
import com.scottlogic.datahelix.generator.profile.services.ConstraintService;
import com.scottlogic.datahelix.generator.profile.services.FieldService;
import com.scottlogic.datahelix.generator.profile.services.NameRetrievalService;
import com.scottlogic.datahelix.generator.profile.validators.ConfigValidator;
import com.scottlogic.datahelix.generator.profile.validators.CreateProfileValidator;
import com.scottlogic.datahelix.generator.profile.validators.ReadRelationshipsValidator;
import com.scottlogic.datahelix.generator.profile.validators.profile.ProfileValidator;
import org.junit.Assert;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.nullValue;

public class ProfileValidationTests {
    @TestFactory
    Collection<DynamicTest> shouldAllValidateWithoutErrors() throws IOException {
        Collection<DynamicTest> dynamicTests = new ArrayList<>();

        File[] directoriesArray =
            Paths.get("..", "examples")
                .toFile()
                .listFiles(File::isDirectory);

        CsvInputStreamReaderFactory csvReaderFactory = new CsvInputStreamReaderFactory();
        CustomConstraintFactory customConstraintFactory = new CustomConstraintFactory(new CustomGeneratorList());
        ConstraintService constraintService = new ConstraintService(
            customConstraintFactory,
            new NameRetrievalService(csvReaderFactory));
        ProfileDeserialiser profileDeserialiser = new ProfileDeserialiser(
            new ConfigValidator(new FileUtils()),
            new ConstraintDeserializerFactory(new FileReader(csvReaderFactory)));
        CommandBus commandBus = new ProfileCommandBus(
            new FieldService(),
            constraintService,
            customConstraintFactory,
            new CreateProfileValidator(new ProfileValidator(null)),
            new ReadRelationshipsValidator(),
            profileDeserialiser);

        JsonProfileReader profileReader = new JsonProfileReader(commandBus, profileDeserialiser);

        for (File dir : directoriesArray) {
            File profileFile = Paths.get(dir.getCanonicalPath(), "profile.json").toFile();

            DynamicTest test = DynamicTest.dynamicTest(
                dir.getName(),
                () -> {
                    Profile profile = profileReader.read(profileFile);
                    Assert.assertThat(profile, is(not(nullValue())));
                });
            dynamicTests.add(test);
        }
        return dynamicTests;
    }
}
