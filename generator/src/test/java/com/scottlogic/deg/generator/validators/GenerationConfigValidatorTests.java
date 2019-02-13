package com.scottlogic.deg.generator.validators;

import com.scottlogic.deg.generator.generation.GenerationConfig;
import com.scottlogic.deg.generator.generation.TestGenerationConfigSource;
import com.scottlogic.deg.generator.outputs.targets.FileOutputTarget;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsCollectionContaining.hasItem;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GenerationConfigValidatorTests {

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
    void setup() throws IOException {
        //Arrange
        validator = new GenerationConfigValidator(mockConfigSource, outputTarget);
        when(outputTarget.isDirectory()).thenReturn(false);
        when(outputTarget.exists()).thenReturn(false);
        when(mockConfigSource.shouldViolate()).thenReturn(false);
    }

    @Test
    public void interestingWithNoMaxRowsReturnsValid() {
        //Act
        ValidationResult validationResult = validator.validateCommandLine(config);

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
        ValidationResult validationResult = validator.validateCommandLine(config);

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
        ValidationResult validationResult = validator.validateCommandLine(config);

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
        ValidationResult validationResult = validator.validateCommandLine(config);

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
        ValidationResult validationResult = validator.validateCommandLine(config);

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
        ValidationResult validationResult = validator.validateCommandLine(config);

        //Assert
        Assert.assertTrue(validationResult.isValid());
        Assert.assertThat(validationResult.errorMessages, is(empty()));
    }

    @Test
    public void generateOutputFileAlreadyExists() {

        when(outputTarget.exists()).thenReturn(true);

        //Act
        ValidationResult validationResult = validator.validateCommandLine(config);

        //Assert
        Assert.assertFalse(validationResult.isValid());
    }

    @Test
    public void generateOutputFileDoesNotExist() {

        //Act
        ValidationResult validationResult = validator.validateCommandLine(config);

        //Assert
        Assert.assertTrue(validationResult.isValid());
    }

    @Test
    public void generateOutputDirNotFile() {

        when(outputTarget.isDirectory()).thenReturn(true);

        //Act
        ValidationResult validationResult = validator.validateCommandLine(config);

        //Assert
        Assert.assertFalse(validationResult.isValid());
    }

    @Test
    public void generateViolationOutputFileAlreadyExists() {
        //Arrange
        when(mockConfigSource.shouldViolate()).thenReturn(true);
        when(outputTarget.exists()).thenReturn(true);

        //Act
        ValidationResult validationResult = validator.validateCommandLine(config);

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
        ValidationResult validationResult = validator.validateCommandLine(config);

        //Assert
        Assert.assertFalse(validationResult.isValid());
    }

    @Test
    public void generateViolationOutputDirNotFile() {
        //Arrange
        when(mockConfigSource.shouldViolate()).thenReturn(true);
        when(outputTarget.isDirectory()).thenReturn(true);

        //Act
        ValidationResult validationResult = validator.validateCommandLine(config);

        //Assert
        Assert.assertTrue(validationResult.isValid());
    }

}
