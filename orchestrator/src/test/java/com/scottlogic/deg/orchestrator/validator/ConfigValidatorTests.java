package com.scottlogic.deg.orchestrator.validator;

import com.scottlogic.deg.generator.config.detail.DataGenerationType;
import com.scottlogic.deg.generator.outputs.targets.OutputTargetValidationException;
import com.scottlogic.deg.generator.utils.FileUtils;
import com.scottlogic.deg.orchestrator.guice.AllConfigSource;
import org.junit.jupiter.api.Test;

import java.io.File;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ConfigValidatorTests {
    private FileUtils mockFileUtils = mock(FileUtils.class);
    private AllConfigSource mockConfigSource =mock(AllConfigSource.class);
    private File mockProfileFile = mock(File.class);

    @Test
    public void preProfileChecks_withValid_returnsNoErrorMessages() {
        ConfigValidator configValidator = new ConfigValidator(mockFileUtils);
        when(mockConfigSource.getProfileFile()).thenReturn(mockProfileFile);
        when(mockFileUtils.containsInvalidChars(mockProfileFile)).thenReturn(false);
        when(mockProfileFile.exists()).thenReturn(true);
        when(mockProfileFile.isDirectory()).thenReturn(false);
        when(mockFileUtils.isFileEmpty(mockProfileFile)).thenReturn(false);

        assertDoesNotThrow(()->configValidator.preProfileChecks(mockConfigSource),"Expected no exception, but one was thrown.");
    }

    @Test
    public void preProfileChecks_randomWithMaxRows_returnsNoErrorMessages() {
        ConfigValidator configValidator = new ConfigValidator(mockFileUtils);
        when(mockConfigSource.getProfileFile()).thenReturn(mockProfileFile);
        when(mockFileUtils.containsInvalidChars(mockProfileFile)).thenReturn(false);
        when(mockProfileFile.exists()).thenReturn(true);
        when(mockProfileFile.isDirectory()).thenReturn(false);
        when(mockFileUtils.isFileEmpty(mockProfileFile)).thenReturn(false);
        when(mockConfigSource.getGenerationType()).thenReturn(DataGenerationType.RANDOM);
        when(mockConfigSource.getMaxRows()).thenReturn(25L);

        assertDoesNotThrow(()->configValidator.preProfileChecks(mockConfigSource),"Expected no exception, but one was thrown.");
    }

    @Test
    public void validateCommandLineOptions_traceConstraintsOutputFileDoesNotExist_returnsNoErrorMessages() {
        ConfigValidator configValidator = new ConfigValidator(mockFileUtils);
        when(mockConfigSource.getProfileFile()).thenReturn(mockProfileFile);
        when(mockFileUtils.containsInvalidChars(mockProfileFile)).thenReturn(false);
        when(mockProfileFile.exists()).thenReturn(true);
        when(mockProfileFile.isDirectory()).thenReturn(false);
        when(mockFileUtils.isFileEmpty(mockProfileFile)).thenReturn(false);
        when(mockConfigSource.isEnableTracing()).thenReturn(true);
        when(mockFileUtils.getTraceFile(any())).thenReturn(mock(File.class));

        assertDoesNotThrow(()->configValidator.preProfileChecks(mockConfigSource),"Expected no exception, but one was thrown.");

    }

    @Test
    public void validateCommandLineOptions_traceConstraintsOutputFileAlreadyExistsNoOverwrite_returnsCorrectErrorMessage() {
        ConfigValidator configValidator = new ConfigValidator(mockFileUtils);
        when(mockConfigSource.getProfileFile()).thenReturn(mockProfileFile);
        when(mockFileUtils.containsInvalidChars(mockProfileFile)).thenReturn(false);
        when(mockProfileFile.exists()).thenReturn(true);
        when(mockProfileFile.isDirectory()).thenReturn(false);
        when(mockFileUtils.isFileEmpty(mockProfileFile)).thenReturn(false);
        when(mockConfigSource.isEnableTracing()).thenReturn(true);
        when(mockFileUtils.getTraceFile(any())).thenReturn(mock(File.class));
        when(mockFileUtils.getTraceFile(any()).exists()).thenReturn(true);

        assertThrows(OutputTargetValidationException.class, ()->configValidator.preProfileChecks(mockConfigSource),"Expected OutputTargetValidationException to throw, but didn't");
   }
}
