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
Feature: running datetimes related to otherfield datetimes for multiple fields

  Background:
    Given the generation strategy is full
    And there is a non nullable field foobar
    And foobar has type "datetime"
    And there is a non nullable field foo
    And foo has type "datetime"
    And there is a non nullable field bar
    And bar has type "datetime"
    And the combination strategy is exhaustive

  Scenario: Running a "before" and "after" constraint
    Given the generator can generate at most 1 rows
    And foobar is before field bar
    And foobar is after field foo
    Then the following data should be generated:
      | foobar                   | foo                      | bar                      |
      | 0001-01-01T00:00:00.001Z | 0001-01-01T00:00:00.000Z | 0001-01-01T00:00:00.002Z |

  Scenario: Running a "before" and "equalTo" constraint
    Given the generator can generate at most 1 rows
    And foobar is before field bar
    And foobar is equal to field foo
    Then the following data should be generated:
      | foobar                   | foo                      | bar                      |
      | 0001-01-01T00:00:00.000Z | 0001-01-01T00:00:00.000Z | 0001-01-01T00:00:00.001Z |

  Scenario: Running an "after" and "equalTo" constraint
    Given the generator can generate at most 1 rows
    And foobar is after field bar
    And foobar is equal to field foo
    Then the following data should be generated:
      | foobar                   | foo                      | bar                      |
      | 0001-01-01T00:00:00.001Z | 0001-01-01T00:00:00.001Z | 0001-01-01T00:00:00.000Z |

  Scenario: Running two "after" constraints
    Given the generator can generate at most 1 rows
    And foobar is after field bar
    And foobar is after field foo
    Then the following data should be generated:
      | foobar                   | foo                      | bar                      |
      | 0001-01-01T00:00:00.001Z | 0001-01-01T00:00:00.000Z | 0001-01-01T00:00:00.000Z |

  Scenario: Running two "before" constraints switched other field constraints
    Given the generator can generate at most 1 rows
    And bar is before field foobar
    And foo is before field foobar
    Then the following data should be generated:
      | foobar                   | foo                      | bar                      |
      | 0001-01-01T00:00:00.001Z | 0001-01-01T00:00:00.000Z | 0001-01-01T00:00:00.000Z |

  Scenario: Running two "before" constraints
    Given the generator can generate at most 1 rows
    And foobar is before field bar
    And foobar is before field foo
    Then the following data should be generated:
      | foobar                   | foo                      | bar                      |
      | 0001-01-01T00:00:00.000Z | 0001-01-01T00:00:00.001Z | 0001-01-01T00:00:00.001Z |

  Scenario: Running two "after" switched other field constraints
    Given the generator can generate at most 1 rows
    And bar is after field foobar
    And foo is after field foobar
    Then the following data should be generated:
      | foobar                   | foo                      | bar                      |
      | 0001-01-01T00:00:00.000Z | 0001-01-01T00:00:00.001Z | 0001-01-01T00:00:00.001Z |

  Scenario: Running linked "after" constraint
    Given the generator can generate at most 1 rows
    And foobar is after field bar
    And bar is after field foo
    Then the following data should be generated:
      | foobar                   | foo                      | bar                      |
      | 0001-01-01T00:00:00.002Z | 0001-01-01T00:00:00.000Z | 0001-01-01T00:00:00.001Z |

  Scenario: Running linked "before" constraint
    Given the generator can generate at most 1 rows
    And foobar is before field bar
    And bar is before field foo
    Then the following data should be generated:
      | foobar                   | foo                      | bar                      |
      | 0001-01-01T00:00:00.000Z | 0001-01-01T00:00:00.002Z | 0001-01-01T00:00:00.001Z |

  Scenario: Running linked "before" constraint with lower limit
    Given the generator can generate at most 1 rows
    And foobar is before 2019-01-01T00:00:00.000Z
    And foobar is before field bar
    And bar is before field foo
    Then the following data should be generated:
      | foobar                   | foo                      | bar                      |
      | 0001-01-01T00:00:00.000Z | 0001-01-01T00:00:00.002Z | 0001-01-01T00:00:00.001Z |

  Scenario: Running linked "equalTo" constraint
    Given the generator can generate at most 1 rows
    And foobar is equal to field bar
    And bar is equal to field foo
    Then the following data should be generated:
      | foobar                   | foo                      | bar                      |
      | 0001-01-01T00:00:00.000Z | 0001-01-01T00:00:00.000Z | 0001-01-01T00:00:00.000Z |
