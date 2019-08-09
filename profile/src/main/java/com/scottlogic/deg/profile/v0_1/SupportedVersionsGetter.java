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
package com.scottlogic.deg.profile.v0_1;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class SupportedVersionsGetter {
    private final String resourcesPath = "profileschema/";
    /**
     *  Searches profile/src/main/resources/profileschema/ for sub-directories. The names of these directories
     *  are counted as valid schema versions.
     *
     *  The alternative is to hard-code the supported schema versions.
     *
     *  This method picks up any new schema versions added when following the instructions at
     *  docs/developer/HowToAddSupportForNewSchemaVersion.md
     *
     *  An error in this method could cause the JarExecuteTests to fail because it uses different logic depending
     *  on whether the JAR has been built.
     *
     * @return all valid schema versions
     **/
    List<String> getSupportedSchemaVersions() {
        // Taken from https://stackoverflow.com/a/20073154/
        List<String> supportedSchemaVersions = new ArrayList<>();
        File jarFile = new File(getClass().getProtectionDomain().getCodeSource().getLocation().getPath());
        try {
            if (jarFile.isFile()) {  // Run with JAR file
                final JarFile jar = new JarFile(jarFile);
                final Enumeration<JarEntry> entries = jar.entries(); //gives ALL entries in jar
                while (entries.hasMoreElements()) {
                    final String name = entries.nextElement().getName();
                    if (name.startsWith(resourcesPath)) { //filter according to the path
                        if (name.split("/").length == 2) {
                            supportedSchemaVersions.add(name.split("/")[1]);
                        }
                    }
                }
                jar.close();
            } else {
                supportedSchemaVersions = getSupportedSchemaVersionsFromResources();
            }
        } catch (IOException ignored) {

        }
        return supportedSchemaVersions;
    }

    private List<String> getSupportedSchemaVersionsFromResources() {
        String directoryContainingSchemas = this.getClass().getResource("/" + resourcesPath).getPath();
        File file = new File(directoryContainingSchemas);
        String[] directoriesArray = file.list((current, name) -> new File(current, name).isDirectory());
        List<String> directories = new ArrayList<>();
        if (directoriesArray != null) {
            directories = Arrays.asList(directoriesArray);
        }
        return directories;
    }
}
