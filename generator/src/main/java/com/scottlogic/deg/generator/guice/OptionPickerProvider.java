package com.scottlogic.deg.generator.guice;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.scottlogic.deg.generator.config.detail.DataGenerationType;
import com.scottlogic.deg.generator.generation.GenerationConfigSource;
import com.scottlogic.deg.generator.walker.decisionbased.OptionPicker;
import com.scottlogic.deg.generator.walker.decisionbased.RandomOptionPicker;
import com.scottlogic.deg.generator.walker.decisionbased.SequentialOptionPicker;

public class OptionPickerProvider implements Provider<OptionPicker> {

    private final GenerationConfigSource config;
    private final RandomOptionPicker randomOptionPicker;
    private final SequentialOptionPicker sequentialOptionPicker;

    @Inject
    public OptionPickerProvider(GenerationConfigSource config, RandomOptionPicker randomOptionPicker, SequentialOptionPicker sequentialOptionPicker){
        this.config = config;
        this.randomOptionPicker = randomOptionPicker;
        this.sequentialOptionPicker = sequentialOptionPicker;
    }

    @Override
    public OptionPicker get() {
        if (config.getGenerationType() == DataGenerationType.RANDOM){
            return randomOptionPicker;
        }

        return sequentialOptionPicker;
    }
}
