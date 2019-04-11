package com.scottlogic.deg.generator.inputs;

import com.google.inject.Inject;
import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.Profile;
import com.scottlogic.deg.generator.ProfileFields;
import com.scottlogic.deg.generator.Rule;
import com.scottlogic.deg.generator.constraints.Constraint;
import com.scottlogic.deg.generator.constraints.atomic.IsStringShorterThanConstraint;
import com.scottlogic.deg.generator.constraints.atomic.SoftAtomicConstraint;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SoftConstraintAppendingProfileReader implements ProfileReader {
    private final ProfileReader underlyingReader;

    @Inject
    public SoftConstraintAppendingProfileReader(ProfileReader underlyingReader) {
        this.underlyingReader = underlyingReader;
    }

    @Override
    public Profile read(Path filePath) throws IOException, InvalidProfileException {
        Profile profile = underlyingReader.read(filePath);

        Collection<Constraint> softConstraints = createSoftConstraintsRule(profile.fields);

        return new Profile(
            profile.fields,
            profile.rules
                .stream()
                .map(r -> new Rule(r.ruleInformation, concat(softConstraints, r.constraints)))
                .collect(Collectors.toList())
        );
    }

    private static <T> Collection<T> concat(Collection<T> source, Collection<T> append){
        return Stream.concat(
            source.stream(),
            append.stream()
        ).collect(Collectors.toList());
    }


    private Collection<Constraint> createSoftConstraintsRule(ProfileFields profileFields) {
        return profileFields
            .stream()
            .flatMap(this::createSoftConstraintsForField)
            .collect(Collectors.toList());
    }

    private Stream<Constraint> createSoftConstraintsForField(Field field) {
        return Stream.of(
            new SoftAtomicConstraint(
                new IsStringShorterThanConstraint(field, 256, Collections.emptySet()))
        );
    }

}
