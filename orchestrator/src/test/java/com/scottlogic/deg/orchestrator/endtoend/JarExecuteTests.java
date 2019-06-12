package com.scottlogic.deg.orchestrator.endtoend;

import org.junit.jupiter.api.Test;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class JarExecuteTests {

    @Test
    void GenerateSuccessfullyFromJar() throws Exception {
            ProcessBuilder pb = new ProcessBuilder("java", "-jar", "\"build\\libs\\generator.jar\"", "generate","-p=\"src\\test\\java\\com\\scottlogic\\deg\\orchestrator\\endtoend\\testprofile.profile.json\"","--max-rows=1");
            pb.redirectErrorStream(true);
            Process p = pb.start();
            BufferedReader BufferedSTDOUTReader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            List<String> STDOUT = new ArrayList<String>();
            String line;
            while ((line = BufferedSTDOUTReader.readLine()) != null) {
                STDOUT.add(line);
            }
            p.waitFor();
            p.destroy();

            assertEquals("\"Generation successful\"",STDOUT.get(0));
    }
    }

