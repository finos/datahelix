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

package com.scottlogic.deg.profile.commands;

import com.scottlogic.datahelix.generator.common.commands.Command;
import com.scottlogic.datahelix.generator.core.profile.Profile;
import com.scottlogic.deg.profile.dtos.ProfileDTO;

public class CreateProfile extends Command<Profile>
{
    public final ProfileDTO profileDTO;

    public CreateProfile(ProfileDTO profileDTO)
    {
        this.profileDTO = profileDTO;
    }
}
