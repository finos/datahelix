package com.scottlogic.deg.generator.commandLine;

import com.scottlogic.deg.generator.CommandLine.GenerationConfigValidator;
import com.scottlogic.deg.generator.CommandLine.ValidationResult;
import com.scottlogic.deg.generator.generation.GenerationConfig;
import com.scottlogic.deg.generator.generation.TestGenerationConfigSource;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsCollectionContaining.hasItem;

public class GenerationConfigValidatorTests {
    @Test
    public void validConfigShouldReturnValid() {
        //Arrange
        GenerationConfig config = new GenerationConfig(
            new TestGenerationConfigSource(
                GenerationConfig.DataGenerationType.INTERESTING,
                GenerationConfig.TreeWalkerType.REDUCTIVE,
                GenerationConfig.CombinationStrategyType.PINNING
                )
        );
        GenerationConfigValidator validator = new GenerationConfigValidator();

        //Act
        ValidationResult validationResult = validator.validateCommandLine(config);

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
                GenerationConfig.CombinationStrategyType.PINNING
            )
        );
        GenerationConfigValidator validator = new GenerationConfigValidator();

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
            GenerationConfig.CombinationStrategyType.PINNING
        );
        testConfigSource.setMaxRows(1234567L);
        GenerationConfig config = new GenerationConfig(testConfigSource);
        GenerationConfigValidator validator = new GenerationConfigValidator();

        //Act
        ValidationResult validationResult = validator.validateCommandLine(config);

        //Assert
        Assert.assertTrue(validationResult.isValid());
        Assert.assertThat(validationResult.errorMessages, is(empty()));
    }

}
