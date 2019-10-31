package com.scottlogic.deg.profile.handlers;

import com.scottlogic.deg.common.commands.CommandBus;
import com.scottlogic.deg.common.commands.CommandHandler;
import com.scottlogic.deg.common.commands.CommandResult;
import com.scottlogic.deg.common.profile.*;
import com.scottlogic.deg.common.util.NumberUtils;
import com.scottlogic.deg.common.util.defaults.DateTimeDefaults;
import com.scottlogic.deg.common.util.defaults.NumericDefaults;
import com.scottlogic.deg.common.validators.Validator;
import com.scottlogic.deg.generator.fieldspecs.relations.*;
import com.scottlogic.deg.generator.fieldspecs.whitelist.DistributedList;
import com.scottlogic.deg.generator.profile.Profile;
import com.scottlogic.deg.generator.profile.Rule;
import com.scottlogic.deg.generator.profile.constraints.Constraint;
import com.scottlogic.deg.generator.profile.constraints.atomic.*;
import com.scottlogic.deg.generator.profile.constraints.grammatical.AndConstraint;
import com.scottlogic.deg.generator.profile.constraints.grammatical.ConditionalConstraint;
import com.scottlogic.deg.generator.profile.constraints.grammatical.GrammaticalConstraint;
import com.scottlogic.deg.generator.profile.constraints.grammatical.OrConstraint;
import com.scottlogic.deg.profile.commands.CreateFields;
import com.scottlogic.deg.profile.commands.CreateProfile;
import com.scottlogic.deg.profile.common.ConstraintType;
import com.scottlogic.deg.profile.dtos.RuleDTO;
import com.scottlogic.deg.profile.dtos.constraints.*;
import com.scottlogic.deg.profile.reader.FileReader;
import com.scottlogic.deg.profile.reader.InvalidProfileException;
import com.scottlogic.deg.profile.reader.NameRetriever;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class CreateProfileHandler extends CommandHandler<CreateProfile, Profile>
{
    private final FileReader fileReader;
    private final CommandBus bus;

    public CreateProfileHandler(FileReader fileReader, CommandBus bus, Validator<CreateProfile> validator)
    {
        super(validator);
        this.fileReader = fileReader;
        this.bus = bus;
    }

    @Override
    protected CommandResult<Profile> handleCommand(CreateProfile command)
    {
        CommandResult<Fields> createFieldsResult = bus.send(new CreateFields(command.profileDTO.fields, command.profileDTO.rules));
        if (!createFieldsResult.isSuccess) return CommandResult.failure(createFieldsResult.errors);
        Fields fields = createFieldsResult.value;

        CommandResult<List<Rule>> createRulesResult = createRules(command.profileDTO.rules, fields);
        if (!createRulesResult.isSuccess) return CommandResult.failure(createRulesResult.errors);
        List<Rule> rules = createRulesResult.value;

        createNotNullableRule(fields).ifPresent(rules::add);
        createSpecificTypeRule(fields).ifPresent(rules::add);

        return CommandResult.success(new Profile(fields, rules, command.profileDTO.description));
    }

    private CommandResult<List<Rule>> createRules(List<RuleDTO> ruleDTOs, Fields fields)
    {
        return CommandResult.combine(ruleDTOs.stream()
            .map(ruleDTO -> createRule(ruleDTO, fields))
            .collect(Collectors.toList()));
    }

    private CommandResult<Rule> createRule(RuleDTO ruleDTO, Fields fields)
    {
        return createConstraints(ruleDTO.constraints, fields)
            .map(constraints -> new Rule(ruleDTO.description, constraints));
    }

    private CommandResult<List<Constraint>> createConstraints(List<ConstraintDTO> constraintDTOs, Fields fields)
    {
        CommandResult<List<Constraint>> createConstraintsResult = CommandResult.combine(constraintDTOs.stream()
            .map(dto -> createConstraint(dto, fields))
            .collect(Collectors.toList()));

        return createConstraintsResult.isSuccess
            ? CommandResult.success(createConstraintsResult.value)
            : CommandResult.failure(createConstraintsResult.errors);
    }

    private CommandResult<Constraint> createConstraint(ConstraintDTO dto, Fields fields)
    {
        if (dto == null) {
            throw new InvalidProfileException("Constraint is null");
        } else if (dto.getType()== ConstraintType.IN_MAP) {
            InMapConstraintDTO inMapConstraintDTO = (InMapConstraintDTO) dto;
            return CommandResult.success(new InMapRelation(fields.getByName(inMapConstraintDTO.field), fields.getByName(inMapConstraintDTO.file),
                DistributedList.uniform(fileReader.listFromMapFile(inMapConstraintDTO.file, inMapConstraintDTO.key).stream()
                    .map(value -> readAnyType(fields.getByName(inMapConstraintDTO.field), value))
                    .collect(Collectors.toList()))));
        } else if (dto.getType()==ConstraintType.NOT)
            return createConstraint(((NotConstraintDTO)dto).constraint, fields).map(Constraint::negate);
        else if (dto instanceof RelationalConstraintDTO) {
            return CommandResult.success(readRelationalConstraintDto((RelationalConstraintDTO) dto, fields));
        } else if (dto instanceof AtomicConstraintDTO)
            return CommandResult.success(readAtomicConstraintDto((AtomicConstraintDTO) dto, fields));
        else {
            return readGrammaticalConstraintDto(dto, fields).map(c -> c);
        }
    }

    private AtomicConstraint readAtomicConstraintDto(AtomicConstraintDTO dto, Fields fields) {
        Field field = fields.getByName(dto.field);
        switch (dto.getType()) {
            case EQUAL_TO:
                return new EqualToConstraint(field, readAnyType(field, ((EqualToConstraintDTO) dto).value));
            case IN_SET:
                InSetConstraintDTO inSetConstraintDTO = (InSetConstraintDTO) dto;
                return new InSetConstraint(field, prepareValuesForSet(inSetConstraintDTO, field));
            case MATCHES_REGEX:
                return new MatchesRegexConstraint(field, readPattern(((MatchesRegexConstraintDTO) dto).value));
            case CONTAINS_REGEX:
                return new ContainsRegexConstraint(field, readPattern(((ContainsRegexConstraintDTO) dto).value));
            case OF_LENGTH:
                return new StringHasLengthConstraint(field, HelixStringLength.create(((OfLengthConstraintDTO) dto).value));
            case SHORTER_THAN:
                return new IsStringShorterThanConstraint(field, HelixStringLength.create(((ShorterThanConstraintDTO) dto).value));
            case LONGER_THAN:
                return new IsStringLongerThanConstraint(field, HelixStringLength.create(((LongerThanConstraintDTO) dto).value));
            case GREATER_THAN:
                return new GreaterThanConstraint(field, HelixNumber.create(((GreaterThanConstraintDTO) dto).value));
            case GREATER_THAN_OR_EQUAL_TO:
                return new GreaterThanOrEqualToConstraint(field, HelixNumber.create(((GreaterThanOrEqualToConstraintDTO) dto).value));
            case LESS_THAN:
                return new LessThanConstraint(field, HelixNumber.create(((LessThanConstraintDTO) dto).value));
            case LESS_THAN_OR_EQUAL_TO:
                return new IsLessThanOrEqualToConstantConstraint(field, HelixNumber.create(((LessThanOrEqualToConstraintDTO) dto).value));
            case AFTER:
                return new AfterConstraint(field, HelixDateTime.create(((AfterConstraintDTO) dto).value));
            case AFTER_OR_AT:
                return new AfterOrAtConstraint(field, HelixDateTime.create(((AfterOrAtConstraintDTO) dto).value));
            case BEFORE:
                return new BeforeConstraint(field, HelixDateTime.create(((BeforeConstraintDTO) dto).value));
            case BEFORE_OR_AT:
                return new BeforeOrAtConstraint(field, HelixDateTime.create(((BeforeOrAtConstraintDTO) dto).value));
            case GRANULAR_TO:
                GranularToConstraintDTO granularToConstraintDTO = (GranularToConstraintDTO) dto;
                return granularToConstraintDTO.value instanceof Number
                    ? new GranularToNumericConstraint(field, NumericGranularity.create(granularToConstraintDTO.value))
                    : new GranularToDateConstraint(field, DateTimeGranularity.create((String) granularToConstraintDTO.value));
            case IS_NULL:
                IsNullConstraint isNullConstraint = new IsNullConstraint(fields.getByName(((NullConstraintDTO) dto).field));
                return ((NullConstraintDTO)dto).isNull
                    ? isNullConstraint
                    : isNullConstraint.negate();
            default:
                throw new InvalidProfileException("Atomic constraint type not found: " + dto);
        }
    }

    private DistributedList<Object> prepareValuesForSet(InSetConstraintDTO inSetConstraintDTO, Field field) {
        return (inSetConstraintDTO instanceof InSetFromFileConstraintDTO
            ? fileReader.setFromFile(((InSetFromFileConstraintDTO) inSetConstraintDTO).file)
            : DistributedList.uniform(((InSetOfValuesConstraintDTO) inSetConstraintDTO).values.stream()
            .distinct()
            .map(o -> readAnyType(field, o))
            .collect(Collectors.toList())));
    }

    private Object readAnyType(Field field, Object value) {
        switch (field.getType()) {
            case DATETIME:
                return HelixDateTime.create((String) value).getValue();
            case NUMERIC:
                return NumberUtils.coerceToBigDecimal(value);
            default:
                return value;
        }
    }

    private Pattern readPattern(Object value) {
        if (value instanceof Pattern) return (Pattern) value;
        try {
            return Pattern.compile((String) value);
        } catch (IllegalArgumentException e) {
            throw new InvalidProfileException(e.getMessage());
        }
    }

    private FieldSpecRelations readRelationalConstraintDto(RelationalConstraintDTO dto, Fields fields) {
        Field main = fields.getByName(dto.field);
        Field other = fields.getByName(dto.getOtherField());
        switch (dto.getType()) {
            case EQUAL_TO_FIELD:
                Granularity offsetGranularity = readGranularity(main.getType(), dto.offsetUnit);
                return offsetGranularity != null
                    ? new EqualToOffsetRelation(main, other, offsetGranularity, dto.offset)
                    : new EqualToRelation(main, other);
            case AFTER_FIELD:
                return new AfterRelation(main, other, false, DateTimeDefaults.get());
            case AFTER_OR_AT_FIELD:
                return new AfterRelation(main, other, true, DateTimeDefaults.get());
            case BEFORE_FIELD:
                return new BeforeRelation(main, other, false, DateTimeDefaults.get());
            case BEFORE_OR_AT_FIELD:
                return new BeforeRelation(main, other, true, DateTimeDefaults.get());
            case GREATER_THAN_FIELD:
                return new AfterRelation(main, other, false, NumericDefaults.get());
            case GREATER_THAN_OR_EQUAL_TO_FIELD:
                return new AfterRelation(main, other, true, NumericDefaults.get());
            case LESS_THAN_FIELD:
                return new BeforeRelation(main, other, false, NumericDefaults.get());
            case LESS_THAN_OR_EQUAL_TO_FIELD:
                return new BeforeRelation(main, other, true, NumericDefaults.get());
            default:
                throw new InvalidProfileException("Unexpected relation data type " + dto.getType());
        }
    }

    private Granularity readGranularity(FieldType type, String offsetUnit) {
        if (offsetUnit == null) return null;
        switch (type) {
            case NUMERIC:
                return NumericGranularity.create(offsetUnit);
            case DATETIME:
                return DateTimeGranularity.create(offsetUnit);
            default:
                return null;
        }
    }

    private CommandResult<GrammaticalConstraint> readGrammaticalConstraintDto(ConstraintDTO dto, Fields fields) {
        switch (dto.getType()) {
            case ALL_OF:
                return createConstraints(((AllOfConstraintDTO) dto).constraints, fields).map(AndConstraint::new);
            case ANY_OF:
                return createConstraints(((AnyOfConstraintDTO) dto).constraints, fields).map(OrConstraint::new);
            case IF:
                return createConditionalConstraint((IfConstraintDTO)dto, fields).map(c -> c);
            default:
                return CommandResult.failure(Collections.singletonList("Grammatical constraint type not found: " + dto));
        }
    }

    private CommandResult<ConditionalConstraint> createConditionalConstraint(IfConstraintDTO dto, Fields fields)
    {
        CommandResult<Constraint> ifConstraint = createConstraint(dto.ifConstraint, fields);
        CommandResult<Constraint> thenConstraint = createConstraint(dto.thenConstraint, fields);
        CommandResult<Constraint>  elseConstraint = dto.elseConstraint == null
            ? CommandResult.success((Constraint) null)
            : createConstraint(dto.elseConstraint, fields);

        return ifConstraint.isSuccess && thenConstraint.isSuccess && elseConstraint.isSuccess
            ? CommandResult.success(new ConditionalConstraint(ifConstraint.value, thenConstraint.value, elseConstraint.value))
            : CommandResult.failure(ifConstraint.errors, thenConstraint.errors, elseConstraint.errors);
    }

    private Optional<Rule> createNotNullableRule(Fields fields)
    {
        List<Constraint> notNullableConstraints =  fields.stream()
            .filter(field -> !field.isNullable())
            .map(field -> new IsNullConstraint(field).negate())
            .collect(Collectors.toList());

        return notNullableConstraints.isEmpty()
            ? Optional.empty()
            : Optional.of(new Rule("not-nullable", notNullableConstraints));
    }

    private Optional<Rule> createSpecificTypeRule(Fields fields)
    {
        List<Constraint> specificTypeConstraints = fields.stream()
            .map(this::createSpecificTypeConstraint)
            .filter(Optional::isPresent)
            .map(Optional::get)
            .collect(Collectors.toList());

         return specificTypeConstraints.isEmpty()
             ? Optional.empty()
             : Optional.of(new Rule("specific-types", specificTypeConstraints));
    }
    
    private Optional<Constraint> createSpecificTypeConstraint(Field field)
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
                return Optional.of(new InSetConstraint(field, NameRetriever.loadNamesFromFile(NameConstraintTypes.FIRST)));
            case LAST_NAME:
                return Optional.of(new InSetConstraint(field, NameRetriever.loadNamesFromFile(NameConstraintTypes.LAST)));
            case FULL_NAME:
                return Optional.of(new InSetConstraint(field, NameRetriever.loadNamesFromFile(NameConstraintTypes.FULL)));
            default:
                return Optional.empty();
        }
    }
}
