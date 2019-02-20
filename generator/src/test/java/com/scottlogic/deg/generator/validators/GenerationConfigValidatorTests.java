package com.scottlogic.deg.generator.validators;

import com.scottlogic.deg.generator.Profile;
import com.scottlogic.deg.generator.generation.GenerationConfig;
import com.scottlogic.deg.generator.generation.TestGenerationConfigSource;
import com.scottlogic.deg.generator.outputs.targets.FileOutputTarget;
import com.scottlogic.deg.generator.utils.FileUtils;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsCollectionContaining.hasItem;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GenerationConfigValidatorTests {

    private FileOutputTarget mockOutputTarget = mock(FileOutputTarget.class);
    private FileUtils mockFileUtils = mock(FileUtils.class);
    private GenerationConfig config = new GenerationConfig(
        new TestGenerationConfigSource(
            GenerationConfig.DataGenerationType.INTERESTING,
            GenerationConfig.TreeWalkerType.REDUCTIVE,
            GenerationConfig.CombinationStrategyType.EXHAUSTIVE
        )
    );
    private GenerationConfigValidator validator;
    private Profile profile;
    private TestGenerationConfigSource mockConfigSource = mock(TestGenerationConfigSource.class);

    @BeforeEach
    void setup() {
        //Arrange
        validator = new GenerationConfigValidator(mockFileUtils, mockConfigSource, mockOutputTarget);
        when(mockFileUtils.isDirectory(eq(mockOutputTarget))).thenReturn(false);
        when(mockFileUtils.exists(eq(mockOutputTarget))).thenReturn(false);
        when(mockConfigSource.shouldViolate()).thenReturn(false);
        profile = new Profile(new ArrayList<>(), new ArrayList<>());
    }

    @Test
    public void interestingWithNoMaxRowsReturnsValid() {
        //Arrange


        //Act
        ValidationResult validationResult = validator.validatePreProfile(config);

        //Assert
        Assert.assertTrue(validationResult.isValid());
        Assert.assertThat(validationResult.errorMessages, is(empty()));
    }

    @Test
    public void interestingWithMaxRowsReturnsValid() {
        //Arrange
        TestGenerationConfigSource testConfigSource = new TestGenerationConfigSource(
            GenerationConfig.DataGenerationType.INTERESTING,
            GenerationConfig.TreeWalkerType.REDUCTIVE,
            GenerationConfig.CombinationStrategyType.EXHAUSTIVE
        );
        testConfigSource.setMaxRows(1234567L);
        GenerationConfig config = new GenerationConfig(testConfigSource);
        validator = new GenerationConfigValidator(mockFileUtils, mockConfigSource, mockOutputTarget);

        //Act
        ValidationResult validationResult = validator.validatePreProfile(config);

        //Assert
        Assert.assertTrue(validationResult.isValid());
        Assert.assertThat(validationResult.errorMessages, is(empty()));
    }

    @Test
    public void randomWithNoMaxRowsReturnsNotValid() {
        //Arrange
        TestGenerationConfigSource testConfigSource = new TestGenerationConfigSource(
            GenerationConfig.DataGenerationType.RANDOM,
            GenerationConfig.TreeWalkerType.REDUCTIVE,
            GenerationConfig.CombinationStrategyType.EXHAUSTIVE
        );
        GenerationConfig config = new GenerationConfig(testConfigSource);
        validator = new GenerationConfigValidator(mockFileUtils, mockConfigSource, mockOutputTarget);

        //Act
        ValidationResult validationResult = validator.validatePreProfile(config);

        //Assert
        Assert.assertFalse(validationResult.isValid());
        Assert.assertThat(validationResult.errorMessages,
            hasItem("RANDOM mode requires max row limit: use -n=<row limit> option"));
    }

    @Test
    public void randomWithMaxRowsReturnsValid() {
        //Arrange
        TestGenerationConfigSource testConfigSource = new TestGenerationConfigSource(
            GenerationConfig.DataGenerationType.RANDOM,
            GenerationConfig.TreeWalkerType.REDUCTIVE,
            GenerationConfig.CombinationStrategyType.EXHAUSTIVE
        );
        testConfigSource.setMaxRows(1234567L);
        GenerationConfig config = new GenerationConfig(testConfigSource);
        validator = new GenerationConfigValidator(mockFileUtils, mockConfigSource, mockOutputTarget);

        //Act
        ValidationResult validationResult = validator.validatePreProfile(config);

        //Assert
        Assert.assertTrue(validationResult.isValid());
        Assert.assertThat(validationResult.errorMessages, is(empty()));
    }

    @Test
    public void fullSequentialWithNoMaxRowsReturnsValid() {
        //Arrange
        GenerationConfig config = new GenerationConfig(
            new TestGenerationConfigSource(
                GenerationConfig.DataGenerationType.FULL_SEQUENTIAL,
                GenerationConfig.TreeWalkerType.REDUCTIVE,
                GenerationConfig.CombinationStrategyType.EXHAUSTIVE
            )
        );
        validator = new GenerationConfigValidator(mockFileUtils, mockConfigSource, mockOutputTarget);

        //Act
        ValidationResult validationResult = validator.validatePreProfile(config);

        //Assert
        Assert.assertTrue(validationResult.isValid());
        Assert.assertThat(validationResult.errorMessages, is(empty()));
    }

    @Test
    public void fullSequentialWithMaxRowsReturnsValid() {
        //Arrange
        TestGenerationConfigSource testConfigSource = new TestGenerationConfigSource(
            GenerationConfig.DataGenerationType.FULL_SEQUENTIAL,
            GenerationConfig.TreeWalkerType.REDUCTIVE,
            GenerationConfig.CombinationStrategyType.EXHAUSTIVE
        );
        testConfigSource.setMaxRows(1234567L);
        GenerationConfig config = new GenerationConfig(testConfigSource);
        validator = new GenerationConfigValidator(mockFileUtils, mockConfigSource, mockOutputTarget);

        //Act
        ValidationResult validationResult = validator.validatePreProfile(config);

        //Assert
        Assert.assertTrue(validationResult.isValid());
        Assert.assertThat(validationResult.errorMessages, is(empty()));
    }

    @Test
    public void generateOutputFileAlreadyExists() {
        //Arrange
        when(mockFileUtils.exists(eq(mockOutputTarget))).thenReturn(true);

        //Act
        ValidationResult validationResult = validator
            .validateCommandLinePostProfile(profile);

        //Assert
        Assert.assertFalse(validationResult.isValid());
    }

    @Test
    public void generateOutputFileAlreadyExistsCommandLineOverwrite() {
        //Arrange
        when(mockFileUtils.exists(eq(mockOutputTarget))).thenReturn(true);
        when(mockConfigSource.overwriteOutputFiles()).thenReturn(true);

        //Act
        ValidationResult validationResult = validator
            .validateCommandLinePostProfile(profile);

        //Assert
        Assert.assertTrue(validationResult.isValid());
    }

    @Test
    public void generateOutputFileDoesNotExist() {

        //Act
        ValidationResult validationResult = validator
            .validateCommandLinePostProfile(profile);

        //Assert
        Assert.assertTrue(validationResult.isValid());
    }

    @Test
    public void generateOutputDirNotFile() {
        //Arrange
        when(mockFileUtils.isDirectory(eq(mockOutputTarget))).thenReturn(true);

        //Act
        ValidationResult validationResult = validator
            .validateCommandLinePostProfile(profile);

        //Assert
        Assert.assertFalse(validationResult.isValid());
    }

    @Test
    public void generateViolationOutputFileAlreadyExists() {
        //Arrange
        when(mockConfigSource.shouldViolate()).thenReturn(true);
        when(mockFileUtils.exists(eq(mockOutputTarget))).thenReturn(true);

        //Act
        ValidationResult validationResult = validator
            .validateCommandLinePostProfile(profile);

        //Assert
        Assert.assertFalse(validationResult.isValid());
    }

    @Test
    public void generateViolationOutputFileDoesNotExist() {
        //Arrange
        when(mockConfigSource.shouldViolate()).thenReturn(true);
        when(mockFileUtils.isDirectory(eq(mockOutputTarget))).thenReturn(false);
        when(mockFileUtils.exists(eq(mockOutputTarget))).thenReturn(false);

        //Act
        ValidationResult validationResult = validator
            .validateCommandLinePostProfile(profile);

        //Assert
        Assert.assertFalse(validationResult.isValid());
    }

    @Test
    public void generateViolationOutputDirNotExists() {
        //Arrange
        when(mockConfigSource.shouldViolate()).thenReturn(true);
        when(mockFileUtils.exists(eq(mockOutputTarget))).thenReturn(false);
        when(mockFileUtils.isDirectory(eq(mockOutputTarget))).thenReturn(true);
        when(mockFileUtils.isDirectoryEmpty(eq(mockOutputTarget), anyInt())).thenReturn(true);

        //Act
        ValidationResult validationResult = validator
            .validateCommandLinePostProfile(profile);

        //Assert
        Assert.assertFalse(validationResult.isValid());
    }

    @Test
    public void generateViolationOutputDirNotFile() {
        //Arrange
        when(mockConfigSource.shouldViolate()).thenReturn(true);
        when(mockFileUtils.exists(eq(mockOutputTarget))).thenReturn(true);
        when(mockFileUtils.isDirectory(eq(mockOutputTarget))).thenReturn(true);
        when(mockFileUtils.isDirectoryEmpty(eq(mockOutputTarget), anyInt())).thenReturn(true);

        //Act
        ValidationResult validationResult = validator.validateCommandLinePostProfile(profile);

        //Assert
        Assert.assertTrue(validationResult.isValid());
    }

    @Test
    public void generateViolationOutputDirNotEmpty() {
        //Arrange
        when(mockConfigSource.shouldViolate()).thenReturn(true);
        when(mockFileUtils.isDirectory(eq(mockOutputTarget))).thenReturn(true);
        when(mockFileUtils.isDirectoryEmpty(eq(mockOutputTarget), anyInt())).thenReturn(false);

        //Act
        ValidationResult validationResult = validator.validateCommandLinePostProfile(profile);

        //Assert
        Assert.assertFalse(validationResult.isValid());
    }

}
