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
Feature: User can specify that decimal fields are granular to a certain number of decimal places

  Background:
    Given the generation strategy is full
    And there is a non nullable field foo
    And foo has type "decimal"

  Scenario: User requires to create a numeric field with data values that include a decimal value to one decimal point
    Given foo is granular to 0.1
    And foo is greater than or equal to 0
    And foo is less than or equal to 1
    Then the following data should be generated:
      | foo |
      | 0   |
      | 0.1 |
      | 0.2 |
      | 0.3 |
      | 0.4 |
      | 0.5 |
      | 0.6 |
      | 0.7 |
      | 0.8 |
      | 0.9 |
      | 1.0 |

  Scenario: User requires to create a numeric field with negative data values that include a decimal value to one decimal point
    Given foo is granular to 0.1
    And foo is less than or equal to 0
    And foo is greater than or equal to -1
    Then the following data should be generated:
      | foo  |
      | 0    |
      | -0.1 |
      | -0.2 |
      | -0.3 |
      | -0.4 |
      | -0.5 |
      | -0.6 |
      | -0.7 |
      | -0.8 |
      | -0.9 |
      | -1   |

  Scenario: Running granularTo against a non contradicting granularTo should be successful
    Given foo is granular to 1
    And foo is granular to 1
    And foo is greater than 0
    And the generator can generate at most 5 rows
    Then the following data should be generated:
      | foo |
      | 1   |
      | 2   |
      | 3   |
      | 4   |
      | 5   |

  Scenario: Running granularTo against a non contradicting granularTo should be successful
    Given foo is granular to 1
    And foo is granular to 0.1
    And foo is greater than 0
    And the generator can generate at most 5 rows
    Then the following data should be generated:
      | foo |
      | 1   |
      | 2   |
      | 3   |
      | 4   |
      | 5   |
          ### related field
  Scenario: The one where a user can specify that one decimal number should be greater than another decimal number
    Given foo is granular to 0.1
    And the combination strategy is exhaustive
    And foo is greater than or equal to 1
    And there is a non nullable field bar
    And bar has type "decimal"
    And bar is granular to 0.1
    And bar is less than 1.4
    And bar is greater than or equal to 1
    And foo is less than 1.4
    And bar is greater than field foo
    Then the following data should be generated:
      | foo | bar |
      | 1   | 1.1 |
      | 1   | 1.2 |
      | 1   | 1.3 |
      | 1.1 | 1.2 |
      | 1.1 | 1.3 |
      | 1.2 | 1.3 |
