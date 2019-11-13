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
Feature: Generator can produce correct data for complex profiles.

  Scenario: Running a random strategy on a profile where there are hard to detect contradictions within or statements does not crash
    Given the generation strategy is Random
    And the generator can generate at most 5 rows
    And the following non nullable fields exist:
      | foo |
      | bar |
    And foo has type "integer"
    And bar has type "integer"
    And Any Of the next 2 constraints
    And All Of the next 3 constraints
    And Any Of the next 2 constraints
    And bar is equal to 1
    And bar is equal to 2
    And Any Of the next 2 constraints
    And foo is equal to 1
    And bar is equal to 3
    And Any Of the next 2 constraints
    And foo is equal to 2
    And bar is equal to 4
    And All Of the next 2 constraints
    And foo is equal to 10
    And bar is equal to 10
    Then the following data should be generated:
      | foo | bar |
      | 10  | 10  |
      | 10  | 10  |
      | 10  | 10  |
      | 10  | 10  |
      | 10  | 10  |
