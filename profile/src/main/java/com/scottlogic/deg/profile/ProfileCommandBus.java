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
package com.scottlogic.deg.profile;

import com.google.inject.Inject;
import com.scottlogic.deg.common.commands.CommandBus;
import com.scottlogic.deg.profile.reader.FileReader;
import com.scottlogic.deg.profile.reader.commands.CreateConstraints;
import com.scottlogic.deg.profile.reader.commands.CreateFields;
import com.scottlogic.deg.profile.reader.commands.CreateProfile;
import com.scottlogic.deg.profile.reader.handlers.CreateConstraintsHandler;
import com.scottlogic.deg.profile.reader.handlers.CreateFieldsHandler;
import com.scottlogic.deg.profile.reader.handlers.CreateProfileHandler;
import com.scottlogic.deg.profile.reader.validators.CreateConstraintsValidator;
import com.scottlogic.deg.profile.reader.validators.CreateFieldsValidator;
import com.scottlogic.deg.profile.reader.validators.CreateProfileValidator;

public class ProfileCommandBus extends CommandBus
{
    @Inject
    public ProfileCommandBus(FileReader fileReader)
    {
        register(CreateProfile.class, new CreateProfileHandler(this, new CreateProfileValidator()));
        register(CreateFields.class, new CreateFieldsHandler(new CreateFieldsValidator()));
        register(CreateConstraints.class, new CreateConstraintsHandler(fileReader, new CreateConstraintsValidator()));
    }
}
