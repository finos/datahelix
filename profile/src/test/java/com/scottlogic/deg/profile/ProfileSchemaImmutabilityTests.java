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

package com.scottlogic.deg.profile;

import com.scottlogic.deg.profile.dto.SupportedVersionsGetter;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ProfileSchemaImmutabilityTests {

    private static class VersionHash {
        private final String version;
        private final String hash;

        public VersionHash(String version, String hash) {
            this.version = version;
            this.hash = hash;
        }

        public String version() {
            return version;
        }
    }

    private static Set<VersionHash> versionToHash() {
        Set<VersionHash> versionToHash = new HashSet<>();
        // DO NOT MODIFY EXISTING HASHES! ONLY ADD!
        versionToHash.add(new VersionHash(
            "0.1",
            "6346faec92ba67686cc617d1573baed2230fff42acdbc961bf043c4b591bf246"));
        return versionToHash;
    }

    private static class VersionHashesProvider implements ArgumentsProvider {

        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext context) throws Exception {
            return versionToHash().stream().map(Arguments::of);
        }
    }

    // If this test fails, you have added a new version of the schema or modified an existing one.
    // You need to include the hash of the new schema in the map in this file.
    // Do not modify existing hashes.
    @Test
    public void verifyAllHashesArePresent() {
        Set<String> existingSchemas = new HashSet<>(new SupportedVersionsGetter().getSupportedSchemaVersions());
        assertEquals(existingSchemas, versionToHash().stream().map(VersionHash::version).collect(Collectors.toSet()));
    }

    @ParameterizedTest
    @ArgumentsSource(VersionHashesProvider.class)
    public void validateSchemasAreUnique(VersionHash wrapper) throws NoSuchAlgorithmException {
        byte[] bytes = readClassResourceAsBytes("profileschema/" + wrapper.version + "/datahelix.schema.json");
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] encoded = digest.digest(bytes);

        assertEquals(wrapper.hash, bytesToHex(encoded));
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        for (byte each : bytes) {
            result.append(byteToHex(each));
        }
        return result.toString();
    }

    private static String byteToHex(byte in) {
        final char[] chars = "0123456789abcdef".toCharArray();
        StringBuilder output = new StringBuilder();
        int quotient = in < 0 ? 256 + in : in; // Account for twos complement

        for (int i = 0; i < 2; i++) {
            int remainder = quotient % 16;
            output.append(chars[remainder]);
            quotient = quotient / 16;
        }
        return output.reverse().toString();
    }

    private byte[] readClassResourceAsBytes(String location) {
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream schema = classLoader.getResourceAsStream(location);

        try {
            return IOUtils.toByteArray(schema);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

}
