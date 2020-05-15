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

package com.scottlogic.datahelix.generator.profile.services;

import com.google.inject.Inject;
import com.scottlogic.datahelix.generator.common.ValidationException;
import com.scottlogic.datahelix.generator.common.commands.CommandBus;
import com.scottlogic.datahelix.generator.common.commands.CommandResult;
import com.scottlogic.datahelix.generator.common.profile.Fields;
import com.scottlogic.datahelix.generator.core.generation.relationships.ExtentAugmentedFields;
import com.scottlogic.datahelix.generator.core.profile.Profile;
import com.scottlogic.datahelix.generator.core.profile.constraints.Constraint;
import com.scottlogic.datahelix.generator.core.profile.relationships.Relationship;
import com.scottlogic.datahelix.generator.profile.commands.CreateProfile;
import com.scottlogic.datahelix.generator.profile.dtos.RelationalProfileDTO;
import com.scottlogic.datahelix.generator.profile.dtos.RelationshipDTO;
import com.scottlogic.datahelix.generator.profile.dtos.constraints.ConstraintDTO;
import com.scottlogic.datahelix.generator.profile.serialisation.ProfileDeserialiser;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class RelationshipService {
    private final CommandBus commandBus;
    private final ConstraintService constraintService;
    private final ProfileDeserialiser profileDeserialiser;

    @Inject
    public RelationshipService(
        CommandBus commandBus,
        ConstraintService constraintService,
        ProfileDeserialiser profileDeserialiser) {
        this.commandBus = commandBus;
        this.constraintService = constraintService;
        this.profileDeserialiser = profileDeserialiser;
    }

    public List<Relationship> createRelationships(Path profileDirectory, Fields fields, List<RelationshipDTO> relationships) {
        if (relationships == null) {
            return Collections.emptyList();
        }

        return relationships.stream()
            .map(relationshipDTO -> createRelationship(profileDirectory, fields, relationshipDTO))
            .collect(Collectors.toList());
    }

    private Relationship createRelationship(Path profileDirectory, Fields fields, RelationshipDTO relationshipDTO) {
        return new Relationship(
            relationshipDTO.name,
            relationshipDTO.description,
            relationshipDTO.profile != null
                ? createProfile(profileDirectory, relationshipDTO.profile)
                : readProfile(profileDirectory, relationshipDTO.profileFile),
            mapConstraints(fields, relationshipDTO.extents)
        );
    }

    private List<Constraint> mapConstraints(Fields fields, List<ConstraintDTO> extents) {
        if (extents == null || extents.isEmpty()){
            return Collections.emptyList();
        }

        return extents
            .stream()
            .map(c -> createConstraint(fields, c))
            .collect(Collectors.toList());
    }

    private Constraint createConstraint(Fields fields, ConstraintDTO extent) {
        return constraintService.createConstraints(
            Collections.singletonList(extent),
            new ExtentAugmentedFields(fields)).get(0);
    }

    private Profile readProfile(Path profileDirectory, String profileFile) {
        if (profileFile == null || profileFile.equals("")) {
            throw new ValidationException("Relationships must have a `profile` (sub profile) or a `profileFile` (file path) property populated");
        }

        //read file from disk.
        File file = Paths.get(profileDirectory.toString(), profileFile).toFile();
        try {
            RelationalProfileDTO profileDTO = profileDeserialiser.deserialise(file);
            return createProfile(profileDirectory, profileDTO);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private Profile createProfile(Path profileDirectory, RelationalProfileDTO profile) {
        CommandResult<Profile> createProfileResult = commandBus.send(new CreateProfile(profileDirectory, profile));
        if (!createProfileResult.isSuccess){
            throw new ValidationException(createProfileResult.errors);
        }
        return createProfileResult.value;
    }
}
