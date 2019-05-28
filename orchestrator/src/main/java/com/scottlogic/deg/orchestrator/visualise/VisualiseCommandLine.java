package com.scottlogic.deg.orchestrator.visualise;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.scottlogic.deg.orchestrator.generate.GenerateCommandLine;
import com.scottlogic.deg.orchestrator.guice.AllModule;
import picocli.CommandLine;

import java.io.IOException;


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
    public Integer call() throws IOException {
        Module container = new AllModule(this);
        Injector injector = Guice.createInjector(container);

        injector.getInstance(VisualiseExecute.class).execute();
        return 0;
    }
}
