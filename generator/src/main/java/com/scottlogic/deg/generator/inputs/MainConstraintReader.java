package com.scottlogic.deg.generator.inputs;

import com.scottlogic.deg.generator.ProfileFields;
import com.scottlogic.deg.generator.constraints.*;
import com.scottlogic.deg.generator.constraints.grammatical.AndConstraint;
import com.scottlogic.deg.generator.constraints.grammatical.ConditionalConstraint;
import com.scottlogic.deg.generator.constraints.grammatical.OrConstraint;
import com.scottlogic.deg.schemas.v3.ConstraintDTO;
import com.scottlogic.deg.schemas.v3.RuleDTO;

public class MainConstraintReader implements IConstraintReader {
    private final AtomicConstraintReaderLookup atomicConstraintReaderLookup;

    public MainConstraintReader() {
        this.atomicConstraintReaderLookup = new AtomicConstraintReaderLookup();
    }

    @Override
    public Constraint apply(
        ConstraintDTO dto,
        ProfileFields fields,
        ConstraintRule rule)
        throws InvalidProfileException {

        if (dto == null) {
            throw new InvalidProfileException("Constraint is null");
        }

        if (dto.is != null) {
            IConstraintReader subReader = this.atomicConstraintReaderLookup.getByTypeCode(dto.is);

            if (subReader == null) {
                throw new InvalidProfileException("Couldn't recognise constraint type from DTO: " + dto.is);
            }

            return subReader.apply(dto, fields, rule);
        }

        if (dto.not != null) {
            return this.apply(dto.not, fields, rule).negate();
        }

        if (dto.allOf != null) {
            return new AndConstraint(
                ProfileReader.mapDtos(
                    dto.allOf,
                    subConstraintDto -> this.apply(
                        subConstraintDto,
                        fields,
                        rule)));
        }

        if (dto.anyOf != null) {
            return new OrConstraint(
                ProfileReader.mapDtos(
                    dto.anyOf,
                    subConstraintDto -> this.apply(
                        subConstraintDto,
                        fields,
                        rule)));
        }

        if (dto.if_ != null) {
            return new ConditionalConstraint(
                this.apply(
                    dto.if_,
                    fields,
                    rule),
                this.apply(
                    dto.then,
                    fields,
                    rule),
                dto.else_ != null
                    ? this.apply(
                        dto.else_,
                        fields,
                        rule)
                    : null);
        }

        throw new InvalidProfileException("Couldn't interpret constraint");
    }
}
