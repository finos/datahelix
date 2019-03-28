package com.scottlogic.deg.generator;

import com.scottlogic.deg.generator.generation.GenerationConfig;
import com.scottlogic.deg.generator.generation.GenerationConfigSource;
import com.scottlogic.deg.generator.inputs.InvalidProfileException;
import com.scottlogic.deg.generator.inputs.JsonProfileReader;
import com.scottlogic.deg.generator.inputs.validation.Criticality;
import com.scottlogic.deg.generator.inputs.validation.ProfileValidator;
import com.scottlogic.deg.generator.inputs.validation.ValidationAlert;
import com.scottlogic.deg.generator.inputs.validation.ValidationType;
import com.scottlogic.deg.generator.inputs.validation.messages.InputValidationMessage;
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
    private Collection<ValidationAlert> validationResult;
    private ValidationResult schemaValidationResult = mock(ValidationResult.class);
    private Profile mockProfile = mock(Profile.class);
    private ProfileValidationReporter validationReporter = mock(ProfileValidationReporter.class);
    private ProfileSchemaValidator mockProfileSchemaValidator = mock(ProfileSchemaValidator.class);
    private ValidationAlert invalid = new ValidationAlert(
        Criticality.ERROR,
        new InputValidationMessage("Some error", mock(File.class)),
        ValidationType.INPUT,
        null
    );

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
    public void run_whenPreProfileChecksFail_shouldNotReadProfile() throws IOException, InvalidProfileException {
        //Arrange
        validationResult = Collections.singleton(invalid);
        when(configValidator.preProfileChecks(config, configSource)).thenReturn(validationResult);

        //Act
        executor.run();

        //Assert
        verify(validationReporter, times(1)).output(validationResult);
        verify(profileReader, never()).read((Path) any());
    }

    @Test
    public void run_whenPreProfileChecksFail_shouldNotValidateTheSchema() {
        //Arrange
        validationResult = Collections.singleton(invalid);
        File testFile = new File("TestFile");
        when(configValidator.preProfileChecks(config, configSource)).thenReturn(validationResult);
        when(configSource.getProfileFile()).thenReturn(testFile);

        //Act
        executor.run();

        //Assert
        verify(mockProfileSchemaValidator, never()).validateProfile(testFile);
    }

    @Test
    public void run_whenPreProfileChecksPass_shouldValidateTheSchema() throws IOException, InvalidProfileException {
        //Arrange
        File testFile = new File("TestFile");
        validationResult = Collections.emptySet();
        when(configValidator.preProfileChecks(config, configSource)).thenReturn(validationResult);
        when(configSource.getProfileFile()).thenReturn(testFile);
        when(profileReader.read(eq(testFile.toPath()))).thenReturn(mockProfile);
        when(mockProfileSchemaValidator.validateProfile(testFile)).thenReturn(schemaValidationResult);

        //Act
        executor.run();

        //Assert
        verify(mockProfileSchemaValidator, times(1)).validateProfile(testFile);
    }

    @Test
    public void run_whenSchemaValidationFails_shouldNotReadTheProfile() throws IOException, InvalidProfileException {
        //Arrange
        File testFile = new File("TestFile");
        validationResult = Collections.emptySet();
        when(configValidator.preProfileChecks(config, configSource)).thenReturn(validationResult);
        when(configSource.getProfileFile()).thenReturn(testFile);
        when(profileReader.read(eq(testFile.toPath()))).thenReturn(mockProfile);
        when(mockProfileSchemaValidator.validateProfile(testFile)).thenReturn(schemaValidationResult);
        when(schemaValidationResult.isValid()).thenReturn(false);

        //Act
        executor.run();

        //Assert
        verify(mockProfileSchemaValidator, times(1)).validateProfile(testFile);
        verify(profileReader, never()).read(any(Path.class));
        verify(errorReporter, times(1)).display(same(schemaValidationResult));
    }

    @Test
    public void run_whenSchemaValidationPasses_shouldReadProfile() throws IOException, InvalidProfileException {
        //Arrange
        File testFile = new File("TestFile");
        validationResult = Collections.emptySet();
        when(configValidator.preProfileChecks(config, configSource)).thenReturn(validationResult);
        when(configSource.getProfileFile()).thenReturn(testFile);
        when(profileReader.read(eq(testFile.toPath()))).thenReturn(mockProfile);
        when(mockProfileSchemaValidator.validateProfile(testFile)).thenReturn(schemaValidationResult);
        when(schemaValidationResult.isValid()).thenReturn(true);

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
        Collection<ValidationAlert> validationAlerts = Collections.singleton(invalid);
        validationResult = Collections.emptySet();
        when(configSource.getProfileFile()).thenReturn(testFile);
        when(profileReader.read(eq(testFile.toPath()))).thenReturn(mockProfile);
        when(configValidator.preProfileChecks(any(), any())).thenReturn(validationResult);
        when(profileValidator.validate(any())).thenReturn(validationAlerts);
        when(mockProfileSchemaValidator.validateProfile(testFile)).thenReturn(schemaValidationResult);
        when(schemaValidationResult.isValid()).thenReturn(true);

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
        validationResult = Collections.emptySet();
        when(schemaValidationResult.isValid()).thenReturn(true);
        when(configSource.getProfileFile()).thenReturn(testFile);
        when(profileReader.read(eq(testFile.toPath()))).thenReturn(mockProfile);
        when(configValidator.preProfileChecks(any(), any())).thenReturn(validationResult);
        when(profileValidator.validate(any())).thenReturn(validationAlerts);
        when(mockProfileSchemaValidator.validateProfile(testFile)).thenReturn(schemaValidationResult);

        //Act
        executor.run();

        //Assert
        verify(validationReporter, times(1)).output(any());
        verify(generationEngine, times(1)).generateDataSet(any(), any(), any());
    }
}
