/*
 * Copyright 2019 Scott Logic Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
