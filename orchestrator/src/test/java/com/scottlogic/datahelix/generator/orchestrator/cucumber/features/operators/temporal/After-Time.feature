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
Feature: User can specify that a time is after, but not equal to, a specified time

  Background:
    Given the generation strategy is full
    And there is a nullable field foo
    And foo has type "time"

  Scenario: 'After' valid date is successful for a single row
    Given foo is after 00:00:00.000
    And the generator can generate at most 1 rows
    Then the following data should be generated:
      | foo                      |
      | 00:00:00.001 |

  Scenario: 'After' valid date is successful
    Given foo is after 00:00:00.000
    And the generator can generate at most 4 rows
    Then the following data should be generated:
      | foo          |
      | 00:00:00.001 |
      | 00:00:00.002 |
      | 00:00:00.003 |
      | 00:00:00.004 |
    

  ### after ###

  Scenario: 'After' with a non-contradicting 'After' is successful
    Given foo is after 00:00:00.000
    And foo is after 00:00:00.000
    And the generator can generate at most 5 rows
    Then the following data should be generated:
      | foo          |
      | 00:00:00.001 |
      | 00:00:00.002 |
      | 00:00:00.003 |
      | 00:00:00.004 |
      | 00:00:00.005 |

  Scenario: 'After' with a non-contradicting 'Not After' is successful
    Given foo is after 00:00:00.000
    And foo is anything but after 00:00:00.005
    And the generator can generate at most 5 rows
    Then the following data should be generated:
      | foo                      |
      | 00:00:00.001 |
      | 00:00:00.002 |
      | 00:00:00.003 |
      | 00:00:00.004 |
      | 00:00:00.005 |

  Scenario: 'After' with a contradicting 'Not After' generates null
    Given foo is after 00:00:00.000
    And foo is anything but after 00:00:00.000
    Then the following data should be generated:
      | foo  |
      | null |

  Scenario: 'After' the max value is not successful
    Given foo is after 23:59:59.999
    And the generator can generate at most 5 rows
    Then the following data should be generated:
      | foo  |
      | null |

### afterOrAt ###

  Scenario: 'After' with a non-contradicting 'After Or At' is successful
    Given foo is after 00:00:00.000
    And foo is after or at 00:00:00.010
    And the generator can generate at most 5 rows
    Then the following data should be generated:
      | foo                      |
      | 00:00:00.010 |
      | 00:00:00.011 |
      | 00:00:00.012 |
      | 00:00:00.013 |
      | 00:00:00.014 |

  Scenario: 'After' with a non-contradicting 'Not After Or At' is successful
    Given foo is after 00:00:00.000
    And foo is anything but after or at 00:00:01.000
    And the generator can generate at most 5 rows
    Then the following data should be generated:
      | foo          |
      | 00:00:00.001 |
      | 00:00:00.002 |
      | 00:00:00.003 |
      | 00:00:00.004 |
      | 00:00:00.005 |

  Scenario: 'Not After' with a non-contradicting 'After Or At' is successful
    Given foo is anything but after 00:00:01.000
    And foo is after or at 00:00:00.000
    And the generator can generate at most 5 rows
    Then the following data should be generated:
      | foo                      |
      | 00:00:00.000 |
      | 00:00:00.001 |
      | 00:00:00.002 |
      | 00:00:00.003 |
      | 00:00:00.004 |

  Scenario: 'After' with a contradicting 'Not After Or At' only generates null
    Given foo is after 00:00:00.005
    And foo is anything but after or at 00:00:00.005
    And the generator can generate at most 5 rows
    Then the following data should be generated:
      | foo  |
      | null |

### before ###

  Scenario: 'After' with a non-contradicting 'Before' is successful
    Given foo is after 00:00:00.000
    And foo is before 00:00:00.005
    And the generator can generate at most 4 rows
    Then the following data should be generated:
      | foo          |
      | 00:00:00.001 |
      | 00:00:00.002 |
      | 00:00:00.003 |
      | 00:00:00.004 |

  Scenario: 'After' with a non-contradicting 'Not Before' is successful
    Given foo is after 00:00:00.000
    And foo is anything but before 00:00:00.000
    And the generator can generate at most 5 rows
    Then the following data should be generated:
      | foo          |
      | 00:00:00.001 |
      | 00:00:00.002 |
      | 00:00:00.003 |
      | 00:00:00.004 |
      | 00:00:00.005 |

  Scenario: 'Not After' with a non-contradicting 'Not Before' is successful
    Given foo is anything but after 00:00:00.020
    And foo is anything but before 00:00:00.005
    And the generator can generate at most 5 rows
    Then the following data should be generated:
      | foo                      |
      | 00:00:00.005 |
      | 00:00:00.006 |
      | 00:00:00.007 |
      | 00:00:00.008 |
      | 00:00:00.009 |

  Scenario: 'Not After' with contradicting 'Not Before' only generates null
    Given foo is anything but after 00:00:00.500
    And foo is anything but before 00:00:00.500
    Then the following data should be generated:
      | foo          |
      | 00:00:00.500 |
      | null         |

  Scenario: 'After' with a contradicting 'Before' only generates null
    Given foo is after 00:00:00.005
    And foo is before 00:00:00.005
    Then the following data should be generated:
      | foo  |
      | null |

### beforeOrAt ###

  Scenario: 'After' with a non-contradicting 'Before' is successful
    Given foo is after 00:00:00.000
    And foo is before or at 23:00:00.000
    And the generator can generate at most 5 rows
    Then the following data should be generated:
      | foo                      |
      | 00:00:00.001 |
      | 00:00:00.002 |
      | 00:00:00.003 |
      | 00:00:00.004 |
      | 00:00:00.005 |

  Scenario: 'After' with a non-contradicting 'Not Before Or At' is successful
    Given foo is after 00:00:00.000
    And foo is anything but before or at 00:00:00.000
    And the generator can generate at most 5 rows
    Then the following data should be generated:
      | foo                      |
      | 00:00:00.001 |
      | 00:00:00.002 |
      | 00:00:00.003 |
      | 00:00:00.004 |
      | 00:00:00.005 |

  Scenario: 'Not After' with a non-contradicting 'Not Before Or At' is successful
    Given foo is anything but after 23:00:00.000
    And foo is anything but before or at 00:00:00.000
    And the generator can generate at most 5 rows
    Then the following data should be generated:
      | foo                      |
      | 00:00:00.001 |
      | 00:00:00.002 |
      | 00:00:00.003 |
      | 00:00:00.004 |
      | 00:00:00.005 |

  Scenario: 'After' with a contradicting 'Before Or At' only generates null
    Given foo is after 00:00:00.000
    And foo is before or at 00:00:00.000
    Then the following data should be generated:
      | foo  |
      | null |

  Scenario: 'Not After' with a contradicting not 'Before Or At' only generates null
    Given foo is anything but after 00:00:00.000
    And foo is anything but before or at 00:00:00.000
    Then the following data should be generated:
      | foo  |
      | null |

  Scenario: 'after' run with maximum possible date should only generate null
    Given foo is after 23:59:59.999
    Then the following data should be generated:
      | foo  |
      | null |
