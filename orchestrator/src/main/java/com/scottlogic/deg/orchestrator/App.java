package com.scottlogic.deg.orchestrator;

import com.scottlogic.deg.orchestrator.generate.GenerateCommandLine;
import com.scottlogic.deg.orchestrator.visualise.VisualiseCommandLine;
import picocli.CommandLine;

import java.util.stream.Collectors;

@CommandLine.Command(name = "datahelix")
public class App implements Runnable {
    private static final CommandLine picoCliCommandLine = new CommandLine(new App())
        .addSubcommand("generate", new GenerateCommandLine())
        .addSubcommand("visualise", new VisualiseCommandLine())
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
