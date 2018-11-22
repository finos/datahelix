package com.scottlogic.deg.generator;

import picocli.CommandLine;

import java.util.stream.Collectors;

@CommandLine.Command(name = "dg")
public class App implements Runnable {
    private static final CommandLine picoCliCommandLine = new CommandLine(new App())
        .addSubcommand("generate", new Generate())
        .addSubcommand("generateTestCases", new GenerateTestCases())
        .addSubcommand("visualise", new Visualise())
        .setCaseInsensitiveEnumValuesAllowed(true);

    public static void main(String[] args) {
        picoCliCommandLine.parseWithHandler(
            new CommandLine.RunLast(), // the base command (ie, this class) is always identified as a possible handler. RunLast picks the most specific handler
            args);
    }

    @Override
    public void run() {
        String commandListString = picoCliCommandLine.getSubcommands().keySet().stream().sorted().collect(Collectors.joining(", "));

        System.err.println("Valid commands: " + commandListString);
    }
}
