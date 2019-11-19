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
package com.scottlogic.datahelix.generator.profile.reader;

import com.google.inject.Inject;
import com.scottlogic.datahelix.generator.common.commands.CommandBus;
import com.scottlogic.datahelix.generator.common.validators.Validator;
import com.scottlogic.datahelix.generator.profile.commands.CreateProfile;
import com.scottlogic.datahelix.generator.profile.custom.CustomConstraintFactory;
import com.scottlogic.datahelix.generator.profile.handlers.CreateProfileHandler;
import com.scottlogic.datahelix.generator.profile.services.ConstraintService;
import com.scottlogic.datahelix.generator.profile.services.FieldService;

public class ProfileCommandBus extends CommandBus
{
    @Inject
    public ProfileCommandBus(FieldService fieldService, ConstraintService constraintService
        , CustomConstraintFactory customConstraintFactory, Validator<CreateProfile> validator)
    {
        register(CreateProfile.class, new CreateProfileHandler(fieldService, constraintService, customConstraintFactory, validator));
    }
}
