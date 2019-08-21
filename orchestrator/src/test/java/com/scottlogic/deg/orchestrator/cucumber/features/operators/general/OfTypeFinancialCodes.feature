Feature: User can specify that a field must be a financial code type

  Background:
    Given the generation strategy is full
    And there is a field foo

  Scenario: An ofType constraint with the value "ISIN" generates valid ISINs
    Given foo is of type "ISIN"
    And foo is in set:
      | "GB0002634946" |
    Then the following data should be generated:
      | foo            |
      | null           |
      | "GB0002634946" |

  Scenario: Sequential isins are generated uniquely
    Given foo is of type "ISIN"
    And foo is anything but null
    And the generator can generate at most 4 rows
    Then the following data should be generated:
      | foo            |
      | "AD0000000003" |
      | "AD0000000011" |
      | "AD0000000029" |
      | "AD0000000037" |

  Scenario: An ofType constraint with the value "isin" fails with an invalid profile error message
    Given foo is of type "isin"
    Then the profile is invalid because "Profile is invalid: no constraints known for \"is\": \"ofType\", \"value\": \"isin\""
    And no data is created

  Scenario: An ofType constraint with the value "SEDOL" generates valid SEDOLs
    Given foo is of type "SEDOL"
    And foo is in set:
      | "0263494" |
    Then the following data should be generated:
      | foo       |
      | null      |
      | "0263494" |

  Scenario: An ofType constraint with the value "sedol" fails with an invalid profile error message
    Given foo is of type "sedol"
    Then the profile is invalid because "Profile is invalid: no constraints known for \"is\": \"ofType\", \"value\": \"sedol\""
    And no data is created

  Scenario: An ofType constraint with the value "CUSIP" generates valid CUSIPs
    Given foo is of type "CUSIP"
    And foo is in set:
      | "38259P508" |
    Then the following data should be generated:
      | foo         |
      | null        |
      | "38259P508" |

  Scenario: An ofType constraint with the value "cusip" fails with an invalid profile error message
    Given foo is of type "cusip"
    Then the profile is invalid because "Profile is invalid: no constraints known for \"is\": \"ofType\", \"value\": \"cusip\""
    And no data is created

  Scenario: An ofType constraint with the value "BURRITO" fails with an invalid profile error message
    Given foo is of type "BURRITO"
    Then the profile is invalid because "Profile is invalid: no constraints known for \"is\": \"ofType\", \"value\": \"BURRITO\""
    And no data is created

  Scenario: An ofType constraint with a value that is an empty string fails with an invalid profile error message
    Given foo is of type ""
    Then the profile is invalid because "Profile is invalid: no constraints known for \"is\": \"ofType\", \"value\": \"\""
    And no data is created

  Scenario: An ofType constraint with a null value fails with an error message
    Given foo is of type null
    Then the profile is invalid because "Field \[foo\]: Couldn't recognise 'value' property, it must be set to a value"
    And no data is created

  Scenario: Two ISIN constraints combined generate valid ISINs
    Given foo is of type "ISIN"
    And foo is of type "ISIN"
    And foo is in set:
      | "GB0002634946" |
    Then the following data should be generated:
      | foo            |
      | null           |
      | "GB0002634946" |

  @ignore "Standard constraints e.g. ISINs cannot yet be negated."
  Scenario: An ISIN constraint combined with a non-ISIN constraint generates no data
    Given foo is of type "ISIN"
    And foo is anything but null
    And foo is anything but of type "ISIN"
    Then no data is created

  Scenario: An ISIN constraint combined with a greater than constraint generates valid ISINs
    Given foo is of type "ISIN"
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

  Scenario: An ISIN constraint combined with a not greater than constraint generates valid ISINs
    Given foo is of type "ISIN"
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

  Scenario: An ISIN constraint combined with a greater than or equal to constraint generates valid ISINs
    Given foo is of type "ISIN"
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

  Scenario: An ISIN constraint combined with a not greater than or equal to constraint generates valid ISINs
    Given foo is of type "ISIN"
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

  Scenario: An ISIN constraint combined with a less than constraint generates valid ISINs
    Given foo is of type "ISIN"
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

  Scenario: An ISIN constraint combined with a not less than constraint generates valid ISINs
    Given foo is of type "ISIN"
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

  Scenario: An ISIN constraint combined with a less than or equal to constraint generates valid ISINs
    Given foo is of type "ISIN"
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

  Scenario: An ISIN constraint combined with a not less than or equal to constraint generates valid ISINs
    Given foo is of type "ISIN"
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

  Scenario: An ISIN constraint combined with a granular to constraint generates valid ISINs
    Given foo is of type "ISIN"
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

  Scenario: An ISIN constraint combined with a not granular to constraint generates valid ISINs
    Given foo is of type "ISIN"
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

  Scenario: An ISIN constraint combined with an is after constraint generates valid ISINs
    Given foo is of type "ISIN"
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

  Scenario: An ISIN constraint combined with a not after constraint generates valid ISINs
    Given foo is of type "ISIN"
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

  Scenario: An ISIN constraint combined with an is after or at constraint generates valid ISINs
    Given foo is of type "ISIN"
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

  Scenario: An ISIN constraint combined with a not after or at constraint generates valid ISINs
    Given foo is of type "ISIN"
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

  Scenario: An ISIN constraint combined with an is before constraint generates valid ISINs
    Given foo is of type "ISIN"
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

  Scenario: An ISIN constraint combined with a not before constraint generates valid ISINs
    Given foo is of type "ISIN"
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

  Scenario: An ISIN constraint combined with an is before or at constraint generates valid ISINs
    Given foo is of type "ISIN"
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

  Scenario: An ISIN constraint combined with a not is before or at constraint generates valid ISINs
    Given foo is of type "ISIN"
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

  @ignore "Standard constraints e.g. ISINs cannot yet be combined with regex constraints."
  Scenario: An ISIN constraint combined with a matching regex constraint that matches valid ISINs should generate matching valid ISINs
    Given foo is of type "ISIN"
    And foo is matching regex "US9311421039"
    Then the following data should be generated:
      | foo            |
      | "US9311421039" |
      | null           |

  @ignore "Standard constraints e.g. ISINs cannot yet be combined with regex constraints."
  Scenario: An ISIN constraint combined with a matching regex constraint that cannot match any valid ISIN due to its length should only generate null
    Given foo is of type "ISIN"
    And foo is matching regex "US[0-9]{9}"
    Then the following data should be generated:
      | foo            |
      | null           |

  @ignore "Standard constraints e.g. ISINs cannot yet be combined with regex constraints."
  Scenario: An ISIN constraint combined with a matching regex constraint that cannot match any valid ISIN due to its check digit should only generate null
    Given foo is of type "ISIN"
    And foo is matching regex "US9311421038"
    Then the following data should be generated:
      | foo            |
      | null           |

  Scenario: A SEDOL constraint combined with another SEDOL constraint generates valid SEDOLs
    Given foo is of type "SEDOL"
    And foo is of type "SEDOL"
    And foo is in set:
      | "0263494" |
    Then the following data should be generated:
      | foo       |
      | null      |
      | "0263494" |

  @ignore "Standard constraints e.g. ISINs cannot yet be negated."
  Scenario: A SEDOL constraint combined with a non-SEDOL constraint generates no data
    Given foo is of type "SEDOL"
    And foo is anything but null
    And foo is anything but of type "SEDOL"
    Then no data is created

  Scenario: A SEDOL constraint combined with a greater than constraint generates valid SEDOLs
    Given foo is of type "SEDOL"
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

  Scenario: A SEDOL constraint combined with a not greater than constraint generates valid SEDOLs
    Given foo is of type "SEDOL"
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

  Scenario: A SEDOL constraint combined with a greater than or equal to constraint generates valid SEDOLs
    Given foo is of type "SEDOL"
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

  Scenario: A SEDOL constraint combined with a not greater than or equal to constraint generates valid SEDOLs
    Given foo is of type "SEDOL"
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

  Scenario: A SEDOL constraint combined with a less than constraint generates valid SEDOLs
    Given foo is of type "SEDOL"
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

  Scenario: A SEDOL constraint combined with a not less than constraint generates valid SEDOLs
    Given foo is of type "SEDOL"
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

  Scenario: A SEDOL constraint combined with a less than or equal to constraint generates valid SEDOLs
    Given foo is of type "SEDOL"
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

  Scenario: A SEDOL constraint combined with a not less than or equal to constraint generates valid SEDOLs
    Given foo is of type "SEDOL"
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

  Scenario: A SEDOL constraint combined with a granular to constraint generates valid SEDOLs
    Given foo is of type "SEDOL"
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

  Scenario: A SEDOL constraint combined with a not granular to constraint generates valid SEDOLs
    Given foo is of type "SEDOL"
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

  Scenario: A SEDOL constraint combined with an after constraint generates valid SEDOLs
    Given foo is of type "SEDOL"
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

  Scenario: A SEDOL constraint combined with a not after constraint generates valid SEDOLs
    Given foo is of type "SEDOL"
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

  Scenario: A SEDOL constraint combined with an after or at constraint generates valid SEDOLs
    Given foo is of type "SEDOL"
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

  Scenario: A SEDOL constraint combined with a not after or at constraint generates valid SEDOLs
    Given foo is of type "SEDOL"
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

  Scenario: A SEDOL constraint combined with a before constraint generates valid SEDOLs
    Given foo is of type "SEDOL"
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

  Scenario: A SEDOL constraint combined with a not before constraint generates valid SEDOLs
    Given foo is of type "SEDOL"
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

  Scenario: A SEDOL constraint combined with a before or at constraint generates valid SEDOLs
    Given foo is of type "SEDOL"
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

  @ignore "Standard constraints e.g. ISINs cannot yet be combined with date constraints."
  Scenario: A SEDOL constraint combined with a not before or at constraint generates valid SEDOLs
    Given foo is of type "SEDOL"
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

  @ignore "Standard constraints e.g. ISINs cannot yet be combined with regex constraints."
  Scenario: A SEDOL constraint combined with a matching regex constraint that matches valid SEDOLs should generate valid SEDOLs
    Given foo is of type "SEDOL"
    And foo is matching regex "0263494"
    Then the following data should be generated:
      | foo       |
      | "0263494" |
      | null      |

  @ignore "Standard constraints e.g. ISINs cannot yet be combined with regex constraints."
  Scenario: A SEDOL constraint combined with a matching regex constraint that cannot match any valid SEDOL because it has the wrong check digit should only generate null
    Given foo is of type "SEDOL"
    And foo is matching regex "0263492"
    Then the following data should be generated:
      | foo  |
      | null |

  @ignore "Standard constraints e.g. ISINs cannot yet be combined with regex constraints."
  Scenario: A SEDOL constraint combined with a matching regex constraint that cannot match any valid SEDOL because it has the wrong length should only generate null
    Given foo is of type "SEDOL"
    And foo is matching regex "[0-9]{6}"
    Then the following data should be generated:
      | foo            |
      | null           |

  @ignore "Standard constraints e.g. ISINs cannot yet be combined with regex constraints."
  Scenario: A SEDOL constraint combined with a matching regex constraint that cannot match any valid SEDOL because it cannot have a correct check digit should only generate null
    Given foo is of type "SEDOL"
    And foo is matching regex "0[023]63492"
    Then the following data should be generated:
      | foo  |
      | null |

  Scenario: A CUSIP constraint combined with a second CUSIP constraint generates valid CUSIPs
    Given foo is of type "CUSIP"
    And foo is of type "CUSIP"
    And foo is in set:
      | "38259P508" |
    Then the following data should be generated:
      | foo         |
      | null        |
      | "38259P508" |

  @ignore "Standard constraints e.g. ISINs cannot yet be negated."
  Scenario: A CUSIP constraint combined with a non-CUSIP constraint generates no data
    Given foo is of type "CUSIP"
    And foo is anything but null
    And foo is anything but of type "CUSIP"
    Then no data is created

  Scenario: A CUSIP constraint combined with a greater than constraint generates valid CUSIPs
    Given foo is of type "CUSIP"
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

  Scenario: A CUSIP constraint combined with a not greater than constraint generates valid CUSIPs
    Given foo is of type "CUSIP"
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

  Scenario: A CUSIP constraint combined with a greater than or equal to constraint generates valid CUSIPs
    Given foo is of type "CUSIP"
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

  Scenario: A CUSIP constraint combined with a not greater than or equal to constraint generates valid CUSIPs
    Given foo is of type "CUSIP"
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

  Scenario: A CUSIP constraint combined with a less than constraint generates valid CUSIPs
    Given foo is of type "CUSIP"
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

  Scenario: A CUSIP constraint combined with a not less than constraint generates valid CUSIPs
    Given foo is of type "CUSIP"
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

  Scenario: A CUSIP constraint combined with a less than or equal to constraint generates valid CUSIPs
    Given foo is of type "CUSIP"
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

  Scenario: A CUSIP constraint combined with a not less than or equal to constraint generates valid CUSIPs
    Given foo is of type "CUSIP"
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

  Scenario: A CUSIP constraint combined with a granular to constraint generates valid CUSIPs
    Given foo is of type "CUSIP"
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

  Scenario: A CUSIP constraint combined with a not granular to constraint generates valid CUSIPs
    Given foo is of type "CUSIP"
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

  Scenario: A CUSIP constraint combined with an after constraint generates valid CUSIPs
    Given foo is of type "CUSIP"
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

  Scenario: A CUSIP constraint combined with a not after constraint generates valid CUSIPs
    Given foo is of type "CUSIP"
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

  Scenario: A CUSIP constraint combined with an after or at constraint generates valid CUSIPs
    Given foo is of type "CUSIP"
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

  Scenario: A CUSIP constraint combined with a not after or at constraint generates valid CUSIPs
    Given foo is of type "CUSIP"
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

  Scenario: A CUSIP constraint combined with a before constraint generates valid CUSIPs
    Given foo is of type "CUSIP"
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

  Scenario: A CUSIP constraint combined with a not before constraint generates valid CUSIPs
    Given foo is of type "CUSIP"
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

  Scenario: A CUSIP constraint combined with a before or at constraint generates valid CUSIPs
    Given foo is of type "CUSIP"
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

  Scenario: A CUSIP constraint combined with a not before or at constraint generates valid CUSIPs
    Given foo is of type "CUSIP"
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

  @ignore "Standard constraints e.g. ISINs cannot yet be combined with regex constraints."
  Scenario: A CUSIP constraint combined with a matching regex constraint that matches a valid CUSIP generates valid CUSIPs
    Given foo is of type "CUSIP"
    And foo is matching regex "38259P508"
    Then the following data should be generated:
      | foo         |
      | "38259P508" |
      | null        |

  @ignore "Standard constraints e.g. ISINs cannot yet be combined with regex constraints."
  Scenario: A CUSIP constraint combined with a matching regex constraint that cannot match a valid CUSIP because it has an invalid check digit should only generate null
    Given foo is of type "CUSIP"
    And foo is matching regex "38259P509"
    Then the following data should be generated:
      | foo  |
      | null |

  @ignore "Standard constraints e.g. ISINs cannot yet be combined with regex constraints."
  Scenario: A CUSIP constraint combined with a matching regex constraint that cannot match a valid CUSIP because it has the wrong length should only generate null
    Given foo is of type "CUSIP"
    And foo is matching regex "[0-9]{3}.{4}[0-9]"
    Then the following data should be generated:
      | foo            |
      | null           |

  Scenario: A RIC constraint combined with a not null constraint generates valid RICs
    Given foo is of type "RIC"
    And foo is anything but null
    And foo is in set:
      | "AB.PQ"    |
    Then the following data should be generated:
      | foo         |
      | "AB.PQ"    |

  Scenario: A RIC constraint combined with a not null constraint and an in set constraint that does not contain any valid RICs generates no data
    Given foo is of type "RIC"
    And foo is anything but null
    And foo is in set:
      | "NOPE"    |
    Then the following data should be generated:
      | foo         |

  Scenario: A RIC constraint combined with an of length constraint returns valid RICs of the specified length
    Given foo is of type "RIC"
    And foo is anything but null
    And foo is of length 6
    And foo is in set:
      | "AB.PQ"    |
      | "ABC.PQ"    |
      | "ABCD.PQ"    |
    Then the following data should be generated:
      | foo         |
      | "ABC.PQ"    |

