package com.scottlogic.deg.orchestrator.generate;

import com.scottlogic.deg.common.output.GeneratedObject;
import com.scottlogic.deg.common.profile.Profile;
import com.scottlogic.deg.generator.generation.DataGenerator;
import com.scottlogic.deg.generator.generation.DataGeneratorMonitor;
import com.scottlogic.deg.generator.inputs.validation.ProfileValidator;
import com.scottlogic.deg.generator.walker.RetryLimitReachedException;
import com.scottlogic.deg.orchestrator.guice.AllConfigSource;
import com.scottlogic.deg.orchestrator.validator.ConfigValidator;
import com.scottlogic.deg.output.outputtarget.SingleDatasetOutputTarget;
import com.scottlogic.deg.profile.reader.ProfileReader;
import com.scottlogic.deg.profile.v0_1.ProfileSchemaValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.stream.Stream;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.atLeastOnce;

class GenerateExecuteTests {
    private AllConfigSource configSource;
    private SingleDatasetOutputTarget singleDatasetOutputTarget;
    private ConfigValidator configValidator;
    private ProfileReader profileReader;
    private DataGenerator dataGenerator;
    private ProfileValidator profileValidator;
    private DataGeneratorMonitor monitor;
    private ProfileSchemaValidator profileSchemaValidator;
    private GenerateExecute generateExecute;
    private Profile profile;

    @BeforeEach
    void setup() {
        configSource = mock(AllConfigSource.class);
        singleDatasetOutputTarget = mock(SingleDatasetOutputTarget.class);
        configValidator = mock(ConfigValidator.class);
        profileReader = mock(ProfileReader.class);
        dataGenerator = mock(DataGenerator.class);
        profileValidator = mock(ProfileValidator.class);
        monitor = mock(DataGeneratorMonitor.class);
        profileSchemaValidator = mock(ProfileSchemaValidator.class);
        profile = mock(Profile.class);

        generateExecute = new GenerateExecute(
            profileReader,
            dataGenerator,
            configSource,
            singleDatasetOutputTarget,
            configValidator,
            profileValidator,
            profileSchemaValidator,
            monitor);
    }


    @Test
    public void execute_onRetryFail_reportsError() throws IOException {
        when(profileReader.read(any())).thenReturn(profile);
        File file = mock(File.class);
        when(configSource.getProfileFile()).thenReturn(file);
        when(file.toPath()).thenReturn(mock(Path.class));
        when(profile.getSchemaVersion()).thenReturn("0.1");
        when(dataGenerator.generateData(profile)).thenReturn(
            Stream.iterate(mock(GeneratedObject.class), dataBag -> {
                throw new RetryLimitReachedException();
            }).skip(1));

        generateExecute.execute();

        verify(monitor, atLeastOnce()).addLineToPrintAtEndOfGeneration(anyString());
    }
}