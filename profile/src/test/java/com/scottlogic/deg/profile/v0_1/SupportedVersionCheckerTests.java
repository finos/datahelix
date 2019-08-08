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

import com.scottlogic.deg.common.ValidationException;
import com.scottlogic.deg.profile.guice.ProfileConfigSource;
import com.scottlogic.deg.profile.serialisation.SchemaVersionRetriever;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SupportedVersionCheckerTests {
    @Test
    void getSchemaFile_withSupportedVersion_returnsNonNullURL() throws IOException {
        //Arrange
        SchemaVersionRetriever retriever = mock(SchemaVersionRetriever.class);
        when(retriever.getSchemaVersionOfJson(any())).thenReturn("0.1");
        ProfileConfigSource configSource = mock(ProfileConfigSource.class);
        File mockFile = mock(File.class);
        when(mockFile.toPath()).thenReturn(mock(Path.class));
        when(configSource.getProfileFile()).thenReturn(mockFile);
        SupportedVersionChecker validator = new SupportedVersionChecker(retriever, configSource);

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
        SchemaVersionRetriever retriever = mock(SchemaVersionRetriever.class);
        when(retriever.getSchemaVersionOfJson(any())).thenReturn("101.53");
        ProfileConfigSource configSource = mock(ProfileConfigSource.class);
        File mockFile = mock(File.class);
        when(mockFile.toPath()).thenReturn(mock(Path.class));
        when(configSource.getProfileFile()).thenReturn(mockFile);
        SupportedVersionChecker validator = new SupportedVersionChecker(retriever, configSource);


        //Act & Assert
        assertThrows(ValidationException.class, validator::getSchemaFile);
    }
}
