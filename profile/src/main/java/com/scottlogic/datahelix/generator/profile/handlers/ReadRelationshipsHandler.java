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
import com.scottlogic.datahelix.generator.common.validators.Validator;
import com.scottlogic.datahelix.generator.core.profile.relationships.Relationship;
import com.scottlogic.datahelix.generator.profile.commands.ReadRelationships;
import com.scottlogic.datahelix.generator.profile.services.RelationshipService;

import java.util.List;

public class ReadRelationshipsHandler extends CommandHandler<ReadRelationships, List<Relationship>> {
    private final RelationshipService relationshipService;

    @Inject
    public ReadRelationshipsHandler(
        RelationshipService relationshipService,
        Validator<ReadRelationships> validator) {
        super(validator);
        this.relationshipService = relationshipService;
    }

    @Override
    public CommandResult<List<Relationship>> handleCommand(ReadRelationships command) {
        return CommandResult.success(relationshipService.createRelationships(command.profileDirectory, command.fields, command.relationships));
    }
}
