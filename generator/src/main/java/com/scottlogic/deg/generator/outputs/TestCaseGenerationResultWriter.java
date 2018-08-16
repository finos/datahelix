package com.scottlogic.deg.generator.outputs;

import com.scottlogic.deg.generator.outputs.manifest.ManifestDTO;
import com.scottlogic.deg.generator.outputs.manifest.ManifestWriter;
import com.scottlogic.deg.generator.outputs.manifest.TestCaseDTO;

import java.io.IOException;
import java.nio.file.Path;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TestCaseGenerationResultWriter {
    private final ManifestWriter manifestWriter;
    private final IDataSetWriter datasetWriter;

    public TestCaseGenerationResultWriter() {
        this.datasetWriter = new JsonTestCaseDataWriter();
        this.manifestWriter = new ManifestWriter();
    }

    public void writeToDirectory(TestCaseGenerationResult result, Path directoryPath) throws IOException {
        DecimalFormat intFormatter = getDecimalFormat(result.datasets.size());

        List<TestCaseDTO> testCaseDtos = new ArrayList<>();

        System.out.println("Writing test case files");
        int index = 1;
        for (TestCaseDataSet dataset : result.datasets)
        {
            String filenameWithoutExtension = intFormatter.format(index);

            String filename = this.datasetWriter.write(
                result.profile,
                dataset,
                directoryPath,
                filenameWithoutExtension);

            testCaseDtos.add(
                new TestCaseDTO(
                    filename,
                    dataset.violation == null
                    ? Collections.emptyList()
                    : Collections.singleton(dataset.violation)));

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

    private static DecimalFormat getDecimalFormat(int numberOfDatasets)
    {
        int maxNumberOfDigits = (int)Math.floor(Math.log10(numberOfDatasets));

        char[] zeroes = new char[maxNumberOfDigits];
        Arrays.fill(zeroes, '0');

        return new DecimalFormat(new String(zeroes));
    }
}
