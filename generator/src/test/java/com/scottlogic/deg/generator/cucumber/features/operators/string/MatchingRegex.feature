Feature: User can specify that a value either matches or contains a specified regex

Background:
     Given the generation strategy is full
      And there is a field foo
      And foo is of type "string"

Scenario: Running a 'matchingRegex' request that includes roman alphabet lowercase chars (a-z) only should be successful
     Given foo is matching regex /[a-z]{1}/
     Then the following data should be generated:
       | foo  |
       | null |
       | "a"  |
       | "b"  |
       | "c"  |
       | "d"  |
       | "e"  |
       | "f"  |
       | "g"  |
       | "h"  |
       | "i"  |
       | "j"  |
       | "k"  |
       | "l"  |
       | "m"  |
       | "n"  |
       | "o"  |
       | "p"  |
       | "q"  |
       | "r"  |
       | "s"  |
       | "t"  |
       | "u"  |
       | "v"  |
       | "w"  |
       | "x"  |
       | "y"  |
       | "z"  |

Scenario: Running a 'matchingRegex' request that includes roman alphabet uppercase chars (A-Z) only should be successful
     Given foo is matching regex /[A-Z]{1}/
     Then the following data should be generated:
       | foo  |
       | null |
       | "A"  |
       | "B"  |
       | "C"  |
       | "D"  |
       | "E"  |
       | "F"  |
       | "G"  |
       | "H"  |
       | "I"  |
       | "J"  |
       | "K"  |
       | "L"  |
       | "M"  |
       | "N"  |
       | "O"  |
       | "P"  |
       | "Q"  |
       | "R"  |
       | "S"  |
       | "T"  |
       | "U"  |
       | "V"  |
       | "W"  |
       | "X"  |
       | "Y"  |
       | "Z"  |

Scenario: Running a 'matchingRegex' request that includes roman numeric chars (0-9) only should be successful
     Given foo is matching regex /[0-9]{1}/
     Then the following data should be generated:
       | foo  |
       | null |
       | "0"  |
       | "1"  |
       | "2"  |
       | "3"  |
       | "4"  |
       | "5"  |
       | "6"  |
       | "7"  |
       | "8"  |
       | "9"  |

Scenario: Running a 'matchingRegex' request that includes basic punctuation characters (!-.) only should be successful
     Given foo is matching regex /[!-.]{1}/
     Then the following data should be generated:
       | foo  |
       | null |
       | "!"  |
       | """  |
       | "#"  |
       | "$"  |
       | "%"  |
       | "&"  |
       | "'"  |
       | "("  |
       | ")"  |
       | "*"  |
       | "+"  |
       | ","  |
       | "-"  |
       | "."  |

Scenario: Running a 'matchingRegex' request that includes special characters (non roman character maps: Hiragana) should be successful
     Given foo is matching regex /[あ-げ]{1}/
     Then the following data should be generated:
       | foo  |
       | null |
       | "あ" |
       | "ぃ" |
       | "い" |
       | "ぅ" |
       | "う" |
       | "ぇ" |
       | "え" |
       | "ぉ" |
       | "お" |
       | "か" |
       | "が" |
       | "き" |
       | "ぎ" |
       | "く" |
       | "ぐ" |
       | "け" |
       | "げ" |

Scenario: Running a 'matchingRegex' request that includes special characters (emoji) only should be successful
     Given foo is matching regex /[😁-😘]{1}/
     Then the following data should be generated:
      | foo  |
      | null |

Scenario: Running a 'matchingRegex' request that includes anchors ^ and $ should be successful
     Given foo is matching regex /^[a-c]{2}$/
     Then the following data should be generated:
       | foo  |
       | null |
       | "aa" |
       | "ab" |
       | "ac" |
       | "ba" |
       | "bb" |
       | "bc" |
       | "ca" |
       | "cb" |
       | "cc" |

Scenario: Running a 'matchingRegex' request that includes only anchor ^ should be successful
     Given foo is matching regex /^[a-c]{2}/
     Then the following data should be generated:
       | foo  |
       | null |
       | "aa" |
       | "ab" |
       | "ac" |
       | "ba" |
       | "bb" |
       | "bc" |
       | "ca" |
       | "cb" |
       | "cc" |

Scenario: Running a 'matchingRegex' request that includes only anchor $ should be successful
     Given foo is matching regex /[a-c]{2}$/
     Then the following data should be generated:
       | foo  |
       | null |
       | "aa" |
       | "ab" |
       | "ac" |
       | "ba" |
       | "bb" |
       | "bc" |
       | "ca" |
       | "cb" |
       | "cc" |

Scenario: Running a 'matchingRegex' request for a single character (a) should be successful
     Given foo is matching regex /[a]{1}/
     Then the following data should be generated:
       | foo  |
       | null |
       | "a"  |

Scenario: Running a 'matchingRegex' request for a range over a single character ([a-a]) should be successful
     Given foo is matching regex /[a-a]{1}/
     Then the following data should be generated:
       | foo  |
       | null |
       | "a"  |

Scenario: Running a 'matchingRegex' for a minimum length of 0 should be successful
     Given foo is matching regex /[a]{0,1}/
     Then the following data should be generated:
       | foo  |
       | null |
       | ""   |
       | "a"  |

Scenario: Running a 'matchingRegex' for a maximum length smaller than the minimum length should fail with an error
     Given foo is matching regex /[a]{1,0}/
     Then I am presented with an error message
       And no data is created

Scenario: Running a 'matchingRegex' for a minimum length of a decimal value should fail with an error
     Given foo is matching regex /[a]{1.1}/
     Then I am presented with an error message
       And no data is created

Scenario: Running a 'matchingRegex' for a minimum length that is less zero should fail with an error message
     Given foo is matching regex /[a]{-1}/
     Then I am presented with an error message
       And no data is created

Scenario: Running a 'matchingRegex' with an empty regex should fail with an error message
     Given foo is matching regex /[]{}/
     Then I am presented with an error message
       And no data is created

Scenario: Running a 'matchingRegex' request with the value property set to a null entry (null) should throw an error
     Given there is a field foo
       And foo is matching regex null
     Then the profile is invalid because "Couldn't recognise 'value' property, it must be set to a value"
       And no data is created

Scenario: User using matchingRegex operator to provide an exact set of values
     Given foo is matching regex /[a]{1,3}/
       And foo is anything but null
     Then the following data should be generated:
       | foo   |
       | "a"   |
       | "aa"  |
       | "aaa" |

Scenario: Running a 'matchingRegex' request alongside a non-contradicting matchingRegex constraint should be successful
     Given foo is matching regex /[a-z]{1,3}/
       And foo is matching regex /[b]{2}/
     Then the following data should be generated:
       | foo  |
       | null |
       | "bb" |

Scenario: matchingRegex run against a non contradicting not matchingRegex should be successful
     Given foo is matching regex /[a]{1}/
       And foo is anything but matching regex /[b]{2}/
     Then the following data should be generated:
       | foo  |
       | null |
       | "a" |

Scenario: Running a 'matchingRegex' request alongside a contradicting matchingRegex constraint should generate null
     Given foo is matching regex /[a]{1}/
       And foo is matching regex /[b]{1}/
      Then the following data should be generated:
        | foo   |
        | null  |

Scenario: matchingRegex run against a contradicting matchingRegex length should only generate null
     Given foo is matching regex /[a]{1}/
       And foo is matching regex /[a]{2}/
     Then the following data should be generated:
       | foo   |
       | null  |

Scenario: Running a 'matchingRegex' request alongside a contradicting matchingRegex constraint should generate null
     Given foo is matching regex /[a]{1}/
       And foo is anything but matching regex /[a]{1}/
     Then the following data should be generated:
       | foo   |
       | null  |

Scenario: Running a 'matchingRegex' request alongside a non-contradicting containingRegex constraint should be successful
     Given foo is matching regex /[b]{2}/
       And foo is containing regex /[a-z]{1,3}/
     Then the following data should be generated:
       | foo  |
       | null |
       | "bb" |

Scenario: matchingRegex run against a non contradicting not containingRegex should be successful
     Given foo is matching regex /[a]{1}/
       And foo is anything but containing regex /[7]{1}/
     Then the following data should be generated:
       | foo  |
       | null |
       | "a"  |

Scenario: matchingRegex run against a non contradicting not containingRegex should be successful
     Given foo is anything but matching regex /[a]{1}/
       And foo is containing regex /[7]{1}/
       And foo is of length 1
     Then the following data should be generated:
       | foo  |
       | null |
       | "7"  |

Scenario: Running a 'matchingRegex' request alongside a contradicting containingRegex constraint should generate null
     Given foo is matching regex /[b]{3}/
       And foo is containing regex /[a]{1,2}/
      Then the following data should be generated:
        | foo   |
        | null  |

Scenario: matchingRegex run against a contradicting containingRegex length should only generate null
     Given foo is matching regex /[b]{3}/
       And foo is containing regex /[b]{7}/
     Then the following data should be generated:
       | foo   |
       | null  |

Scenario: matchingRegex run against a contradicting not containingRegex should only generate null
     Given foo is matching regex /[a]{1}/
       And foo is anything but containing regex /[a]{1}/
     Then the following data should be generated:
       | foo   |
       | null  |

Scenario: matchingRegex run against a non contradicting ofLength should be successful
     Given foo is matching regex /[b]{2}/
       And foo is of length 2
     Then the following data should be generated:
       | foo  |
       | null |
       | "bb" |

Scenario: matchingRegex run against a non contradicting not ofLength should be successful
     Given foo is matching regex /[a]{1}/
       And foo is anything but of length 5
     Then the following data should be generated:
       | foo  |
       | null |
       | "a"  |

@ignore #626
Scenario: not matchingRegex run against a non contradicting ofLength should be successful
     Given foo is matching regex /[a-z]{1}/
       And foo is anything but matching regex /[a]{1}/
       And foo is of length 1
       And the generator can generate at most 5 rows
     Then the following data should be generated:
       | foo  |
       | null |
       | "b"  |
       | "c"  |
       | "d"  |
       | "e"  |

Scenario: matchingRegex run against a contradicting ofLength should only generate null
     Given foo is matching regex /[b]{2}/
       And foo is of length 1
     Then the following data should be generated:
       | foo   |
       | null  |

Scenario: matchingRegex run against a contradicting not ofLength should only generate null
     Given foo is matching regex /[b]{2}/
       And foo is anything but of length 2
     Then the following data should be generated:
       | foo   |
       | null  |

Scenario: matchingRegex run against a non contradicting longerThan should be successful
     Given foo is matching regex /[b]{2}/
       And foo is longer than 1
     Then the following data should be generated:
       | foo  |
       | null |
       | "bb" |

Scenario: matchingRegex run against a non contradicting not longerThan should be successful
     Given foo is matching regex /[b]{2}/
       And foo is anything but longer than 3
     Then the following data should be generated:
       | foo  |
       | null |
       | "bb" |

Scenario: not matchingRegex run against a non contradicting longerThan should be successful
     Given foo is matching regex /[b]{2}/
       And foo is anything but longer than 3
     Then the following data should be generated:
       | foo  |
       | null |
       | "bb" |

Scenario: matchingRegex run against a contradicting longerThan should only generate null
     Given foo is matching regex /[b]{2}/
       And foo is longer than 2
      Then the following data should be generated:
        | foo   |
        | null  |

Scenario: matchingRegex run against a contradicting not longerThan should only generate null
     Given foo is matching regex /[b]{2}/
       And foo is anything but longer than 1
     Then the following data should be generated:
       | foo   |
       | null  |

Scenario: matchingRegex run against a non contradicting shorterThan should be successful
       Given foo is matching regex /[b]{1}/
       And foo is shorter than 2
     Then the following data should be generated:
       | foo  |
       | null |
       | "b"  |

Scenario: matchingRegex run against a non contradicting not shorterThan should be successful
     Given foo is matching regex /[b]{2}/
       And foo is anything but shorter than 1
     Then the following data should be generated:
       | foo  |
       | null |
       | "bb" |

@ignore #626
Scenario: not matchingRegex run against a non contradicting shorterThan should be successful
     Given foo is matching regex /[a-h]{1}/
       And foo is anything but matching regex /[b]{1}/
       And foo is shorter than 2
       And the generator can generate at most 5 rows
     Then the following data should be generated:
       | foo  |
       | null |
       | "a"  |
       | "c"  |
       | "d"  |
       | "e"  |

Scenario: matchingRegex run against a contradicting shorterThan should only generate null
     Given foo is matching regex /[b]{2}/
       And foo is shorter than 2
      Then the following data should be generated:
        | foo   |
        | null  |

Scenario: matchingRegex run against a contradicting not shorterThan should only generate null
     Given foo is matching regex /[b]{1}/
       And foo is anything but shorter than 2
     Then the following data should be generated:
        | foo  |
        | null |

Scenario: matchingRegex string run against a non contradicting greaterThan should be successful
     Given foo is matching regex /[b]{2}/
       And foo is greater than 1
     Then the following data should be generated:
       | foo  |
       | null |
       | "bb" |

  Scenario: matchingRegex string run against a non contradicting not greaterThan should be successful
    Given foo is matching regex /[b]{2}/
    And foo is anything but greater than 1
    Then the following data should be generated:
      | foo  |
      | null |
      | "bb" |

Scenario: matchingRegex string run against a non contradicting greaterThanOrEqualTo should be successful
     Given foo is matching regex /[b]{2}/
       And foo is greater than or equal to 1
     Then the following data should be generated:
       | foo  |
       | null |
       | "bb" |

Scenario: matchingRegex string run against a non contradicting not greaterThanOrEqualTo should be successful
     Given foo is matching regex /[b]{2}/
       And foo is anything but greater than or equal to 1
     Then the following data should be generated:
       | foo  |
       | null |
       | "bb" |

Scenario: matchingRegex string run against a non contradicting lessThan should be successful
     Given foo is matching regex /[b]{2}/
       And foo is less than 3
     Then the following data should be generated:
        | foo  |
        | null |
        | "bb" |

Scenario: matchingRegex string run against a non contradicting not lessThan should be successful
     Given foo is matching regex /[b]{2}/
       And foo is anything but less than 3
     Then the following data should be generated:
       | foo  |
       | null |
       | "bb" |

Scenario: matchingRegex string run against a non contradicting lessThanOrEqualTo should be successful
    Given foo is matching regex /[b]{2}/
       And foo is less than or equal to 3
    Then the following data should be generated:
      | foo  |
      | null |
      | "bb" |

Scenario: matchingRegex string run against a non contradicting not lessThanOrEqualTo should be successful
     Given foo is matching regex /[b]{2}/
       And foo is anything but less than or equal to 3
     Then the following data should be generated:
       | foo  |
       | null |
       | "bb" |

Scenario: Running a 'matchingRegex' request alongside a granularTo constraint should should be successful
    Given foo is matching regex /[0-1]{2}/
       And foo is granular to 1
    Then the following data should be generated:
      | foo  |
      | null |
      | "00" |
      | "11" |
      | "01" |
      | "10" |

Scenario: matchingRegex string run against a non contradicting not granularTo should be successful
     Given foo is matching regex /[0-1]{2}/
       And foo is anything but granular to 1
     Then the following data should be generated:
       | foo  |
       | null |
       | "00" |
       | "11" |
       | "01" |
       | "10" |

# Defect 626 "null data is not created when regex is applied with a datetime value" is related to this scenario
@ignore
Scenario: matchingRegex string run against a non contradicting after should be successful
     Given foo is matching regex /[a-e]{2}/
       And foo is after 2018-10-10T00:00:00.000Z
       And the generator can generate at most 5 rows
     Then the following data should be generated:
        | foo  |
        | null |
        | "aa" |
        | "ab" |
        | "ac" |
        | "ad" |

# Defect 626 "null data is not created when regex is applied with a datetime value" is related to this scenario
@ignore
Scenario: matchingRegex string run against a non contradicting not after should be successful
     Given foo is matching regex /[a-e]{2}/
       And foo is anything but after 2018-10-10T00:00:00.000Z
       And the generator can generate at most 5 rows
     Then the following data should be generated:
       | foo  |
       | null |
       | "aa" |
       | "ab" |
       | "ac" |
       | "ad" |

# Defect 626 "null data is not created when regex is applied with a datetime value" is related to this scenario
@ignore
Scenario: matchingRegex string run against a non contradicting afterOrAt should be successful
     Given foo is matching regex /[a-e]{2}/
       And foo is after or at 2018-10-10T00:00:00.000Z
       And the generator can generate at most 5 rows
     Then the following data should be generated:
       | foo  |
       | null |
       | "aa" |
       | "ab" |
       | "ac" |
       | "ad" |

# Defect 626 "null data is not created when regex is applied with a datetime value" is related to this scenario
@ignore
Scenario: matchingRegex string run against a non contradicting not afterOrAt should be successful
     Given foo is matching regex /[a-e]{2}/
       And foo is anything but after or at 2018-10-10T00:00:00.000Z
       And the generator can generate at most 5 rows
     Then the following data should be generated:
       | foo  |
       | null |
       | "aa" |
       | "ab" |
       | "ac" |
       | "ad" |

# Defect 626 "null data is not created when regex is applied with a datetime value" is related to this scenario
@ignore
Scenario: matchingRegex string run against a non contradicting before should be successful
     Given foo is matching regex /[a-e]{2}/
       And foo is before 2018-10-10T00:00:00.000Z
       And the generator can generate at most 5 rows
     Then the following data should be generated:
       | foo  |
       | null |
       | "aa" |
       | "ab" |
       | "ac" |
       | "ad" |

# Defect 626 "null data is not created when regex is applied with a datetime value" is related to this scenario
@ignore
Scenario: matchingRegex string run against a non contradicting not before should be successful
     Given foo is matching regex /[a-e]{2}/
       And foo is anything but before 2018-10-10T00:00:00.000Z
       And the generator can generate at most 5 rows
     Then the following data should be generated:
       | foo  |
       | null |
       | "aa" |
       | "ab" |
       | "ac" |
       | "ad" |

# Defect 626 "null data is not created when regex is applied with a datetime value" is related to this scenario
@ignore
Scenario: matchingRegex string run against a non contradicting beforeOrAt should be successful
     Given foo is matching regex /[a-e]{2}/
       And foo is before or at 2018-10-10T00:00:00.000Z
       And the generator can generate at most 5 rows
     Then the following data should be generated:
       | foo  |
       | null |
       | "aa" |
       | "ab" |
       | "ac" |
       | "ad" |

# Defect 626 "null data is not created when regex is applied with a datetime value" is related to this scenario
@ignore
Scenario: matchingRegex string run against a non contradicting not beforeOrAt should be successful
     Given foo is matching regex /[a-e]{2}/
       And foo is before or at 2018-10-10T00:00:00.000Z
       And the generator can generate at most 5 rows
     Then the following data should be generated:
       | foo  |
       | null |
       | "aa" |
       | "ab" |
       | "ac" |
       | "ad" |

Scenario: Running a 'matchingRegex' and 'inSet' and 'integer' request nulls are generated last
     Given there is a field bar
       And the combination strategy is exhaustive
       And foo is matching regex /[a]{1}/
       And bar is in set:
         | "AA" |
       And there is a field lee
       And lee is of type "integer"
       And lee is granular to 1
       And lee is less than 2
       And lee is greater than 0
     Then the following data should be generated:
       | foo  | bar  | lee  |
       | "a"  | "AA" | 1    |
       | "a"  | "AA" | null |
       | "a"  | null | 1    |
       | "a"  | null | null |
       | null | "AA" | 1    |
       | null | "AA" | null |
       | null | null | 1    |
       | null | null | null |