package com.scottlogic.deg.orchestrator.visualise;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.scottlogic.deg.generator.commandline.GenerateCommandLine;
import com.scottlogic.deg.generator.guice.BaseModule;
import picocli.CommandLine;


/**
 * This class holds the visualisation specific command line options.
 *
 * @see <a href="https://github.com/ScottLogic/datahelix/blob/master/docs/Options/VisualiseOptions.md">
 * Visualise options</a> for more details.
 */
@CommandLine.Command(
    name = "visualise",
    description = "Produces a decision tree in DOT format for the specified profile.",
    descriptionHeading = "%nDescription:%n",
    parameterListHeading = "%nParameters:%n",
    optionListHeading = "%nOptions:%n",
    abbreviateSynopsis = true)
public class VisualiseCommandLine extends GenerateCommandLine {

    @Override
    public void run() {
        BaseModule container = new BaseModule(this);
        Injector injector = Guice.createInjector(container);

        Runnable task = injector.getInstance(VisualiseExecute.class);

        task.run();
    }
}
