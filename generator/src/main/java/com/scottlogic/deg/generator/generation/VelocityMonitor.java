package com.scottlogic.deg.generator.generation;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.fieldspecs.FieldSpec;
import com.scottlogic.deg.generator.fieldspecs.RowSpec;
import com.scottlogic.deg.generator.outputs.GeneratedObject;
import com.scottlogic.deg.generator.walker.reductive.FixedField;
import com.scottlogic.deg.generator.walker.reductive.ReductiveState;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class VelocityMonitor implements ReductiveDataGeneratorMonitor {
    private static final BigDecimal millisecondsInSecond = BigDecimal.valueOf(1_000);
    private static final BigDecimal nanoSecondsInMillisecond = BigDecimal.valueOf(1_000_000);

    private LocalDateTime startedGenerating;
    private long rowsSinceLastSample;
    private BigInteger rowsEmitted;
    private Timer timer = new Timer(true);
    private DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
    private long previousVelocity = 0;

    @Override
    public void generationStarting(GenerationConfig generationConfig) {
        startedGenerating = LocalDateTime.now();
        rowsSinceLastSample = 0;
        rowsEmitted = BigInteger.ZERO;

        System.out.println("Generation started at: " + timeFormatter.format(startedGenerating) + "\n");
        System.out.println("Number of rows | Velocity (rows/sec) | Velocity trend");
        System.out.println("---------------+---------------------+---------------");

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                reportVelocity(rowsSinceLastSample);
                rowsSinceLastSample = 0;
            }
        }, 1000L, 1000L);
    }
    
    @Override
    public void rowEmitted(GeneratedObject row) {
        rowsSinceLastSample++;
        rowsEmitted = rowsEmitted.add(BigInteger.ONE);
    }

    @Override
    public void endGeneration() {
        timer.cancel();

        LocalDateTime finished = LocalDateTime.now();
        Duration totalDuration = Duration.between(startedGenerating, finished);

        BigDecimal nanoSecondsAsMilliseconds = BigDecimal
            .valueOf(totalDuration.getNano())
            .divide(nanoSecondsInMillisecond, RoundingMode.DOWN);

        BigDecimal secondsAsMilliseconds = BigDecimal
            .valueOf(totalDuration.getSeconds())
            .multiply(millisecondsInSecond);

        BigDecimal totalMilliseconds = nanoSecondsAsMilliseconds.add(secondsAsMilliseconds);
        BigInteger averageRowsPerSecond = new BigDecimal(rowsEmitted)
            .setScale(2, RoundingMode.UNNECESSARY)
            .divide(totalMilliseconds, RoundingMode.HALF_UP)
            .multiply(millisecondsInSecond).toBigInteger();

        System.out.println(String.format(
            "%-14s | %-19d | Finished",
            rowsEmitted.toString(),
            averageRowsPerSecond
        ));

        System.out.println(
            String.format(
                "\nGeneration finished at: %s",
                timeFormatter.format(finished)));
    }

    private void reportVelocity(long rowsSinceLastSample) {
        String trend = rowsSinceLastSample > previousVelocity ? "+" : "-";
        System.out.println(
        String.format(
            "%-14s | %-19d | %s",
            rowsEmitted.toString(),
            rowsSinceLastSample,
            trend)
        );
        previousVelocity = rowsSinceLastSample;
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
