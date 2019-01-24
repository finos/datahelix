package com.scottlogic.deg.generator.Guice;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.scottlogic.deg.generator.Profile;
import com.scottlogic.deg.generator.generation.GenerationConfigSource;
import com.scottlogic.deg.generator.inputs.InvalidProfileException;
import com.scottlogic.deg.generator.inputs.JsonProfileReader;

import java.io.IOException;

public class ProfileProvider implements Provider<Profile> {
    private final GenerationConfigSource commandLine;
    private final JsonProfileReader profileReader;

    @Inject
    public ProfileProvider(GenerationConfigSource commandLine, JsonProfileReader profileReader) {
        this.commandLine = commandLine;
        this.profileReader = profileReader;
    }

    @Override
    public Profile get() {
        try {
            return this.profileReader.read(commandLine.getProfileFile().toPath());
        } catch (IOException | InvalidProfileException e) {
            throw new RuntimeException(e);
        }
    }
}
