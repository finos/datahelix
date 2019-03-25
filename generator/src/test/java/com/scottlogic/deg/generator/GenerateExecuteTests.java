package com.scottlogic.deg.generator;

import com.scottlogic.deg.generator.generation.GenerationConfig;
import com.scottlogic.deg.generator.generation.GenerationConfigSource;
import com.scottlogic.deg.generator.inputs.InvalidProfileException;
import com.scottlogic.deg.generator.inputs.JsonProfileReader;
import com.scottlogic.deg.generator.outputs.targets.FileOutputTarget;
import com.scottlogic.deg.generator.validators.ErrorReporter;
import com.scottlogic.deg.generator.validators.GenerationConfigValidator;
import com.scottlogic.deg.schemas.common.ValidationResult;
import com.scottlogic.deg.schemas.v0_1.ProfileSchemaValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;

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
    private Profile mockProfile = mock(Profile.class);
    private ProfileSchemaValidator mockProfileSchemaValidator = mock(ProfileSchemaValidator.class);

    private GenerateExecute executor = new GenerateExecute(config, profileReader, generationEngine, configSource,
        outputTarget, validator, errorReporter, mockProfileSchemaValidator);

    @BeforeEach
    public void setup(){
        when(mockProfileSchemaValidator.validateProfile(any(File.class))).thenReturn(new ValidationResult(new ArrayList<>()));
    }

    @Test
    public void invalidConfigCallsCorrectMethods() throws IOException, InvalidProfileException {
        //Arrange
        when(validator.preProfileChecks(config, configSource)).thenReturn(validationResult);
        when(validationResult.isValid()).thenReturn(false);

        //Act
        executor.run();

        //Assert
        verify(errorReporter, times(1)).display(validationResult);
        verify(profileReader, never()).read((Path) any());
    }

    @Test
    public void validConfigCallsCorrectMethods() throws IOException, InvalidProfileException {
        //Arrange
        File testFile = new File("TestFile");
        when(validator.preProfileChecks(config, configSource)).thenReturn(validationResult);
        when(validator.postProfileChecks(any(Profile.class), same(configSource), same(outputTarget))).thenReturn(validationResult);
        when(configSource.getProfileFile()).thenReturn(testFile);
        when(profileReader.read(eq(testFile.toPath()))).thenReturn(mockProfile);

        when(validationResult.isValid()).thenReturn(true);

        //Act
        executor.run();

        //Assert
        verify(profileReader, times(1)).read(testFile.toPath());
        verify(errorReporter, never()).display(any());
    }
}
