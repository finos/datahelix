package com.scottlogic.deg.generator.generation;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.fieldspecs.FieldSpec;
import com.scottlogic.deg.generator.fieldspecs.RowSpec;
import com.scottlogic.deg.generator.outputs.GeneratedObject;
import com.scottlogic.deg.generator.walker.reductive.FixedField;
import com.scottlogic.deg.generator.walker.reductive.ReductiveState;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class VelocityMonitor implements ReductiveDataGeneratorMonitor {
    private String startedGenerating;
    private long rowsSinceLastSample;
    private BigInteger rowsEmitted;
    private Timer timer = new Timer(true);
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm:ss");
    private long previousVelocity = 0;

    @Override
    public void generationStarting(GenerationConfig generationConfig) {
        startedGenerating = simpleDateFormat.format( new Date());
        rowsSinceLastSample = 0;
        rowsEmitted = BigInteger.ZERO;

        System.out.println("Generation started at: " + startedGenerating + "\n");
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
        reportVelocity(0);
        System.out.println("\n\nGeneration finished at: " + simpleDateFormat.format(new Date()));
    }

    public void reportVelocity(long rowsSinceLastSample) {
        String trend = rowsSinceLastSample > previousVelocity ? "+" : "-";
        System.out.print(
        String.format(
            "%-14s | %-19d | %s \n",
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
