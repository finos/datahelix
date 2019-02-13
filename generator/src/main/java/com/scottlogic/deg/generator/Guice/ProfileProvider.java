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
    private Profile profile;

    @Inject
    public ProfileProvider(GenerationConfigSource configSource, ProfileReader profileReader) {
        this.configSource = configSource;
        this.profileReader = profileReader;
    }

    @Override
    public Profile get() {
        if (profile != null) {
            return profile;
        }

        try {
            profile = profileReader.read(configSource.getProfileFile().toPath());
            return profile;
        } catch (IOException | InvalidProfileException e) {
            throw new RuntimeException(e);
        }
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }
}
