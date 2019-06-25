package com.scottlogic.deg.generator.generation;

import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.io.PrintWriter;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.times;

public class VelocityMonitorTests {
    @Test
    public void endGeneration_printsAllSavedMessages() {
        //Arrange
        PrintWriter mockWriter = Mockito.mock(PrintWriter.class);
        VelocityMonitor monitor = new VelocityMonitor(mockWriter);
        String firstString = "First St-- HELP I'M TRAPPED IN A PRINT STREAM FACTORY --ring.";
        String secondString = "Second String";

        //Act
        monitor.generationStarting();
        monitor.addLineToPrintAtEndOfGeneration(firstString);
        monitor.addLineToPrintAtEndOfGeneration(secondString);
        monitor.endGeneration();

        //Assert
        ArgumentCaptor<String> args = ArgumentCaptor.forClass(String.class);
        Mockito.verify(mockWriter, times(5)).println(args.capture());

        assertThat(args.getAllValues(), hasItem(firstString));
        assertThat(args.getAllValues(), hasItem(secondString));
    }
}
