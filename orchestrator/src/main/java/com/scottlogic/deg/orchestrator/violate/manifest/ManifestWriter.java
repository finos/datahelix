/*
 * Copyright 2019 Scott Logic Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.scottlogic.deg.orchestrator.violate.manifest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Charsets;
import com.google.inject.Inject;
import com.scottlogic.deg.orchestrator.violate.ViolatedProfile;
import com.scottlogic.deg.common.util.FileUtils;
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
public class ManifestWriter {
    private final Path outputPath;

    @Inject
    public ManifestWriter(OutputPath outputPath){
        this.outputPath = outputPath.getPath();
    }

    public void writeManifest(List<ViolatedProfile> result) throws IOException {
        AtomicInteger dataSetIndex = new AtomicInteger(1);
        DecimalFormat intFormatter = FileUtils.getDecimalFormat(result.size());

        List<ManifestDTO.TestCaseDTO> testCaseDtos = result
            .stream()
            .map(profile -> new ManifestDTO.TestCaseDTO(
                intFormatter.format(dataSetIndex.getAndIncrement()),
                Collections.singleton(profile.violatedRule.getDescription())))
            .collect(Collectors.toList());

        write(new ManifestDTO(testCaseDtos), outputPath.resolve("manifest.json"));
    }

    private void write(ManifestDTO manifest, Path filepath) throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        String manifestAsJson = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(manifest);

        Files.write(filepath, manifestAsJson.getBytes(Charsets.UTF_8));
    }
}
