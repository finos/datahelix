package com.scottlogic.deg.generator.validators;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.scottlogic.deg.generator.Profile;
import com.scottlogic.deg.generator.outputs.targets.FileOutputTarget;
import com.scottlogic.deg.generator.visualisation.TestVisualiseConfigSource;
import com.scottlogic.deg.generator.visualisation.VisualisationConfig;
import java.io.IOException;
import java.util.ArrayList;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class VisualisationConfigValidatorTests {

    private Profile profile;
    private FileOutputTarget outputTarget = mock(FileOutputTarget.class);
    private TestVisualiseConfigSource mockConfigSource = mock(TestVisualiseConfigSource.class);
    private VisualisationConfig config = new VisualisationConfig(mockConfigSource);
    private VisualisationConfigValidator validator;

    @BeforeEach
    void setup() throws IOException {
        //Arrange
        validator = new VisualisationConfigValidator(mockConfigSource, outputTarget);
        when(outputTarget.isDirectory()).thenReturn(false);
        when(outputTarget.exists()).thenReturn(false);
        profile = new Profile(new ArrayList<>(), new ArrayList<>());
    }

    @Test
    public void generateOutputFileAlreadyExists() {
        //Arrange
        when(outputTarget.exists()).thenReturn(true);

        //Act
        ValidationResult validationResult = validator.validateCommandLine(config);

        //Assert
        Assert.assertFalse(validationResult.isValid());
    }

    @Test
    public void generateOutputFileAlreadyExistsCommandLineOverwrite() {
        //Arrange
        when(outputTarget.exists()).thenReturn(true);
        when(mockConfigSource.overwriteOutputFiles()).thenReturn(true);

        //Act
        ValidationResult validationResult = validator.validateCommandLine(config);

        //Assert
        Assert.assertTrue(validationResult.isValid());
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
        //Arrange
        when(outputTarget.isDirectory()).thenReturn(true);

        //Act
        ValidationResult validationResult = validator.validateCommandLine(config);

        //Assert
        Assert.assertFalse(validationResult.isValid());
    }

}
