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

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;
import org.leadpony.justify.api.JsonSchema;
import org.leadpony.justify.api.JsonValidationService;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
        // The new checksum hash can be found by running the shell command sha256sum on the respective schema file
        // example: sha256sum profile/src/main/resources/profileschema/0.1/datahelix.schema.json
        // Ensure you run this on a unix based machine and the profile has unix line endings.
        // Alternatively, add the new version to the map, and the test will give you the hash it should be.
        versionToHash.add(new VersionHash(
            "0.1",
            "575c572e9d00d69b5775cf50f01fc79d8cf7babcb6eb2ac51b1a9572d490487c"));
        versionToHash.add(new VersionHash(
            "0.2",
            "1208511af02fcf69285e6d16d1a0caaa3cae3aef95912d1d940d8dfe4eda6031"));
        versionToHash.add(new VersionHash(
            "0.3",
            "545db9f55228cda6eeff076c4cb1905f6c1a3b061a388dcdeadfbfb6cdd36676"));
        versionToHash.add(new VersionHash(
            "0.4",
            "9d4cdf397ae8d6c9e841a7577c859caca56fd906168eff77ba66d4ddf5e06ba7"));
        versionToHash.add(new VersionHash(
            "0.5",
            "3d114a0ee1e3d201faa7442a8e6da7b9e8d67d72e772ed9324ced7f4c8936adc"));
        versionToHash.add(new VersionHash(
            "0.6",
            "943ca15209ba515a9be9885c93fcc2f876e7e5ece0814a73c138ad965e29bbc6"));
        versionToHash.add(new VersionHash(
            "0.7",
            "902b527b950a91beb1c1897d61fd63da1d0101e1f619580653b442166a508099"));
        versionToHash.add(new VersionHash(
            "0.8",
            "a92498b428e5866eab85a011c5c071afd11ebd76520714e2a301968e06b1997e"));
        versionToHash.add(new VersionHash(
            "0.9",
            "3e84866fba173ce6528da8994acf7a60fd879ae472246482b0e29ec2b7906321"));
        versionToHash.add(new VersionHash(
            "0.10",
            "30c46ec7e1a17ba2dd7a7069394bc4b2d6b0661adac27f378801d7006edecaf8"));
        versionToHash.add(new VersionHash(
            "0.11",
            "eed1d1faaf4c8bb50144f3e72c2f14d56295c7d5da09a0dd6ed6266daee4a559"));
        versionToHash.add(new VersionHash(
            "0.12",
            "d876b4f874296404f5015f533baed6bb266442469886ea2060d721dc5d497d62"));
        versionToHash.add(new VersionHash(
            "0.13",
            "3b812a8dfe5b2a77e2b427f343ffc237e3e31c0364197e820f2b7702fa6ef260"));
        versionToHash.add(new VersionHash(
            "0.14",
            "307d231bede9bcc4e77cd6ac5e3d45ddf427719d688930a45af0c139a314cf92"));
        return versionToHash;
    }

    // If this test fails, you have added a new version of the schema or modified an existing one.
    // You need to include the hash of the new schema in the map in this file.
    // Do not modify existing hashes.
    @Test
    public void hashesMustBeAddedToThisFileForAnyNewSchemas() {
        Set<String> existingSchemas = new HashSet<>(new SupportedVersionsGetter().getSupportedSchemaVersions());
        assertTrue(
            versionToHash().stream().map(VersionHash::version).collect(Collectors.toSet()).containsAll(existingSchemas),
            "At least one version is either missing or erroneously present in this test's list of checksums. " +
            "The new checksum must be added to the map of checksum hashes in this test file.");
    }

    @Test
    public void schemasMustNotBeModifiedWithoutNewVersion() throws NoSuchAlgorithmException {
        String location = "profileschema/datahelix.schema.json";
        byte[] bytes = normaliseLineEndings(readClassResourceAsBytes(location));

        InputStream schemaPath = getClass().getClassLoader().getResourceAsStream(location);
        JsonValidationService service = JsonValidationService.newInstance();
        JsonSchema schema = service.readSchema(schemaPath);
        String version = schema.getSubschemaAt("/definitions/schemaVersion")
                .toJson().asJsonObject().get("const").toString().replace("\"", "");

        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] encoded = digest.digest(bytes);


        Optional<VersionHash> versionHash = versionToHash().stream()
            .filter(v -> v.version.equals(version))
            .findFirst();
        assertTrue(versionHash.isPresent());
        assertEquals(
            versionHash.get().hash,
            bytesToHex(encoded),
            "The expected hash for version %s does not match the hash supplied." +
            "If you weren't testing for a new hash, you probably modified an existing schema. Do not do this. " +
                "Create a new schema version instead.");
    }

    private static byte[] normaliseLineEndings(byte[] bytes) {
        final String encoding = "UTF-8";
        try {
            return new String(bytes, encoding).replaceAll("\\r\\n?", "\n").getBytes(encoding);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
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
