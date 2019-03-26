package com.scottlogic.deg.generator;

import com.scottlogic.deg.generator.generation.GenerationConfig;
import com.scottlogic.deg.generator.generation.GenerationConfigSource;
import com.scottlogic.deg.generator.inputs.InvalidProfileException;
import com.scottlogic.deg.generator.inputs.JsonProfileReader;
import com.scottlogic.deg.generator.inputs.validation.Criticality;
import com.scottlogic.deg.generator.inputs.validation.ProfileValidator;
import com.scottlogic.deg.generator.inputs.validation.ValidationAlert;
import com.scottlogic.deg.generator.inputs.validation.ValidationType;
import com.scottlogic.deg.generator.inputs.validation.messages.StringValidationMessage;
import com.scottlogic.deg.generator.inputs.validation.reporters.ProfileValidationReporter;
import com.scottlogic.deg.generator.outputs.targets.FileOutputTarget;
import com.scottlogic.deg.generator.validators.ErrorReporter;
import com.scottlogic.deg.generator.validators.GenerationConfigValidator;
import com.scottlogic.deg.schemas.common.ValidationResult;
import com.scottlogic.deg.schemas.v0_1.ProfileSchemaValidator;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import static org.mockito.Mockito.*;

public class GenerateExecuteTests {

    private GenerationConfig config = mock(GenerationConfig.class);
    private JsonProfileReader profileReader = mock(JsonProfileReader.class);
    private StandardGenerationEngine generationEngine = mock(StandardGenerationEngine.class);
    private GenerationConfigSource configSource = mock(GenerationConfigSource.class);
    private FileOutputTarget outputTarget = mock(FileOutputTarget.class);
    private GenerationConfigValidator configValidator = mock(GenerationConfigValidator.class);
    private ProfileValidator profileValidator = mock(ProfileValidator.class);
    private ErrorReporter errorReporter = mock(ErrorReporter.class);
    private ValidationResult validationResult = mock(ValidationResult.class);
    private Profile mockProfile = mock(Profile.class);
    private ProfileValidationReporter validationReporter = mock(ProfileValidationReporter.class);
    private ProfileSchemaValidator mockProfileSchemaValidator = mock(ProfileSchemaValidator.class);

    private GenerateExecute executor = new GenerateExecute(
        config,
        profileReader,
        generationEngine,
        configSource,
        outputTarget,
        configValidator,
        errorReporter,
        profileValidator,
        mockProfileSchemaValidator,
        validationReporter);

    @Test
    public void invalidConfigCallsCorrectMethods() throws IOException, InvalidProfileException {
        //Arrange
        when(configValidator.preProfileChecks(config, configSource)).thenReturn(validationResult);
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
        when(configValidator.preProfileChecks(config, configSource)).thenReturn(validationResult);
        when(configValidator.postProfileChecks(any(Profile.class), same(configSource), same(outputTarget))).thenReturn(validationResult);
        when(configSource.getProfileFile()).thenReturn(testFile);
        when(profileReader.read(eq(testFile.toPath()))).thenReturn(mockProfile);
        when(mockProfileSchemaValidator.validateProfile(testFile)).thenReturn(new ValidationResult(Collections.emptyList()));

        when(validationResult.isValid()).thenReturn(true);

        //Act
        executor.run();

        //Assert
        verify(profileReader, times(1)).read(testFile.toPath());
        verify(errorReporter, never()).display(any());
    }

    @Test
    public void run_whenProfileValidationYieldsErrors_shouldNotGenerate() throws IOException, InvalidProfileException {
        //Arrange
        File testFile = new File("TestFile");
        Collection<ValidationAlert> validationAlerts = Collections.singleton(
            new ValidationAlert(Criticality.ERROR, new StringValidationMessage(""), ValidationType.NULL, new Field("")));

        when(configSource.getProfileFile()).thenReturn(testFile);
        when(profileReader.read(eq(testFile.toPath()))).thenReturn(mockProfile);
        when(configValidator.preProfileChecks(any(), any())).thenReturn(new ValidationResult(new ArrayList<>()));
        when(profileValidator.validate(any())).thenReturn(validationAlerts);
        when(mockProfileSchemaValidator.validateProfile(testFile)).thenReturn(new ValidationResult(Collections.emptyList()));

        //Act
        executor.run();

        //Assert
        verify(validationReporter, times(1)).output(any());
        verify(generationEngine, never()).generateDataSet(any(), any(), any());
    }

    @Test
    public void run_whenProfileValidationYieldsInformationOrWarnings_shouldStillGenerate() throws IOException, InvalidProfileException {
        //Arrange
        File testFile = new File("TestFile");
        Collection<ValidationAlert> validationAlerts = Arrays.asList(
            new ValidationAlert(Criticality.INFORMATION, new StringValidationMessage(""), ValidationType.NULL, new Field("")),
            new ValidationAlert(Criticality.WARNING, new StringValidationMessage(""), ValidationType.NULL, new Field("")));

        when(configSource.getProfileFile()).thenReturn(testFile);
        when(profileReader.read(eq(testFile.toPath()))).thenReturn(mockProfile);
        when(configValidator.preProfileChecks(any(), any())).thenReturn(new ValidationResult(new ArrayList<>()));
        when(configValidator.postProfileChecks(any(), any(), any())).thenReturn(new ValidationResult(new ArrayList<>()));
        when(profileValidator.validate(any())).thenReturn(validationAlerts);
        when(mockProfileSchemaValidator.validateProfile(testFile)).thenReturn(new ValidationResult(Collections.emptyList()));

        //Act
        executor.run();

        //Assert
        verify(validationReporter, times(1)).output(any());
        verify(generationEngine, times(1)).generateDataSet(any(), any(), any());
    }
}
