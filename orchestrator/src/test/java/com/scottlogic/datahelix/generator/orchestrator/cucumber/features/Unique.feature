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
Feature: As a user
  I want to be able to specify that a field is unique
  So that I can avoid duplication issues for certain fields

  Background:
    Given there is a non nullable field unique
    And unique is unique
    And unique has type "integer"
    And unique is greater than 0
    And the combination strategy is minimal
    And the generator can generate at most 5 rows

  Scenario: The one where there are 2 unique fields and 1 non unique field
    Given there is a non nullable field foo
    And foo is unique
    And foo has type "integer"
    And foo is greater than 0
    And there is a non nullable field bar
    And bar has type "string"
    And bar is equal to "not unique"
    And the generation strategy is random
    Then the following data should be generated:
      | unique | foo | bar          |
      | 1      | 1   | "not unique" |
      | 2      | 2   | "not unique" |
      | 3      | 3   | "not unique" |
      | 4      | 4   | "not unique" |
      | 5      | 5   | "not unique" |

  Scenario: The one where the range is exceeded
    Given unique is less than 3
    And there is a non nullable field foo
    And foo has type "integer"
    And foo is greater than 0
    Then the following data should be generated:
      | unique | foo |
      | 1      | 1   |
      | 2      | 2   |

  Scenario: The one where combination strategy is minimal
    And there is a non nullable field foo
    And foo has type "integer"
    And foo is greater than 0
    And foo is less than 4
    And there is a non nullable field bar
    And bar has type "integer"
    And bar is greater than 0
    And bar is less than 4
    Then the following data should be generated:
      | unique | foo | bar |
      | 1      | 1   | 1   |
      | 2      | 2   | 2   |
      | 3      | 3   | 3   |
      | 4      | 3   | 3   |
      | 5      | 3   | 3   |

  Scenario: The one where combination strategy is exhaustive
    Given the combination strategy is exhaustive
    And there is a non nullable field foo
    And foo has type "integer"
    And foo is greater than 0
    And foo is less than 4
    And there is a non nullable field bar
    And bar has type "integer"
    And bar is greater than 0
    And bar is less than 4
    Then the profile is invalid because "Unique fields do not work when not using Minimal combination strategy"
    And no data is created
