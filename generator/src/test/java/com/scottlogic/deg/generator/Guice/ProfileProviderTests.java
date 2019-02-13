package com.scottlogic.deg.generator.Guice;

import com.scottlogic.deg.generator.Profile;
import com.scottlogic.deg.generator.generation.GenerationConfigSource;
import com.scottlogic.deg.generator.inputs.InvalidProfileException;
import com.scottlogic.deg.generator.inputs.JsonProfileReader;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.mockito.Mockito.*;

public class ProfileProviderTests {

    private GenerationConfigSource configSource = mock(GenerationConfigSource.class);
    private JsonProfileReader profileReader = mock(JsonProfileReader.class);
    private Profile profile = mock(Profile.class);
    private File file = mock(File.class);
    private ProfileProvider profileProvider = new ProfileProvider(configSource, profileReader);

    @Test
    public void get_profileIsNull_profileReadIsCalled() throws IOException, InvalidProfileException {
        //Arrange
        profileProvider.setProfile(null);
        when(configSource.getProfileFile()).thenReturn(file);
        when(profileReader.read(configSource.getProfileFile().toPath())).thenReturn(profile);

        //Act
        profileProvider.get();

        //Assert
        verify(profileReader, times(1)).read(configSource.getProfileFile().toPath());
    }

    @Test
    public void get_profileIsNotNull_profileReadIsNotCalled() throws IOException, InvalidProfileException {
        //Arrange
        profileProvider.setProfile(profile);
        when(configSource.getProfileFile()).thenReturn(file);
        when(profileReader.read(configSource.getProfileFile().toPath())).thenReturn(profile);

        //Act
        profileProvider.get();

        //Assert
        verify(profileReader, times(0)).read(configSource.getProfileFile().toPath());
    }

}
