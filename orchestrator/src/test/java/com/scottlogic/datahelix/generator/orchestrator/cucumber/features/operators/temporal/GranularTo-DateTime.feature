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
Feature: User can specify that datetime fields are granular to a certain unit

  Background:
    Given the generation strategy is full
    And there is a non nullable field foo
    And foo has type "datetime"

  Scenario Outline: User is able to specify supported temporal granularities with after operator
    Given foo is granular to <unit>
    And foo is after 2000-01-01T00:00:00.000Z
    And the generator can generate at most 1 rows
    Then the following data should be generated:
      | foo      |
      | <output> |
    Examples:
      | unit      | output                   |
      | "millis"  | 2000-01-01T00:00:00.001Z |
      | "seconds" | 2000-01-01T00:00:01.000Z |
      | "minutes" | 2000-01-01T00:01:00.000Z |
      | "hours"   | 2000-01-01T01:00:00.000Z |
      | "days"    | 2000-01-02T00:00:00.000Z |
      | "months"  | 2000-02-01T00:00:00.000Z |
      | "years"   | 2001-01-01T00:00:00.000Z |

  Scenario Outline: User is able to specify supported temporal granularities with after or at operator
    Given foo is granular to <unit>
    And foo is after or at 2000-01-01T00:00:00.000Z
    And the generator can generate at most 2 rows
    Then the following data should be generated:
      | foo       |
      | <output1> |
      | <output2> |
    Examples:
      | unit      | output1                  | output2                  |
      | "millis"  | 2000-01-01T00:00:00.000Z | 2000-01-01T00:00:00.001Z |
      | "seconds" | 2000-01-01T00:00:00.000Z | 2000-01-01T00:00:01.000Z |
      | "minutes" | 2000-01-01T00:00:00.000Z | 2000-01-01T00:01:00.000Z |
      | "hours"   | 2000-01-01T00:00:00.000Z | 2000-01-01T01:00:00.000Z |
      | "days"    | 2000-01-01T00:00:00.000Z | 2000-01-02T00:00:00.000Z |
      | "months"  | 2000-01-01T00:00:00.000Z | 2000-02-01T00:00:00.000Z |
      | "years"   | 2000-01-01T00:00:00.000Z | 2001-01-01T00:00:00.000Z |

  Scenario Outline: User is able to specify supported temporal granularities with before operator
    Given foo is granular to <unit>
    And foo is before 2002-02-02T01:01:01.001Z
    And the generator can generate at most 2 rows
    Then the following data should be generated:
      | foo       |
      | <output1> |
      | <output2> |
    Examples:
      | unit      | output1                  | output2                  |
      | "millis"  | 0001-01-01T00:00:00.000Z | 0001-01-01T00:00:00.001Z |
      | "seconds" | 0001-01-01T00:00:00.000Z | 0001-01-01T00:00:01.000Z |
      | "minutes" | 0001-01-01T00:00:00.000Z | 0001-01-01T00:01:00.000Z |
      | "hours"   | 0001-01-01T00:00:00.000Z | 0001-01-01T01:00:00.000Z |
      | "days"    | 0001-01-01T00:00:00.000Z | 0001-01-02T00:00:00.000Z |
      | "months"  | 0001-01-01T00:00:00.000Z | 0001-02-01T00:00:00.000Z |
      | "years"   | 0001-01-01T00:00:00.000Z | 0002-01-01T00:00:00.000Z |

  Scenario Outline: User is able to specify supported temporal granularities with before or at operator
    Given foo is granular to <unit>
    And foo is before or at 2002-02-02T01:01:01.001Z
    And the generator can generate at most 2 rows
    Then the following data should be generated:
      | foo       |
      | <output1> |
      | <output2> |
    Examples:
      | unit      | output1                  | output2                  |
      | "millis"  | 0001-01-01T00:00:00.000Z | 0001-01-01T00:00:00.001Z |
      | "seconds" | 0001-01-01T00:00:00.000Z | 0001-01-01T00:00:01.000Z |
      | "minutes" | 0001-01-01T00:00:00.000Z | 0001-01-01T00:01:00.000Z |
      | "hours"   | 0001-01-01T00:00:00.000Z | 0001-01-01T01:00:00.000Z |
      | "days"    | 0001-01-01T00:00:00.000Z | 0001-01-02T00:00:00.000Z |
      | "months"  | 0001-01-01T00:00:00.000Z | 0001-02-01T00:00:00.000Z |
      | "years"   | 0001-01-01T00:00:00.000Z | 0002-01-01T00:00:00.000Z |

  Scenario: Applying two valid datetime granularTo constraints generates data that matches both (coarser)
    Given foo is granular to "millis"
    And foo is granular to "seconds"
    And foo is after 2000-01-01T00:00:00.000Z
    And the generator can generate at most 1 rows
    Then the following data should be generated:
      | foo                      |
      | 2000-01-01T00:00:01.000Z |

  Scenario: The one where a user can specify after working day granularity
    Given foo is granular to "working days"
    And foo is after 2019-10-04T00:00:00.000Z
    And the generator can generate at most 1 rows
    Then the following data should be generated:
      | foo                      |
      | 2019-10-07T00:00:00.000Z |

  Scenario: The one where a user can specify before working day granularity
    Given foo is granular to "working days"
    And foo is after 2019-10-03T00:00:00.000Z
    And foo is before 2019-10-07T00:00:00.000Z
    Then the following data should be generated:
      | foo                      |
      | 2019-10-04T00:00:00.000Z |

  @ignore #pending development of #1426 - Make datetime friendly to use in the profile
  Scenario Outline: The one where the datetime format is relaxed
    Given foo is granular to <unit>
    And foo is equal to <constraintDate>
    And the generator can generate at most 1 rows
    Then the following data should be generated:
      | foo      |
      | <output> |
    Examples:
      | unit      | constraintDate           | output                   |
      | "millis"  | 2000-01-01T00:00:00.000Z | 2000-01-01T00:00:00.000Z |
      | "seconds" | 2000-01-01T00:00:00      | 2000-01-01T00:00:00      |
      | "minutes" | 2000-01-01T00:00         | 2000-01-01T00:00         |
      | "hours"   | 2000-01-01T00            | 2000-01-01T00            |
      | "days"    | 2000-01-01T              | 2000-01-01T              |
      | "months"  | 2000-01                  | 2000-01                  |
      | "years"   | 2000                     | 2000                     |

  Scenario: Applying an invalid datetime granularTo constraint fails with an appropriate error
    Given foo is granular to "decades"
    Then the profile is invalid with error "Granularity 'decades' is not supported | Field: 'foo' | Constraint: 'granularTo'"
    And no data is created
