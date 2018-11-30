package com.scottlogic.deg.generator.generation;

import com.scottlogic.deg.generator.outputs.GeneratedObject;

import java.math.BigInteger;
import java.time.Duration;
import java.time.Instant;

public class SystemOutDataGeneratorMonitor implements DataGeneratorMonitor {
    private Instant startedGenerating;
    private long rowsSinceLastSample;
    private Instant lastSampleTime;
    private BigInteger rowsEmitted;

    @Override
    public void generationStarting() {
        startedGenerating = Instant.now();
        lastSampleTime = startedGenerating;
        rowsSinceLastSample = 0;
        rowsEmitted = BigInteger.ZERO;
    }

    @Override
    public void rowEmitted(GeneratedObject row) {
        rowsSinceLastSample++;

        if (rowsSinceLastSample >= 1000){
            Instant newSampleTime = Instant.now();
            reportVelocity(rowsSinceLastSample, lastSampleTime, newSampleTime);
            lastSampleTime = newSampleTime;
            rowsEmitted = rowsEmitted.add(BigInteger.valueOf(rowsSinceLastSample));
            rowsSinceLastSample = 0;
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
}

