package com.scottlogic.deg.generator.inputs;

import com.scottlogic.deg.generator.ProfileFields;
import com.scottlogic.deg.generator.constraints.*;
import com.scottlogic.deg.schemas.v3.ConstraintDTO;

public class MainConstraintReader implements IConstraintReader {
    private final AtomicConstraintReaderLookup atomicConstraintReaderLookup;

    public MainConstraintReader() {
        this.atomicConstraintReaderLookup = new AtomicConstraintReaderLookup();
    }

    @Override
    public IConstraint apply(
        ConstraintDTO dto,
        ProfileFields fields)
        throws InvalidProfileException {

        if (dto == null) {
            throw new InvalidProfileException("Constraint is null");
        }

        if (dto.is != null) {
            IConstraintReader subReader = this.atomicConstraintReaderLookup.getByTypeCode(dto.is);

            if (subReader == null) {
                throw new InvalidProfileException("Couldn't recognise constraint type from DTO: " + dto.is);
            }

            return subReader.apply(dto, fields);
        }

        if (dto.not != null) {
            return new NotConstraint(this.apply(dto.not, fields));
        }

        if (dto.allOf != null) {
            return new AndConstraint(
                ProfileReader.mapDtos(
                    dto.allOf,
                    subConstraintDto -> this.apply(
                        subConstraintDto,
                        fields)));
        }

        if (dto.anyOf != null) {
            return new OrConstraint(
                ProfileReader.mapDtos(
                    dto.anyOf,
                    subConstraintDto -> this.apply(
                        subConstraintDto,
                        fields)));
        }

        if (dto.if_ != null) {
            return new ConditionalConstraint(
                this.apply(
                    dto.if_,
                    fields),
                this.apply(
                    dto.then,
                    fields),
                dto.else_ != null
                    ? this.apply(
                        dto.else_,
                        fields)
                    : null);
        }

        throw new InvalidProfileException("Couldn't interpret constraint");
    }
}
