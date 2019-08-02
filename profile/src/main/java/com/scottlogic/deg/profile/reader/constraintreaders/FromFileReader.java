package com.scottlogic.deg.profile.reader.constraintreaders;

import com.scottlogic.deg.common.ValidationException;
import com.scottlogic.deg.common.profile.Field;
import com.scottlogic.deg.common.profile.ProfileFields;
import com.scottlogic.deg.common.profile.constraints.Constraint;
import com.scottlogic.deg.common.profile.constraints.atomic.IsInSetConstraint;
import com.scottlogic.deg.generator.fieldspecs.whitelist.DistributedSet;
import com.scottlogic.deg.generator.fieldspecs.whitelist.FrequencyDistributedSet;
import com.scottlogic.deg.generator.fieldspecs.whitelist.WeightedElement;
import com.scottlogic.deg.profile.reader.ConstraintReader;
import com.scottlogic.deg.profile.reader.file.CsvInputStreamReader;
import com.scottlogic.deg.profile.v0_1.ConstraintDTO;

import java.io.*;
import java.util.stream.Collectors;

import static com.scottlogic.deg.profile.reader.ConstraintReaderHelpers.getValidatedValue;

public class FromFileReader implements ConstraintReader {
    private final String fromFilePath;

    public FromFileReader(String fromFilePath) {
        this.fromFilePath = fromFilePath;
    }

    @Override
    public Constraint apply(ConstraintDTO dto, ProfileFields fields) {
        String value = getValidatedValue(dto, String.class);

        InputStream streamFromPath = createStreamFromPath(appendPath(value));
        DistributedSet<String> names = CsvInputStreamReader.retrieveLines(streamFromPath);
        closeStream(streamFromPath);

        DistributedSet<Object> downcastedNames = new FrequencyDistributedSet<>(
            names.distributedSet().stream()
                .map(holder -> new WeightedElement<>((Object) holder.element(), holder.weight()))
                .collect(Collectors.toSet()));
        Field field = fields.getByName(dto.field);

        return new IsInSetConstraint(field, downcastedNames);
    }

    private String appendPath(String path) {
        return fromFilePath == null ? path : fromFilePath + path;
    }

    private static InputStream createStreamFromPath(String path) {
        try {
            return new FileInputStream(path);
        } catch (FileNotFoundException e) {
            throw new ValidationException(e.getMessage());
        }
    }

    private static void closeStream(InputStream stream) {
        try {
            stream.close();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
