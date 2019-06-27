package com.scottlogic.deg.orchestrator.violate;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.scottlogic.deg.orchestrator.generate.GenerateCommandLine;
import com.scottlogic.deg.profile.v0_1.AtomicConstraintType;
import picocli.CommandLine;

import java.io.IOException;
import java.util.List;

@CommandLine.Command(
    name = "violate",
    description = "Produces a decision tree in DOT format for the specified profile.",
    descriptionHeading = "%nDescription:%n",
    parameterListHeading = "%nParameters:%n",
    optionListHeading = "%nOptions:%n",
    abbreviateSynopsis = true)
public class ViolateCommandLine extends GenerateCommandLine implements ViolateConfigSource {

    @Override
    public Integer call() throws IOException {
        Module container = new ViolateModule(this);
        Injector injector = Guice.createInjector(container);

        injector.getInstance(ViolateExecute.class).execute();
        return 0;
    }

    @CommandLine.Option(
        names = {"--dont-violate"},
        arity = "0..",
        description = "Choose types of constraint should not be violated")
    private List<AtomicConstraintType> constraintsToNotViolate;

    public List<AtomicConstraintType> getConstraintsToNotViolate() {
        return constraintsToNotViolate;
    }

}
