package com.scottlogic.deg.generator.generation.object_generation;

import com.google.inject.Inject;
import com.scottlogic.deg.generator.Profile;
import com.scottlogic.deg.generator.fieldspecs.RowSpec;
import com.scottlogic.deg.generator.generation.GenerationConfig;
import com.scottlogic.deg.generator.generation.databags.ConcatenatingDataBagSource;
import com.scottlogic.deg.generator.generation.databags.DataBag;
import com.scottlogic.deg.generator.generation.databags.DataBagSource;
import com.scottlogic.deg.generator.generation.databags.RowSpecDataBagSourceFactory;
import com.scottlogic.deg.generator.outputs.GeneratedObject;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DataBagObjectGenerator implements ObjectGenerator {
    private final GenerationConfig generationConfig;
    private final RowSpecDataBagSourceFactory dataBagSourceFactory;

    @Inject
    public DataBagObjectGenerator(GenerationConfig generationConfig, RowSpecDataBagSourceFactory dataBagSourceFactory){
        this.generationConfig = generationConfig;
        this.dataBagSourceFactory = dataBagSourceFactory;
    }

    @Override
    public Stream<GeneratedObject> generateObjectsFromRowSpecs(Profile profile, Stream<RowSpec> rowSpecs) {

        DataBagSource allDataBagSources = new ConcatenatingDataBagSource(
            rowSpecs.map(dataBagSourceFactory::createDataBagSource));

        Stream<DataBag> dataBagStream = allDataBagSources
            .generate(generationConfig);

        return dataBagStream
            .map(dataBag -> new GeneratedObject(
                profile.fields.stream()
                    .map(dataBag::getValueAndFormat)
                    .collect(Collectors.toList()),
                dataBag.getRowSource(profile.fields)));
    }
}
