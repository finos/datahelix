Feature: User can specify that contains a specified regex

Background:
     Given the generation strategy is full
       And foo is of type "string"

Scenario: Running a 'containingRegex' request that includes roman alphabet lowercase chars (a-z) only should be successful
     Given there is a field foo
       And foo is containing regex /[a-z]{1}/
       And foo is of length 1
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

Scenario: Running a 'containingRegex' request that includes roman alphabet uppercase chars (A-Z) only should be successful
     Given there is a field foo
       And foo is containing regex /[A-Z]{1}/
       And foo is of length 1
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

Scenario: Running a 'containingRegex' request that includes roman numeric chars (0-9) only should be successful
     Given there is a field foo
       And foo is containing regex /[0-9]{1}/
       And foo is of length 1
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

Scenario: Running a 'containingRegex' request that includes basic punctuation characters (!-.) only should be successful
     Given there is a field foo
       And foo is containing regex /[!-.]{1}/
       And foo is of length 1
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

Scenario: Running a 'containingRegex' request that includes special characters (non roman character maps: Hiragana) should be successful
     Given there is a field foo
       And foo is containing regex /[あ-げ]{1}/
       And foo is of length 1
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

@ignore #issue 294
Scenario: Running a 'containingRegex' request that includes special characters (emoji) only should be successful
     Given there is a field foo
       And foo is containing regex /[😁-😘]{1}/
       And foo is of length 1
     Then the following data should be generated:
       | foo  |
       | null |
       | "😁" |
       | "😂" |
       | "😃" |
       | "😄" |
       | "😅" |
       | "😆" |
       | "😉" |
       | "😊" |
       | "😋" |
       | "😌" |
       | "😍" |
       | "😏" |
       | "😒" |
       | "😓" |
       | "😔" |
       | "😖" |
       | "😘" |

Scenario: Running a 'containingRegex' request that includes anchors ^ and $ should be successful
     Given there is a field foo
       And foo is containing regex /^[a-c]{1}$/
       And foo is of length 1
     Then the following data should be generated:
       | foo  |
       | null |
       | "a"  |
       | "b"  |
       | "c"  |


Scenario: Running a 'containingRegex' request that includes only anchor ^ should be successful
     Given there is a field foo
       And foo is containing regex /^[a-c]{1}/
       And foo is of length 1
     Then the following data should be generated:
       | foo  |
       | null |
       | "a"  |
       | "b"  |
       | "c"  |

Scenario: Running a 'containingRegex' request that includes only anchor $ should be successful
     Given there is a field foo
       And foo is containing regex /[a-c]{1}$/
       And foo is of length 1
     Then the following data should be generated:
       | foo  |
       | null |
       | "a"  |
       | "b"  |
       | "c"  |

Scenario: Running a 'containingRegex' request for a single character (a) should be successful
     Given there is a field foo
       And foo is containing regex /[a]{1}/
       And foo is of length 1
     Then the following data should be generated:
       | foo  |
       | null |
       | "a"  |

Scenario: Running a 'containingRegex' request for a range over a single character ([a-a]) should be successful
     Given there is a field foo
       And foo is containing regex /[a-a]{1}/
       And foo is of length 1
     Then the following data should be generated:
       | foo  |
       | null |
       | "a"  |

Scenario: Running a 'containingRegex' for a maximum length smaller than the minimum length should fail with an error
     Given there is a field foo
       And foo is containing regex /[a]{1,0}/
     Then I am presented with an error message
       And no data is created

Scenario: Running a 'containingRegex' for a minimum length of a decimal value should fail with an error
     Given there is a field foo
       And foo is containing regex /[a]{1.1}/
     Then I am presented with an error message
       And no data is created

Scenario: Running a 'containingRegex' for a minimum length that is less zero should fail with an error message
     Given there is a field foo
       And foo is containing regex /[a]{-1}/
     Then I am presented with an error message
       And no data is created

Scenario: Running a 'containingRegex' request alongside a non-contradicting equalTo constraint should be successful
     Given there is a field foo
       And foo is containing regex /[a]{3}/
       And foo is equal to "aaa"
     Then the following data should be generated:
       | foo   |
       | null  |
       | "aaa" |

Scenario: Running a 'containingRegex' request alongside a contradicting equalTo constraint should produce null
     Given there is a field foo
       And foo is containing regex /[a]{3}/
       And foo is equal to "bbb"
     Then the following data should be generated:
       | foo  |
       | null |
       
Scenario: Running a 'containingRegex' request alongside a non-contradicting inSet constraint should be successful
     Given there is a field foo
       And foo is containing regex /[a]{1,3}/
       And foo is in set:
         | "a"   |
         | "aaa" |
       And foo is anything but null
       Then the following data should be generated:
       | foo   |
       | "a"   |
       | "aaa" |

Scenario: Running a 'containingRegex' request alongside a contradicting inSet constraint should produce null
     Given there is a field foo
       And foo is containing regex /[a]{1,3}/
       And foo is in set:
         | "b"   |
         | "bbb" |
     Then the following data should be generated:
         | foo  |
         | null |

Scenario: Running a 'containingRegex' request alongside a null constraint should be successful
     Given there is a field foo
       And foo is containing regex /[a]{1,3}/
       And foo is null
     Then the following data should be generated:
       | foo  |
       | null |

Scenario: Running a 'containingRegex' request alongside a non-contradicting matchingRegex constraint should be successful
     Given there is a field foo
       And foo is containing regex /[a-z]{1,3}/
       And foo is matching regex /[b]{2}/
       And foo is of length 2
     Then the following data should be generated:
       | foo  |
       | null |
       | "bb" |

Scenario: Running a 'containingRegex' request alongside a contradicting matchingRegex constraint should generate null
     Given there is a field foo
       And foo is containing regex /[a]{1}/
       And foo is matching regex /[b]{2}/
       And foo is in set:
        | "a"   |
        | "bb"  |
        | "abb" |
     Then the following data should be generated:
      | foo  |
      | null |

Scenario: Running a 'containingRegex' request alongside a non-contradicting containingRegex constraint should be successful
     Given there is a field foo
       And foo is containing regex /[b]{2}/
       And foo is containing regex /[a-z]{1,3}/
       And foo is of length 2
     Then the following data should be generated:
       | foo  |
       | null |
       | "bb" |

Scenario: Running a 'containingRegex' request alongside a contradicting containingRegex constraint should be successful
     Given there is a field foo
       And foo is containing regex /[b]{3}/
       And foo is containing regex /[a]{1,2}/
       And foo is in set:
          | "a"      |
          | "aa"     |
          | "bbb"    |
          | "abbb"   |
          | "aabba"  |
          | "abbbaa" |
     Then the following data should be generated:
       | foo      |
       | null     |
       | "abbb"   |
       | "abbbaa" |

Scenario: Running a 'containingRegex' request alongside a non-contradicting ofLength constraint should be successful
     Given there is a field foo
       And foo is containing regex /[b]{2}/
       And foo is of length 2
     Then the following data should be generated:
       | foo  |
       | null |
       | "bb" |

Scenario: Running a 'containingRegex' request alongside a contradicting ofLength (too short) constraint should generate null
     Given there is a field foo
       And foo is containing regex /[b]{2}/
       And foo is of length 1
       And foo is in set:
         | "a"   |
         | "bb"  |
         | "abb" |
     Then the following data should be generated:
        | foo   |
        | null  |

Scenario: Running a 'containingRegex' request alongside a greaterThan constraint should be successful
     Given there is a field foo
       And foo is containing regex /[b]{2}/
       And foo is greater than 1
       And foo is in set:
        | "a"   |
        | "bb"  |
        | "abb" |
     Then the following data should be generated:
       | foo   |
       | null  |
       | "bb"  |
       | "abb" |

Scenario: Running a 'containingRegex' request alongside a greaterThanOrEqualTo constraint should be successful
     Given there is a field foo
       And foo is containing regex /[b]{2}/
       And foo is greater than or equal to 1
       And foo is in set:
        | "a"   |
        | "bb"  |
        | "abb" |
     Then the following data should be generated:
      | foo   |
      | null  |
      | "bb"  |
      | "abb" |

Scenario: Running a 'containingRegex' request alongside a lessThan constraint should be successful
     Given there is a field foo
       And foo is containing regex /[b]{2}/
       And foo is less than 3
       And foo is in set:
        | "a"   |
        | "bb"  |
        | "abb" |
     Then the following data should be generated:
      | foo   |
      | null  |
      | "bb"  |
      | "abb" |

Scenario: Running a 'containingRegex' request alongside a lessThanOrEqualTo constraint should be successful
     Given there is a field foo
       And foo is containing regex /[b]{2}/
       And foo is less than or equal to 3
       And foo is in set:
        | "a"   |
        | "bb"  |
        | "abb" |
     Then the following data should be generated:
      | foo   |
      | null  |
      | "bb"  |
      | "abb" |

Scenario: Running a 'containingRegex' request alongside a granularTo constraint should be successful
     Given there is a field foo
       And foo is containing regex /[0-1]{2}/
       And foo is granular to 1
       And foo is in set:
        | "00"  |
        | "11"  |
        | "010" |
        | "1"   |
        | "0"   |
     Then the following data should be generated:
        | foo   |
        | null  |
        | "00"  |
        | "11"  |
        | "010" |

Scenario: Running a 'containingRegex' request alongside a after constraint should be successful
     Given there is a field foo
       And foo is containing regex /[0-z]{23}/
       And foo is after 2018-10-10T00:00:00.000
       And foo is in set:
        | "abcdefghijklmnopqrstuvwxyz"                  |
        | "2018-10-10T00:00:00.001"                     |
        | "23232323232323232323232"                     |
        | "2018-10-10T00:00:00.000fhgunbsljbtfsiqrfbmh" |
     Then the following data should be generated:
       | foo                                           |
       | null                                          |
       | "abcdefghijklmnopqrstuvwxyz"                  |
       | "23232323232323232323232"                     |
       | "2018-10-10T00:00:00.000fhgunbsljbtfsiqrfbmh" |

Scenario: Running a 'containingRegex' request alongside a afterOrAt constraint should be successful
     Given there is a field foo
       And foo is containing regex /[0-z]{23}/
       And foo is after or at 2018-10-10T00:00:00.000
       And foo is in set:
        | "abcdefghijklmnopqrstuvwxyz"                  |
        | "2018-10-10T00:00:00.001"                     |
        | "23232323232323232323232"                     |
        | "2018-10-10T00:00:00.000fhgunbsljbtfsiqrfbmh" |
     Then the following data should be generated:
      | foo                                           |
      | null                                          |
      | "abcdefghijklmnopqrstuvwxyz"                  |
      | "23232323232323232323232"                     |
      | "2018-10-10T00:00:00.000fhgunbsljbtfsiqrfbmh" |

Scenario: Running a 'containingRegex' request alongside a before constraint should be successful
     Given there is a field foo
       And foo is containing regex /[0-z]{23}/
       And foo is before 2018-10-10T00:00:00.000
       And foo is in set:
        | "abcdefghijklmnopqrstuvwxyz"                  |
        | "2018-09-10T00:00:00.001"                     |
        | "23232323232323232323232"                     |
        | "2018-10-10T00:00:00.000fhgunbsljbtfsiqrfbmh" |
     Then the following data should be generated:
      | foo                                           |
      | null                                          |
      | "abcdefghijklmnopqrstuvwxyz"                  |
      | "23232323232323232323232"                     |
      | "2018-10-10T00:00:00.000fhgunbsljbtfsiqrfbmh" |

Scenario: Running a 'containingRegex' request alongside a beforeOrAt constraint should be successful
     Given there is a field foo
       And foo is containing regex /[0-z]{23}/
       And foo is before or at 2018-10-10T00:00:00.000
       And foo is in set:
        | "abcdefghijklmnopqrstuvwxyz"                  |
        | "2018-09-10T00:00:00.001"                     |
        | "23232323232323232323232"                     |
        | "2018-10-10T00:00:00.000fhgunbsljbtfsiqrfbmh" |
     Then the following data should be generated:
      | foo                                           |
      | null                                          |
      | "abcdefghijklmnopqrstuvwxyz"                  |
      | "23232323232323232323232"                     |
      | "2018-10-10T00:00:00.000fhgunbsljbtfsiqrfbmh" |

Scenario: Running a 'containingRegex' request with a not constraint should be successful
     Given there is a field foo
       And foo is anything but containing regex /[0-1]{1}/
       And foo is containing regex /[0-9]{1}/
       And foo is of length 1
     Then the following data should not be included in what is generated:
       | foo  |
       | "0"  |
       | "1"  |

  @ignore #failing due to null duplication issue 91
Scenario: Running a 'containingRegex' request as part of a non-contradicting anyOf constraint should be successful
     Given there is a field foo
       And there is a constraint:
       """
       { "anyOf": [
         { "field": "foo", "is": "containingRegex", "value": "[a-b]{1}" },
         { "field": "foo", "is": "containingRegex", "value": "[c-d]{1}" }
       ]}
       """
       And foo is of length 1
     Then the following data should be generated:
       | foo  |
       | null |
       | "a"  |
       | "b"  |
       | "c"  |
       | "d"  |

Scenario: Running a 'containingRegex' request as part of an if constraint should be successful
     Given the following fields exist:
       | foo   |
       | price |
       And foo is containing regex /[a-d]{1}/
       And there is a constraint:
       """
       {
         "if": { "field": "foo", "is": "containingRegex", "value": "[a-b]{1}" },
         "then": { "field": "price", "is": "equalTo", "value": 1 },
         "else": { "field": "price", "is": "equalTo", "value": 2 }
         }
       """
       And foo is of length 1
       And foo is anything but null
       And price is anything but null
     Then the following data should be generated:
       | foo | price |
       | "a" |  1    |
       | "b" |  1    |
       | "c" |  2    |
       | "d" |  2    |

  Scenario: Running a 'containingRegex' request with the value property set to a null entry (null) should throw an error
    Given there is a field foo
      And foo is containing regex null
    Then the profile is invalid because "Couldn't recognise 'value' property, it must be set to a value"
      And no data is created