package com.scottlogic.deg.generator.generation.object_generation;

import com.google.inject.Inject;
import com.scottlogic.deg.generator.Profile;
import com.scottlogic.deg.generator.fieldspecs.RowSpec;
import com.scottlogic.deg.generator.generation.GenerationConfig;
import com.scottlogic.deg.generator.generation.databags.ConcatenatingDataBagSource;
import com.scottlogic.deg.generator.generation.databags.DataBag;
import com.scottlogic.deg.generator.generation.databags.DataBagSource;
import com.scottlogic.deg.generator.outputs.GeneratedObject;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DataBagObjectGenerator implements ObjectGenerator {
    private final GenerationConfig generationConfig;

    @Inject
    public DataBagObjectGenerator(GenerationConfig generationConfig){
        this.generationConfig = generationConfig;
    }

    @Override
    public Stream<GeneratedObject> generateObjectsFromRowSpecs(Profile profile, Stream<RowSpec> rowSpecs) {

        DataBagSource allDataBagSources = new ConcatenatingDataBagSource(
            rowSpecs.map(RowSpec::createDataBagSource));

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
