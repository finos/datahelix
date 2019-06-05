Feature: User can specify that a field must be a valid ISIN (International Securities Identification Number)

  Background:
    Given the generation strategy is full
    And there is a field foo
    And foo is of type "string"

  Scenario: Running an 'aValid' request with the value "ISIN" should be successful
    Given foo is a valid "ISIN"
    And foo is in set:
      | "GB0002634946" |
    Then the following data should be generated:
      | foo            |
      | null           |
      | "GB0002634946" |

  Scenario: Running a 'aValid' request that includes the string "isin" should fail with an error message
    Given foo is a valid "isin"
    Then the profile is invalid because "No enum constant com.scottlogic.deg.common.profile.constraints.atomic.StandardConstraintTypes.isin"
    And no data is created

  Scenario: Running an 'aValid' request with the value "SEDOL" should be successful
    Given foo is a valid "SEDOL"
    And foo is in set:
      | "0263494" |
    Then the following data should be generated:
      | foo       |
      | null      |
      | "0263494" |

  Scenario: Running a 'aValid' request that includes the string "sedol" should fail with an error message
    Given foo is a valid "sedol"
    Then the profile is invalid because "No enum constant com.scottlogic.deg.common.profile.constraints.atomic.StandardConstraintTypes.sedol"
    And no data is created

  Scenario: Running an 'aValid' request with the value "CUSIP" should be successful
    Given foo is a valid "CUSIP"
    And foo is in set:
      | "38259P508" |
    Then the following data should be generated:
      | foo         |
      | null        |
      | "38259P508" |

  Scenario: Running a 'aValid' request that includes the string "cusip" should fail with an error message
    Given foo is a valid "cusip"
    Then the profile is invalid because "No enum constant com.scottlogic.deg.common.profile.constraints.atomic.StandardConstraintTypes.cusip"
    And no data is created

  Scenario: Running a 'aValid' request with an invalid value should fail with an error message
    Given foo is a valid "BURRITO"
    Then the profile is invalid because "No enum constant com.scottlogic.deg.common.profile.constraints.atomic.StandardConstraintTypes.BURRITO"
    And no data is created

  Scenario: Running an 'aValid' request that includes a null entry ("") characters should fail with an error message
    Given foo is a valid ""
    Then the profile is invalid because "No enum constant com.scottlogic.deg.common.profile.constraints.atomic.StandardConstraintTypes."
    And no data is created

  Scenario: Running an 'aValid' request with the value property set to a null entry (null) should throw an error
    Given foo is a valid null
    Then the profile is invalid because "Field \[foo\]: Couldn't recognise 'value' property, it must be set to a value"
    And no data is created

  Scenario: aValid run against a non contradicting aValid ISIN should be successful
    Given foo is a valid "ISIN"
    And foo is a valid "ISIN"
    And foo is in set:
      | "GB0002634946" |
    Then the following data should be generated:
      | foo            |
      | null           |
      | "GB0002634946" |

  Scenario: aValid run against a non contradicting not aValid ISIN should only generate null data
    Given foo is a valid "ISIN"
    And foo is anything but a valid "ISIN"
    Then the following data should be generated:
      | foo  |
      | null |

  Scenario: aValid ISIN run against a non contradicting greaterThan should be successful
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

  Scenario: aValid ISIN run against a non contradicting not greaterThan should be successful
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

  Scenario: aValid ISIN run against a non contradicting greaterThanOrEqualTo should be successful
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

  Scenario: aValid ISIN run against a non contradicting not greaterThanOrEqualTo should be successful
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

  Scenario: aValid ISIN run against a non contradicting lessThan should be successful
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

  Scenario: aValid ISIN run against a non contradicting not lessThan should be successful
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

  Scenario: aValid ISIN run against a non contradicting lessThanOrEqualTo should be successful
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

  Scenario: aValid ISIN run against a non contradicting not lessThanOrEqualTo should be successful
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

  Scenario: aValid ISIN run against a non contradicting granularTo should be successful
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

  Scenario: aValid ISIN run against a non contradicting not granularTo should be successful
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

  Scenario: aValid ISIN run against a non contradicting after should be successful
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

  Scenario: aValid ISIN run against a non contradicting not after should be successful
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

  Scenario: aValid ISIN run against a non contradicting afterOrAt should be successful
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

  Scenario: aValid ISIN run against a non contradicting not afterOrAt should be successful
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

  Scenario: aValid ISIN run against a non contradicting before should be successful
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

  Scenario: aValid ISIN run against a non contradicting not before should be successful
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

  Scenario: aValid ISIN run against a non contradicting beforeOrAt should be successful
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

  Scenario: aValid ISIN run against a non contradicting not beforeOrAt should be successful
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

  Scenario: aValid run against a non contradicting aValid SEDOL should be successful
    Given foo is a valid "SEDOL"
    And foo is a valid "SEDOL"
    And foo is in set:
      | "0263494" |
    Then the following data should be generated:
      | foo       |
      | null      |
      | "0263494" |

  Scenario: aValid run against a non contradicting not aValid SEDOL should only generate null data
    Given foo is a valid "SEDOL"
    And foo is anything but a valid "SEDOL"
    Then the following data should be generated:
      | foo  |
      | null |

  Scenario: aValid SEDOL run against a non contradicting greaterThan should be successful
    Given foo is a valid "SEDOL"
    And foo is greater than 0
    And foo is in set:
      | "0263494"  |
      | "026349"   |
      | "02634948" |
      | 1          |
    Then the following data should be generated:
      | foo       |
      | "0263494" |
      | null      |

  Scenario: aValid SEDOL run against a non contradicting not greaterThan should be successful
    Given foo is a valid "SEDOL"
    And foo is anything but greater than 0
    And foo is in set:
      | "0263494"  |
      | "026349"   |
      | "02634948" |
      | 1          |
    Then the following data should be generated:
      | foo       |
      | "0263494" |
      | null      |

  Scenario: aValid SEDOL run against a non contradicting greaterThanOrEqualTo should be successful
    Given foo is a valid "SEDOL"
    And foo is greater than or equal to 0
    And foo is in set:
      | "0263494"  |
      | "026349"   |
      | "02634948" |
      | 1          |
    Then the following data should be generated:
      | foo       |
      | "0263494" |
      | null      |

  Scenario: aValid SEDOL run against a non contradicting not greaterThanOrEqualTo should be successful
    Given foo is a valid "SEDOL"
    And foo is anything but greater than or equal to 0
    And foo is in set:
      | "0263494"  |
      | "026349"   |
      | "02634948" |
      | 1          |
    Then the following data should be generated:
      | foo       |
      | "0263494" |
      | null      |

  Scenario: aValid SEDOL run against a non contradicting lessThan should be successful
    Given foo is a valid "SEDOL"
    And foo is less than 0
    And foo is in set:
      | "0263494"  |
      | "026349"   |
      | "02634948" |
      | 1          |
    Then the following data should be generated:
      | foo       |
      | "0263494" |
      | null      |

  Scenario: aValid SEDOL run against a non contradicting not lessThan should be successful
    Given foo is a valid "SEDOL"
    And foo is anything but less than 0
    And foo is in set:
      | "0263494"  |
      | "026349"   |
      | "02634948" |
      | 1          |
    Then the following data should be generated:
      | foo       |
      | "0263494" |
      | null      |

  Scenario: aValid SEDOL run against a non contradicting lessThanOrEqualTo should be successful
    Given foo is a valid "SEDOL"
    And foo is less than or equal to 0
    And foo is in set:
      | "0263494"  |
      | "026349"   |
      | "02634948" |
      | 1          |
    Then the following data should be generated:
      | foo       |
      | "0263494" |
      | null      |

  Scenario: aValid SEDOL run against a non contradicting not lessThanOrEqualTo should be successful
    Given foo is a valid "SEDOL"
    And foo is anything but less than or equal to 0
    And foo is in set:
      | "0263494"  |
      | "026349"   |
      | "02634948" |
      | 1          |
    Then the following data should be generated:
      | foo       |
      | "0263494" |
      | null      |

  Scenario: aValid SEDOL run against a non contradicting granularTo should be successful
    Given foo is a valid "SEDOL"
    And foo is granular to 1
    And foo is in set:
      | "0263494"  |
      | "026349"   |
      | "02634948" |
      | 1          |
    Then the following data should be generated:
      | foo       |
      | "0263494" |
      | null      |

  Scenario: aValid SEDOL run against a non contradicting not granularTo should be successful
    Given foo is a valid "SEDOL"
    And foo is anything but granular to 1
    And foo is in set:
      | "0263494"  |
      | "026349"   |
      | "02634948" |
      | 1          |
    Then the following data should be generated:
      | foo       |
      | "0263494" |
      | null      |

  Scenario: aValid SEDOL run against a non contradicting after should be successful
    Given foo is a valid "SEDOL"
    And foo is after 2019-01-01T00:00:00.000Z
    And foo is in set:
      | "0263494"  |
      | "026349"   |
      | "02634948" |
      | 1          |
    Then the following data should be generated:
      | foo       |
      | "0263494" |
      | null      |

  Scenario: aValid SEDOL run against a non contradicting not after should be successful
    Given foo is a valid "SEDOL"
    And foo is anything but after 2019-01-01T00:00:00.000Z
    And foo is in set:
      | "0263494"  |
      | "026349"   |
      | "02634948" |
      | 1          |
    Then the following data should be generated:
      | foo       |
      | "0263494" |
      | null      |

  Scenario: aValid SEDOL run against a non contradicting afterOrAt should be successful
    Given foo is a valid "SEDOL"
    And foo is after or at 2019-01-01T00:00:00.000Z
    And foo is in set:
      | "0263494"  |
      | "026349"   |
      | "02634948" |
      | 1          |
    Then the following data should be generated:
      | foo       |
      | "0263494" |
      | null      |

  Scenario: aValid SEDOL run against a non contradicting not afterOrAt should be successful
    Given foo is a valid "SEDOL"
    And foo is anything but after or at 2019-01-01T00:00:00.000Z
    And foo is in set:
      | "0263494"  |
      | "026349"   |
      | "02634948" |
      | 1          |
    Then the following data should be generated:
      | foo       |
      | "0263494" |
      | null      |

  Scenario: aValid SEDOL run against a non contradicting before should be successful
    Given foo is a valid "SEDOL"
    And foo is before 2019-01-01T00:00:00.000Z
    And foo is in set:
      | "0263494"  |
      | "026349"   |
      | "02634948" |
      | 1          |
    Then the following data should be generated:
      | foo       |
      | "0263494" |
      | null      |

  Scenario: aValid SEDOL run against a non contradicting not before should be successful
    Given foo is a valid "SEDOL"
    And foo is anything but before 2019-01-01T00:00:00.000Z
    And foo is in set:
      | "0263494"  |
      | "026349"   |
      | "02634948" |
      | 1          |
    Then the following data should be generated:
      | foo       |
      | "0263494" |
      | null      |

  Scenario: aValid SEDOL run against a non contradicting beforeOrAt should be successful
    Given foo is a valid "SEDOL"
    And foo is before or at 2019-01-01T00:00:00.000Z
    And foo is in set:
      | "0263494"  |
      | "026349"   |
      | "02634948" |
      | 1          |
    Then the following data should be generated:
      | foo       |
      | "0263494" |
      | null      |

  Scenario: aValid SEDOL run against a non contradicting not beforeOrAt should be successful
    Given foo is a valid "SEDOL"
    And foo is anything but before or at 2019-01-01T00:00:00.000Z
    And foo is in set:
      | "0263494"  |
      | "026349"   |
      | "02634948" |
      | 1          |
    Then the following data should be generated:
      | foo       |
      | "0263494" |
      | null      |

  Scenario: aValid run against a non contradicting aValid CUSIP should be successful
    Given foo is a valid "CUSIP"
    And foo is a valid "CUSIP"
    And foo is in set:
      | "38259P508" |
    Then the following data should be generated:
      | foo         |
      | null        |
      | "38259P508" |

  Scenario: aValid run against a non contradicting not aValid CUSIP should only generate null data
    Given foo is a valid "CUSIP"
    And foo is anything but a valid "CUSIP"
    Then the following data should be generated:
      | foo  |
      | null |

  Scenario: aValid CUSIP run against a non contradicting greaterThan should be successful
    Given foo is a valid "CUSIP"
    And foo is greater than 0
    And foo is in set:
      | "38259P508"  |
      | "38259P58"   |
      | "38259EP508" |
      | 1            |
    Then the following data should be generated:
      | foo         |
      | "38259P508" |
      | null        |

  Scenario: aValid CUSIP run against a non contradicting not greaterThan should be successful
    Given foo is a valid "CUSIP"
    And foo is anything but greater than 0
    And foo is in set:
      | "38259P508"  |
      | "38259P58"   |
      | "38259EP508" |
      | 1            |
    Then the following data should be generated:
      | foo         |
      | "38259P508" |
      | null        |

  Scenario: aValid CUSIP run against a non contradicting greaterThanOrEqualTo should be successful
    Given foo is a valid "CUSIP"
    And foo is greater than or equal to 0
    And foo is in set:
      | "38259P508"  |
      | "38259P08"   |
      | "38259EP508" |
      | 1            |
    Then the following data should be generated:
      | foo         |
      | "38259P508" |
      | null        |

  Scenario: aValid CUSIP run against a non contradicting not greaterThanOrEqualTo should be successful
    Given foo is a valid "CUSIP"
    And foo is anything but greater than or equal to 0
    And foo is in set:
      | "38259P508"  |
      | "38259P58"   |
      | "38259EP508" |
      | 1            |
    Then the following data should be generated:
      | foo         |
      | "38259P508" |
      | null        |

  Scenario: aValid CUSIP run against a non contradicting lessThan should be successful
    Given foo is a valid "CUSIP"
    And foo is less than 0
    And foo is in set:
      | "38259P508"  |
      | "38259P58"   |
      | "38259EP508" |
      | 1            |
    Then the following data should be generated:
      | foo         |
      | "38259P508" |
      | null        |

  Scenario: aValid CUSIP run against a non contradicting not lessThan should be successful
    Given foo is a valid "CUSIP"
    And foo is anything but less than 0
    And foo is in set:
      | "38259P508"  |
      | "38259P58"   |
      | "38259EP508" |
      | 1            |
    Then the following data should be generated:
      | foo         |
      | "38259P508" |
      | null        |

  Scenario: aValid CUSIP run against a non contradicting lessThanOrEqualTo should be successful
    Given foo is a valid "CUSIP"
    And foo is less than or equal to 0
    And foo is in set:
      | "38259P508"  |
      | "38259P58"   |
      | "38259EP508" |
      | 1            |
    Then the following data should be generated:
      | foo         |
      | "38259P508" |
      | null        |

  Scenario: aValid CUSIP run against a non contradicting not lessThanOrEqualTo should be successful
    Given foo is a valid "CUSIP"
    And foo is anything but less than or equal to 0
    And foo is in set:
      | "38259P508"  |
      | "38259P58"   |
      | "38259EP508" |
      | 1            |
    Then the following data should be generated:
      | foo         |
      | "38259P508" |
      | null        |

  Scenario: aValid CUSIP run against a non contradicting granularTo should be successful
    Given foo is a valid "CUSIP"
    And foo is granular to 1
    And foo is in set:
      | "38259P508"  |
      | "38259P58"   |
      | "38259EP508" |
      | 1            |
    Then the following data should be generated:
      | foo         |
      | "38259P508" |
      | null        |

  Scenario: aValid CUSIP run against a non contradicting not granularTo should be successful
    Given foo is a valid "CUSIP"
    And foo is anything but granular to 1
    And foo is in set:
      | "38259P508"  |
      | "38259P58"   |
      | "38259EP508" |
      | 1            |
    Then the following data should be generated:
      | foo         |
      | "38259P508" |
      | null        |

  Scenario: aValid CUSIP run against a non contradicting after should be successful
    Given foo is a valid "CUSIP"
    And foo is after 2019-01-01T00:00:00.000Z
    And foo is in set:
      | "38259P508"  |
      | "38259P58"   |
      | "38259EP508" |
      | 1            |
    Then the following data should be generated:
      | foo         |
      | "38259P508" |
      | null        |

  Scenario: aValid CUSIP run against a non contradicting not after should be successful
    Given foo is a valid "CUSIP"
    And foo is anything but after 2019-01-01T00:00:00.000Z
    And foo is in set:
      | "38259P508"  |
      | "38259P58"   |
      | "38259EP508" |
      | 1            |
    Then the following data should be generated:
      | foo         |
      | "38259P508" |
      | null        |

  Scenario: aValid CUSIP run against a non contradicting afterOrAt should be successful
    Given foo is a valid "CUSIP"
    And foo is after or at 2019-01-01T00:00:00.000Z
    And foo is in set:
      | "38259P508"  |
      | "38259P58"   |
      | "38259EP508" |
      | 1            |
    Then the following data should be generated:
      | foo         |
      | "38259P508" |
      | null        |

  Scenario: aValid CUSIP run against a non contradicting not afterOrAt should be successful
    Given foo is a valid "CUSIP"
    And foo is anything but after or at 2019-01-01T00:00:00.000Z
    And foo is in set:
      | "38259P508"  |
      | "38259P58"   |
      | "38259EP508" |
      | 1            |
    Then the following data should be generated:
      | foo         |
      | "38259P508" |
      | null        |

  Scenario: aValid CUSIP run against a non contradicting before should be successful
    Given foo is a valid "CUSIP"
    And foo is before 2019-01-01T00:00:00.000Z
    And foo is in set:
      | "38259P508"  |
      | "38259P58"   |
      | "38259EP508" |
      | 1            |
    Then the following data should be generated:
      | foo         |
      | "38259P508" |
      | null        |

  Scenario: aValid CUSIP run against a non contradicting not before should be successful
    Given foo is a valid "CUSIP"
    And foo is anything but before 2019-01-01T00:00:00.000Z
    And foo is in set:
      | "38259P508"  |
      | "38259P58"   |
      | "38259EP508" |
      | 1            |
    Then the following data should be generated:
      | foo         |
      | "38259P508" |
      | null        |

  Scenario: aValid CUSIP run against a non contradicting beforeOrAt should be successful
    Given foo is a valid "CUSIP"
    And foo is before or at 2019-01-01T00:00:00.000Z
    And foo is in set:
      | "38259P508"  |
      | "38259P58"   |
      | "38259EP508" |
      | 1            |
    Then the following data should be generated:
      | foo         |
      | "38259P508" |
      | null        |

  Scenario: aValid CUSIP run against a non contradicting not beforeOrAt should be successful
    Given foo is a valid "CUSIP"
    And foo is anything but before or at 2019-01-01T00:00:00.000Z
    And foo is in set:
      | "38259P508"  |
      | "38259P58"   |
      | "38259EP508" |
      | 1            |
    Then the following data should be generated:
      | foo         |
      | "38259P508" |
      | null        |

  Scenario: aValid RIC run against a non contradicting set should be successful
    Given foo is a valid "RIC"
    And foo is anything but null
    And foo is in set:
      | "AB.PQ"    |
    Then the following data should be generated:
      | foo         |
      | "AB.PQ"    |

  Scenario: aValid RIC run against a contradicting set should not return data
    Given foo is a valid "RIC"
    And foo is anything but null
    And foo is in set:
      | "NOPE"    |
    Then the following data should be generated:
      | foo         |

  Scenario: aValid RIC run against a length should be successful
    Given foo is a valid "RIC"
    And foo is anything but null
    And foo is of length 6
    And foo is in set:
      | "AB.PQ"    |
      | "ABC.PQ"    |
      | "ABCD.PQ"    |
    Then the following data should be generated:
      | foo         |
      | "ABC.PQ"    |
