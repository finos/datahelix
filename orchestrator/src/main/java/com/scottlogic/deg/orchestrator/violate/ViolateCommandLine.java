package com.scottlogic.deg.orchestrator.violate;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.scottlogic.deg.orchestrator.generate.GenerateCommandLine;
import com.scottlogic.deg.orchestrator.guice.AllModule;
import picocli.CommandLine;

@CommandLine.Command(
    name = "violate",
    description = "Produces a decision tree in DOT format for the specified profile.",
    descriptionHeading = "%nDescription:%n",
    parameterListHeading = "%nParameters:%n",
    optionListHeading = "%nOptions:%n",
    abbreviateSynopsis = true)
public class ViolateCommandLine extends GenerateCommandLine {
    @Override
    public void run() {
        Module container = new ViolateModule(this);
        Injector injector = Guice.createInjector(container);

        Runnable task = injector.getInstance(ViolateExecute.class);

        task.run();
    }
}
