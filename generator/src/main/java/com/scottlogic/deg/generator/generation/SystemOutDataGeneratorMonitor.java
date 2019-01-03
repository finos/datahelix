package com.scottlogic.deg.generator.generation;

import com.scottlogic.deg.generator.outputs.GeneratedObject;
import com.scottlogic.deg.generator.walker.reductive.FixedField;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigInteger;
import java.time.Duration;
import java.time.Instant;

public class SystemOutDataGeneratorMonitor implements DataGeneratorMonitor {
    Logger logger = LogManager.getLogger(FixedField.class);
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

        logger.debug("\n\n\n{} rows emitted since {}: {} rows/sec\n\n\n",
                this.rowsEmitted.toString(),
                this.startedGenerating.toString(),
                rowsPerSecond);
    }
}

