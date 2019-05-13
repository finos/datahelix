package com.scottlogic.deg.generator.validators;

import com.scottlogic.deg.generator.generation.GenerationConfig;
import com.scottlogic.deg.generator.generation.TestGenerationConfigSource;
import com.scottlogic.deg.generator.inputs.validation.ValidationAlert;
import com.scottlogic.deg.generator.outputs.targets.FileOutputTarget;
import com.scottlogic.deg.generator.utils.FileUtils;
import com.scottlogic.deg.profile.common.ValidationResult;
import com.scottlogic.deg.profile.v0_1.ProfileSchemaValidator;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GenerationConfigValidatorTests {

    private FileOutputTarget mockOutputTarget = mock(FileOutputTarget.class);
    private Path mockFilePath = mock(Path.class);
    private FileUtils mockFileUtils = mock(FileUtils.class);
    private File mockFile = mock(File.class);
    private GenerationConfig config = mock(GenerationConfig.class);
    private GenerationConfigValidator validator;
    private ProfileSchemaValidator mockProfileSchemaValidator = mock(ProfileSchemaValidator.class);
    private TestGenerationConfigSource mockConfigSource = mock(TestGenerationConfigSource.class);

    @BeforeEach
    void setup() {
        //Arrange
        when(mockOutputTarget.getFilePath()).thenReturn(mockFilePath);
        validator = new GenerationConfigValidator(mockFileUtils);

        //Generic Setup
        when(config.getDataGenerationType()).thenReturn(GenerationConfig.DataGenerationType.INTERESTING);
        when(mockConfigSource.getProfileFile()).thenReturn(mockFile);
        //Pre Profile Checks
        when(config.getMaxRows()).thenReturn(Optional.empty());
        when(mockConfigSource.isEnableTracing()).thenReturn(false);
        when(mockFile.exists()).thenReturn(true);
        when(mockFile.isDirectory()).thenReturn(false);
        when(mockFileUtils.containsInvalidChars(mockFile)).thenReturn(false);
        when(mockFileUtils.isFileEmpty(mockFile)).thenReturn(false);
        //Post Profile Checks
        when(mockFileUtils.exists(mockFilePath)).thenReturn(true);
        when(mockFileUtils.isDirectory(mockFilePath)).thenReturn(false);
        when(mockConfigSource.shouldViolate()).thenReturn(false);
        when(mockProfileSchemaValidator.validateProfile(any(File.class))).thenReturn(new ValidationResult(new ArrayList<>()));
    }

    @Test
    public void preProfileChecks_withValid_returnsNoErrorMessages() {
        //Act
        Collection<ValidationAlert> actualResult = validator.preProfileChecks(config, mockConfigSource);

        //Assert
        Assert.assertThat(actualResult, empty());
    }

    @Test
    public void preProfileChecks_randomWithMaxRows_returnsNoErrorMessages() {
        //Arrange
        Mockito.reset(config);
        when(config.getDataGenerationType()).thenReturn(GenerationConfig.DataGenerationType.RANDOM);
        when(config.getMaxRows()).thenReturn(Optional.of(1234L));
        validator = new GenerationConfigValidator(mockFileUtils);

        //Act
        Collection<ValidationAlert> actualResult = validator.preProfileChecks(config, mockConfigSource);

        //Assert
        Assert.assertThat(actualResult, empty());
    }

    @Test
    public void validateCommandLineOptions_traceConstraintsOutputFileDoesNotExist_returnsNoErrorMessages() {
        //Arrange
        when(mockConfigSource.isEnableTracing()).thenReturn(true);
        when(mockFileUtils.getTraceFile(mockConfigSource)).thenReturn(mock(File.class));

        //Act
        Collection<ValidationAlert> actualResult = validator.preProfileChecks(config, mockConfigSource);

        //Assert
        Assert.assertThat(actualResult, empty());
    }

    @Test
    public void validateCommandLineOptions_traceConstraintsOutputFileAlreadyExistsNoOverwrite_returnsCorrectErrorMessage() {
        //Arrange
        when(mockConfigSource.isEnableTracing()).thenReturn(true);
        when(mockFileUtils.getTraceFile(mockConfigSource)).thenReturn(mock(File.class));
        when(mockFileUtils.getTraceFile(mockConfigSource).exists()).thenReturn(true);

        //Act
        Collection<ValidationAlert> actualResult = validator.preProfileChecks(config, mockConfigSource);

        //Assert
        Collection<String> messages = actualResult
            .stream()
            .map(va -> va.getMessage().getVerboseMessage())
            .collect(Collectors.toList());
        Assert.assertThat(messages, hasItem("trace file already exists, please use a different output filename or use the --replace option"));
    }

    @Test
    public void validateCommandLineOptions_traceConstraintsOutputFileAlreadyExistsOverwrite_returnsNoErrorMessages() {
        //Arrange
        when(mockConfigSource.isEnableTracing()).thenReturn(true);
        when(mockFileUtils.getTraceFile(mockConfigSource)).thenReturn(mock(File.class));
        when(mockFileUtils.getTraceFile(mockConfigSource).exists()).thenReturn(true);
        when(mockConfigSource.overwriteOutputFiles()).thenReturn(true);

        //Act
        Collection<ValidationAlert> actualResult = validator.preProfileChecks(config, mockConfigSource);

        //Assert
        Assert.assertThat(actualResult, empty());
    }

    @Test
    public void preProfileChecks_profileFilePathContainsInvalidChars_returnsCorrectErrorMessage() {
        //Arrange
        when(mockFileUtils.containsInvalidChars(mockFile)).thenReturn(true);

        //Act
        Collection<ValidationAlert> actualResult = validator.preProfileChecks(config, mockConfigSource);

        //Assert
        Collection<String> messages = actualResult
            .stream()
            .map(va -> va.getMessage().getVerboseMessage())
            .collect(Collectors.toList());
        Assert.assertThat(messages, hasItem(matchesPattern("^Profile file path \\(.+\\) contains one or more invalid characters .*")));
    }

    @Test
    public void preProfileChecks_profileFileDoesNotExist_returnsCorrectErrorMessage() {
        //Arrange
        when(mockFile.exists()).thenReturn(false);

        //Act
        Collection<ValidationAlert> actualResult = validator.preProfileChecks(config, mockConfigSource);

        //Assert
        Collection<String> messages = actualResult
            .stream()
            .map(va -> va.getMessage().getVerboseMessage())
            .collect(Collectors.toList());
        Assert.assertThat(messages, hasItem(matchesPattern("^Profile file \\(.+\\) does not exist$")));
    }

    @Test
    public void preProfileChecks_profileFileIsDir_returnsCorrectErrorMessage() {
        //Arrange
        when(mockFile.isDirectory()).thenReturn(true);

        //Act
        Collection<ValidationAlert> actualResult = validator.preProfileChecks(config, mockConfigSource);

        //Assert
        Collection<String> messages = actualResult
            .stream()
            .map(va -> va.getMessage().getVerboseMessage())
            .collect(Collectors.toList());
        Assert.assertThat(messages, hasItem(matchesPattern("^Profile file path \\(.+\\) provided is to a directory$")));
    }

    @Test
    public void preProfileChecks_profileFileIsEmpty_returnsCorrectErrorMessage() {
        //Arrange
        when(mockFileUtils.isFileEmpty(mockFile)).thenReturn(true);

        //Act
        Collection<ValidationAlert> actualResult = validator.preProfileChecks(config, mockConfigSource);

        //Assert
        Collection<String> messages = actualResult
            .stream()
            .map(va -> va.getMessage().getVerboseMessage())
            .collect(Collectors.toList());
        Assert.assertThat(messages, hasItem(matchesPattern("^Profile file \\(.+\\) has no content$")));
    }
}
