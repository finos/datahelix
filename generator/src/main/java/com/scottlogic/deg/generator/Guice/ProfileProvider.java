package com.scottlogic.deg.generator.Guice;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.scottlogic.deg.generator.CommandLine.GenerateCommandLine;
import com.scottlogic.deg.generator.Profile;
import com.scottlogic.deg.generator.inputs.InvalidProfileException;
import com.scottlogic.deg.generator.inputs.ProfileReader;
import com.scottlogic.deg.generator.inputs.validation.ProfileValidator;

import java.io.IOException;

public class ProfileProvider implements Provider<Profile> {
    private final GenerateCommandLine commandLine;
    private final ProfileValidator validator;
    private Profile profile;

    @Inject
    public ProfileProvider(GenerateCommandLine commandLine, ProfileValidator validator) {
        this.commandLine = commandLine;
        this.validator = validator;
    }

    @Override
    public Profile get() {
        try {
            profile = new ProfileReader(validator).read(commandLine.getProfileFile().toPath());
        } catch (IOException | InvalidProfileException e) {
            e.printStackTrace();
        }
        return profile;
    }
}
