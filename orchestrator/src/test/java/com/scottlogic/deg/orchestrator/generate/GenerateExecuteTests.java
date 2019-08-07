package com.scottlogic.deg.orchestrator.generate;

import com.scottlogic.deg.common.output.GeneratedObject;
import com.scottlogic.deg.common.profile.Profile;
import com.scottlogic.deg.generator.generation.DataGenerator;
import com.scottlogic.deg.generator.generation.DataGeneratorMonitor;
import com.scottlogic.deg.generator.inputs.validation.ProfileValidator;
import com.scottlogic.deg.generator.walker.RetryLimitReachedException;
import com.scottlogic.deg.output.outputtarget.SingleDatasetOutputTarget;
import com.scottlogic.deg.profile.reader.ValidatingProfileReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.stream.Stream;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

class GenerateExecuteTests {
    private SingleDatasetOutputTarget singleDatasetOutputTarget;
    private DataGenerator dataGenerator;
    private ProfileValidator profileValidator;
    private DataGeneratorMonitor monitor;
    private GenerateExecute generateExecute;
    private ValidatingProfileReader validatingProfileReader;
    private Profile profile;

    @BeforeEach
    void setup() {
        singleDatasetOutputTarget = mock(SingleDatasetOutputTarget.class);
        dataGenerator = mock(DataGenerator.class);
        profileValidator = mock(ProfileValidator.class);
        monitor = mock(DataGeneratorMonitor.class);
        validatingProfileReader = mock(ValidatingProfileReader.class);

        profile = mock(Profile.class);

        generateExecute = new GenerateExecute(
            dataGenerator,
            singleDatasetOutputTarget,
            validatingProfileReader,
            profileValidator,
            monitor
        );
    }


    @Test
    public void execute_onRetryFail_reportsError() throws IOException {
        when(validatingProfileReader.read()).thenReturn(profile);
        when(dataGenerator.generateData(profile)).thenReturn(
            Stream.iterate(mock(GeneratedObject.class), dataBag -> {
                throw new RetryLimitReachedException();
            }).skip(1));

        generateExecute.execute();

        verify(monitor, atLeastOnce()).addLineToPrintAtEndOfGeneration(anyString());
    }
}