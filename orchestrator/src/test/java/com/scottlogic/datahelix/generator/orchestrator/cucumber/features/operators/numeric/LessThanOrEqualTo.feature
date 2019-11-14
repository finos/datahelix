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
Feature: User can specify that a numeric value is lower than, or equal to, a specified threshold

  Background:
    Given the generation strategy is full
    And there is a nullable field foo

  Scenario: lessThanOrEqualTo run against a non contradicting not lessThanOrEqualToOrEqualTo should be successful
    Given foo is less than or equal to 5
    And foo is anything but less than or equal to 3
    And foo has type "integer"
    Then the following data should be generated:
      | foo  |
      | null |
      | 5    |
      | 4    |

  Scenario: not lessThanOrEqualTo run against a non contradicting not lessThanOrEqualToOrEqualTo should be successful
    Given foo is anything but less than or equal to 3
    And foo is anything but less than or equal to 3
    And foo has type "integer"
    And the generator can generate at most 5 rows
    Then the following data should be generated:
      | foo |
      | 4   |
      | 5   |
      | 6   |
      | 7   |
      | 8   |

  Scenario: lessThanOrEqualTo run against a contradicting not lessThanOrEqualToOrEqualTo should only only generate null
    Given foo is less than or equal to 3
    And foo is anything but less than or equal to 3
    And foo has type "integer"
    Then the following data should be generated:
      | foo  |
      | null |

  Scenario: not lessThanOrEqualTo run against a non contradicting granularTo should be successful
    Given foo is anything but less than or equal to 3
    And the generator can generate at most 5 rows
    And foo has type "decimal"
    And foo is granular to 1
    Then the following data should be generated:
      | foo |
      | 4   |
      | 5   |
      | 6   |
      | 7   |
      | 8   |
