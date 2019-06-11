package com.scottlogic.deg.output.manifest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Charsets;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.scottlogic.deg.common.profile.ViolatedProfile;
import com.scottlogic.deg.output.FileUtils;
import com.scottlogic.deg.output.FileUtilsImpl;
import com.scottlogic.deg.output.OutputPath;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DecimalFormat;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * write out a JSON manifest file during violation.
 * This file shows which rule has been violated in which output file.
 */
public class JsonManifestWriter implements ManifestWriter {
    private final Path outputPath;

    @Inject
    public JsonManifestWriter(OutputPath outputPath){
        this.outputPath = outputPath.getPath();
    }

    public void writeManifest(List<ViolatedProfile> result) throws IOException {
        AtomicInteger dataSetIndex = new AtomicInteger(1);
        DecimalFormat intFormatter = FileUtilsImpl.getDecimalFormat(result.size());

        List<ManifestDTO.TestCaseDTO> testCaseDtos = result
            .stream()
            .map(profile -> new ManifestDTO.TestCaseDTO(
                intFormatter.format(dataSetIndex.getAndIncrement()),
                Collections.singleton(profile.violatedRule.ruleInformation.getDescription())))
            .collect(Collectors.toList());

        write(new ManifestDTO(testCaseDtos), outputPath.resolve("manifest.json"));
    }

    private void write(ManifestDTO manifest, Path filepath) throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        String manifestAsJson = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(manifest);

        Files.write(filepath, manifestAsJson.getBytes(Charsets.UTF_8));
    }
}
