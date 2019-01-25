package com.scottlogic.deg.generator.generation;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.fieldspecs.FieldSpec;
import com.scottlogic.deg.generator.fieldspecs.RowSpec;
import com.scottlogic.deg.generator.outputs.GeneratedObject;
import com.scottlogic.deg.generator.walker.reductive.FixedField;
import com.scottlogic.deg.generator.walker.reductive.ReductiveState;

import java.math.BigInteger;
import java.time.Duration;
import java.time.Instant;
import java.util.Map;

public class VelocityMonitor implements ReductiveDataGeneratorMonitor {
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
        double fractionOfSecondToProduceRows = duration.getSeconds() + (duration.getNano() / 1_000_000_000.0);
        double rowsPerSecond = rowsEmittedInDuration / fractionOfSecondToProduceRows;

        System.out.print(
            String.format(
                "%s rows emitted since %s: %.0f rows/sec\r",
                this.rowsEmitted.toString(),
                this.startedGenerating.toString(),
                rowsPerSecond));

    }

    @Override
    public void rowSpecEmitted(FixedField lastFixedField, FieldSpec fieldSpecForValuesInLastFixedField, RowSpec rowSpecWithAllValuesForLastFixedField) {

    }

    @Override
    public void fieldFixedToValue(Field field, Object current) {

    }

    @Override
    public void unableToStepFurther(ReductiveState reductiveState) {

    }

    @Override
    public void noValuesForField(ReductiveState reductiveState) {

    }

    @Override
    public void unableToEmitRowAsSomeFieldSpecsAreEmpty(ReductiveState reductiveState, Map<Field, FieldSpec> fieldSpecsPerField) {

    }
}
