package com.scottlogic.deg.generator.outputs.manifest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.scottlogic.deg.generator.ViolatedProfile;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DecimalFormat;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class ManifestWriter {

    public void writeManifest(
        List<ViolatedProfile> result,
        Path directoryPath,
        DecimalFormat intFormatter,
        int initialFileNumber) throws IOException {

        AtomicInteger dataSetIndex = new AtomicInteger(initialFileNumber);

        List<ManifestDTO.TestCaseDTO> testCaseDtos = result
            .stream()
            .map(profile -> new ManifestDTO.TestCaseDTO(
                intFormatter.format(dataSetIndex.getAndIncrement()),
                Collections.singleton(profile.violatedRule.rule.getDescription())))
            .collect(Collectors.toList());

        System.out.println("Writing manifest");
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
                Charset.forName("UTF-8")));
    }
}
