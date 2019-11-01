package com.scottlogic.deg.profile.reader.services;

import com.scottlogic.deg.common.profile.FieldType;
import com.scottlogic.deg.common.profile.Fields;
import com.scottlogic.deg.generator.profile.constraints.Constraint;
import com.scottlogic.deg.generator.profile.constraints.grammatical.AndConstraint;
import com.scottlogic.deg.generator.profile.constraints.grammatical.ConditionalConstraint;
import com.scottlogic.deg.generator.profile.constraints.grammatical.GrammaticalConstraint;
import com.scottlogic.deg.generator.profile.constraints.grammatical.OrConstraint;
import com.scottlogic.deg.profile.common.ConstraintType;
import com.scottlogic.deg.profile.dtos.constraints.ConstraintDTO;
import com.scottlogic.deg.profile.dtos.constraints.InMapConstraintDTO;
import com.scottlogic.deg.profile.dtos.constraints.NotConstraintDTO;
import com.scottlogic.deg.profile.dtos.constraints.atomic.AtomicConstraintDTO;
import com.scottlogic.deg.profile.dtos.constraints.grammatical.AllOfConstraintDTO;
import com.scottlogic.deg.profile.dtos.constraints.grammatical.AnyOfConstraintDTO;
import com.scottlogic.deg.profile.dtos.constraints.grammatical.ConditionalConstraintDTO;
import com.scottlogic.deg.profile.dtos.constraints.grammatical.GrammaticalConstraintDTO;
import com.scottlogic.deg.profile.dtos.constraints.relations.RelationalConstraintDTO;
import com.scottlogic.deg.profile.reader.FileReader;
import com.scottlogic.deg.profile.reader.services.constraint_factories.*;
import com.scottlogic.deg.profile.reader.services.relation_factories.DateTimeRelationFactory;
import com.scottlogic.deg.profile.reader.services.relation_factories.FieldSpecRelationFactory;
import com.scottlogic.deg.profile.reader.services.relation_factories.NumericRelationFactory;
import com.scottlogic.deg.profile.reader.services.relation_factories.StringRelationFactory;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ConstraintService
{
    private final Map<FieldType, AtomicConstraintFactory> atomicConstraintFactoryMap;
    private final Map<FieldType, FieldSpecRelationFactory> relationFactoryMap;

    public ConstraintService(FileReader fileReader)
    {
        atomicConstraintFactoryMap = new EnumMap<>(FieldType.class);
        atomicConstraintFactoryMap.put(FieldType.DATETIME, new DateTimeConstraintFactory(fileReader));
        atomicConstraintFactoryMap.put(FieldType.NUMERIC, new NumericConstraintFactory(fileReader));
        atomicConstraintFactoryMap.put(FieldType.STRING, new StringConstraintFactory(fileReader));

        relationFactoryMap = new EnumMap<>(FieldType.class);
        relationFactoryMap.put(FieldType.DATETIME, new DateTimeRelationFactory());
        relationFactoryMap.put(FieldType.NUMERIC, new NumericRelationFactory());
        relationFactoryMap.put(FieldType.STRING, new StringRelationFactory());
    }

    public List<Constraint> createConstraints(List<ConstraintDTO> constraintDTOs, Fields fields)
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
