package com.scottlogic.deg.generator.guice;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.scottlogic.deg.generator.generation.GenerationConfigSource;
import com.scottlogic.deg.generator.walker.reductive.IterationVisualiser;
import com.scottlogic.deg.generator.walker.reductive.NoOpIterationVisualiser;
import com.scottlogic.deg.generator.walker.reductive.ReductiveIterationVisualiser;


public class IterationVisualiserProvider implements Provider<IterationVisualiser> {
    private final GenerationConfigSource source;
    private final NoOpIterationVisualiser noOpIterationVisualiser;
    private final ReductiveIterationVisualiser reductiveIterationVisualiser;

    @Inject
    public IterationVisualiserProvider(
        GenerationConfigSource source,
        NoOpIterationVisualiser noOpIterationVisualiser,
        ReductiveIterationVisualiser reductiveIterationVisualiser) {
        this.source = source;
        this.noOpIterationVisualiser = noOpIterationVisualiser;
        this.reductiveIterationVisualiser = reductiveIterationVisualiser;
    }

    @Override
    public IterationVisualiser get() {
        //if (source.visualiseReductions()){
            return reductiveIterationVisualiser;
        //}

        //return noOpIterationVisualiser;
    }
}
