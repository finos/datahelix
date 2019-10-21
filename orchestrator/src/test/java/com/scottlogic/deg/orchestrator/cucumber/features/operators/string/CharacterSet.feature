Feature: Strings will be generated using characters from only latin characters

  Background:
    Given the generation strategy is full
    And there is a non nullable field foo
    And foo has type "string"

  Scenario: Running a 'matchingRegex' request that includes roman alphabet lowercase chars (a-z) only should be successful
    Given foo is matching regex /./
    Then the following data should be generated:
      | foo  |
      | " "  |
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
      | "/"  |
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
      | ":"  |
      | ";"  |
      | "<"  |
      | "="  |
      | ">"  |
      | "?"  |
      | "@"  |
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
      | "["  |
      | "\"  |
      | "]"  |
      | "^"  |
      | "_"  |
      | "`"  |
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
      | "{"  |
      | "\|"  |
      | "}"  |
      | "~"  |
