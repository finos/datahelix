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

package com.scottlogic.deg.orchestrator.validator;

import com.scottlogic.deg.common.ValidationException;
import com.scottlogic.deg.common.util.FileUtils;
import com.scottlogic.deg.profile.reader.ConfigValidator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ConfigValidatorTests {
    @Mock
    private File mockProfileFile = mock(File.class);

    @Test
    public void checkProfileInputFile_withValidFile_returnsNoErrorMessages() {
        ConfigValidator configValidator = new ConfigValidator(mockProfileFile, new FileUtils());

        when(mockProfileFile.getPath()).thenReturn("path");
        when(mockProfileFile.exists()).thenReturn(true);
        when(mockProfileFile.isDirectory()).thenReturn(false);
        when(mockProfileFile.length()).thenReturn(1L);

        assertDoesNotThrow(configValidator::checkProfileInputFile,"Expected no exception, but one was thrown.");
    }

    @Test
    public void checkProfileInputFile_profileFilePathContainsInvalidChars_throwsException() {
        ConfigValidator configValidator = new ConfigValidator(mockProfileFile, new FileUtils());

        when(mockProfileFile.getPath()).thenReturn("path?");

        assertThrows(ValidationException.class, configValidator::checkProfileInputFile,"Expected ValidationException to throw, but didn't");
    }

    @Test
    public void checkProfileInputFile_profileFileDoesNotExist_throwsException() {
        ConfigValidator configValidator = new ConfigValidator(mockProfileFile, new FileUtils());

        when(mockProfileFile.getPath()).thenReturn("path");
        when(mockProfileFile.exists()).thenReturn(false);

        assertThrows(ValidationException.class, configValidator::checkProfileInputFile,"Expected ValidationException to throw, but didn't");
    }

    @Test
    public void checkProfileInputFile_profileFileIsDir_throwsException() {
        ConfigValidator configValidator = new ConfigValidator(mockProfileFile, new FileUtils());

        when(mockProfileFile.getPath()).thenReturn("path");
        when(mockProfileFile.exists()).thenReturn(true);
        when(mockProfileFile.isDirectory()).thenReturn(true);

        assertThrows(ValidationException.class, configValidator::checkProfileInputFile,"Expected ValidationException to throw, but didn't");
    }

    @Test
    public void checkProfileInputFile_profileFileIsEmpty_throwsException() {
        ConfigValidator configValidator = new ConfigValidator(mockProfileFile, new FileUtils());

        when(mockProfileFile.getPath()).thenReturn("path");
        when(mockProfileFile.exists()).thenReturn(true);
        when(mockProfileFile.isDirectory()).thenReturn(false);
        when(mockProfileFile.length()).thenReturn(0L);

        assertThrows(ValidationException.class, configValidator::checkProfileInputFile,"Expected ValidationException to throw, but didn't");
    }
}