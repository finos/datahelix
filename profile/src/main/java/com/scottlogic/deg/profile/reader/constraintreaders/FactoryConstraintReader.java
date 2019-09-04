package com.scottlogic.deg.profile.reader.constraintreaders;

import com.scottlogic.deg.common.profile.ProfileFields;
import com.scottlogic.deg.common.profile.constraints.Constraint;
import com.scottlogic.deg.profile.dto.AtomicConstraintType;
import com.scottlogic.deg.profile.dto.ConstraintDTO;
import com.scottlogic.deg.profile.reader.atomic.AtomicConstraintFactory;
import com.scottlogic.deg.profile.reader.AtomicConstraintReader;
import com.scottlogic.deg.profile.reader.InvalidProfileException;

public class FactoryConstraintReader implements AtomicConstraintReader {

    private final AtomicConstraintType type;

    public FactoryConstraintReader(AtomicConstraintType type) {
        this.type = type;
    }

    @Override
    public Constraint apply(ConstraintDTO dto, ProfileFields fields) {
        try {
            return AtomicConstraintFactory.create(type, fields.getByName(dto.field), dto.value);
        } catch (InvalidProfileException e){
            String newMessage = e.getMessage().replaceAll("REPLACE", String.format(dto.field));
            throw new InvalidProfileException(newMessage);
        }
    }
}
