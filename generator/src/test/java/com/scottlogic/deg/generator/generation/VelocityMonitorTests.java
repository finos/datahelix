package com.scottlogic.deg.generator.generation;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;

public class VelocityMonitorTests {
    @Test
    public void endGeneration_printsAllSavedMessages() {
        //Arrange
        PrintStream mockOutput = Mockito.mock(PrintStream.class);
        PrintWriter mockWriter = Mockito.mock(PrintWriter.class);
        VelocityMonitor monitor = new VelocityMonitor(mockWriter);
        String firstString = "First St-- HELP I'M TRAPPED IN A PRINT STREAM FACTORY --ring.";
        String secondString = "Second String";
        List<String> expectedStrings = Arrays.asList(firstString, secondString);

        //Act
        monitor.generationStarting();
        monitor.addLineToPrintAtEndOfGeneration(firstString, mockOutput);
        monitor.addLineToPrintAtEndOfGeneration(secondString, mockOutput);
        monitor.endGeneration();

        //Assert
        ArgumentCaptor<String> args = ArgumentCaptor.forClass(String.class);
        Mockito.verify(mockOutput, times(2)).println(args.capture());
        assertEquals(expectedStrings, args.getAllValues());
    }
}
