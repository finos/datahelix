package com.scottlogic.deg.generator.outputs.manifest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Charsets;
import com.scottlogic.deg.generator.utils.FileUtilsImpl;
import com.scottlogic.deg.generator.violations.ViolatedProfile;

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

    public void writeManifest(
        List<ViolatedProfile> result,
        Path directoryPath) throws IOException {

        AtomicInteger dataSetIndex = new AtomicInteger(1);
        DecimalFormat intFormatter = FileUtilsImpl.getDecimalFormat(result.size());

        List<ManifestDTO.TestCaseDTO> testCaseDtos = result
            .stream()
            .map(profile -> new ManifestDTO.TestCaseDTO(
                intFormatter.format(dataSetIndex.getAndIncrement()),
                Collections.singleton(profile.violatedRule.ruleInformation.getDescription())))
            .collect(Collectors.toList());

        write(
            new ManifestDTO(testCaseDtos),
            directoryPath.resolve(
                "manifest.json"));
    }

    private void write(ManifestDTO manifest, Path filepath) throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        String manifestAsJson = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(manifest);

        Files.write(
            filepath,
            manifestAsJson.getBytes(
                Charsets.UTF_8));
    }
}
