package com.scottlogic.deg.generator.validators;

import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsCollectionContaining.hasItem;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.scottlogic.deg.generator.Profile;
import com.scottlogic.deg.generator.generation.GenerationConfig;
import com.scottlogic.deg.generator.generation.TestGenerationConfigSource;
import com.scottlogic.deg.generator.outputs.targets.FileOutputTarget;
import java.util.ArrayList;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class GenerationConfigValidatorTests {

    private Profile profile;
    private FileOutputTarget outputTarget = mock(FileOutputTarget.class);
    private TestGenerationConfigSource mockConfigSource = mock(TestGenerationConfigSource.class);
    private GenerationConfig config = new GenerationConfig(
        new TestGenerationConfigSource(
            GenerationConfig.DataGenerationType.INTERESTING,
            GenerationConfig.TreeWalkerType.REDUCTIVE,
            GenerationConfig.CombinationStrategyType.EXHAUSTIVE
        )
    );
    private GenerationConfigValidator validator;

    @BeforeEach
    void setup() {
        //Arrange
        validator = new GenerationConfigValidator(mockConfigSource, outputTarget);
        when(outputTarget.isDirectory()).thenReturn(false);
        when(outputTarget.exists()).thenReturn(false);
        when(mockConfigSource.shouldViolate()).thenReturn(false);
        profile = new Profile(new ArrayList<>(), new ArrayList<>());
    }

    @Test
    public void interestingWithNoMaxRowsReturnsValid() {
        //Act
        ValidationResult validationResult = validator.validateCommandLinePreProfile(config);

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

        //Act
        ValidationResult validationResult = validator.validateCommandLinePreProfile(config);

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
        validator = new GenerationConfigValidator(mockConfigSource, outputTarget);

        //Act
        ValidationResult validationResult = validator.validateCommandLinePreProfile(config);

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
        validator = new GenerationConfigValidator(mockConfigSource, outputTarget);

        //Act
        ValidationResult validationResult = validator.validateCommandLinePreProfile(config);

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

        //Act
        ValidationResult validationResult = validator.validateCommandLinePreProfile(config);

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

        //Act
        ValidationResult validationResult = validator.validateCommandLinePreProfile(config);

        //Assert
        Assert.assertTrue(validationResult.isValid());
        Assert.assertThat(validationResult.errorMessages, is(empty()));
    }

    @Test
    public void generateOutputFileAlreadyExists() {
        //Arrange
        when(outputTarget.exists()).thenReturn(true);

        //Act
        ValidationResult validationResult = validator
            .validateCommandLinePostProfile(config, profile);

        //Assert
        Assert.assertFalse(validationResult.isValid());
    }

    @Test
    public void generateOutputFileAlreadyExistsCommandLineOverwrite() {
        //Arrange
        when(outputTarget.exists()).thenReturn(true);
        when(mockConfigSource.overwriteOutputFiles()).thenReturn(true);

        //Act
        ValidationResult validationResult = validator
            .validateCommandLinePostProfile(config, profile);

        //Assert
        Assert.assertTrue(validationResult.isValid());
    }

    @Test
    public void generateOutputFileDoesNotExist() {

        //Act
        ValidationResult validationResult = validator
            .validateCommandLinePostProfile(config, profile);

        //Assert
        Assert.assertTrue(validationResult.isValid());
    }

    @Test
    public void generateOutputDirNotFile() {
        //Arrange
        when(outputTarget.isDirectory()).thenReturn(true);

        //Act
        ValidationResult validationResult = validator
            .validateCommandLinePostProfile(config, profile);

        //Assert
        Assert.assertFalse(validationResult.isValid());
    }

    @Test
    public void generateViolationOutputFileAlreadyExists() {
        //Arrange
        when(mockConfigSource.shouldViolate()).thenReturn(true);
        when(outputTarget.exists()).thenReturn(true);

        //Act
        ValidationResult validationResult = validator
            .validateCommandLinePostProfile(config, profile);

        //Assert
        Assert.assertFalse(validationResult.isValid());
    }

    @Test
    public void generateViolationOutputFileDoesNotExist() {
        //Arrange
        when(mockConfigSource.shouldViolate()).thenReturn(true);
        when(outputTarget.isDirectory()).thenReturn(false);
        when(outputTarget.exists()).thenReturn(false);

        //Act
        ValidationResult validationResult = validator
            .validateCommandLinePostProfile(config, profile);

        //Assert
        Assert.assertFalse(validationResult.isValid());
    }

    @Test
    public void generateViolationOutputDirNotExists() {
        //Arrange
        when(mockConfigSource.shouldViolate()).thenReturn(true);
        when(outputTarget.exists()).thenReturn(false);
        when(outputTarget.isDirectory()).thenReturn(true);
        when(outputTarget.isDirectoryEmpty(anyInt())).thenReturn(true);

        //Act
        ValidationResult validationResult = validator
            .validateCommandLinePostProfile(config, profile);

        //Assert
        Assert.assertFalse(validationResult.isValid());
    }
    @Test
    public void generateViolationOutputDirNotFile() {
        //Arrange
        when(mockConfigSource.shouldViolate()).thenReturn(true);
        when(outputTarget.exists()).thenReturn(true);
        when(outputTarget.isDirectory()).thenReturn(true);
        when(outputTarget.isDirectoryEmpty(anyInt())).thenReturn(true);

        //Act
        ValidationResult validationResult = validator
            .validateCommandLinePostProfile(config, profile);

        //Assert
        Assert.assertTrue(validationResult.isValid());
    }

    @Test
    public void generateViolationOutputDirNotEmpty() {
        //Arrange
        when(mockConfigSource.shouldViolate()).thenReturn(true);
        when(outputTarget.isDirectory()).thenReturn(true);
        when(outputTarget.isDirectoryEmpty(anyInt())).thenReturn(false);

        //Act
        ValidationResult validationResult = validator
            .validateCommandLinePostProfile(config, profile);

        //Assert
        Assert.assertFalse(validationResult.isValid());
    }

}
