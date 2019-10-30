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

package com.scottlogic.deg.profile.dtos;

import com.scottlogic.deg.common.ValidationException;
import com.scottlogic.deg.profile.SupportedVersionChecker;
import com.scottlogic.deg.profile.SupportedVersionsGetter;
import com.scottlogic.deg.profile.guice.ProfileConfigSource;
import com.scottlogic.deg.profile.SchemaVersionGetter;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SupportedVersionCheckerTests {
    @Test
    void getSchemaFile_withSupportedVersion_returnsNonNullURL() throws IOException {
        //Arrange
        SchemaVersionGetter retriever = mock(SchemaVersionGetter.class);
        when(retriever.getSchemaVersionOfJson(any())).thenReturn("0.2");
        ProfileConfigSource configSource = mock(ProfileConfigSource.class);
        File mockFile = mock(File.class);
        when(mockFile.toPath()).thenReturn(mock(Path.class));
        when(configSource.getProfileFile()).thenReturn(mockFile);
        SupportedVersionsGetter supportedVersionsGetter = mock(SupportedVersionsGetter.class);
        when(supportedVersionsGetter.getSupportedSchemaVersions()).thenReturn(Arrays.asList("0.2"));
        SupportedVersionChecker validator = new SupportedVersionChecker(retriever, configSource, supportedVersionsGetter);

        //Act
        URL schema = null;
        try {
            schema = validator.getSchemaFile();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        //Assert
        assertNotNull(schema);
    }

    @Test
    void getSchemaFile_withUnsupportedVersion_throwsValidationException() throws IOException {
        //Arrange
        SchemaVersionGetter retriever = mock(SchemaVersionGetter.class);
        when(retriever.getSchemaVersionOfJson(any())).thenReturn("101.53");
        ProfileConfigSource configSource = mock(ProfileConfigSource.class);
        File mockFile = mock(File.class);
        when(mockFile.toPath()).thenReturn(mock(Path.class));
        when(configSource.getProfileFile()).thenReturn(mockFile);
        SupportedVersionsGetter supportedVersionsGetter = mock(SupportedVersionsGetter.class);
        when(supportedVersionsGetter.getSupportedSchemaVersions()).thenReturn(Arrays.asList("0.2"));
        SupportedVersionChecker validator = new SupportedVersionChecker(retriever, configSource, supportedVersionsGetter);

        //Act & Assert
        assertThrows(ValidationException.class, validator::getSchemaFile);
    }
}
