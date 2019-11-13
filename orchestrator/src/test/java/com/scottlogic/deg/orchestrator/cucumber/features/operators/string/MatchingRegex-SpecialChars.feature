# Copyright 2019 Scott Logic Ltd
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
Feature: Whilst including non-latin characters, user can specify that a value either matches or contains a specified regex

  Background:
    Given the generation strategy is full
    And there is a non nullable field foo
    And foo has type "string"

  Scenario: Running a 'matchingRegex' request that includes special characters (emoji) only should be successful
    Given foo is matching regex /[😁-😘]{1}/
    Then the following data should be generated:
      | foo |

