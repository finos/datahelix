package com.scottlogic.deg.generator.inputs.profileviolation;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.util.Modules;
import com.scottlogic.deg.common.profile.Field;
import com.scottlogic.deg.generator.config.details.CombinationStrategyType;
import com.scottlogic.deg.generator.config.details.DataGenerationType;
import com.scottlogic.deg.generator.config.details.TreeWalkerType;
import com.scottlogic.deg.generator.guice.BaseModule;
import com.scottlogic.deg.generator.guice.TestModule;
import com.scottlogic.deg.common.profile.Profile;
import com.scottlogic.deg.common.profile.ProfileFields;
import com.scottlogic.deg.common.profile.Rule;
import com.scottlogic.deg.generator.builders.AndBuilder;
import com.scottlogic.deg.generator.builders.OrBuilder;
import com.scottlogic.deg.generator.builders.RuleBuilder;
import com.scottlogic.deg.generator.generation.TestGenerationConfigSource;
import com.scottlogic.deg.generator.violations.ViolatedProfile;
import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

import static com.scottlogic.deg.generator.inputs.profileviolation.TypeEqualityHelper.assertProfileListsAreEquivalent;

/**
 * Defines integration tests for all classes involved in Profile Violation including Guice creation.
 */
public class ProfileViolationIntegrationTests {
    private IndividualRuleProfileViolator target;
    private Path tempOutputPath;

    @BeforeEach
    public void setUp() {
        TestGenerationConfigSource configSource = new TestGenerationConfigSource(
            DataGenerationType.FULL_SEQUENTIAL,
            TreeWalkerType.REDUCTIVE,
            CombinationStrategyType.EXHAUSTIVE
        );
        configSource.outputPath = initTempOutputPath();

        Module testModule = Modules
            .override(new BaseModule(configSource))
            .with(new TestModule(configSource));

        Injector injector = Guice.createInjector(testModule);

        target = injector.getInstance(IndividualRuleProfileViolator.class);
    }

    @AfterEach
    public void tearDown() {
        deleteTempManifestFiles();
    }

    /**
     * Tests that the violator can be created by Guice with all units working as intended.
     * Note that this test includes writing a manifest to the temporary directory.
     */
    @Test
    public void violate_fullIntegrationTest() throws IOException {
        //Arrange
        Field fooField = new Field("foo");
        Rule rule1 = new RuleBuilder("Rule 1")
            .withLessThanConstraint(fooField, 100)
            .withLessThanConstraint(fooField, 200)
            .build();

        Profile inputProfile = new Profile(
            Collections.singletonList(fooField),
            Collections.singletonList(rule1),
            "Profile 1");

        //Act
        List<Profile> violatedProfiles = target.violate(inputProfile);

        //Assert
        Rule violatedRule1 = new RuleBuilder("Rule 1")
            .withOrConstraint(new OrBuilder()
                .withAndConstraint(new AndBuilder()
                    .withLessThanConstraint(fooField, 100).negate().wrapAtomicWithViolate()
                    .withLessThanConstraint(fooField, 200)
                )
                .withAndConstraint(new AndBuilder()
                    .withLessThanConstraint(fooField, 100)
                    .withLessThanConstraint(fooField, 200).negate().wrapAtomicWithViolate()
                )
            )
            .build();

        Profile violatedProfile1 = new ViolatedProfile(
            rule1,
            new ProfileFields(Collections.singletonList(fooField)),
            Collections.singletonList(violatedRule1),
            "Profile 1 -- Violating: Rule 1"
        );

        List<Profile> expectedViolatedProfiles = Collections.singletonList(violatedProfile1);

        assertProfileListsAreEquivalent(expectedViolatedProfiles, violatedProfiles);
        Assert.assertTrue("Unable to find manifest.json file.", tempOutputPath.resolve("manifest.json").toFile().isFile());
    }

    private void deleteTempManifestFiles() {
        File tempFolder = tempOutputPath.toFile();
        if (tempFolder != null) {
            File[] tempFiles = tempFolder.listFiles();
            if (tempFiles != null) {
                for (File file : tempFiles) {
                    Assert.assertTrue("Unable to delete manifest file " + file.toString(), file.delete());
                }
            }
            Assert.assertTrue("Unable to delete temp manifest folder", tempFolder.delete());
        }
    }

    private Path initTempOutputPath() {
        tempOutputPath = Paths.get(System.getProperty("java.io.tmpdir"), "tempManifestFiles");
        if (!Files.exists(tempOutputPath)) {
            try {
                Files.createDirectory(tempOutputPath);
            } catch (IOException e) {
                e.printStackTrace();
                Assert.fail("Unable to create directory for manifest file, failing test.");
            }
        }
        return tempOutputPath;
    }
}
