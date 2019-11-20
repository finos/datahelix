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

package com.scottlogic.datahelix.generator.profile.handlers;

import com.google.inject.Inject;
import com.scottlogic.datahelix.generator.common.commands.CommandHandler;
import com.scottlogic.datahelix.generator.common.commands.CommandResult;
import com.scottlogic.datahelix.generator.common.profile.Field;
import com.scottlogic.datahelix.generator.common.profile.Fields;
import com.scottlogic.datahelix.generator.common.validators.Validator;
import com.scottlogic.datahelix.generator.core.profile.Profile;
import com.scottlogic.datahelix.generator.core.profile.constraints.Constraint;
import com.scottlogic.datahelix.generator.core.profile.constraints.atomic.NotNullConstraint;
import com.scottlogic.datahelix.generator.profile.commands.CreateProfile;
import com.scottlogic.datahelix.generator.profile.custom.CustomConstraintFactory;
import com.scottlogic.datahelix.generator.profile.services.ConstraintService;
import com.scottlogic.datahelix.generator.profile.services.FieldService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class CreateProfileHandler extends CommandHandler<CreateProfile, Profile>
{
    private final FieldService fieldService;
    private final ConstraintService constraintService;
    private final CustomConstraintFactory customConstraintFactory;

    @Inject
    public CreateProfileHandler(FieldService fieldService, ConstraintService constraintService,
                                CustomConstraintFactory customConstraintFactory, Validator<CreateProfile> validator)
    {
        super(validator);
        this.fieldService = fieldService;
        this.constraintService = constraintService;
        this.customConstraintFactory = customConstraintFactory;
    }

    @Override
    public CommandResult<Profile> handleCommand(CreateProfile command)
    {
        Fields fields = fieldService.createFields(command.profileDTO);
        List<Constraint> constraints = constraintService.createConstraints(command.profileDTO.constraints, fields);

        constraints.addAll(createNullableConstraints(fields));
        constraints.addAll(createSpecificTypeConstraints(fields));
        constraints.addAll(createCustomGeneratorConstraints(fields));

        return CommandResult.success(new Profile(command.profileDTO.description, fields, constraints));
    }

    private List<Constraint> createNullableConstraints(Fields fields)
    {
        return fields.stream()
            .filter(field -> !field.isNullable())
            .map(NotNullConstraint::new)
            .collect(Collectors.toList());
    }

    private List<Constraint> createSpecificTypeConstraints(Fields fields)
    {
        return fields.stream()
            .map(constraintService::createSpecificTypeConstraint)
            .filter(Optional::isPresent)
            .map(Optional::get)
            .collect(Collectors.toList());
    }

    private List<Constraint> createCustomGeneratorConstraints(Fields fields)
    {
        return fields.stream()
            .filter(Field::usesCustomGenerator)
            .map(f -> customConstraintFactory.create(f, f.getCustomGeneratorName()))
            .collect(Collectors.toList());
    }
}
