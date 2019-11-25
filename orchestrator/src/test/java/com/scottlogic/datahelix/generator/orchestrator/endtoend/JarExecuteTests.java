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

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class JarExecuteTests {
    @Test
    void generateSuccessfullyFromJar() throws Exception {
        Process p = setupProcess("-p=src/test/java/com/scottlogic/datahelix/generator/orchestrator/endtoend/testprofile.profile.json");

        List<String> collectedOutput = collectOutputAndCloseProcess(p);

        assertOnOutputs(collectedOutput, "Generation successful", "");
    }

    @Test
    void generateSuccessfullyFromJarAndLoadFile() throws Exception {
        Process p = setupProcess("-p=src/test/java/com/scottlogic/datahelix/generator/orchestrator/endtoend/loadfiletest.profile.json");

        List<String> collectedOutput = collectOutputAndCloseProcess(p);

        assertOnOutputs(collectedOutput,
            "Generated successfully from file",
            "Either load from file no longer works, or ");
    }

    private void assertOnOutputs(List<String> outputs, String expectedFinalMessage, String extraErrorMessage) {
        String errorMessageOnFailure = "Jar test failed. This may have been caused by one of the following:" +
            "1) You have not built the jar. \n Try running Gradle Build. \n" +
            "2) System.out is being printed to (which interferes with streaming output) e.g. using 'printStackTrace'.\n" +
            "3) There is a bug in code conditional on whether it is running inside the JAR, e.g. in SupportedVersionsGetter. \n";
        String fullErrorMessage = extraErrorMessage + errorMessageOnFailure;
        assertTrue(outputs.size() >= 2, fullErrorMessage);
        assertEquals(outputs.get(outputs.size() - 2), "foo", fullErrorMessage);
        assertEquals(outputs.get(outputs.size() - 1), expectedFinalMessage, fullErrorMessage);
    }

    private Process setupProcess(final String profile) throws IOException {
        ProcessBuilder pb = new ProcessBuilder(
            "java",
            "-jar",
            "build/libs/generator.jar",
            "generate",
            profile,
            "--max-rows=1",
            "--quiet");
        pb.redirectErrorStream(true);
        return pb.start();
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

