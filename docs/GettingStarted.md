<div>
    <a href="https://finos.github.io/datahelix/">
        <img src="https://finos.github.io/datahelix/docs-header.png" />
    </a>
</div>

# Getting Started

The following guide gives a short introduction to the datahelix via a practical example.

For more comprehensive documentation please refer to the [user guide](UserGuide.md).

# Contents

  - [Downloading the release](#Downloading-the-release)
  - [Creating your first profile](#Creating-your-first-profile)
  - [Running the generator](#Running-the-generator)
  - [Adding constraints](#Adding-constraints)
  - [Data types](#Data-types)
  - [Generation modes](#Generation-modes)
  - [Generating large datasets](#Generating-large-datasets)
  - [Next steps](#Next-steps)

## Getting DataHelix

You can get a copy of DataHelix by one of the following means:
- Install via Chocolatey
- Download the zip file
- Clone and build the project

### Install via Chocolatey

[Chocolatey](https://chocolatey.org/) is a Windows package manager for applications, Data Helix - since v2.1.9 - has been released as a community package.
As such you can install the DataHelix generator by running

```
choco install datahelix --version=2.1.9
```

This will install the Java runtime environment as required, and setup 'shortcuts' allowing you to type `datahelix` from any directory. 

### Download the zip file

From version 2.1.8 DataHelix has been published as a zip file this contains:
1. The DataHelix generator (.jar file)
1. A series of scripts to simplify running on Windows or Unix systems

You can download the latest release any time from the [GitHub releases page](https://github.com/finos/datahelix/releases/). Extract the contents to a folder to be able to use the generator. You will need to install the Java Runtime Environment (at least 1.8) manually, see the [Java downloads page](https://www.java.com/en/download/manual.jsp). You can run `java -version` to detect which version of java you have installed currently.

The extracted folder will contain 2 sub-folders:
- `datahelix\bin` - this contains the helper scripts
- `datahelix\lib` - this contains the DataHelix program

If you add `datahelix\bin` to [your PATH environment variable](https://www.java.com/en/download/help/path.xml) then you'll be able to type `datahelix` from any directory.

Prior to 2.1.8 the .jar file was published directly, without any scripts or being bundled in a zip file.

### Clone and build the project
You are also welcome to download the source code and build the generator yourself. To do so, follow the instructions in the [Developer Guide](DeveloperGuide.md#Building).

Datahelix is under active development so expect new features and bug fixes. Please feel free to share any issues, feature requests, or ideas via the [GitHub issues page](https://github.com/finos/datahelix/issues).

## Creating your first profile

We are going to work through creating a profile to generate random personal data about some test users.

Profiles are JSON files that describe the data you want to generate. They are composed of:

-   **fields** - an array of uniquely named fields.
-   **constraints** - an array of restrictions on the types and ranges of data permitted for a given field.

We'll start by creating a simple profile containing a single field `username` with no constraints. Using  your favourite text editor, create the following JSON profile and save it as `profile.json`:

```json
{
    "fields": [{ "name": "username", "type": "string" }],
    "constraints": []
}
```

When manually writing profiles, we recommend using a text editor which can validate profiles using the datahelix schema. Instructions for how to setup automatic profile validation using VS Code can be found in the [Profile Validation documentation](user/ProfileValidation.md).

## Running the generator

Now extract the `datahelix.zip` file (downloaded from the [GitHub releases page](https://github.com/finos/datahelix/releases/)) into the same folder as the profile, open up a terminal, and execute the following:

```shell script
$ datahelix/bin/datahelix --max-rows=100 --replace --profile-file=profile.json --output-path=output.csv
```

The generator is a command line tool which reads a profile, and outputs data in CSV or JSON format. The `--max-rows=100` option tells the generator to create 100 rows of data, and the `--replace` option tells it to overwrite previously generated files. The compulsory `--profile-file` option specifies the name of the input profile, and the `--output-path` option specifies the location to write the output to. In `generate` mode `--output-path` is optional; the generator will default to standard output if it is not supplied. By default the generator outputs progress, in rows per second, to the standard error output. This can be useful when generating large volumes of data.

Use

```shell script
$ datahelix/bin/datahelix --help
```

or see [the User Guide](UserGuide.md#Command-Line-Arguments) to find the full range of command line arguments.

Alternatively you can use the [datahelix playground](https://finos.github.io/datahelix/playground/) to get a feel for what it is like to run the profile without downloading the JAR. Although the playground supports almost all features, there are some it does not, and we don't intend it to be a substitute for downloading and running the Datahelix Generator yourself.

If you open up `output.csv` you'll see something like the following:

```
username
"[EN6^!Sd^ 4Jha'~Z.0-M+B+m#N=\#>SuUh+T0o )%^6}mW8%#R>TeBGQq7v0_S<{tbtxSTF&:j\{}RU.>'{J-SW3b)rrH$6B~|R#:LU^'FxJ5'8)XV]8kPON-I~n?lZSO-]1&@bjH'rZ4NC<.[bk-x+?W!g yG?Uxc4sAv'~(0`'_1a-ynlPXDlH_nAL+$ChP#xq5q1lnc#}xP0B,?5XF,FZ6fO|z&TiVkKm.4[y>oZE#3N+<W#F""ex~6#!^$L\ipQHp<wl:f.U&Miz|e6pidz1` Kt7$|='A`%by^9O?pU5|EWE1IFCGw1<p7L.=zzc.Mad,{$]@EpomnP8DHq4UosSol4,""&)XpLu[VWI--/YzriJsU{zOcG9u^})~<fahg*Tx#scve_bEX9l#Cjqvl{kk8V{mn-}klTF3J|Qy0cOd]`QAjDV\@S\~xlQxM7MVJn\ zP*pm/|8-MU jqJCyYnyLL'dRA.N}4|dM<1y,F7`xfSXQ?/a6C|q{~1>R]mBhmes6Xb:DkyN>EX)UBQ]v,nM5n]`9-4>9NHN~.&:#SH0t><\b^,br.Yi2;kc-aJ-cq>xh7Q1{k[%^)>XL$ca$M"".m:/+AdqiTnXpf%z?7le""?Kk~Wv); <&R~`qn#Jp j""/Da Y~uTQx*aC<|D"
niIU8r.{y'idVK(ki2D[#N8{h?dP[D;
":dT+0YYW35/|91cwp^h)=wXe4!UzD<BSJZm|C>MX,`('kS),{UiuUin\h<PX#vGTa<2U7$O;qr:8VVF}IUaLU.jTqf4z<{m^do4%D)9=trJ|u?ML3c|p~vj\8tw8h:x,Kj&a8Y:#Ct42%PGxl""_zVu""BE/j1Pys'KuuS# Fgj`X#!ai|kjF8W?3K$82"
"O#G8aIx|?fIbVT92Dx7m:JM(~<:cm~oMBp)Vb)-.Ki<z:gE`m6Y@Q>OSr:` tz~Nt18;)-r \j=4)_trW$(P\_a] b\SgvhG?(@SPlK%qy]{e%s,=Tv>K0mq+A""RG0+UAMVCksoGG?(.{{6cV).W{KQQ6-:U;fmscoXuYea[JH@2{pLS%~lmCWqlD'%Qt'j%#5=[w'PT*[QyV^GTO4s`q-Z-@u->I<&@mN~8 a6[X7+""kTQs>W""?,>Gnwr[PkQT""foT=NSUTrdaVeR""n(""(AXPM (s92cU[n<B9`p)@[/!Vr]KtY^B3^Gn6XxU8CAc,~Q9J]xxI43-""U0>;W2{|qG7Wo%-z;h&dt%pjj%EBY4== /s*__~wbAZ#j1p5(0^;tA5Hbn{hnWoQf,,#)N\5{?:L:(9a)l=g>MrpAt;['Ny!Kg^F4Pul>wY`vUKWWh="
"!.@,1HYpac>&H9}\@Fw[B1bIYB0Nc5X o6}$7Cc$!K,a9ot#DQ<;PNxabe;EVcT,=t/'+ES\i(&(%?6dM${tq,~#z^zMYsZ)lFox:iA&$$?PP6zwOdHN/BC[}/VhF@VZV$Wb6U\=i6H/c\\?K_t?`~C&9Idnl7fYQGZfO^H)c`~b|k(wYh'Z{QWL :z}c(4]wV\B}oUq}AL@<YL.\Z u2ps09(J7C_r+h46=G=\<4DPdhS#*""M/aKv8DA`09V.)<n(+~P/elsC*Q?cwC+l]1fmE:dVu\QwAwX`#9^]ebl5]kw;NSe]%DU$~4Z_24TpEkt<X5Yy^${]D5Y{I^^W.DJ5pHHce.Md9z(!_amvPDWi q-)W6~[""u1YXT#[]Sa),ZKIb8DQ""!A1ampKd+,mA9l@Jy\[`()/]vv-RB%!)=0e)VIvo)DV2).`:[Jcl3r%DBSv?qle{NX;|4Vc26P`V+U]v3#Fv!_5b*2MrT!}1?B(X8ydMz*.v;lUn;g~z8tBQlCRw>b=8[;xx+]2$pFv#q?Feh<I-ZT,uF3 %""<X51%zUmSE_G*v}>evv{f5V we;d'6VD74_zt@<g,qL/oCqoC/89hrwYy LC ""k)ATeHcgZpYgm|H[nUNF/bu-UbCK;![;BAmIw9B<z}{N{azSLm5,#=F-fw[,=z6P:O+:B?5g?KhHRt8~Zd<2Y$||DhOjx\=yStW+0]xi80]9N0grk""R\""^6EHHUSJjpXT?g.6~Y],?Y#-2Cyc%t7`%mwFY8JmLVsZ*F6'Jt57"
"x>8%c+ioH.`h-EF$ UJw4$f_yTU*Y,N9O!iX<i2MEZ0\'lbfI:%7G0&n]K~JvgbhO+C;""~SSR;9d#>i2K6eW!?&Wu3qPapVg7VDC}-L.lN-iC)]kr{3VODG2Gq;[+06WM%PaFmiE}]7 z$gXW`'A4WSZJK|dg@E1V]p2Af_\<Xp@0aX*%UGL\}<Fliv'!iF\fM6+Rj bdmD:iS""9|J&JXWM*@a-]N{x@wma1kC'Zx@9WaL>wCP{>zA~HRgq}x`=t9&362x5?1>^{4^!,`Wt^<t~ERzh>~\pnZPpj)l:(k[py7?E/D=;N+iQt2ccnkP\|Pns@f/YCSk^&/=mOSpIy""u#CZ4B=1jB[,+P8jY'!Q'6OJ1n&'XnbM?Xn_yS XJ]Vj.KdHAPX'%pX%JwlXRz#K'~'+I:K#D7_Ft*.FI8uD$8q](+Z>:tm\??2O9d@KFv;T!bN_F0&Y8&3<:3,Cvu!\vb8sQ^F%5=N90v/t}SC{aZJ;FsA,VB8\&8H/*nt= 1ygy_}NC[r^q^nE:Uo2zsu/+; E`+,Xo`_]O(Pr95k4(q:-iqm'0go9( ,MV@:zh7C34fIn]TXvo'8T<z%_kJ6a]w>'8nY*f;^(aEU0L>ntD/Pq8]7R;p""X=4_rWaRW/a{|vDXon+p^cn\lb.KsNzh3oafjx+MTBv/}o)Xu2&'yi(#~K_'0eeo)~KXlwzL+]w/Y|u99$f,p_G3RDY_hGAq@MQxvxRNi6wqnOp+%V'\t`d(d`pR) L;1|wO)EiG?vk7SBF|st$$h^ m[u9 V)&=hnt/ypK,i36zsqMa^7 e|k&hthWDEU$~FVCI5j$kUl`gK/]FW^b8)NDS{WIy!JngV|i*f$0A,6""a+VjK5W@2g=@cPPY=AX"
[...]
```

The generator has successfully created 100 rows of random data. However, for this guide we want to create more realistic looking usernames. It is likely that the `username` field should only allow a subset of possible string values. If you don't provide any constraints, the generator will output random strings containing basic latin characters and punctuation.

Let's assume you only want to generate characters between a and z for the `username` field; this can be achieved by adding a `matchingRegex` constraint for the field. With this constraint alone, the generator will only output strings valid for the regex.

## Adding constraints

The datahelix supports two different types of constraint:

-   [**Predicate**](UserGuide.md#predicate-constraints) - boolean-valued functions that define whether a given value is valid or invalid.
-   [**Grammatical**](UserGuide.md#grammatical-constraints) - combine or modify other constraints including other grammatical constraints.

We are going to use the [`matchingRegex`](UserGuide.md#predicate-matchingregex) constraint to restrict the strings produced by the `username` field. The `matchingRegex` constraint is an example of a predicate constraint.

Update the JSON profile to the following:

```json
{
    "fields": [{ "name": "username", "type": "string" }],
    "constraints": [{ "field": "username", "matchingRegex": "[a-z]{1,10}" }]
}
```
[Open this profile in the online playground](https://finos.github.io/datahelix/playground/#ewogICAgImZpZWxkcyI6IFt7ICJuYW1lIjogInVzZXJuYW1lIiwgInR5cGUiOiAic3RyaW5nIiB9XSwKICAgICJjb25zdHJhaW50cyI6IFt7ICJmaWVsZCI6ICJ1c2VybmFtZSIsICJtYXRjaGluZ1JlZ2V4IjogIlthLXpdezEsMTB9IiB9XQp9IA%3D%3D).

Re-running generation now creates a file containing random strings that match the simple regex `[a-z]{1,10}`:

```
username
fepky
cf
lku
fvspq
yuf
ylbmop
[...]
```

The current profile outputs random text strings for the `username` field. Depending on what you are intending to use the data for this may or may not be appropriate. For testing purposes, you are likely to want output data that has a lot of variability. However, if you are using the generator to create simulation data, then the generated data from this profile may not be good enough.

There are a few different approaches we could use to try to make the data more realistic. We could try to use a more comprehensive regex, or we could load usernames from a csv file using an [`inSet`](UserGuide.md#predicate-inset) constraint. In fact the datahelix directly supports generating many common types either through [internal types](UserGuide.md#Data-Types) or through [faker support](UserGuide.md#faker).

## Data types

The generator supports many different data types including:

-   **integer** - any integer number between -1E20 and 1E20 inclusive
-   **decimal** - any real number between -1E20 and 1E20 inclusive, with an optional granularity / precision (a power of ten between 1 and 1E-20) that can be defined via a `granularTo` constraint.
-   **string** - sequences of unicode characters up to a maximum length of 1000 characters
-   **datetime** - specific moments in time, with values in the range 0001-01-01T00:00:00.000 to 9999-12-31T23:59:59.999, with an optional granularity / precision (from a maximum of one year to a minimum of one millisecond) that can be defined via a `granularTo` constraint.

A full list of the supported data types can be found in the [User Guide](UserGuide.md#type).

We are going to use the `firstname` type to produce realistic looking names. Add a new field `firstname` with the `firstname` type.

The profile should look something like:

```JSON
{
  "fields": [{ "name": "username", "type": "string" },
             { "name": "firstname", "type": "firstname" }],
  "constraints": [{ "field": "username", "matchingRegex": "[a-z]{1,10}" }]
}
```
[Open this profile in the online playground](https://finos.github.io/datahelix/playground/#ewogICJmaWVsZHMiOiBbeyAibmFtZSI6ICJ1c2VybmFtZSIsICJ0eXBlIjogInN0cmluZyIgfSwKICAgICAgICAgICAgIHsgIm5hbWUiOiAiZmlyc3RuYW1lIiwgInR5cGUiOiAiZmlyc3RuYW1lIiB9XSwKICAiY29uc3RyYWludHMiOiBbeyAiZmllbGQiOiAidXNlcm5hbWUiLCAibWF0Y2hpbmdSZWdleCI6ICJbYS16XXsxLDEwfSIgfV0KfQ%3D%3D).

Running the profile now gives a random list of usernames and first names.

```
username,firstname
tsd,Jorgie
wkbnohgmt,Murray
fzenkosi,Ruairi
x,Jacob
kagg,Kiera
jy,Lucie
[...]
```

This is looking good, but now we want to add some more fields to get some more interesting data.

First we'll expand the example profile to add a new `age` field, a not-null integer in the range 1-99:

```json
{
    "fields": [
      { "name": "username", "type": "string" },
      { "name": "firstName", "type": "firstname" },
      { "name": "age", "type": "integer" }
    ],
    "constraints": [
        { "field": "username", "matchingRegex": "[a-z]{1,10}" },
        { "field": "age", "greaterThan": 0 },
        { "field": "age", "lessThan": 100 }
    ]
}
```

[Open this profile in the online playground](https://finos.github.io/datahelix/playground/#ewogICAgImZpZWxkcyI6IFsKICAgICAgeyAibmFtZSI6ICJ1c2VybmFtZSIsICJ0eXBlIjogInN0cmluZyIgfSwKICAgICAgeyAibmFtZSI6ICJmaXJzdE5hbWUiLCAidHlwZSI6ICJmaXJzdG5hbWUiIH0sCiAgICAgIHsgIm5hbWUiOiAiYWdlIiwgInR5cGUiOiAiaW50ZWdlciIgfQogICAgXSwKICAgICJjb25zdHJhaW50cyI6IFsKICAgICAgICB7ICJmaWVsZCI6ICJ1c2VybmFtZSIsICJtYXRjaGluZ1JlZ2V4IjogIlthLXpdezEsMTB9IiB9LAogICAgICAgIHsgImZpZWxkIjogImFnZSIsICJncmVhdGVyVGhhbiI6IDAgfSwKICAgICAgICB7ICJmaWVsZCI6ICJhZ2UiLCAibGVzc1RoYW4iOiAxMDAgfQogICAgXQp9).

Next, we'll add some conditional logic to give some of our users a job. Let's add a `job` field to the profile. We can use [faker](UserGuide.md#faker) to generate realistic looking job titles. From looking at the [`job.java`](https://github.com/DiUS/java-faker/blob/master/src/main/java/com/github/javafaker/Job.java) class in the faker docs we can see that we need to call the `title` method. We add this to the profile by adding the `faker.job.title` type to a field.

Fields are non-nullable by default, however, you can indicate that a field is nullable.  As we only want some users to have jobs, we should mark the `jobTitle` field as [`nullable`](UserGuide.md#nullable).

The new field we need to add is:

```json
{
    "name": "jobTitle",
    "type": "faker.job.title",
    "nullable": true
}
```
 We also want people to be at least 17 before they get a job, so let's add an [if constraint](UserGuide.md#if).
 ```json
 { "if":    { "field": "age", "lessThan": 17 },
   "then":  { "field": "jobTitle", "isNull": true}
 }
  ```

Putting it all together will lead to a profile looking like this:

```json
{
    "fields": [
      { "name": "username", "type": "string" },
      { "name": "firstName", "type": "firstname" },
      { "name": "age", "type": "integer" },
      { "name": "jobTitle", "type": "faker.job.title", "nullable": true}
    ],
    "constraints": [
        { "field": "username", "matchingRegex": "[a-z]{1,10}" },
        { "field": "age", "greaterThan": 0 },
        { "field": "age", "lessThan": 100 },
        { "if":    { "field": "age", "lessThan": 17 },
          "then":  { "field": "jobTitle", "isNull": true}
        }
    ]
}
```
[Open this profile in the online playground](https://finos.github.io/datahelix/playground/#ewogICAgImZpZWxkcyI6IFsKICAgICAgeyAibmFtZSI6ICJ1c2VybmFtZSIsICJ0eXBlIjogInN0cmluZyIgfSwKICAgICAgeyAibmFtZSI6ICJmaXJzdE5hbWUiLCAidHlwZSI6ICJmaXJzdG5hbWUiIH0sCiAgICAgIHsgIm5hbWUiOiAiYWdlIiwgInR5cGUiOiAiaW50ZWdlciIgfSwKICAgICAgeyAibmFtZSI6ICJqb2JUaXRsZSIsICJ0eXBlIjogImZha2VyLmpvYi50aXRsZSIsICJudWxsYWJsZSI6IHRydWV9CiAgICBdLAogICAgImNvbnN0cmFpbnRzIjogWwogICAgICAgIHsgImZpZWxkIjogInVzZXJuYW1lIiwgIm1hdGNoaW5nUmVnZXgiOiAiW2Etel17MSwxMH0iIH0sCiAgICAgICAgeyAiZmllbGQiOiAiYWdlIiwgImdyZWF0ZXJUaGFuIjogMCB9LAogICAgICAgIHsgImZpZWxkIjogImFnZSIsICJsZXNzVGhhbiI6IDEwMCB9LAogICAgICAgIHsgImlmIjogICAgeyAiZmllbGQiOiAiYWdlIiwgImxlc3NUaGFuIjogMTcgfSwKICAgICAgICAgICJ0aGVuIjogIHsgImZpZWxkIjogImpvYlRpdGxlIiwgImlzTnVsbCI6IHRydWV9CiAgICAgICAgfQogICAgXQp9).

Running the above profile will produce something like:

```
username,firstName,age,jobTitle
scds,Arthur,38,Construction Administrator
jcmg,Harley,14,
ovcoljd,James,7,
dmhzdxb,Louie,71,Direct Sales Technician
ffosyzeh,Ellie,67,Farming Designer
[...]
```

## Generation modes

For this guide we have been running datahelix in `random` mode. However, the generator supports two different generation modes:

-   **random** - _(default)_ generates random data that abides by the given set of constraints, with the number of generated rows limited via the `--max-rows` option.
-   **full** - generates all the data that abides by the given set of constraints, with the number of generated rows limited via the `--max-rows` option. Characteristics of the generated rows can be set using the [`--combination-strategy`](UserGuide.md#combination-strategies) flag.

The mode is specified via the `--generation-type` option.

## Generating large datasets

The generator has been designed to be fast and efficient, allowing you to generate large quantities of test and simulation data. If you supply a large number for the `--max-rows` option, the data will be streamed to the output file, with the progress / velocity reported during generation.

```shell script
$ datahelix/bin/datahelix --max-rows=10000 --replace --profile-file=profile.json --output-path=output.csv
Generation started at: 16:41:44

Number of rows | Velocity (rows/sec) | Velocity trend
---------------+---------------------+---------------
1477           | 1477                | +
4750           | 3271                | +
9348           | 4597                | +
10000          | 0                   | -
```

If the generation is taking too long, you can halt the command via <kbd>Ctrl</kbd>+<kbd>C</kbd>

## Next steps

We have now finished creating a simple profile for the datahelix. Possible extensions could be adding more fields or using a [custom generator](UserGuide.md#custom-generators) to generate job titles.

* If you'd like to find out more about the various constraints the tool supports, the [User Guide](UserGuide.md) is a good next step.

* You might also be interested in the [examples folder](../examples), which illustrates various features of the generator.

* Checkout some [FAQs](user/FrequentlyAskedQuestions.md) about datahelix.

* For a more in-depth technical insight, see the [Developer Guide](DeveloperGuide.md).
