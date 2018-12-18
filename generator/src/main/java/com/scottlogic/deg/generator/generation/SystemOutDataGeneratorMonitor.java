package com.scottlogic.deg.generator.generation;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.outputs.GeneratedObject;
import com.scottlogic.deg.generator.restrictions.FieldSpec;
import com.scottlogic.deg.generator.restrictions.RowSpec;
import com.scottlogic.deg.generator.walker.reductive.FieldCollection;
import com.scottlogic.deg.generator.walker.reductive.FixedField;

import java.math.BigInteger;
import java.time.Duration;
import java.time.Instant;

public class SystemOutDataGeneratorMonitor implements ReductiveDataGeneratorMonitor {
    private Instant startedGenerating;
    private long rowsSinceLastSample;
    private Instant lastSampleTime;
    private BigInteger rowsEmitted;
    private BigInteger maxRows;

    @Override
    public void generationStarting(GenerationConfig generationConfig) {
        this.startedGenerating = Instant.now();
        this.lastSampleTime = this.startedGenerating;
        this.rowsSinceLastSample = 0;
        this.rowsEmitted = BigInteger.ZERO;
        this.maxRows = BigInteger.valueOf(generationConfig.getMaxRows());
    }

    @Override
    public void rowEmitted(GeneratedObject row) {
        this.rowsSinceLastSample++;
        this.rowsEmitted = rowsEmitted.add(BigInteger.ONE);

        if (this.rowsSinceLastSample >= 1000){
            Instant newSampleTime = Instant.now();
            reportVelocity(this.rowsSinceLastSample, this.lastSampleTime, newSampleTime);
            this.lastSampleTime = newSampleTime;
            this.rowsSinceLastSample = 0;
        }
    }

    private void reportVelocity(float rowsEmittedInDuration, Instant lastSampleTime, Instant newSampleTime) {
        Duration duration = Duration.between(lastSampleTime, newSampleTime);
        double fractionOfSecondToProduceRows = duration.getNano() / 1_000_000_000.0;
        double rowsPerSecond = rowsEmittedInDuration / fractionOfSecondToProduceRows;

        System.out.println(
            String.format(
                "\n\n\n%s rows emitted since %s: %f rows/sec\n\n\n",
                this.rowsEmitted.toString(),
                this.startedGenerating.toString(),
                rowsPerSecond));
    }

    @Override
    public void rowSpecEmitted(FixedField lastFixedField, FieldSpec fieldSpecForValuesInLastFixedField, RowSpec rowSpecWithAllValuesForLastFixedField) {
        System.out.println(
            String.format(
                "%s %s",
                lastFixedField.field.name,
                fieldSpecForValuesInLastFixedField.toString()));
    }

    @Override
    public void fieldFixedToValue(Field field, Object current) {
        System.out.println(String.format("Field [%s] = %s", field.name, current));
    }

    @Override
    public void unableToStepFurther(FieldCollection fieldCollection) {
        System.out.println(
            String.format(
                "%d: Unable to step further %s ",
                fieldCollection.getFixedFieldsExceptLast().size(),
                fieldCollection.toString(true)));
    }
}

