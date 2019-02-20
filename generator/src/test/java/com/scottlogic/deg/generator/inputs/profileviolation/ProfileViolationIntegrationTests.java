package com.scottlogic.deg.generator.inputs.profileviolation;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.Profile;
import com.scottlogic.deg.generator.ProfileFields;
import com.scottlogic.deg.generator.Rule;
import com.scottlogic.deg.generator.builders.AndBuilder;
import com.scottlogic.deg.generator.builders.OrBuilder;
import com.scottlogic.deg.generator.builders.RuleBuilder;
import com.scottlogic.deg.generator.outputs.manifest.ManifestWriter;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.scottlogic.deg.generator.inputs.profileviolation.TypeEqualityHelper.assertListProfileTypeEquality;
import static com.shazam.shazamcrest.MatcherAssert.assertThat;
import static com.shazam.shazamcrest.matcher.Matchers.sameBeanAs;

/**
 * Defines integration tests for all classes involved in Profile Violation.
 */
public class ProfileViolationIntegrationTests {
    private IndividualRuleProfileViolator target;
    private Path tempOutputPath;

    @BeforeEach
    public void setUp() {
        ManifestWriter manifestWriter = new ManifestWriter();
        initTempOutputPath();
        IndividualConstraintRuleViolator ruleViolator = new IndividualConstraintRuleViolator(new ArrayList<>());
        target = new IndividualRuleProfileViolator(manifestWriter, tempOutputPath, ruleViolator);
    }

    @AfterEach
    public void tearDown() {
        deleteTempManifestFiles();
    }

    /**
     * Tests that the violator can take a profile
     *  Input: Profile with 2 fields foo and bar, 2 single atomic constraint rules affecting foo and bar
     *  Output: 2 Profiles, one with rule 1 negated and rule 2 unaffected, one with rule 1 unaffected and rule 2 negated
     */
    @Test
    public void violate_withTwoSimpleRuleProfile_producesTwoViolatedProfiles() {
        //Arrange
        Field fooField = new Field("foo");
        Rule rule1 = new RuleBuilder("Rule 1")
            .withLessThanConstraint(fooField, 100)
            .withLessThanConstraint(fooField, 200)
            .build();

        Rule rule2 = new RuleBuilder("Rule 2")
            .withGreaterThanConstraint(fooField, 10)
            .withGreaterThanConstraint(fooField, 15)
            .build();

        Profile inputProfile = new Profile(
            Collections.singletonList(fooField),
            Arrays.asList(rule1, rule2),
            "Profile 1");

        //Act
        List<Profile> violatedProfiles = target.violate(inputProfile);

        //Assert
        Rule violatedRule1 = new RuleBuilder("Rule 1")
            .withOrConstraint(new OrBuilder()
                .withAndConstraint(new AndBuilder()
                    .withLessThanConstraint(fooField, 100).violate()
                    .withLessThanConstraint(fooField, 200)
                )
                .withAndConstraint(new AndBuilder()
                    .withLessThanConstraint(fooField, 100)
                    .withLessThanConstraint(fooField, 200).violate()
                )
            )
            .build();

        Rule violatedRule2 = new RuleBuilder("Rule 2")
            .withOrConstraint(new OrBuilder()
                .withAndConstraint(new AndBuilder()
                    .withGreaterThanConstraint(fooField, 10).violate()
                    .withGreaterThanConstraint(fooField, 15)
                )
                .withAndConstraint(new AndBuilder()
                    .withGreaterThanConstraint(fooField, 10)
                    .withGreaterThanConstraint(fooField, 15).violate()
                )
            )
            .build();

        Profile violatedProfile1 = new ViolatedProfile(
            rule1,
            new ProfileFields(Collections.singletonList(fooField)),
            Arrays.asList(violatedRule1, rule2),
            "Profile 1 -- Violating: Rule 1"
        );
        Profile violatedProfile2 = new ViolatedProfile(
            rule2,
            new ProfileFields(Collections.singletonList(fooField)),
            Arrays.asList(rule1, violatedRule2),
            "Profile 1 -- Violating: Rule 2"
        );
        List<Profile> expectedViolatedProfiles = Arrays.asList(violatedProfile1, violatedProfile2);

        assertThat(
            "The violate method should have returned the correct list of correct shaped profiles",
            violatedProfiles,
            sameBeanAs(expectedViolatedProfiles)
        );
        assertListProfileTypeEquality(violatedProfiles, expectedViolatedProfiles);
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

    private void initTempOutputPath() {
        tempOutputPath = Paths.get(System.getProperty("java.io.tmpdir"), "tempManifestFiles");
        if (!Files.exists(tempOutputPath)) {
            try {
                Files.createDirectory(tempOutputPath);
            } catch (IOException e) {
                e.printStackTrace();
                Assert.fail("Unable to create directory for manifest file, failing test.");
            }
        }
    }
}
