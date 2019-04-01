package com.scottlogic.deg.generator.guice;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.scottlogic.deg.generator.generation.GenerationConfig;
import com.scottlogic.deg.generator.generation.GenerationConfigSource;
import com.scottlogic.deg.generator.generation.databags.ReductiveRandomRowSpecDataBagSourceFactory;
import com.scottlogic.deg.generator.generation.databags.RowSpecDataBagSourceFactory;
import com.scottlogic.deg.generator.generation.databags.StandardRowSpecDataBagSourceFactory;

public class RowSpecDataBagSourceFactoryProvider implements Provider<RowSpecDataBagSourceFactory> {
    private final GenerationConfigSource configSource;
    private final StandardRowSpecDataBagSourceFactory normalFactory;
    private final ReductiveRandomRowSpecDataBagSourceFactory reductiveRandomFactory;

    @Inject
    public RowSpecDataBagSourceFactoryProvider(
        GenerationConfigSource configSource,
        StandardRowSpecDataBagSourceFactory normalFactory,
        ReductiveRandomRowSpecDataBagSourceFactory reductiveRandomFactory) {
        this.configSource = configSource;
        this.normalFactory = normalFactory;
        this.reductiveRandomFactory = reductiveRandomFactory;
    }

    @Override
    public RowSpecDataBagSourceFactory get() {
        boolean isReductive = configSource.getWalkerType() == GenerationConfig.TreeWalkerType.REDUCTIVE;
        boolean isRandom = configSource.getGenerationType() == GenerationConfig.DataGenerationType.RANDOM;

        if (isRandom && isReductive){
        //    return reductiveRandomFactory;
        }

        return normalFactory;
    }
}
