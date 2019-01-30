Feature: User can specify that a field must be a valid ISIN (International Securities Identification Number)

Background:
     Given the generation strategy is full
       And there is a field foo
       And foo is of type "string"

Scenario: Running an 'aValid' request that includes a value of a string "ISIN" should be successful
    Given foo is a valid "ISIN"
      And foo is in set:
        | "GB0002634946"  |
        | "US0378331005"  |
        | "GB0002634947"  |
        | "US0378331006"  |
        | "US378331005"   |
        | "US37833100598" |
        | "G00002634946"  |
        | "400002634946"  |
        | "GBP002634946"  |
        | "GB000263494Z"  |
        | "0002634946GB"  |
    Then the following data should be generated:
      | foo            |
      | null           |
      | "GB0002634946" |
      | "US0378331005" |
      And the following data should not be included in what is generated:
        | foo             |
        | "GB0002634947"  |
        | "US0378331006"  |
        | "US378331005"   |
        | "US37833100598" |
        | "G00002634946"  |
        | "400002634946"  |
        | "GBP002634946"  |
        | "GB000263494z"  |


  Scenario: Running an 'aValid' request that includes a value of a string "ISIN" should fail with an error message
    Given foo is a valid "isin"
    And foo is in set:
      | "GB0002634946" |
    Then I am presented with an error message
    And no data is created


  Scenario: Running an 'aValid' request that includes a value of a string that is not "ISIN" should fail with an error message
    Given foo is a valid "BURRITO"
    And foo is in set:
      | "GB0002634946" |
    Then I am presented with an error message
    And no data is created


  Scenario: Running an 'aValid' request that includes a value of a string of a valid ISIN ("GB000000005") should fail with an error message
    Given foo is a valid "GB000000005"
    And foo is in set:
      | "GB0002634946" |
    Then I am presented with an error message
    And no data is created


  Scenario: Running an 'aValid' request that includes a numeric value should fail with an error message
    Given foo is a valid 404
    And foo is in set:
      | "GB0002634946" |
    Then I am presented with an error message
    And no data is created


  Scenario: Running an 'aValid' request that includes a date should fail with an error message
    Given foo is a valid "2010-01-01T00:00"
    And foo is in set:
      | "GB0002634946" |
    Then I am presented with an error message
    And no data is created


  Scenario: Running an 'aValid' request that includes a boolean value should fail with an error message
    Given foo is a valid "true"
    And foo is in set:
      | "GB0002634946" |
    Then I am presented with an error message
    And no data is created


  Scenario: Running an 'aValid' request that includes multiple values within the same statement should fail with an error message
    Given foo is a valid "ISIN"
    And foo is a valid "BURRITO"
    And foo is in set:
      | "GB0002634946" |
    Then I am presented with an error message
    And no data is created


  Scenario: Running an 'aValid' request that includes a null entry ("") characters should fail with an error message
    Given foo is a valid ""
    And foo is in set:
      | "GB0002634946" |
    Then I am presented with an error message
    And no data is created

  # COMBINATION OF CONSTRAINTS #

  Scenario: Running an 'aValid' request alongside a non-contradicting equalTo constraint should be successful
    Given foo is a valid "ISIN"
      And foo is equal to "GB0002634946"
    Then the following data should be generated:
      | foo            |
      | null           |
      | "GB0002634946" |

  Scenario: Running an 'aValid' request alongside a contradicting equalTo constraint should only emit null
    Given foo is a valid "ISIN"
      And foo is equal to "GB00026349"
    Then the following data should be generated:
      | foo  |
      | null |

  Scenario: Running an 'aValid' request alongside a non-contradicting inSet constraint should be successful
    Given foo is a valid "ISIN"
      And foo is in set:
        | "GB0002634946" |
    Then the following data should be generated:
      | foo            |
      | null           |
      | "GB0002634946" |

  Scenario: Running an 'aValid' request alongside a contradicting inSet constraint should only emit null
    Given foo is a valid "ISIN"
      And foo is in set:
        | "GB0002634947"  |
        | "US0378331006"  |
        | "US378331005"   |
        | "US37833100598" |
        | "G00002634946"  |
        | "400002634946"  |
        | "GBP002634946"  |
        | "GB000263494z"  |
    Then the following data should be generated:
      | foo  |
      | null |

  Scenario: Running an 'aValid' request alongside a null constraint should only emit null
    Given foo is a valid "ISIN"
      And foo is null
    Then the following data should be generated:
      | foo  |
      | null |

    @ignore #linked to issue 487
  Scenario Outline: Running an 'aValid' request alongside a non-contradicting matchingRegex constraint should only emit null
    Given foo is a valid "ISIN"
    And foo is matching regex <regex>
    Then the following data should be generated:
      | foo |
      | null |
    Examples:
      | regex               |
      | null                |
      | /GB0002634946/      |
      | /[A-Z]{2}[0-9]{10}/ |
      | /[A-Z0-9]{12}/      |
      | /[A-Z0-9]/          |


  Scenario Outline: Running an 'aValid' request alongside a contradicting matchingRegex constraint should only emit null
    Given foo is a valid "ISIN"
      And foo is matching regex <regex>
    And the following data should be generated:
      | foo  |
      | null |
    Examples:
      | regex               |
      | /GB0002634947/      |
      | /GB000263494/       |
      | /[A-Z]{1}[0-9]{10}/ |
      | /[A-Z]{3}[0-9]{10}/ |
      | /[A-Z]{2}[0-9]{9}/  |
      | /[A-Z]{2}[0-9]{11}/ |
      | /[A-Z0-9]{13}/      |
      | /[A-Z0-9]{11}/      |

  Scenario Outline: Running an 'aValid' request alongside a non-contradicting containingRegex constraint should only emit null
    Given foo is a valid "ISIN"
    And foo is containing regex <regex>
    Then the following data should be generated:
      | foo  |
      | null |
    Examples:
      | regex               |
      | /GB0002634946/      |
      | /GB/                |
      | /0002634946/        |
      | /B000263/           |
      | /[A-Z]{2}[0-9]{10}/ |
      | /[A-Z0-9]{12}/      |
      | /[A-Z0-9]/          |
      | /[A-Z]{1}[0-9]{10}/ |
      | /[A-Z]{2}[0-9]{9}/  |
      | /[A-Z0-9]{11}/      |

  Scenario Outline: Running an 'aValid' request alongside a contradicting containingRegex constraint should only emit null
    Given foo is a valid "ISIN"
      And foo is containing regex <regex>
    And the following data should be generated:
      | foo  |
      | null |
    Examples:
      | regex               |
      | /GB0002634947/      |
      | /GB00026349467/     |
      | /[A-Z]{3}[0-9]{10}/ |
      | /[A-Z]{2}[0-9]{11}/ |
      | /[A-Z0-9]{13}/      |
      | /GBZ/               |
      | /0002634947/        |

  Scenario: Running an 'aValid' request alongside a non-contradicting ofLength constraint should only emit null
    Given foo is a valid "ISIN"
    And foo is of length 12
    And foo is in set:
      | "GB0002634946" |
    Then the following data should be generated:
      | foo  |
      | null |

  Scenario Outline: Running an 'aValid' request alongside a contradicting ofLength constraint should only emit null
    Given foo is a valid "ISIN"
    And foo is of length <length>
    Then the following data should be generated:
      | foo  |
      | null |
    Examples:
    | length |
    | 11     |
    | 13     |
    | 1      |
    | 0      |
    | 9999   |

  Scenario Outline: Running an 'aValid' request alongside a non-contradicting longerThan constraint should only emit null
    Given foo is a valid "ISIN"
    And foo is longer than <length>
    And foo is in set:
      | "GB0002634946" |
    Then the following data should be generated:
      | foo  |
      | null |
    Examples:
      | length |
      | 0      |
      | 1      |
      | 11     |

  Scenario: Running an 'aValid' request alongside a contradicting longerThan constraint should only emit null
    Given foo is a valid "ISIN"
      And foo is longer than 12
    Then the following data should be generated:
      | foo  |
      | null |

  Scenario: Running an 'aValid' request alongside a non-contradicting shorterThan constraint should only emit null
    Given foo is a valid "ISIN"
    And foo is shorter than 13
    And foo is in set:
      | "GB0002634946" |
    Then the following data should be generated:
      | foo  |
      | null |

  Scenario: Running an 'aValid' request alongside a contradicting shorterThan constraint should only emit null
    Given foo is a valid "ISIN"
    And foo is shorter than 12
    Then the following data should be generated:
      | foo  |
      | null |

  Scenario: Running an 'aValid' request alongside a non-contradicting aValid constraint should be successful
    Given foo is a valid "ISIN"
    And foo is a valid "ISIN"
    And foo is in set:
      | "GB0002634946" |
    Then the following data should be generated:
      | foo            |
      | null           |
      | "GB0002634946" |

  Scenario: Running an 'aValid' request alongside a greaterThan constraint should be successful
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

  Scenario: Running an 'aValid' request alongside a greaterThanOrEqualTo constraint should be successful
    Given foo is a valid "ISIN"
      And foo is greater than or equal to 1
      And foo is in set:
        | "GB0002634946"  |
        | "GB000263494"   |
        | "GB00026349468" |
        | 1               |
    Then the following data should be generated:
      | foo            |
      | "GB0002634946" |
      | null           |

  Scenario: Running an 'aValid' request alongside a lessThan constraint should be successful
    Given foo is a valid "ISIN"
      And foo is less than 13
      And foo is in set:
        | "GB0002634946"  |
        | "GB000263494"   |
        | "GB00026349468" |
        | 1               |
    Then the following data should be generated:
      | foo            |
      | "GB0002634946" |
      | null           |

  Scenario: Running an 'aValid' request alongside a lessThanOrEqualTo constraint should be successful
    Given foo is a valid "ISIN"
      And foo is less than or equal to 12
      And foo is in set:
        | "GB0002634946"  |
        | "GB000263494"   |
        | "GB00026349468" |
        | 1               |
    Then the following data should be generated:
      | foo            |
      | "GB0002634946" |
      | null           |

  Scenario: Running an 'aValid' request alongside a granularTo constraint should be successful
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

  Scenario: Running an 'aValid' request alongside an after constraint should be successful
    Given foo is a valid "ISIN"
      And foo is after 2018-09-01T00:00:00.000
      And foo is in set:
        | "GB0002634946"          |
        | "GB000263494"           |
        | "GB00026349468"         |
        | 2018-09-01T00:00:00.001 |
    Then the following data should be generated:
      | foo            |
      | "GB0002634946" |
      | null           |

  Scenario: Running an 'aValid' request alongside an afterOrAt constraint should be successful
    Given foo is a valid "ISIN"
      And foo is after or at 2018-09-01T00:00:00.000
      And foo is in set:
        | "GB0002634946"          |
        | "GB000263494"           |
        | "GB00026349468"         |
        | 2018-09-01T00:00:00.001 |
    Then the following data should be generated:
      | foo            |
      | "GB0002634946" |
      | null           |

  Scenario: Running an 'aValid' request alongside a before constraint should be successful
    Given foo is a valid "ISIN"
    And foo is before 2018-09-01T00:00:00.000
    And foo is in set:
      | "GB0002634946"          |
      | "GB000263494"           |
      | "GB00026349468"         |
      | 2018-08-01T00:00:00.000 |
    Then the following data should be generated:
      | foo            |
      | "GB0002634946" |
      | null           |

  Scenario: Running an 'aValid' request alongside a beforeOrAt constraint should be successful
    Given foo is a valid "ISIN"
      And foo is before or at 2018-09-01T00:00:00.000
      And foo is in set:
        | "GB0002634946"          |
        | "GB000263494"           |
        | "GB00026349468"         |
        | 2018-08-01T00:00:00.000 |
    Then the following data should be generated:
      | foo            |
      | "GB0002634946" |
      | null           |

  Scenario: Running an 'aValid' request with a not constraint should be successful
    Given foo is anything but a valid "ISIN"
    And foo is in set:
      | "333"                     |
      | 123                       |
      | 2018-09-01T00:00:00.000   |
      | "GB0002634946"            |
    Then the following data should be generated:
      | foo                       |
      | null                      |
      | "333" |
    And the following data should not be included in what is generated:
      | foo            |
      | "GB0002634946" |

    @ignore #failing due to null duplication see issue 91 and issue with string contradictions alongside ISIN issue 487
  Scenario: Running an 'aValid' request as part of a non-contradicting anyOf constraint should be successful
    Given there is a constraint:
      """
       { "anyOf": [
         { "field": "foo", "is": "aValid", "value": "ISIN" },
         { "field": "foo", "is": "ofLength", "value": 1 }
       ]}
      """
    And foo is in set:
      | "GB0002634946" |
      | "1"            |
      | "GB0002634947" |
      | "333"          |
    Then the following data should be generated:
      | foo            |
      | null           |
      | "GB0002634946" |
      | "1"            |
    And the following data should not be included in what is generated:
      | foo            |
      | "GB0002634947" |
      | "333"          |

  Scenario: Running an 'aValid' request as part of a non-contradicting allOf constraint should be successful
    Given there is a constraint:
      """
        { "allOf": [
           { "field": "foo", "is": "aValid", "value": "ISIN" },
           { "field": "foo", "is": "equalTo", "value": "GB0002634946" }
        ]}
      """
    Then the following data should be generated:
      | foo            |
      | null           |
      | "GB0002634946" |

  Scenario: Running an 'aValid' request as part of a contradicting allOf constraint should only emit null
    Given there is a constraint:
      """
        { "allOf": [
           { "field": "foo", "is": "aValid", "value": "ISIN" },
           { "field": "foo", "is": "equalTo", "value": "GB0002634947" }
        ]}
      """
    Then the following data should be generated:
      | foo            |
      | null           |