# DataHelix Profile Grammar

the following syntax diagrams were produced using this [online diagram creator](https://www.bottlecaps.de/rr) from the [DataHelix grammar BNF](../json/datahelix.profile.bnf):


## <a id="profile">profile:</a>
![](profile-grammar/profile.png)
##### no references


## <a id="schemaVersion">schemaVersion:</a>
The DataHelix Profile version number will be in [Semantic Version](https://semver.org/) format with the patch level being optional.\
![](profile-grammar/schemaVersion.png)
##### referenced by:
_**[profile](#profile)**_


## <a id="schemaDescription">schemaDescription:</a>
This is a free text field which can be used to describe what data the profile is modelling\
![](profile-grammar/schemaDescription.png)
##### referenced by:
_**[profile](#profile)**_



## <a id="fields">fields:</a>
![](profile-grammar/fields.png)
##### referenced by:
_**[profile](#profile)**_


## <a id="fieldName">fieldName:</a>
![](profile-grammar/fieldName.png)
##### referenced by:
_**[fields](#fields)**_



## <a id="rules">rules:</a>
![](profile-grammar/rules.png)
##### referenced by:</a>
_**[profile](#profile)**_


## <a id="rule">rule:</a>
![](profile-grammar/rule.png)
##### referenced by:</a>
_**[rules](#rules)**_



## <a id="constraint">constraint:</a>
Constraints are described in more detail in the following documents:
 * [EpistemicConstraints.md](EpistemicConstraints.md)
 * [GrammaticalConstraints.md](GrammaticalConstraints.md)
 * [PresentationalConstraints.md](PresentationalConstraints.md)
 
![](profile-grammar/constraint.png)
##### referenced by:
_**[rule](#rule)**_\
_**[not](#not)**_\
_**[anyOf](#anyOf)**_\
_**[allOf](#allOf)**_\
_**[if](#if)**_


## <a id="grammaticalConstraint">grammaticalConstraint:</a>
![](profile-grammar/grammaticalConstraint.png)
##### referenced by:
_**[constraint](#constraint)**_



## <a id="presentationalConstraint">presentationalConstraint:</a>
![](profile-grammar/presentationalConstraint.png)
##### referenced by:
_**[constraint](#constraint)**_



## <a id="epistemicConstraint">epistemicConstraint:</a>
![](profile-grammar/epistemicConstraint.png)
##### referenced by:
_**[constraint](#constraint)**_



## <a id="not">not:</a>
![](profile-grammar/not.png)
##### referenced by:
_**[grammaticalConstraint](#grammaticalConstraint)**_


## <a id="anyOf">anyOf:</a>
![](profile-grammar/anyOf.png)
##### referenced by:
_**[grammaticalConstraint](#grammaticalConstraint)**_


## <a id="allOf">allOf:</a>
![](profile-grammar/allOf.png)
##### referenced by:
_**[grammaticalConstraint](#grammaticalConstraint)**_


## <a id="if">if:</a>
![](profile-grammar/if.png)
##### referenced by:
_**[grammaticalConstraint](#grammaticalConstraint)**_


## <a id="ofType">ofType:</a>
![](profile-grammar/ofType.png)
##### referenced by:
_**[epistemicConstraint](#epistemicConstraint)**_


## <a id="stringConstraint">stringConstraint:</a>
![](profile-grammar/stringConstraint.png)
##### referenced by:
_**[epistemicConstraint](#epistemicConstraint)**_


## <a id="numberConstraint">numberConstraint:</a>
![](profile-grammar/numberConstraint.png)
##### referenced by:
_**[epistemicConstraint](#epistemicConstraint)**_


## <a id="temporalConstraint">temporalConstraint:</a>
![](profile-grammar/temporalConstraint.png)
##### referenced by:
_**[epistemicConstraint](#epistemicConstraint)**_


## <a id="inSet">inSet:</a>
![](profile-grammar/inSet.png)
##### referenced by:
_**[epistemicConstraint](#epistemicConstraint)**_


## <a id="equalTo">equalTo:</a>
![](profile-grammar/equalTo.png)
##### referenced by:
_**[epistemicConstraint](#epistemicConstraint)**_


## <a id="formattedAs">formattedAs:</a>
![](profile-grammar/formattedAs.png)
##### referenced by:
_**[presentationalConstraint](#presentationalConstraint)**_


## <a id="temporal">temporal:</a>
![](profile-grammar/temporal.png)
##### referenced by:
_**[equalTo](#equalTo)**_\
_**[inSet](#inSet)**_\
_**[temporalConstraint](#temporalConstraint)**_



## <a id="temporalValue">temporalValue:</a>
A temporal value is a representation of an 
[ISO 8601-1:2019](https://www.iso.org/standard/70907.html)
formatted date time.\
![](profile-grammar/temporalValue.png)
##### referenced by:num
_**[temporal](#temporal)**_


## temporalYear:
![](profile-grammar/temporalYear.png)
##### referenced by:
_**[temporalValue](#temporalValue)**_



## temporalMonth:
![](profile-grammar/temporalMonth.png)
##### referenced by:
_**[temporalValue](#temporalValue)**_



## temporalDay:
![](profile-grammar/temporalDay.png)
##### referenced by:
_**[temporalValue](#temporalValue)**_



## temporalHour:
![](profile-grammar/temporalHour.png)
##### referenced by:
_**[temporalValue](#temporalValue)**_



## temporalMinute:
![](profile-grammar/temporalMinute.png)
##### referenced by:
_**[temporalValue](#temporalValue)**_


## temporalSecond:
![](profile-grammar/temporalSecond.png)
##### referenced by:
_**[temporalValue](#temporalValue)**_



## temporalMillisec:
![](profile-grammar/temporalMillisec.png)
##### referenced by:
_**[temporalValue](#temporalValue)**_



## <a id="string">string:</a>
![](profile-grammar/string.png)
##### referenced by:
_**[equalTo](#equalTo)**_\
_**[fieldNameString](#fieldNameString)**_\
_**[inSet](#inSet)**_\
_**[ruleDescription](#ruleDescription)**_\
_**[schemaDescription](#schemaDescription)**_\
_**[stringConstraint](#stringConstraint)**_\



## <a id="stringFormatPattern">stringFormatPattern:</a>
![](profile-grammar/stringFormatPattern.png)
##### referenced by:
_**[formattedAs](#formattedAs)**_



## <a id="character">character:</a>
![](profile-grammar/character.png)
##### referenced by:
_**[stringFormatPattern](#stringFormatPattern)**_\
_**[string](#string)**_


## unescapedChar:
![](profile-grammar/unescapedChar.png)
##### referenced by:
_**[character](#character)**_


## escapedChar:
![](profile-grammar/escapedChar.png)
##### referenced by:
_**[character](#character)**_

## <a id="hexDigit">hexDigit:</a>
![](profile-grammar/hexDigit.png)
##### referenced by:
_**[escapedChar](#escapedChar)**_



## <a id="number">number:</a>
![](profile-grammar/number.png)
##### referenced by:
_**[equalTo](#equalTo)**_\
_**[inSet](#inSet)**_\
_**[numberConstraint](#numberConstraint)**_\
_**[stringConstraint](#stringConstraint)**_



## <a id="digit">digit:</a>
![](profile-grammar/digit.png)
##### referenced by:
_**[hexDigit](#hexDigit)**_\
_**[number](#number)**_\
_**[schemaVersion](#schemaVersion)**_\
_**[temporalDay](#temporalDay)**_\
_**[temporalHour](#temporalHour)**_\
_**[temporalMillisec](#temporalMillisec)**_\
_**[temporalMinute](#temporalMinute)**_\
_**[temporalMonth](#temporalMonth)**_\
_**[temporalSecond](#temporalSecond)**_\
_**[temporalYear](#temporalYear)**_
