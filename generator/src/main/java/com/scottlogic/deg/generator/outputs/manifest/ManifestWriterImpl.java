package com.scottlogic.deg.generator.outputs.manifest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.scottlogic.deg.generator.utils.FileUtils;
import com.scottlogic.deg.generator.violations.ViolatedProfile;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DecimalFormat;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class ManifestWriterImpl implements ManifestWriter{

    public void writeManifest(
        List<ViolatedProfile> result,
        Path directoryPath) throws IOException {

        AtomicInteger dataSetIndex = new AtomicInteger(1);
        DecimalFormat intFormatter = FileUtils.getDecimalFormat(result.size());

        List<ManifestDTO.TestCaseDTO> testCaseDtos = result
            .stream()
            .map(profile -> new ManifestDTO.TestCaseDTO(
                intFormatter.format(dataSetIndex.getAndIncrement()),
                Collections.singleton(profile.violatedRule.ruleInformation.getDescription())))
            .collect(Collectors.toList());

        System.out.println("Writing manifest.json file");
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
