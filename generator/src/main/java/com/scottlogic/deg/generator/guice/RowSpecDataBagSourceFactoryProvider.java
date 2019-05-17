package com.scottlogic.deg.generator.guice;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.scottlogic.deg.generator.config.detail.DataGenerationType;
import com.scottlogic.deg.generator.config.detail.TreeWalkerType;
import com.scottlogic.deg.generator.generation.GenerationConfigSource;
import com.scottlogic.deg.generator.generation.databags.ReductiveRandomRowSpecDataBagGenerator;
import com.scottlogic.deg.generator.generation.databags.RowSpecDataBagGenerator;
import com.scottlogic.deg.generator.generation.databags.StandardRowSpecDataBagGenerator;

public class RowSpecDataBagSourceFactoryProvider implements Provider<RowSpecDataBagGenerator> {
    private final GenerationConfigSource configSource;
    private final StandardRowSpecDataBagGenerator normalFactory;
    private final ReductiveRandomRowSpecDataBagGenerator reductiveRandomFactory;

    @Inject
    public RowSpecDataBagSourceFactoryProvider(
        GenerationConfigSource configSource,
        StandardRowSpecDataBagGenerator normalFactory,
        ReductiveRandomRowSpecDataBagGenerator reductiveRandomFactory) {
        this.configSource = configSource;
        this.normalFactory = normalFactory;
        this.reductiveRandomFactory = reductiveRandomFactory;
    }

    @Override
    public RowSpecDataBagGenerator get() {
        boolean isReductive = configSource.getWalkerType() == TreeWalkerType.REDUCTIVE;
        boolean isRandom = configSource.getGenerationType() == DataGenerationType.RANDOM;

        if (isRandom && isReductive){
            return reductiveRandomFactory;
        }

        return normalFactory;
    }
}
