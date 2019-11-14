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
Feature: User can specify a datetime in a particular format

  Background:
    Given the generation strategy is full
    And there is a nullable field foo
    And foo has type "datetime"

  Scenario: 'EqualTo' valid date time is successful for date time specified fully
    Given foo is equal to 2018-09-01T00:00:00.000Z
    And the generator can generate at most 1 rows
    Then the following data should be generated:
      | foo                      |
      | 2018-09-01T00:00:00.000Z |

  Scenario: 'EqualTo' valid date time is successful for date time specified to seconds
    Given foo is equal to 2018-09-01T00:00:00
    And the generator can generate at most 1 rows
    Then the following data should be generated:
      | foo                      |
      | 2018-09-01T00:00:00.000Z |

  Scenario: 'EqualTo' valid date time is successful for date time specified to minutes
    Given foo is equal to 2018-09-01T00:00
    And the generator can generate at most 1 rows
    Then the following data should be generated:
      | foo                      |
      | 2018-09-01T00:00:00.000Z |
