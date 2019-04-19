package com.scottlogic.deg.generator.guice;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.scottlogic.deg.generator.generation.*;
import com.scottlogic.deg.generator.walker.ReductiveDataGenerator;

public class DataGeneratorProvider implements Provider<DataGenerator> {

    private final WalkingDataGenerator walkingDataGenerator;
    private final ReductiveDataGenerator reductiveDataGenerator;

    private final GenerationConfigSource configSource;

    @Inject
    public DataGeneratorProvider(WalkingDataGenerator walkingDataGenerator,
                                 ReductiveDataGenerator reductiveDataGenerator,
                                 GenerationConfigSource configSource){
        this.walkingDataGenerator = walkingDataGenerator;
        this.reductiveDataGenerator = reductiveDataGenerator;
        this.configSource = configSource;
    }

    @Override
    public DataGenerator get() {
        boolean isReductive = configSource.getWalkerType() == GenerationConfig.TreeWalkerType.REDUCTIVE;

        return isReductive
            ? reductiveDataGenerator
            : walkingDataGenerator;
    }
}
