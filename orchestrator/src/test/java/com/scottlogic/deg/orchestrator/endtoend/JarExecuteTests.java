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

        assertEquals(Arrays.asList("foo", "\"Generation successful\""), collectedOutput);
    }

    @Test
    void generateSuccessfullyFromJarAndLoadFile() throws Exception {
        Process p = setupProcess("-p=src/test/java/com/scottlogic/deg/orchestrator/endtoend/loadfiletest.profile.json");

        List<String> collectedOutput = collectOutputAndCloseProcess(p);

        assertEquals(Arrays.asList("foo", "\"Generated successfully from file\""), collectedOutput);
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

