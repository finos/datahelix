package com.scottlogic.deg.generator.validators;

import com.scottlogic.deg.generator.Profile;
import com.scottlogic.deg.generator.generation.TestGenerationConfigSource;
import com.scottlogic.deg.generator.inputs.validation.ValidationAlert;
import com.scottlogic.deg.generator.outputs.targets.FileOutputTarget;
import com.scottlogic.deg.generator.utils.FileUtils;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GenerationOutputValidatorTests {
    private FileOutputTarget mockOutputTarget = mock(FileOutputTarget.class);
    private Path mockFilePath = mock(Path.class);
    private FileUtils mockFileUtils = mock(FileUtils.class);
    private File mockFile = mock(File.class);
    private GenerationOutputValidator validator;
    private Profile profile;
    private TestGenerationConfigSource mockConfigSource = mock(TestGenerationConfigSource.class);

    @BeforeEach
    void setup() {
        //Arrange
        when(mockOutputTarget.getFilePath()).thenReturn(mockFilePath);
        validator = new GenerationOutputValidator(mockFileUtils, mockConfigSource, mockOutputTarget);
        profile = new Profile(new ArrayList<>(), new ArrayList<>());

        when(mockConfigSource.getProfileFile()).thenReturn(mockFile);
        when(mockFileUtils.exists(mockFilePath)).thenReturn(true);
        when(mockFileUtils.isDirectory(mockFilePath)).thenReturn(false);
        when(mockConfigSource.shouldViolate()).thenReturn(false);
    }

    @Test
    public void validate_generateOutputFileParentDirAlreadyExists_isNotValid()
        throws IOException {
        Path mockPath = Paths.get("/a/b/c/tmp.out");
        when(mockOutputTarget.getFilePath()).thenReturn(mockPath);
        when(mockFileUtils.createDirectories(eq(mockPath))).thenReturn(false);
        when(mockConfigSource.overwriteOutputFiles()).thenReturn(true);
        when(mockFileUtils.exists(eq(mockFilePath))).thenReturn(false);

        //Act
        Collection<ValidationAlert> actualResult = validator.validate(profile);

        //Assert
        Collection<String> messages = actualResult
            .stream()
            .map(va -> va.getMessage().getVerboseMessage())
            .collect(Collectors.toList());
        Assert.assertThat(messages, hasItem("parent directory of output file "
            + "already exists but is not a directory, please use a different output filename"));
    }

    @Test
    public void validate_generateOutputFileAlreadyExists_isNotValid() throws IOException {
        //Arrange
        when(mockFileUtils.createDirectories(mockFilePath.getParent())).thenReturn(true);
        when(mockFileUtils.exists(eq(mockFilePath))).thenReturn(true);

        //Act
        Collection<ValidationAlert> actualResult = validator.validate(profile);

        //Assert
        Collection<String> messages = actualResult
            .stream()
            .map(va -> va.getMessage().getVerboseMessage())
            .collect(Collectors.toList());
        Assert.assertThat(messages, hasItem("file already exists, please use a different output filename " +
            "or use the --replace option"));
    }

    @Test
    public void validate_generateOutputFileAlreadyExistsCommandLineOverwrite_isValid() throws IOException {
        //Arrange
        when(mockFileUtils.createDirectories(mockFilePath.getParent())).thenReturn(true);
        when(mockConfigSource.overwriteOutputFiles()).thenReturn(true);

        //Act
        Collection<ValidationAlert> actualResult = validator.validate(profile);

        //Assert
        Assert.assertThat(actualResult, empty());
    }

    @Test
    public void validate_generateOutputFileDoesNotExist_isValid() throws IOException {
        //Arrange
        Path mockPath = Paths.get("/a/b/c/tmp.out");
        when(mockOutputTarget.getFilePath()).thenReturn(mockPath);
        when(mockFileUtils.createDirectories(anyObject())).thenReturn(true);
        when(mockFileUtils.exists(mockFilePath)).thenReturn(false);

        //Act
        Collection<ValidationAlert> actualResult = validator.validate(profile);

        //Assert
        Assert.assertThat(actualResult, empty());
    }

    @Test
    public void validate_generateOutputDirNotFile_isNotValid() throws IOException {
        //Arrange
        when(mockFileUtils.createDirectories(mockFilePath.getParent())).thenReturn(true);
        when(mockFileUtils.isDirectory(eq(mockFilePath))).thenReturn(true);

        //Act
        Collection<ValidationAlert> actualResult = validator.validate(profile);

        //Assert
        Collection<String> messages = actualResult
            .stream()
            .map(va -> va.getMessage().getVerboseMessage())
            .collect(Collectors.toList());
        Assert.assertThat(messages, hasItem("target is a directory, please use a different output filename"));
    }

    @Test
    public void validate_generateViolationOutputFileNotDir_isNotValid() {
        //Arrange
        when(mockConfigSource.shouldViolate()).thenReturn(true);

        //Act
        Collection<ValidationAlert> actualResult = validator.validate(profile);

        //Assert
        Collection<String> messages = actualResult
            .stream()
            .map(va -> va.getMessage().getVerboseMessage())
            .collect(Collectors.toList());
        Assert.assertThat(messages, hasItem("not a directory, please enter a valid directory name"));
    }

    @Test
    public void validate_generateViolationValid_isValid() {
        //Arrange
        when(mockConfigSource.shouldViolate()).thenReturn(true);
        when(mockFileUtils.isDirectory(eq(mockFilePath))).thenReturn(true);
        when(mockFileUtils.isDirectoryEmpty(eq(mockFilePath), anyInt())).thenReturn(true);

        //Act
        Collection<ValidationAlert> actualResult = validator.validate(profile);

        //Assert
        Assert.assertThat(actualResult, empty());
    }

    @Test
    public void validate_generateViolationOutputDirNotEmpty_isNotValid() {
        //Arrange
        when(mockConfigSource.shouldViolate()).thenReturn(true);
        when(mockFileUtils.isDirectory(eq(mockFilePath))).thenReturn(true);
        when(mockFileUtils.isDirectoryEmpty(eq(mockFilePath), anyInt())).thenReturn(false);

        //Act
        Collection<ValidationAlert> actualResult = validator.validate(profile);

        //Assert
        Collection<String> messages = actualResult
            .stream()
            .map(va -> va.getMessage().getVerboseMessage())
            .collect(Collectors.toList());
        Assert.assertThat(messages, hasItem("directory not empty, please remove any 'manifest.json' " +
            "and '[0-9].csv' files or use the --replace option"));
    }
}