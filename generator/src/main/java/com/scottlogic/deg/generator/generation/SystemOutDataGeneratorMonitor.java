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

        if (rowsSinceLastSample > 10){
            Instant newSampleTime = Instant.now();
            reportVelocity(rowsSinceLastSample, lastSampleTime, newSampleTime);
            lastSampleTime = newSampleTime;
            rowsEmitted = rowsEmitted.add(BigInteger.valueOf(rowsSinceLastSample));
            rowsSinceLastSample = 0;
        }
    }

    private void reportVelocity(float rowsEmittedInDuration, Instant lastSampleTime, Instant newSampleTime) {
        Duration duration = Duration.between(lastSampleTime, newSampleTime);
        long secondsToProduceRows = duration.getSeconds();
        float rowsPerSecond = rowsEmittedInDuration / secondsToProduceRows;

        System.out.println(
            String.format(
                "\n\n\n%s rows emitted since %s: %f rows/sec\n\n\n",
                this.rowsEmitted.toString(),
                this.startedGenerating.toString(),
                rowsPerSecond));
    }
}

