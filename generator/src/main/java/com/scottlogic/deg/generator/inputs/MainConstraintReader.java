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
            return this.readAtomic(dto, fields);
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

    private IConstraint readAtomic(
        ConstraintDTO dto,
        FieldLookup fields)
        throws InvalidProfileException {

        String[] splitConstraintType = dto.type.split("\\s+");

        final String typeCode;
        final boolean isNegated;

        if (splitConstraintType.length > 2) {
            throw new InvalidProfileException("Constraint type " + dto.type + " is not valid");
        }

        if (splitConstraintType.length == 1) {
            typeCode = splitConstraintType[0];
            isNegated = false;
        }
        else {
            typeCode = splitConstraintType[1];

            if (!splitConstraintType[0].equals("not"))
                throw new InvalidProfileException("Constraint type " + dto.type + " is not valid");

            isNegated = true;
        }

        IConstraintReader subReader = this.atomicConstraintReaderLookup.getByTypeCode(typeCode);

        IConstraint baseConstraint = subReader.apply(dto, fields);

        if (isNegated)
            return new NotConstraint(baseConstraint);
        else
            return baseConstraint;
    }
}
