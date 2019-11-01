package com.scottlogic.deg.profile.reader.handlers;

import com.scottlogic.deg.common.commands.CommandHandler;
import com.scottlogic.deg.common.commands.CommandResult;
import com.scottlogic.deg.common.profile.*;
import com.scottlogic.deg.common.util.NumberUtils;
import com.scottlogic.deg.common.util.defaults.DateTimeDefaults;
import com.scottlogic.deg.common.util.defaults.NumericDefaults;
import com.scottlogic.deg.common.validators.Validator;
import com.scottlogic.deg.generator.fieldspecs.relations.*;
import com.scottlogic.deg.generator.fieldspecs.whitelist.DistributedList;
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
import com.scottlogic.deg.profile.dtos.constraints.relations.EqualToFieldConstraintDTO;
import com.scottlogic.deg.profile.dtos.constraints.relations.RelationalConstraintDTO;
import com.scottlogic.deg.profile.reader.FileReader;
import com.scottlogic.deg.profile.reader.commands.CreateConstraints;
import com.scottlogic.deg.profile.reader.services.constraints.atomic.AtomicConstraintService;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class CreateConstraintsHandler extends CommandHandler<CreateConstraints, List<Constraint>>
{
    private final FileReader fileReader;
    private final AtomicConstraintService atomicConstraintService;

    public CreateConstraintsHandler(FileReader fileReader, Validator<CreateConstraints> validator)
    {
        super(validator);
        this.fileReader = fileReader;
        this.atomicConstraintService = new AtomicConstraintService(fileReader);
    }

    @Override
    public CommandResult<List<Constraint>> handleCommand(CreateConstraints command)
    {
        try
        {
            return CommandResult.success(createConstraints(command.constraintDTOs, command.fields));
        }
        catch (Exception e)
        {
            return CommandResult.failure(Collections.singletonList(e.getMessage()));
        }
    }

    private List<Constraint> createConstraints(List<ConstraintDTO> constraintDTOs, Fields fields)
    {
        return constraintDTOs.stream().map(dto -> createConstraint(dto, fields)).collect(Collectors.toList());
    }

    private Constraint createConstraint(ConstraintDTO dto, Fields fields)
    {
        if (dto.getType() == ConstraintType.IN_MAP)
        {
            return createInMapRelation((InMapConstraintDTO) dto, fields);
        }
        if (dto.getType() == ConstraintType.NOT)
        {
            return createConstraint(((NotConstraintDTO) dto).constraint, fields).negate();
        }
        if (dto instanceof RelationalConstraintDTO)
        {
            return createRelation((RelationalConstraintDTO) dto, fields);
        }
        if (dto instanceof AtomicConstraintDTO)
        {
            return atomicConstraintService.create((AtomicConstraintDTO) dto, fields);
        }
        if (dto instanceof GrammaticalConstraintDTO)
        {
            return createGrammaticalConstraint((GrammaticalConstraintDTO) dto, fields);
        }
        throw new IllegalStateException("Unexpected constraint type: " + dto.getType());
    }

    private FieldSpecRelations createRelation(RelationalConstraintDTO dto, Fields fields)
    {
        Field main = fields.getByName(dto.field);
        Field other = fields.getByName(dto.getOtherField());
        DateTimeDefaults dateTimeDefaults = DateTimeDefaults.get();
        NumericDefaults numericDefaults = NumericDefaults.get();
        switch (dto.getType())
        {
            case EQUAL_TO_FIELD:
                return createEqualToRelation((EqualToFieldConstraintDTO) dto, fields);
            case AFTER_FIELD:
                return new AfterRelation(main, other, false, dateTimeDefaults);
            case AFTER_OR_AT_FIELD:
                return new AfterRelation(main, other, true, dateTimeDefaults);
            case BEFORE_FIELD:
                return new BeforeRelation(main, other, false, dateTimeDefaults);
            case BEFORE_OR_AT_FIELD:
                return new BeforeRelation(main, other, true, dateTimeDefaults);
            case GREATER_THAN_FIELD:
                return new AfterRelation(main, other, false, numericDefaults);
            case GREATER_THAN_OR_EQUAL_TO_FIELD:
                return new AfterRelation(main, other, true, numericDefaults);
            case LESS_THAN_FIELD:
                return new BeforeRelation(main, other, false, numericDefaults);
            case LESS_THAN_OR_EQUAL_TO_FIELD:
                return new BeforeRelation(main, other, true, numericDefaults);
            default:
                throw new IllegalStateException("Unexpected relation type: " + dto.getType());
        }
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



    private InMapRelation createInMapRelation(InMapConstraintDTO dto, Fields fields)
    {
        Field main = fields.getByName(dto.field);
        Field other = fields.getByName(dto.file);

        List<Object> values = fileReader.listFromMapFile(dto.file, dto.key).stream()
            .map(value -> parseGenericValue(fields.getByName(dto.field), value))
            .collect(Collectors.toList());

        return new InMapRelation(main, other, DistributedList.uniform(values));
    }

    private FieldSpecRelations createEqualToRelation(EqualToFieldConstraintDTO dto, Fields fields)
    {
        Field main = fields.getByName(dto.field);
        Field other = fields.getByName(dto.getOtherField());
        if( dto.offsetUnit == null) return new EqualToRelation(main, other);
        switch (main.getType())
        {
            case NUMERIC:
                return new EqualToOffsetRelation(main, other, NumericGranularity.create(dto.offsetUnit), dto.offset);
            case DATETIME:
                return new EqualToOffsetRelation(main, other, DateTimeGranularity.create(dto.offsetUnit), dto.offset);
            default:
                return new EqualToRelation(main, other);
        }
    }

    private ConditionalConstraint createConditionalConstraint(ConditionalConstraintDTO dto, Fields fields)
    {
        Constraint ifConstraint = createConstraint(dto.ifConstraint, fields);
        Constraint thenConstraint = createConstraint(dto.thenConstraint, fields);
        Constraint elseConstraint = dto.elseConstraint == null ? null : createConstraint(dto.elseConstraint, fields);

        return new ConditionalConstraint(ifConstraint, thenConstraint, elseConstraint);
    }


   private Object parseGenericValue(Field field, Object value)
    {
        switch (field.getType())
        {
            case DATETIME:
                return HelixDateTime.create((String) value).getValue();
            case NUMERIC:
                return NumberUtils.coerceToBigDecimal(value);
            default:
                return value;
        }
    }
}
