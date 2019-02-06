package com.scottlogic.deg.generator.commandLine;

import com.scottlogic.deg.generator.CommandLine.CommandLineValidator;
import com.scottlogic.deg.generator.CommandLine.ValidationResult;
import com.scottlogic.deg.generator.generation.GenerationConfig;
import com.scottlogic.deg.generator.generation.TestGenerationConfigSource;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class CommandLineValidatorTest {
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
        CommandLineValidator validator = new CommandLineValidator();
        ArrayList<String> errorsReturned = new ArrayList<String>();

        //Act
        ValidationResult validationResult = validator.validateCommandLine(config);

        //Assert
        Assert.assertTrue(validationResult.isValid());
        Assert.assertEquals(validationResult.errorMessages, errorsReturned);
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
        ArrayList<String> errorsReturned = new ArrayList<>();
        errorsReturned.add("RANDOM mode requires max row limit\nuse -n=<row limit> option\n");
        CommandLineValidator validator = new CommandLineValidator();

        //Act
        ValidationResult validationResult = validator.validateCommandLine(config);

        //Assert
        Assert.assertFalse(validationResult.isValid());
        Assert.assertEquals(validationResult.errorMessages, errorsReturned);
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
        ArrayList<String> errorsReturned = new ArrayList<>();
        CommandLineValidator validator = new CommandLineValidator();

        //Act
        ValidationResult validationResult = validator.validateCommandLine(config);

        //Assert
        Assert.assertTrue(validationResult.isValid());
        Assert.assertEquals(validationResult.errorMessages, errorsReturned);
    }

}
