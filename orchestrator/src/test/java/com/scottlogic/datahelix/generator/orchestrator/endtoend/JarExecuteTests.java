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

package com.scottlogic.datahelix.generator.orchestrator.endtoend;

import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class JarExecuteTests {
    private static final Map<Process, String> commandLineMap = new HashMap<>();

    @Test
    void generateSuccessfullyFromJar() throws Exception {
        Process p = setupProcess("-p=src/test/java/com/scottlogic/datahelix/generator/orchestrator/endtoend/testprofile.profile.json");

        List<String> collectedOutput = collectOutputAndCloseProcess(p);

        assertCsvOutputs(
            collectedOutput,
            "Generation successful",
            "",
            p);
    }

    @Test
    void generateSuccessfullyFromJarAndLoadFile() throws Exception {
        Process p = setupProcess("-p=src/test/java/com/scottlogic/datahelix/generator/orchestrator/endtoend/loadfiletest.profile.json");

        List<String> collectedOutput = collectOutputAndCloseProcess(p);

        assertCsvOutputs(collectedOutput,
            "Generated successfully from file",
            "Either load from file no longer works, or ",
            p);
    }

    @Test
    void generateSuccessfullyFromJarAndLoadFileWithinSubDirectory() throws Exception {
        Process p = setupProcess("-p=src/test/java/com/scottlogic/datahelix/generator/orchestrator/endtoend/loadfiletest-subfolder.profile.json");

        List<String> collectedOutput = collectOutputAndCloseProcess(p);

        assertCsvOutputs(collectedOutput,
            "Generated successfully from file",
            "Either load from file no longer works, or ",
            p);
    }

    @Test
    void generateRelationalDataSuccessfullyFromJar() throws Exception {
        Process p = setupProcess("-p=src/test/java/com/scottlogic/datahelix/generator/orchestrator/relational/profile.json", "JSON");

        List<String> collectedOutput = collectOutputAndCloseProcess(p);

        assertJsonOutputs(collectedOutput,
            "shortName",
            null,
            p);
    }

    @Test
    void generateRelationalDataSuccessfullyFromJarAndLoadFileWithinSubDirectory() throws Exception {
        Process p = setupProcess("-p=src/test/java/com/scottlogic/datahelix/generator/orchestrator/relational/profile-referenced-relationship.json", "JSON");

        List<String> collectedOutput = collectOutputAndCloseProcess(p);

        assertJsonOutputs(collectedOutput,
            "age",
            "\"Generated successfully from file\"",
            p);
    }

    private void assertCsvOutputs(List<String> outputs, String expectedFinalMessage, String extraErrorMessage, Process process) {
        String commandLine = commandLineMap.get(process);

        String errorMessageOnFailure = "Jar test failed. This may have been caused by one of the following:" +
            "1) You have not built the jar. \n Try running Gradle Build. \n" +
            "2) System.out is being printed to (which interferes with streaming output) e.g. using 'printStackTrace'.\n" +
            "3) There is a bug in code conditional on whether it is running inside the JAR, e.g. in SupportedVersionsGetter. \n" +
            "Command line used: '" + commandLine + "'\n" +
            outputs.stream().limit(5).collect(Collectors.joining("\n"));
        String fullErrorMessage = extraErrorMessage + errorMessageOnFailure;
        assertThat(fullErrorMessage, outputs.size(), is(greaterThanOrEqualTo(2)));
        assertEquals("foo", outputs.get(outputs.size() - 2), fullErrorMessage);
        assertEquals(expectedFinalMessage, outputs.get(outputs.size() - 1), fullErrorMessage);
    }

    private void assertJsonOutputs(List<String> outputs, String expectedJsonPropertyName, String expectedData, Process process) {
        String commandLine = commandLineMap.get(process);

        String errorMessageOnFailure = "Jar test failed. This may have been caused by one of the following:" +
            "1) You have not built the jar. \n Try running Gradle Build. \n" +
            "2) System.out is being printed to (which interferes with streaming output) e.g. using 'printStackTrace'.\n" +
            "3) There is a bug in code conditional on whether it is running inside the JAR, e.g. in SupportedVersionsGetter. \n" +
            "Command line used: '" + commandLine + "'\n" +
            outputs.stream().limit(5).collect(Collectors.joining("\n"));
        assertThat(errorMessageOnFailure, outputs.size(), is(greaterThanOrEqualTo(1)));
        assertThat(errorMessageOnFailure, outputs.get(outputs.size() - 1), containsString("\"" + expectedJsonPropertyName + "\":"));
        if (expectedData != null){
            assertThat(errorMessageOnFailure, outputs.get(outputs.size() - 1), containsString(":" + expectedData));
        }
    }

    private Process setupProcess(final String profile) throws IOException {
        return setupProcess(profile, null);
    }

    private Process setupProcess(final String profile, final String outputFormat) throws IOException {
        ProcessBuilder pb = new ProcessBuilder(
            "java",
            "-jar",
            "build/libs/datahelix.jar",
            profile,
            "--max-rows=1",
            "--quiet",
            outputFormat == null
                ? ""
                : "--output-format=" + outputFormat);

        pb.redirectErrorStream(true);
        Process process = pb.start();

        String commandLine = String.join(" ", pb.command());
        commandLineMap.put(process, commandLine);

        return process;
    }

    private List<String> collectOutputAndCloseProcess(Process process) throws IOException, InterruptedException {
        BufferedReader BufferedStdOutReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        List<String> collectedOutput = new ArrayList<>();
        String line;
        while ((line = BufferedStdOutReader.readLine()) != null) {
            collectedOutput.add(line);
        }
        process.waitFor();
        process.destroy();

        return collectedOutput;
    }
}

