package com.scottlogic.deg.generator.validators;

import com.scottlogic.deg.generator.Profile;
import com.scottlogic.deg.generator.outputs.targets.FileOutputTarget;
import com.scottlogic.deg.generator.utils.FileUtils;
import com.scottlogic.deg.generator.visualisation.TestVisualisationConfigSource;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class VisualisationConfigValidatorTests {

    private FileOutputTarget mockOutputTarget = mock(FileOutputTarget.class);
    private FileUtils mockFileUtils = mock(FileUtils.class);
    private Profile profile;
    private TestVisualisationConfigSource mockConfigSource = mock(TestVisualisationConfigSource.class);
    private VisualisationConfigValidator validator;

    @BeforeEach
    void setup() throws IOException {
        //Arrange
        validator = new VisualisationConfigValidator(mockFileUtils);
        when(mockFileUtils.isDirectory(eq(mockOutputTarget))).thenReturn(false);
        when(mockFileUtils.exists(eq(mockOutputTarget))).thenReturn(false);
        profile = new Profile(new ArrayList<>(), new ArrayList<>());
    }

    @Test
    public void generateOutputFileAlreadyExists() {
        //Arrange
        when(mockFileUtils.exists(eq(mockOutputTarget))).thenReturn(true);

        //Act
        ValidationResult validationResult = validator.validateCommandLine(false, mockOutputTarget);

        //Assert
        Assert.assertFalse(validationResult.isValid());
    }

    @Test
    public void generateOutputFileAlreadyExistsCommandLineOverwrite() {
        //Arrange
        when(mockFileUtils.exists(eq(mockOutputTarget))).thenReturn(true);

        //Act
        ValidationResult validationResult = validator.validateCommandLine(true, mockOutputTarget);

        //Assert
        Assert.assertTrue(validationResult.isValid());
    }

    @Test
    public void generateOutputFileDoesNotExist() {

        //Act
        ValidationResult validationResult = validator.validateCommandLine(false, mockOutputTarget);

        //Assert
        Assert.assertTrue(validationResult.isValid());
    }

    @Test
    public void generateOutputDirNotFile() {
        //Arrange
        when(mockFileUtils.isDirectory(eq(mockOutputTarget))).thenReturn(true);

        //Act
        ValidationResult validationResult = validator.validateCommandLine(false, mockOutputTarget);

        //Assert
        Assert.assertFalse(validationResult.isValid());
    }

}
