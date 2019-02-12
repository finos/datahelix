package com.scottlogic.deg.generator.validators;

import com.scottlogic.deg.generator.generation.GenerationConfig;
import com.scottlogic.deg.generator.generation.TestGenerationConfigSource;
import com.scottlogic.deg.generator.outputs.targets.FileOutputTarget;
import com.scottlogic.deg.generator.outputs.targets.OutputTarget;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsCollectionContaining.hasItem;
import static org.mockito.Mockito.mock;

public class GenerationConfigValidatorTests {

    private OutputTarget fileOutputTarget = mock(OutputTarget.class);

    @Test
    public void interestingWithNoMaxRowsReturnsValid() {
        //Arrange
        GenerationConfig config = new GenerationConfig(
            new TestGenerationConfigSource(
                GenerationConfig.DataGenerationType.INTERESTING,
                GenerationConfig.TreeWalkerType.REDUCTIVE,
                GenerationConfig.CombinationStrategyType.EXHAUSTIVE
                )
        );
        GenerationConfigValidator validator = new GenerationConfigValidator();

        //Act
        ValidationResult validationResult = validator.validateCommandLine(config, fileOutputTarget);

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
        GenerationConfigValidator validator = new GenerationConfigValidator();

        //Act
        ValidationResult validationResult = validator.validateCommandLine(config, fileOutputTarget);

        //Assert
        Assert.assertTrue(validationResult.isValid());
        Assert.assertThat(validationResult.errorMessages, is(empty()));
    }

    @Test
    public void randomWithNoMaxRowsReturnsNotValid() {
        //Arrange
        GenerationConfig config = new GenerationConfig(
            new TestGenerationConfigSource(
                GenerationConfig.DataGenerationType.RANDOM,
                GenerationConfig.TreeWalkerType.REDUCTIVE,
                GenerationConfig.CombinationStrategyType.EXHAUSTIVE
            )
        );
        GenerationConfigValidator validator = new GenerationConfigValidator();

        //Act
        ValidationResult validationResult = validator.validateCommandLine(config, fileOutputTarget);

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
        GenerationConfigValidator validator = new GenerationConfigValidator();

        //Act
        ValidationResult validationResult = validator.validateCommandLine(config, fileOutputTarget);

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
        GenerationConfigValidator validator = new GenerationConfigValidator();

        //Act
        ValidationResult validationResult = validator.validateCommandLine(config, fileOutputTarget);

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
        GenerationConfigValidator validator = new GenerationConfigValidator();

        //Act
        ValidationResult validationResult = validator.validateCommandLine(config, fileOutputTarget);

        //Assert
        Assert.assertTrue(validationResult.isValid());
        Assert.assertThat(validationResult.errorMessages, is(empty()));
    }

    @Test
    public void outputFileAlreadyExists() throws IOException {
        //Arrange
        TestGenerationConfigSource testConfigSource = new TestGenerationConfigSource(
            GenerationConfig.DataGenerationType.FULL_SEQUENTIAL,
            GenerationConfig.TreeWalkerType.REDUCTIVE,
            GenerationConfig.CombinationStrategyType.EXHAUSTIVE
        );
        testConfigSource.setMaxRows(1234567L);
        GenerationConfig config = new GenerationConfig(testConfigSource);
        GenerationConfigValidator validator = new GenerationConfigValidator();

        Path tmpPath = Files.createTempFile("datahelix-test", "csv");
        FileOutputTarget existingFileOutputTarget = new FileOutputTarget(tmpPath, null);

        //Act
        ValidationResult validationResult = validator.validateCommandLine(config, existingFileOutputTarget);

        //Assert
        Assert.assertFalse(validationResult.isValid());
    }

    @Test
    public void outputFileDoesNotExist() throws IOException {
        //Arrange
        TestGenerationConfigSource testConfigSource = new TestGenerationConfigSource(
            GenerationConfig.DataGenerationType.FULL_SEQUENTIAL,
            GenerationConfig.TreeWalkerType.REDUCTIVE,
            GenerationConfig.CombinationStrategyType.EXHAUSTIVE
        );
        testConfigSource.setMaxRows(1234567L);
        GenerationConfig config = new GenerationConfig(testConfigSource);
        GenerationConfigValidator validator = new GenerationConfigValidator();

        Path tmpPath = Files.createTempFile("datahelix-test", "csv");
        Files.delete(tmpPath);
        FileOutputTarget newFileOutputTarget = new FileOutputTarget(tmpPath, null);

        //Act
        ValidationResult validationResult = validator.validateCommandLine(config, newFileOutputTarget);

        //Assert
        Assert.assertTrue(validationResult.isValid());
    }

}
