package com.scottlogic.deg.generator.Guice;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.scottlogic.deg.generator.Profile;
import com.scottlogic.deg.generator.generation.GenerationConfigSource;
import com.scottlogic.deg.generator.inputs.InvalidProfileException;
import com.scottlogic.deg.generator.inputs.ProfileReader;

import java.io.IOException;

/**
 * @Deprecated as Profile should not be passed around as a dependency
 *
 * Note: this is not a provider, it is a cache of the profile from the profile reader
 *
 * */
@Deprecated
public class ProfileProvider {
    private final GenerationConfigSource configSource;
    private final ProfileReader profileReader;
    private Profile profile;

    @Inject
    public ProfileProvider(GenerationConfigSource configSource, ProfileReader profileReader) {
        this.configSource = configSource;
        this.profileReader = profileReader;
    }

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
}
