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
package com.scottlogic.deg.profile.validators;

import com.google.inject.Inject;
import com.scottlogic.deg.common.validators.ValidationResult;
import com.scottlogic.deg.common.validators.Validator;
import com.scottlogic.deg.profile.commands.CreateProfile;
import com.scottlogic.deg.profile.dtos.ProfileDTO;

public class CreateProfileValidator implements Validator<CreateProfile>
{
    private final Validator<ProfileDTO> profileValidator;

    @Inject
    public CreateProfileValidator(Validator<ProfileDTO> profileValidator)
    {
        this.profileValidator = profileValidator;
    }

    @Override
    public ValidationResult validate(CreateProfile command)
    {
        return profileValidator.validate(command.profileDTO);
    }
}
