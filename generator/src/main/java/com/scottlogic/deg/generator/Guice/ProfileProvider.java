package com.scottlogic.deg.generator.Guice;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.scottlogic.deg.generator.Profile;
import com.scottlogic.deg.generator.generation.GenerationConfigSource;
import com.scottlogic.deg.generator.inputs.InvalidProfileException;
import com.scottlogic.deg.generator.inputs.ProfileReader;

import java.io.IOException;

public class ProfileProvider implements Provider<Profile> {
    private final GenerationConfigSource configSource;
    private final ProfileReader profileReader;

    @Inject
    public ProfileProvider(GenerationConfigSource configSource, ProfileReader profileReader) {
        this.configSource = configSource;
        this.profileReader = profileReader;
    }

    @Override
    public Profile get() {
        try {
            return profileReader.read(configSource.getProfileFile().toPath());
        } catch (IOException | InvalidProfileException e) {
            throw new RuntimeException(e);
        }
    }
}
