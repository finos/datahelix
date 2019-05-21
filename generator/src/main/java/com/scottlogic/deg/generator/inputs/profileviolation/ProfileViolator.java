package com.scottlogic.deg.generator.inputs.profileviolation;

import com.scottlogic.deg.common.profile.Profile;
import com.scottlogic.deg.common.profile.ViolatedProfile;

import java.io.IOException;
import java.util.List;

/**
 * Defines an interface for a profile validator, a class which has a 
 * specific implementation of how to violate an input profile object.
 */
public interface ProfileViolator {
    /**
     * Violate takes a profile and produces a list of violated profiles
     * according to the violator's specific violation rules.
     * @param profile Input profile.
     * @return List of profile objects that represent the multiple violations.
     */
    List<ViolatedProfile> violate(Profile profile) throws IOException;
}
