package com.scottlogic.deg.generator.validators;

import com.scottlogic.deg.generator.Profile;
import com.scottlogic.deg.generator.outputs.targets.VisualiseOutputTarget;
import com.scottlogic.deg.generator.utils.FileUtils;
import com.scottlogic.deg.generator.visualisation.TestVisualisationConfigSource;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class VisualisationConfigValidatorTests {

    private VisualiseOutputTarget mockOutputTarget = mock(VisualiseOutputTarget.class);
    private FileUtils mockFileUtils = mock(FileUtils.class);
    private Path mockFilePath = mock(Path.class);
    private Profile profile;
    private TestVisualisationConfigSource mockConfigSource = mock(TestVisualisationConfigSource.class);
    private VisualisationConfigValidator validator;

    @BeforeEach
    void setup() throws IOException {
        //Arrange
        when(mockOutputTarget.getFilePath()).thenReturn(mockFilePath);
        validator = new VisualisationConfigValidator(mockFileUtils);
        when(mockFileUtils.isDirectory(eq(mockFilePath))).thenReturn(false);
        when(mockFileUtils.exists(eq(mockFilePath))).thenReturn(false);
        profile = new Profile(new ArrayList<>(), new ArrayList<>());
    }

    @Test
    public void generateOutputFileAlreadyExists() {
        //Arrange
        when(mockFileUtils.exists(eq(mockFilePath))).thenReturn(true);

        //Act
        ValidationResult validationResult = validator.validateCommandLine(false, mockOutputTarget);

        //Assert
        Assert.assertFalse(validationResult.isValid());
    }

    @Test
    public void generateOutputFileAlreadyExistsCommandLineOverwrite() {
        //Arrange
        when(mockFileUtils.exists(eq(mockFilePath))).thenReturn(true);

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
        when(mockFileUtils.isDirectory(eq(mockFilePath))).thenReturn(true);

        //Act
        ValidationResult validationResult = validator.validateCommandLine(false, mockOutputTarget);

        //Assert
        Assert.assertFalse(validationResult.isValid());
    }

}
