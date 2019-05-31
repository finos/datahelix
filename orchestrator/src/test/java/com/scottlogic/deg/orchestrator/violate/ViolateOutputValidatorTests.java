package com.scottlogic.deg.orchestrator.violate;

import com.scottlogic.deg.common.profile.Profile;
import com.scottlogic.deg.common.profile.Rule;
import com.scottlogic.deg.generator.outputs.targets.OutputTargetValidationException;
import com.scottlogic.deg.generator.utils.FileUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ViolateOutputValidatorTests {
    @Mock
    private Path mockFilePath;
    @Mock
    private FileUtils mockFileUtils;
    @Mock
    private Profile mockProfile;

    @Test
    public void validate_generateViolationOutputFileNotDir_isNotValid(){
        when(mockFileUtils.exists(mockFilePath)).thenReturn(true);
        when(mockFileUtils.isDirectory(mockFilePath)).thenReturn(false);
        ViolateOutputValidator outputTarget = new ViolateOutputValidator(false, mockFilePath, mockFileUtils);

        assertThrows(OutputTargetValidationException.class, ()->outputTarget.validate(mockProfile),"Expected OutputTargetValidationException to throw, but didn't");
    }

    @Test
    public void validate_generateViolationOutputFolderEmpty_doesntThrow(){
        Set<Rule> rules = new HashSet<>();
        Rule mockRule = mock(Rule.class);
        rules.add(mockRule);
        when(mockFileUtils.exists(mockFilePath)).thenReturn(true);
        when(mockFileUtils.isDirectory(mockFilePath)).thenReturn(true);
        when(mockProfile.getRules()).thenReturn(rules);
        when(mockFileUtils.isDirectoryEmpty(mockFilePath, 1)).thenReturn(true);
        ViolateOutputValidator outputTarget = new ViolateOutputValidator(false, mockFilePath, mockFileUtils);

        assertDoesNotThrow(() ->outputTarget.validate(mockProfile),"Expected no exception, but one was thrown");
    }

}
