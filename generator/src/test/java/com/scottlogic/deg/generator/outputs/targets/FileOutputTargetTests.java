package com.scottlogic.deg.generator.outputs.targets;

import com.scottlogic.deg.generator.outputs.formats.OutputFormat;
import com.scottlogic.deg.generator.utils.FileUtils;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class FileOutputTargetTests {
    //Create Mocks
    private Path mockFilePath = mock(Path.class);
    private FileUtils mockFileUtils = mock(FileUtils.class);
    private OutputFormat mockOutputFormat = mock(OutputFormat.class);
    private Path mockParentPath = mock(Path.class);

    @Test
    public void validate_generateOutputFileIsADirectory_throwsException(){
        when(mockFileUtils.isDirectory(any())).thenReturn(true);
        when(mockFilePath.getParent()).thenReturn(mockParentPath);
        FileOutputTarget outputTarget = new FileOutputTarget(mockFilePath, mockOutputFormat, false, mockFileUtils);

        assertThrows(OutputTargetValidationException.class, ()->outputTarget.validate(),"Expected OutputTargetValidationException to throw, but didn't");
  }

    @Test
    public void validate_generateOutputFileAlreadyExistsNoOverwrite_throwsException() {
        when(mockFileUtils.isDirectory(mockFilePath)).thenReturn(false);
        when(mockFileUtils.isDirectory(mockParentPath)).thenReturn(true);
        when(mockFilePath.getParent()).thenReturn(mockParentPath);
        when(mockFileUtils.exists(any())).thenReturn(true);
        FileOutputTarget outputTarget = new FileOutputTarget(mockFilePath, mockOutputFormat, false, mockFileUtils);

        assertThrows(OutputTargetValidationException.class, ()->outputTarget.validate(),"Expected OutputTargetValidationException to throw, but didn't");
    }

    @Test
    public void validate_generateOutputFileAlreadyExistsOverwrite_doesntThrow() {
        when(mockFileUtils.isDirectory(mockFilePath)).thenReturn(false);
        when(mockFileUtils.isDirectory(mockParentPath)).thenReturn(true);
        when(mockFilePath.getParent()).thenReturn(mockParentPath);
        when(mockFileUtils.exists(any())).thenReturn(true);
        FileOutputTarget outputTarget = new FileOutputTarget(mockFilePath, mockOutputFormat, true, mockFileUtils);

        assertDoesNotThrow(() ->outputTarget.validate(),"Expected no exception, but one was thrown");
    }

    @Test
    public void validate_generateOutputFileDoesntExist_doesntThrow() throws IOException {
        mockFilePath = Paths.get("/a/b/c/tmp.out");
        when(mockFileUtils.isDirectory(mockFilePath)).thenReturn(false);
        when(mockFileUtils.createDirectories(any())).thenReturn(true);
        FileOutputTarget outputTarget = new FileOutputTarget(mockFilePath, mockOutputFormat, true, mockFileUtils);

        assertDoesNotThrow(() ->outputTarget.validate(),"Expected no exception, but one was thrown");
    }

    @Test
    public void validate_generateOutputFileParentDirIsExistingFile_throwsException(){
        mockFilePath = Paths.get("/a/b/c/tmp.out/a.csv");
        when(mockFileUtils.isDirectory(mockFilePath)).thenReturn(false);
        when(mockFileUtils.isDirectory(mockParentPath)).thenReturn(false);
        FileOutputTarget outputTarget = new FileOutputTarget(mockFilePath, mockOutputFormat, false, mockFileUtils);

        assertThrows(OutputTargetValidationException.class, ()->outputTarget.validate(),"Expected OutputTargetValidationException to throw, but didn't");
    }
}

