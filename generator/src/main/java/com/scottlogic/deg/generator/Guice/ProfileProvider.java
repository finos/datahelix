package com.scottlogic.deg.generator.Guice;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.scottlogic.deg.generator.CommandLine.GenerateCommandLine;
import com.scottlogic.deg.generator.Profile;
import com.scottlogic.deg.generator.inputs.InvalidProfileException;
import com.scottlogic.deg.generator.inputs.ProfileReader;

import java.io.IOException;

public class ProfileProvider implements Provider<Profile> {
    private final GenerateCommandLine commandLine;
    private Profile profile;

    @Inject
    public ProfileProvider(GenerateCommandLine commandLine) {
        this.commandLine = commandLine;
    }

    @Override
    public Profile get() {
        try {
            profile = new ProfileReader().read(commandLine.getProfileFile().toPath());
        } catch (IOException | InvalidProfileException e) {
            e.printStackTrace();
        }
        return profile;
    }
}
