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

package com.scottlogic.deg.orchestrator.endtoend;

import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JarExecuteTests {

    @Test
    void generateSuccessfullyFromJar() throws Exception {
        Process p = setupProcess("-p=src/test/java/com/scottlogic/deg/orchestrator/endtoend/testprofile.profile.json");

        List<String> collectedOutput = collectOutputAndCloseProcess(p);

        assertEquals(Arrays.asList("foo", "\"Generation successful\""), collectedOutput,
            "Jar test failed. This might be because you have not built the jar. \n Try running Gradle Build. \n" +
            "Alternatively, it may be because System.out is being printed to (which interferes with streaming output) e.g. using 'printStackTrace'. ");
    }

    @Test
    void generateSuccessfullyFromJarAndLoadFile() throws Exception {
        Process p = setupProcess("-p=src/test/java/com/scottlogic/deg/orchestrator/endtoend/loadfiletest.profile.json");

        List<String> collectedOutput = collectOutputAndCloseProcess(p);

        assertEquals(Arrays.asList("foo", "\"Generated successfully from file\""), collectedOutput,
            "Jar test failed. This might be because you have not built the jar. \n Try running Gradle Build. \n" +
                "Alternatively, it may be because System.out is being printed to (which interferes with streaming output) e.g. using 'printStackTrace'. ");
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

