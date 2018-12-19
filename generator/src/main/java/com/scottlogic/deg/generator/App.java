package com.scottlogic.deg.generator;

import com.scottlogic.deg.generator.CommandLine.GenerateCommandLine;
import picocli.CommandLine;

@CommandLine.Command(name = "dg")
public class App {
    private static final CommandLine picoCliCommandLine = new CommandLine(new App())
        .addSubcommand("generate", new GenerateCommandLine())
//         TODO - add other commands
        .addSubcommand("visualise", new Visualise())
        .addSubcommand("genTreeJson", new GenerateTreeCollectionJson())
        .setCaseInsensitiveEnumValuesAllowed(true);

    public static void main(String[] args) {
        picoCliCommandLine.parseWithHandler(
            new CommandLine.RunLast(), // the base command (ie, this class) is always identified as a possible handler. RunLast picks the most specific handler
            args);
    }
}
