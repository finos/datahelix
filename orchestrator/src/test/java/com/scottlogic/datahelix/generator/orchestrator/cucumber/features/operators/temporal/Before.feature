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
Feature: User can specify that a datetime date is lower than, but not equal to, a specified threshold

  Background:
    Given the generation strategy is full
    And there is a nullable field foo
    And foo has type "datetime"

#Alone

  Scenario: Running a 'before' request that specifies the lowest valid system date should only generate null data
    Given foo is before 0001-01-01T00:00:00.000Z
    Then the following data should be generated:
      | foo  |
      | null |

  Scenario: Running a 'before' request that specifies an invalid date should be unsuccessful
    Given foo is before 2019-30-30T00:00:00.000Z
    Then the profile is invalid with error containing "must be in ISO-8601 format"
    And no data is created

  Scenario: Running a 'before' request that specifies an invalid time should be unsuccessful
    Given foo is before 2019-01-01T24:00:00.000Z
    Then the profile is invalid with error containing "must be in ISO-8601 format"
    And no data is created

  Scenario: Running a 'before' request that specifies an invalid leap year should be unsuccessful
    Given foo is before 2019-02-29T00:00:00.000Z
    Then the profile is invalid with error containing "must be in ISO-8601 format"
    And no data is created

#before
  Scenario: not 'before' run against a non contradicting not 'before' should be successful
    Given foo is anything but before 2019-01-01T00:00:00.000Z
    And foo is anything but before 2018-01-01T00:00:00.000Z
    And the generator can generate at most 5 rows
    Then the following data should be generated:
      | foo                      |
      | 2019-01-01T00:00:00.000Z |
      | 2019-01-01T00:00:00.001Z |
      | 2019-01-01T00:00:00.002Z |
      | 2019-01-01T00:00:00.003Z |
      | 2019-01-01T00:00:00.004Z |

  Scenario: 'before' run against a contradicting not 'before' should only only generate string, numeric and null
    Given foo is before 2019-01-01T00:00:00.000Z
    And foo is anything but before 2019-01-01T00:00:00.000Z
    And the generator can generate at most 5 rows
    Then the following data should be generated:
      | foo  |
      | null |

#beforeOrAt
  Scenario: 'before' run against a non contradicting not 'beforeOrAt' should be successful
    Given foo is before 2019-01-01T00:00:00.000Z
    And foo is anything but before or at 2018-12-31T23:59:59.996Z
    And the generator can generate at most 5 rows
    Then the following data should be generated:
      | foo                      |
      | null                     |
      | 2018-12-31T23:59:59.999Z |
      | 2018-12-31T23:59:59.998Z |
      | 2018-12-31T23:59:59.997Z |

  Scenario: not 'before' run against a non contradicting 'beforeOrAt' should be successful
    Given foo is anything but before 2018-12-31T23:59:59.997Z
    And foo is before or at 2019-01-01T00:00:00.000Z
    And the generator can generate at most 5 rows
    Then the following data should be generated:
      | foo                      |
      | null                     |
      | 2019-01-01T00:00:00.000Z |
      | 2018-12-31T23:59:59.999Z |
      | 2018-12-31T23:59:59.998Z |
      | 2018-12-31T23:59:59.997Z |

  Scenario: not 'before' run against a non contradicting not 'beforeOrAt' should be successful
    Given foo is anything but before 2019-01-01T00:00:00.000Z
    And foo is anything but before or at 2018-01-01T00:00:00.000Z
    And the generator can generate at most 5 rows
    Then the following data should be generated:
      | foo                      |
      | 2019-01-01T00:00:00.000Z |
      | 2019-01-01T00:00:00.001Z |
      | 2019-01-01T00:00:00.002Z |
      | 2019-01-01T00:00:00.003Z |
      | 2019-01-01T00:00:00.004Z |

  Scenario: 'before' run against a contradicting not 'beforeOrAt' should only only generate string, numeric and null
    Given foo is before 2019-01-01T00:00:00.000Z
    And foo is anything but before or at 2019-01-02T00:00:00.000Z
    And the generator can generate at most 5 rows
    Then the following data should be generated:
      | foo  |
      | null |

  Scenario: Running a 'before' request that specifies the highest valid system date should be unsuccessful
    Given foo is before 0000-01-01T00:00:00.000Z
    Then the profile is invalid with error containing "Dates must be between 0001-01-01T00:00:00.000Z and 9999-12-31T23:59:59.999Z | Field: 'foo' | Constraint: 'before'"
