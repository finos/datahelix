package com.scottlogic.deg.generator;

import com.scottlogic.deg.generator.validators.ErrorReporter;
import com.scottlogic.deg.generator.validators.GenerationConfigValidator;
import com.scottlogic.deg.generator.validators.ValidationResult;
import com.scottlogic.deg.generator.generation.GenerationConfig;
import com.scottlogic.deg.generator.generation.GenerationConfigSource;
import com.scottlogic.deg.generator.inputs.InvalidProfileException;
import com.scottlogic.deg.generator.inputs.JsonProfileReader;
import com.scottlogic.deg.generator.outputs.targets.FileOutputTarget;
import com.scottlogic.deg.generator.outputs.targets.OutputTarget;
import com.scottlogic.deg.generator.validators.ErrorReporter;
import com.scottlogic.deg.generator.validators.GenerationConfigValidator;
import com.scottlogic.deg.generator.validators.ValidationResult;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import static org.mockito.Mockito.*;

public class GenerateExecuteTests {

    private GenerationConfig config = mock(GenerationConfig.class);
    private JsonProfileReader profileReader = mock(JsonProfileReader.class);
    private StandardGenerationEngine generationEngine = mock(StandardGenerationEngine.class);
    private GenerationConfigSource configSource = mock(GenerationConfigSource.class);
    private FileOutputTarget outputTarget = mock(FileOutputTarget.class);
    private GenerationConfigValidator validator = mock(GenerationConfigValidator.class);
    private ErrorReporter errorReporter = mock(ErrorReporter.class);
    private ValidationResult validationResult = mock(ValidationResult.class);

    private GenerateExecute excecutor = new GenerateExecute(config, profileReader, generationEngine, configSource,
        outputTarget, validator, errorReporter);

    @Test
    public void invalidConfigCallsCorrectMethods() throws IOException, InvalidProfileException {
        //Arrange
        when(validator.validateCommandLine(config, outputTarget)).thenReturn(validationResult);
        when(validationResult.isValid()).thenReturn(false);

        //Act
        excecutor.run();

        //Assert
        verify(errorReporter, times(1)).display(validationResult);
        verify(profileReader, never()).read((Path) any());
    }

    @Test
    public void validConfigCallsCorrectMethods() throws IOException, InvalidProfileException {
        //Arrange
        File testFile = new File("TestFile");
        when(validator.validateCommandLine(config, outputTarget)).thenReturn(validationResult);
        when(configSource.getProfileFile()).thenReturn(testFile);
        when(validationResult.isValid()).thenReturn(true);

        //Act
        excecutor.run();

        //Assert
        verify(profileReader, times(1)).read(testFile.toPath());
        verify(errorReporter, never()).display(any());
    }
}
