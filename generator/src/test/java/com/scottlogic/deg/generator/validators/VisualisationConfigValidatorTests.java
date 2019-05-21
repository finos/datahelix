package com.scottlogic.deg.generator.validators;

import com.scottlogic.deg.common.profile.Profile;
import com.scottlogic.deg.generator.outputs.targets.FileOutputTarget;
import com.scottlogic.deg.generator.utils.FileUtils;
import com.scottlogic.deg.generator.visualisation.TestVisualisationConfigSource;
import com.scottlogic.deg.profile.serialisation.ValidationResult;
import com.scottlogic.deg.profile.v0_1.ProfileSchemaValidator;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class VisualisationConfigValidatorTests {

    private FileOutputTarget mockOutputTarget = mock(FileOutputTarget.class);
    private FileUtils mockFileUtils = mock(FileUtils.class);
    private Path mockFilePath = mock(Path.class);
    private Profile profile;
    private TestVisualisationConfigSource mockConfigSource = mock(TestVisualisationConfigSource.class);
    private ProfileSchemaValidator mockProfileSchemaValidator = mock(ProfileSchemaValidator.class);
    private VisualisationConfigValidator validator;

    @BeforeEach
    void setup() throws IOException {
        //Arrange
        validator = new VisualisationConfigValidator(mockFileUtils);
        when(mockFileUtils.isDirectory(eq(mockFilePath))).thenReturn(false);
        when(mockFileUtils.exists(eq(mockFilePath))).thenReturn(false);
        when(mockProfileSchemaValidator.validateProfile(any(File.class))).thenReturn(new ValidationResult(new ArrayList<>()));
        profile = new Profile(new ArrayList<>(), new ArrayList<>());
    }

    @Test
    public void generateOutputFileAlreadyExists() {
        //Arrange
        when(mockFileUtils.exists(eq(mockFilePath))).thenReturn(true);

        //Act
        ValidationResult validationResult = validator.validateCommandLine(false, mockFilePath);

        //Assert
        Assert.assertFalse(validationResult.isValid());
    }

    @Test
    public void generateOutputFileAlreadyExistsCommandLineOverwrite() {
        //Arrange
        when(mockFileUtils.exists(eq(mockFilePath))).thenReturn(true);

        //Act
        ValidationResult validationResult = validator.validateCommandLine(true, mockFilePath);

        //Assert
        Assert.assertTrue(validationResult.isValid());
    }

    @Test
    public void generateOutputFileDoesNotExist() {

        //Act
        ValidationResult validationResult = validator.validateCommandLine(false, mockFilePath);

        //Assert
        Assert.assertTrue(validationResult.isValid());
    }

    @Test
    public void generateOutputDirNotFile() {
        //Arrange
        when(mockFileUtils.isDirectory(eq(mockFilePath))).thenReturn(true);

        //Act
        ValidationResult validationResult = validator.validateCommandLine(false, mockFilePath);

        //Assert
        Assert.assertFalse(validationResult.isValid());
    }

}
