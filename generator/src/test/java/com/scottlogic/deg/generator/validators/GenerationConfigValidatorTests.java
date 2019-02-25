package com.scottlogic.deg.generator.validators;

import com.scottlogic.deg.generator.Profile;
import com.scottlogic.deg.generator.generation.GenerationConfig;
import com.scottlogic.deg.generator.generation.TestGenerationConfigSource;
import com.scottlogic.deg.generator.outputs.targets.FileOutputTarget;
import com.scottlogic.deg.generator.utils.FileUtils;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Optional;

import static com.shazam.shazamcrest.MatcherAssert.assertThat;
import static com.shazam.shazamcrest.matcher.Matchers.sameBeanAs;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

public class GenerationConfigValidatorTests {

    private FileOutputTarget mockOutputTarget = mock(FileOutputTarget.class);
    private FileUtils mockFileUtils = mock(FileUtils.class);
    private File mockFile = mock(File.class);
    private GenerationConfig config = mock(GenerationConfig.class);
    private GenerationConfigValidator validator;
    private Profile profile;
    private ArrayList<String> expectedErrorMessages;
    private ValidationResult expectedResult;
    private TestGenerationConfigSource mockConfigSource = mock(TestGenerationConfigSource.class);

    @BeforeEach
    void setup() throws IOException {
        //Arrange
        validator = new GenerationConfigValidator(mockFileUtils, mockConfigSource, mockOutputTarget);

        when(mockFileUtils.isDirectory(mockOutputTarget)).thenReturn(false);
        when(mockFileUtils.exists(mockOutputTarget)).thenReturn(true);
        when(mockFileUtils.probeContentType(any(Path.class))).thenReturn("application/json");
        when(mockConfigSource.shouldViolate()).thenReturn(false);
        when(mockConfigSource.getProfileFile()).thenReturn(mockFile);
        when(mockFile.exists()).thenReturn(true);
        when(mockConfigSource.getMaxRows()).thenReturn(null);
        when(config.getDataGenerationType()).thenReturn(GenerationConfig.DataGenerationType.INTERESTING);
        when(config.getMaxRows()).thenReturn(Optional.empty());
        when(mockFileUtils.containsInvalidChars(mockFile)).thenReturn(false);
        when(mockFile.exists()).thenReturn(true);
        when(mockFile.isDirectory()).thenReturn(false);
        when(mockFileUtils.isEmpty(mockFile)).thenReturn(false);

        profile = new Profile(new ArrayList<>(), new ArrayList<>());
        expectedErrorMessages = new ArrayList<>();
        expectedResult = new ValidationResult(expectedErrorMessages);
    }

    @Test
    public void validatePreProfile_withValid_returnsNoErrorMessages() {
        //Act
        ValidationResult actualResult = validator.validatePreProfile(config, mockConfigSource);

        //Assert
        assertThat("Validation result did not contain expected error message", actualResult, sameBeanAs(expectedResult));
        Assert.assertTrue(actualResult.isValid());
    }

    @Test
    public void validatePreProfile_randomWithMaxRows_returnsNoErrorMessages() {
        //Arrange
        Mockito.reset(config);
        when(config.getDataGenerationType()).thenReturn(GenerationConfig.DataGenerationType.RANDOM);
        when(config.getMaxRows()).thenReturn(Optional.of(1234L));
        validator = new GenerationConfigValidator(mockFileUtils, mockConfigSource, mockOutputTarget);

        //Act
        ValidationResult actualResult = validator.validatePreProfile(config, mockConfigSource);

        //Assert
        assertThat("Validation result did not contain expected error message", actualResult, sameBeanAs(expectedResult));
        Assert.assertTrue(actualResult.isValid());
    }

    @Test
    public void validateCommandLineOptions_randomWithNoMaxRows_correctErrorMessage() {
        //Arrange
        Mockito.reset(config);
        when(config.getDataGenerationType()).thenReturn(GenerationConfig.DataGenerationType.RANDOM);
        when(config.getMaxRows()).thenReturn(Optional.empty());
        expectedErrorMessages.add("RANDOM mode requires max row limit: use -n=<row limit> option");
        validator = new GenerationConfigValidator(mockFileUtils, mockConfigSource, mockOutputTarget);

        //Act
        ValidationResult actualResult = validator.validatePreProfile(config, mockConfigSource);

        //Assert
        assertThat("Validation result did not contain expected error message", actualResult, sameBeanAs(expectedResult));
        Assert.assertFalse(actualResult.isValid());
    }

    @Test
    public void validatePreProfile_profileFilePathContainsInvalidChars_returnsCorrectErrorMessage() {
        //Arrange
        when(mockFileUtils.containsInvalidChars(mockFile)).thenReturn(true);
        expectedErrorMessages.add(String.format("Profile file path (%s) contains one or more invalid characters " +
            "? : %% \" | > < ", mockFile.toString()));

        //Act
        ValidationResult actualResult = validator.validatePreProfile(config, mockConfigSource);

        //Assert
        assertThat("Validation result did not contain expected error message", actualResult, sameBeanAs(expectedResult));
        Assert.assertFalse(actualResult.isValid());
    }

    @Test
    public void validatePreProfile_profileFileDoesNotExist_returnsCorrectErrorMessage() {
        //Arrange
        when(mockFile.exists()).thenReturn(false);
        expectedErrorMessages.add("Invalid Input - Profile file does not exist");

        //Act
        ValidationResult actualResult = validator.validatePreProfile(config, mockConfigSource);

        //Assert
        assertThat("Validation result did not contain expected error message", actualResult, sameBeanAs(expectedResult));
        Assert.assertFalse(actualResult.isValid());
    }

    @Test
    public void validatePreProfile_profileFileIsDir_returnsCorrectErrorMessage() {
        //Arrange
        when(mockFile.isDirectory()).thenReturn(true);
        expectedErrorMessages.add("Invalid Input - Profile file path provided is to a directory");

        //Act
        ValidationResult actualResult = validator.validatePreProfile(config, mockConfigSource);

        //Assert
        assertThat("Validation result did not contain expected error message", actualResult, sameBeanAs(expectedResult));
        Assert.assertFalse(actualResult.isValid());
    }

    @Test
    public void validatePreProfile_profileFileIsEmpty_returnsCorrectErrorMessage() {
        //Arrange
        when(mockFileUtils.isEmpty(mockFile)).thenReturn(true);
        expectedErrorMessages.add("Invalid Input - Profile file has no content");

        //Act
        ValidationResult actualResult = validator.validatePreProfile(config, mockConfigSource);

        //Assert
        assertThat("Validation result did not contain expected error message", actualResult, sameBeanAs(expectedResult));
        Assert.assertFalse(actualResult.isValid());
    }

    @Test
    public void validateProfileInputFile_profileFileNotTypeJSON_correctErrorMessage() throws IOException {
        //Arrange
        when(mockFileUtils.probeContentType(mockFile.toPath())).thenReturn("invalid_file_type");
        expectedErrorMessages.add("Invalid Input - File is of type invalid_file_type" +
            "\nFile type application/json required");

        //Act
        ValidationResult actualResult = validator.validatePreProfile(config, mockConfigSource);

        //Assert
        assertThat("Validation result did not contain expected error message", actualResult, sameBeanAs(expectedResult));
        Assert.assertFalse(actualResult.isValid());
    }

    @Test
    public void validateCommandLinePostProfile_generateOutputFileAlreadyExists_isNotValid() {
        //Arrange
        when(mockFileUtils.exists(eq(mockOutputTarget))).thenReturn(true);
        expectedErrorMessages.add("Invalid Output - file already exists, please use a different output filename " +
            "or use the --overwrite option");

        //Act
        ValidationResult actualResult = validator
            .validateCommandLinePostProfile(profile, mockConfigSource, mockOutputTarget);

        //Assert
        assertThat("Validation result did not contain expected error message", actualResult, sameBeanAs(expectedResult));
        Assert.assertFalse(actualResult.isValid());
    }

    @Test
    public void validateCommandLinePostProfile_generateOutputFileAlreadyExistsCommandLineOverwrite_isValid() {
        //Arrange
        when(mockFileUtils.exists(eq(mockOutputTarget))).thenReturn(true);
        when(mockConfigSource.overwriteOutputFiles()).thenReturn(true);

        //Act
        ValidationResult actualResult = validator
            .validateCommandLinePostProfile(profile, mockConfigSource, mockOutputTarget);

        //Assert
        assertThat("Validation result did not contain expected error message", actualResult, sameBeanAs(expectedResult));
        Assert.assertTrue(actualResult.isValid());
    }

    @Test
    public void validateCommandLinePostProfile_generateOutputFileDoesNotExist_isValid() {
        //Arrange
        when(mockFileUtils.exists(mockOutputTarget)).thenReturn(false);

        //Act
        ValidationResult actualResult = validator
            .validateCommandLinePostProfile(profile, mockConfigSource, mockOutputTarget);

        //Assert
        assertThat("Validation result did not contain expected error message", actualResult, sameBeanAs(expectedResult));
        Assert.assertTrue(actualResult.isValid());
    }

    @Test
    public void validateCommandLinePostProfile_generateOutputDirNotFile_isNotValid() {
        //Arrange
        when(mockFileUtils.isDirectory(eq(mockOutputTarget))).thenReturn(true);
        expectedErrorMessages.add("Invalid Output - target is a directory, please use a different output filename");

        //Act
        ValidationResult actualResult = validator
            .validateCommandLinePostProfile(profile, mockConfigSource, mockOutputTarget);

        //Assert
        assertThat("Validation result did not contain expected error message", actualResult, sameBeanAs(expectedResult));
        Assert.assertFalse(actualResult.isValid());
    }

    @Test
    public void validateCommandLinePostProfile_generateViolationOutputFileNotDir_isNotValid() {
        //Arrange
        when(mockConfigSource.shouldViolate()).thenReturn(true);
        when(mockFileUtils.exists(eq(mockOutputTarget))).thenReturn(true);
        expectedErrorMessages.add("Invalid Output - not a directory, please enter a valid directory name");

        //Act
        ValidationResult actualResult = validator
            .validateCommandLinePostProfile(profile, mockConfigSource, mockOutputTarget);

        //Assert
        assertThat("Validation result did not contain expected error message", actualResult, sameBeanAs(expectedResult));
        Assert.assertFalse(actualResult.isValid());
    }

    @Test
    public void validateCommandLinePostProfile_generateViolationOutputDirDoesNotExist_isNotValid() {
        //Arrange
        when(mockConfigSource.shouldViolate()).thenReturn(true);
        when(mockFileUtils.isDirectory(eq(mockOutputTarget))).thenReturn(false);
        when(mockFileUtils.exists(eq(mockOutputTarget))).thenReturn(false);
        expectedErrorMessages.add("Invalid Output - output directory must exist, please enter a valid directory name");

        //Act
        ValidationResult actualResult = validator
            .validateCommandLinePostProfile(profile, mockConfigSource, mockOutputTarget);

        //Assert
        assertThat("Validation result did not contain expected error message", actualResult, sameBeanAs(expectedResult));
        Assert.assertFalse(actualResult.isValid());
    }

    @Test
    public void validateCommandLinePostProfile_generateViolationValid_isValid() {
        //Arrange
        when(mockConfigSource.shouldViolate()).thenReturn(true);
        when(mockFileUtils.isDirectory(eq(mockOutputTarget))).thenReturn(true);
        when(mockFileUtils.isDirectoryEmpty(eq(mockOutputTarget), anyInt())).thenReturn(true);

        //Act
        ValidationResult actualResult = validator
            .validateCommandLinePostProfile(profile, mockConfigSource, mockOutputTarget);

        //Assert
        assertThat("Validation result did not contain expected error message", actualResult, sameBeanAs(expectedResult));
        Assert.assertTrue(actualResult.isValid());
    }

    @Test
    public void validateCommandLinePostProfile_generateViolationOutputDirNotEmpty_isNotValid() {
        //Arrange
        when(mockConfigSource.shouldViolate()).thenReturn(true);
        when(mockFileUtils.isDirectory(eq(mockOutputTarget))).thenReturn(true);
        when(mockFileUtils.isDirectoryEmpty(eq(mockOutputTarget), anyInt())).thenReturn(false);
        expectedErrorMessages.add("Invalid Output - directory not empty, please remove any 'manfiest.json' " +
            "and '[0-9].csv' files or use the --overwrite option");

        //Act
        ValidationResult actualResult = validator.validateCommandLinePostProfile(profile, mockConfigSource, mockOutputTarget);

        //Assert
        assertThat("Validation result did not contain expected error message", actualResult, sameBeanAs(expectedResult));
        Assert.assertFalse(actualResult.isValid());
    }
}
