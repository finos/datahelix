Feature: User can specify that a field must be a valid ISIN (International Securities Identification Number)

Background:
     Given the generation strategy is full
       And there is a field foo
       And foo is of type "string"

Scenario: Running an 'aValid' request should be successful
     Given foo is a valid "ISIN"
       And foo is in set:
         | "GB0002634946" |
     Then the following data should be generated:
       | foo            |
       | null           |
       | "GB0002634946" |

Scenario: Running a 'aValid' request that includes the string "isin" should fail with an error message
     Given foo is a valid "isin"
     Then I am presented with an error message
       And no data is created

Scenario: Running a 'aValid' request that includes a non ISIN string should fail with an error message
     Given foo is a valid "BURRITO"
     Then I am presented with an error message
       And no data is created

Scenario: Running an 'aValid' request that includes a null entry ("") characters should fail with an error message
     Given foo is a valid ""
     Then I am presented with an error message
       And no data is created

Scenario: Running an 'aValid' request with the value property set to a null entry (null) should throw an error
     Given foo is a valid null
     Then the profile is invalid because "Couldn't recognise 'value' property, it must be set to a value"
       And no data is created

Scenario: aValid run against a non contradicting aValid should be successful
     Given foo is a valid "ISIN"
       And foo is a valid "ISIN"
       And foo is in set:
         | "GB0002634946" |
     Then the following data should be generated:
       | foo            |
       | null           |
       | "GB0002634946" |

Scenario: aValid run against a non contradicting not aValid should only generate null data
     Given foo is a valid "ISIN"
       And foo is anything but a valid "ISIN"
     Then the following data should be generated:
       | foo            |
       | null           |

Scenario: aValid run against a non contradicting greaterThan should be successful
     Given foo is a valid "ISIN"
       And foo is greater than 0
       And foo is in set:
         | "GB0002634946"  |
         | "GB000263494"   |
         | "GB00026349468" |
         | 1               |
     Then the following data should be generated:
       | foo            |
       | "GB0002634946" |
       | null           |

Scenario: aValid run against a non contradicting not greaterThan should be successful
     Given foo is a valid "ISIN"
       And foo is anything but greater than 0
       And foo is in set:
         | "GB0002634946"  |
         | "GB000263494"   |
         | "GB00026349468" |
         | 1               |
     Then the following data should be generated:
       | foo            |
       | "GB0002634946" |
       | null           |

Scenario: aValid run against a non contradicting greaterThanOrEqualTo should be successful
     Given foo is a valid "ISIN"
       And foo is greater than or equal to 0
       And foo is in set:
         | "GB0002634946"  |
         | "GB000263494"   |
         | "GB00026349468" |
         | 1               |
     Then the following data should be generated:
       | foo            |
       | "GB0002634946" |
       | null           |

Scenario: aValid run against a non contradicting not greaterThanOrEqualTo should be successful
     Given foo is a valid "ISIN"
       And foo is anything but greater than or equal to 0
       And foo is in set:
         | "GB0002634946"  |
         | "GB000263494"   |
         | "GB00026349468" |
         | 1               |
     Then the following data should be generated:
       | foo            |
       | "GB0002634946" |
       | null           |

Scenario: aValid run against a non contradicting lessThan should be successful
     Given foo is a valid "ISIN"
       And foo is less than 0
       And foo is in set:
         | "GB0002634946"  |
         | "GB000263494"   |
         | "GB00026349468" |
         | 1               |
     Then the following data should be generated:
       | foo            |
       | "GB0002634946" |
       | null           |

Scenario: aValid run against a non contradicting not lessThan should be successful
     Given foo is a valid "ISIN"
       And foo is anything but less than 0
       And foo is in set:
         | "GB0002634946"  |
         | "GB000263494"   |
         | "GB00026349468" |
         | 1               |
     Then the following data should be generated:
       | foo            |
       | "GB0002634946" |
       | null           |

Scenario: aValid run against a non contradicting lessThanOrEqualTo should be successful
     Given foo is a valid "ISIN"
       And foo is less than or equal to 0
       And foo is in set:
         | "GB0002634946"  |
         | "GB000263494"   |
         | "GB00026349468" |
         | 1               |
     Then the following data should be generated:
       | foo            |
       | "GB0002634946" |
       | null           |

Scenario: aValid run against a non contradicting not lessThanOrEqualTo should be successful
     Given foo is a valid "ISIN"
       And foo is anything but less than or equal to 0
       And foo is in set:
         | "GB0002634946"  |
         | "GB000263494"   |
         | "GB00026349468" |
         | 1               |
     Then the following data should be generated:
       | foo            |
       | "GB0002634946" |
       | null           |

Scenario: aValid run against a non contradicting granularTo should be successful
     Given foo is a valid "ISIN"
       And foo is granular to 1
       And foo is in set:
         | "GB0002634946"  |
         | "GB000263494"   |
         | "GB00026349468" |
         | 1               |
     Then the following data should be generated:
       | foo            |
       | "GB0002634946" |
       | null           |

Scenario: aValid run against a non contradicting not granularTo should be successful
     Given foo is a valid "ISIN"
       And foo is anything but granular to 1
       And foo is in set:
         | "GB0002634946"  |
         | "GB000263494"   |
         | "GB00026349468" |
         | 1               |
     Then the following data should be generated:
       | foo            |
       | "GB0002634946" |
       | null           |

Scenario: aValid run against a non contradicting after should be successful
    Given foo is a valid "ISIN"
    And foo is after 2019-01-01T00:00:00.000Z
    And foo is in set:
      | "GB0002634946"  |
      | "GB000263494"   |
      | "GB00026349468" |
      | 1               |
    Then the following data should be generated:
      | foo            |
      | "GB0002634946" |
      | null           |

Scenario: aValid run against a non contradicting not after should be successful
    Given foo is a valid "ISIN"
    And foo is anything but after 2019-01-01T00:00:00.000Z
    And foo is in set:
      | "GB0002634946"  |
      | "GB000263494"   |
      | "GB00026349468" |
      | 1               |
    Then the following data should be generated:
      | foo            |
      | "GB0002634946" |
      | null           |

Scenario: aValid run against a non contradicting afterOrAt should be successful
    Given foo is a valid "ISIN"
    And foo is after or at 2019-01-01T00:00:00.000Z
    And foo is in set:
      | "GB0002634946"  |
      | "GB000263494"   |
      | "GB00026349468" |
      | 1               |
    Then the following data should be generated:
      | foo            |
      | "GB0002634946" |
      | null           |

Scenario: aValid run against a non contradicting not afterOrAt should be successful
    Given foo is a valid "ISIN"
    And foo is anything but after or at 2019-01-01T00:00:00.000Z
    And foo is in set:
      | "GB0002634946"  |
      | "GB000263494"   |
      | "GB00026349468" |
      | 1               |
    Then the following data should be generated:
      | foo            |
      | "GB0002634946" |
      | null           |

Scenario: aValid run against a non contradicting before should be successful
    Given foo is a valid "ISIN"
    And foo is before 2019-01-01T00:00:00.000Z
    And foo is in set:
      | "GB0002634946"  |
      | "GB000263494"   |
      | "GB00026349468" |
      | 1               |
    Then the following data should be generated:
      | foo            |
      | "GB0002634946" |
      | null           |

Scenario: aValid run against a non contradicting not before should be successful
     Given foo is a valid "ISIN"
       And foo is anything but before 2019-01-01T00:00:00.000Z
       And foo is in set:
         | "GB0002634946"  |
         | "GB000263494"   |
         | "GB00026349468" |
         | 1               |
     Then the following data should be generated:
       | foo            |
       | "GB0002634946" |
       | null           |

Scenario: aValid run against a non contradicting beforeOrAt should be successful
     Given foo is a valid "ISIN"
       And foo is before or at 2019-01-01T00:00:00.000Z
       And foo is in set:
         | "GB0002634946"  |
         | "GB000263494"   |
         | "GB00026349468" |
         | 1               |
     Then the following data should be generated:
       | foo            |
       | "GB0002634946" |
       | null           |

Scenario: aValid run against a non contradicting not beforeOrAt should be successful
     Given foo is a valid "ISIN"
       And foo is anything but before or at 2019-01-01T00:00:00.000Z
       And foo is in set:
         | "GB0002634946"  |
         | "GB000263494"   |
         | "GB00026349468" |
         | 1               |
     Then the following data should be generated:
       | foo            |
       | "GB0002634946" |
       | null           |