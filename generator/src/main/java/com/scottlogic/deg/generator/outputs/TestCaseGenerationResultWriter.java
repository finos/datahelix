package com.scottlogic.deg.generator.outputs;

import com.scottlogic.deg.generator.ProfileFields;
import com.scottlogic.deg.generator.outputs.dataset_writers.DataSetWriter;
import com.scottlogic.deg.generator.outputs.manifest.ManifestDTO;
import com.scottlogic.deg.generator.outputs.manifest.ManifestWriter;
import com.scottlogic.deg.generator.outputs.manifest.TestCaseDTO;

import java.io.Closeable;
import java.io.IOException;
import java.nio.file.Path;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

public class TestCaseGenerationResultWriter {
    private final ManifestWriter manifestWriter;
    private final DataSetWriter datasetWriter;

    public TestCaseGenerationResultWriter(DataSetWriter datasetWriter) {
        this.manifestWriter = new ManifestWriter();
        this.datasetWriter = datasetWriter;
    }

    public void writeToDirectory(TestCaseGenerationResult result, Path directoryPath) throws IOException {
        DecimalFormat intFormatter = getDecimalFormat(result.datasets.size());

        List<TestCaseDTO> testCaseDtos = new ArrayList<>();

        int index = 1;
        for (TestCaseDataSet dataset : result.datasets) {
            String filenameWithoutExtension = intFormatter.format(index);

            write(result.profile.fields,
                dataset.stream(),
                directoryPath,
                filenameWithoutExtension);

            testCaseDtos.add(
                new TestCaseDTO(
                    filenameWithoutExtension,
                    Collections.singleton(dataset.violation.getDescription())));

            index++;
        }

        ManifestDTO manifestDto = new ManifestDTO(testCaseDtos);

        System.out.println("Writing manifest");
        this.manifestWriter.write(
            manifestDto,
            directoryPath.resolve(
                "manifest.json"));

        System.out.println("Complete");
    }

    private void write(ProfileFields fields, Stream<GeneratedObject> dataSet, Path directory, String filenameWithoutExtension) throws IOException {
        try (Closeable writer = this.datasetWriter.openWriter(directory, filenameWithoutExtension, fields)) {
            dataSet.forEach(row -> {
                try {
                    this.datasetWriter.writeRow(writer, row);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }

    private static DecimalFormat getDecimalFormat(int numberOfDatasets)
    {
        int maxNumberOfDigits = (int)Math.ceil(Math.log10(numberOfDatasets));

        char[] zeroes = new char[maxNumberOfDigits];
        Arrays.fill(zeroes, '0');

        return new DecimalFormat(new String(zeroes));
    }
}
