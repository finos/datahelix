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
package com.scottlogic.deg.profile.services;

import com.google.inject.Inject;
import com.scottlogic.deg.common.profile.*;
import com.scottlogic.deg.generator.profile.constraints.Constraint;
import com.scottlogic.deg.generator.profile.constraints.atomic.*;
import com.scottlogic.deg.generator.profile.constraints.grammatical.AndConstraint;
import com.scottlogic.deg.generator.profile.constraints.grammatical.ConditionalConstraint;
import com.scottlogic.deg.generator.profile.constraints.grammatical.GrammaticalConstraint;
import com.scottlogic.deg.generator.profile.constraints.grammatical.OrConstraint;
import com.scottlogic.deg.profile.dtos.constraints.ConstraintType;
import com.scottlogic.deg.profile.dtos.constraints.ConstraintDTO;
import com.scottlogic.deg.profile.dtos.constraints.grammatical.NotConstraintDTO;
import com.scottlogic.deg.profile.dtos.constraints.atomic.AtomicConstraintDTO;
import com.scottlogic.deg.profile.dtos.constraints.grammatical.AllOfConstraintDTO;
import com.scottlogic.deg.profile.dtos.constraints.grammatical.AnyOfConstraintDTO;
import com.scottlogic.deg.profile.dtos.constraints.grammatical.ConditionalConstraintDTO;
import com.scottlogic.deg.profile.dtos.constraints.grammatical.GrammaticalConstraintDTO;
import com.scottlogic.deg.profile.dtos.constraints.relations.InMapConstraintDTO;
import com.scottlogic.deg.profile.dtos.constraints.relations.RelationalConstraintDTO;
import com.scottlogic.deg.profile.factories.constraint_factories.AtomicConstraintFactory;
import com.scottlogic.deg.profile.factories.constraint_factories.DateTimeConstraintFactory;
import com.scottlogic.deg.profile.factories.constraint_factories.NumericConstraintFactory;
import com.scottlogic.deg.profile.factories.constraint_factories.StringConstraintFactory;
import com.scottlogic.deg.profile.factories.relation_factories.DateTimeRelationFactory;
import com.scottlogic.deg.profile.factories.relation_factories.FieldSpecRelationFactory;
import com.scottlogic.deg.profile.factories.relation_factories.NumericRelationFactory;
import com.scottlogic.deg.profile.factories.relation_factories.StringRelationFactory;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class ConstraintService
{
    private final Map<FieldType, AtomicConstraintFactory> atomicConstraintFactoryMap;
    private final Map<FieldType, FieldSpecRelationFactory> relationFactoryMap;

    @Inject
    public ConstraintService()
    {
        atomicConstraintFactoryMap = new EnumMap<>(FieldType.class);
        atomicConstraintFactoryMap.put(FieldType.DATETIME, new DateTimeConstraintFactory());
        atomicConstraintFactoryMap.put(FieldType.NUMERIC, new NumericConstraintFactory());
        atomicConstraintFactoryMap.put(FieldType.STRING, new StringConstraintFactory());

        relationFactoryMap = new EnumMap<>(FieldType.class);
        relationFactoryMap.put(FieldType.DATETIME, new DateTimeRelationFactory());
        relationFactoryMap.put(FieldType.NUMERIC, new NumericRelationFactory());
        relationFactoryMap.put(FieldType.STRING, new StringRelationFactory());
    }

    Optional<Constraint> createSpecificTypeConstraint(Field field)
    {
        switch (field.getSpecificType()) {
            case DATE:
                return Optional.of(new GranularToDateConstraint(field, new DateTimeGranularity(ChronoUnit.DAYS)));
            case INTEGER:
                return Optional.of(new GranularToNumericConstraint(field, NumericGranularity.create(BigDecimal.ONE)));
            case ISIN:
                return Optional.of(new MatchesStandardConstraint(field, StandardConstraintTypes.ISIN));
            case SEDOL:
                return Optional.of(new MatchesStandardConstraint(field, StandardConstraintTypes.SEDOL));
            case CUSIP:
                return Optional.of(new MatchesStandardConstraint(field, StandardConstraintTypes.CUSIP));
            case RIC:
                return Optional.of(new MatchesStandardConstraint(field, StandardConstraintTypes.RIC));
            case FIRST_NAME:
                return Optional.of(new InSetConstraint(field, NameRetrievalService.loadNamesFromFile(NameConstraintTypes.FIRST)));
            case LAST_NAME:
                return Optional.of(new InSetConstraint(field, NameRetrievalService.loadNamesFromFile(NameConstraintTypes.LAST)));
            case FULL_NAME:
                return Optional.of(new InSetConstraint(field, NameRetrievalService.loadNamesFromFile(NameConstraintTypes.FULL)));
            default:
                return Optional.empty();
        }
    }

    List<Constraint> createConstraints(List<ConstraintDTO> constraintDTOs, Fields fields)
    {
        return constraintDTOs.stream().map(dto -> createConstraint(dto, fields)).collect(Collectors.toList());
    }

    private Constraint createConstraint(ConstraintDTO dto, Fields fields)
    {
        if (dto.getType() == ConstraintType.IN_MAP)
        {
            FieldType type = fields.getByName(((InMapConstraintDTO)dto).field).getType();
            return atomicConstraintFactoryMap.get(type).createInMapRelation((InMapConstraintDTO) dto, fields);
        }
        if (dto.getType() == ConstraintType.NOT)
        {
            return createConstraint(((NotConstraintDTO) dto).constraint, fields).negate();
        }
        if (dto instanceof RelationalConstraintDTO)
        {
            FieldType type = fields.getByName(((RelationalConstraintDTO)dto).field).getType();
            return relationFactoryMap.get(type).createRelation((RelationalConstraintDTO) dto, fields);
        }
        if (dto instanceof AtomicConstraintDTO)
        {
            FieldType type = fields.getByName(((AtomicConstraintDTO)dto).field).getType();
            return atomicConstraintFactoryMap.get(type).createAtomicConstraint((AtomicConstraintDTO) dto, fields);
        }
        if (dto instanceof GrammaticalConstraintDTO)
        {
            return createGrammaticalConstraint((GrammaticalConstraintDTO) dto, fields);
        }
        throw new IllegalStateException("Unexpected constraint type: " + dto.getType());
    }

    private GrammaticalConstraint createGrammaticalConstraint(GrammaticalConstraintDTO dto, Fields fields)
    {
        switch (dto.getType())
        {
            case ALL_OF:
                return new AndConstraint(createConstraints(((AllOfConstraintDTO) dto).constraints, fields));
            case ANY_OF:
                return new OrConstraint(createConstraints(((AnyOfConstraintDTO) dto).constraints, fields));
            case IF:
                return createConditionalConstraint((ConditionalConstraintDTO) dto, fields);
            default:
                throw new IllegalStateException("Unexpected grammatical constraint type: " + dto.getType());
        }
    }

    private ConditionalConstraint createConditionalConstraint(ConditionalConstraintDTO dto, Fields fields)
    {
        Constraint ifConstraint = createConstraint(dto.ifConstraint, fields);
        Constraint thenConstraint = createConstraint(dto.thenConstraint, fields);
        Constraint elseConstraint = dto.elseConstraint == null ? null : createConstraint(dto.elseConstraint, fields);

        return new ConditionalConstraint(ifConstraint, thenConstraint, elseConstraint);
    }
}
