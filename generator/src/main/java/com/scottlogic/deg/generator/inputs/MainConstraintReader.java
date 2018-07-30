package com.scottlogic.deg.generator.inputs;

import com.scottlogic.deg.generator.constraints.*;
import com.scottlogic.deg.schemas.v3.ConstraintDTO;

class MainConstraintReader implements IConstraintReader {
    private final AtomicConstraintReaderLookup atomicConstraintReaderLookup;

    MainConstraintReader() {
        this.atomicConstraintReaderLookup = new AtomicConstraintReaderLookup();
    }

    @Override
    public IConstraint apply(
        ConstraintDTO dto,
        FieldLookup fields)
        throws InvalidProfileException {

        if (dto.type != null) {
            IConstraintReader subReader = this.atomicConstraintReaderLookup.getByTypeCode(dto.type);

            return subReader.apply(dto, fields);
        }

        if (dto.not != null) {
            return new NotConstraint(this.apply(dto, fields));
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
                this.apply(
                    dto.else_,
                    fields));
        }

        throw new InvalidProfileException("Couldn't interpret constraint");
    }
}
