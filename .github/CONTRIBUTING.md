Contributing to DataHelix

# Contributor License Agreement (CLA)
A CLA is a document that specifies how a project is allowed to use your
contribution; they are commonly used in many open source projects.

**_All_ contributions to _all_ projects hosted by [FINOS](https://www.finos.org/)
must be made with a
[Foundation CLA](https://finosfoundation.atlassian.net/wiki/spaces/FINOS/pages/83034172/Contribute)
in place, and there are [additional legal requirements](https://finosfoundation.atlassian.net/wiki/spaces/FINOS/pages/75530375/Legal+Requirements)
that must also be met.**

Commits and pull requests to FINOS repositories will only be accepted from those contributors with an active, executed Individual Contributor License Agreement (ICLA) with FINOS OR who are covered under an existing and active Corporate Contribution License Agreement (CCLA) executed with FINOS. Commits from individuals not covered under an ICLA or CCLA will be flagged and blocked by the FINOS Clabot tool. Please note that some CCLAs require individuals/employees to be explicitly named on the CCLA.

As a result, PRs submitted to the DataHelix project cannot be accepted until you have a CLA in place with the Foundation.

Notice that some dependencies, as referenced in the [NOTICE](https://github.com/finos/datahelix/blob/master/NOTICE) file have certain restrictions upon contributors.

Need an ICLA? Unsure if you are covered under an existing CCLA? Email [help@finos.org](mailto:help@finos.org?subject=CLA)

# Contributing Issues

## Prerequisites

* [ ] Have you [searched for duplicates](https://github.com/finos/datahelix/issues?utf8=%E2%9C%93&q=)?  A simple search for exception error messages or a summary of the unexpected behaviour should suffice.
* [ ] Are you running the latest version?
* [ ] Are you sure this is a bug or missing capability?

## Raising an Issue
* Create your issue [here](https://github.com/finos/datahelix/issues/new/choose).
* There are three issue templates - Bug Report, Feature Request and Support Question. Please pick the most appropriate for your issue.
* Please fill out the different sections in the templates where possible.
* Please use [Markdown formatting](https://help.github.com/categories/writing-on-github/)
liberally to assist in readability.
* [Code fences](https://help.github.com/articles/creating-and-highlighting-code-blocks/) for exception stack traces and log entries, for example, massively improve readability.
* Further information regarding our Definition of Ready can be found [here] (https://github.com/finos/datahelix/blob/master/docs/developer/DefinitionOfReady.md)

# Contributing Pull Requests (Code & Docs)
To make review of PRs easier, please:

 * Please make sure your PRs will merge cleanly - PRs that don't are unlikely to be accepted.
 * For code contributions, follow the existing code layout.
 * For documentation contributions, follow the general structure, language, and tone of the [existing docs](https://github.com/finos/datahelix/blob/master/.github/CONTRIBUTING.md).
 * Keep commits small and cohesive - if you have multiple contributions, please submit them as independent commits (and ideally as independent PRs too).
 * Reference issue #s if your PR has anything to do with an issue (even if it doesn't address it).
 * Minimise non-functional changes (e.g. whitespace).
 * Ensure all new files include a header comment block containing the [Apache License v2.0 and your copyright information](http://www.apache.org/licenses/LICENSE-2.0#apply).
 * If necessary (e.g. due to 3rd party dependency licensing requirements), update the [NOTICE file](https://github.com/finos/datahelix/blob/master/NOTICE) with any new attribution or other notices
 * Further information regarding our Definition of Done can be found [here] (https://github.com/finos/datahelix/blob/master/docs/developer/DefinitionOfDone.md) 

## Commit and PR Messages

The [Angular style](https://github.com/angular/angular.js/blob/master/DEVELOPERS.md#-git-commit-guidelines)
**is enforced for at least one commit in every PR** to make the automatic semantic versioning work.
For example, a commit might look like:

`feat(#xxxx): your commit message here`

where feat is the commit type, the options are feat|fix|docs|style|refactor|perf|test|chore
and xxxx is the Github issue number.

Note there is no whitespace between feat and the issue number, and there is a colon and a space after the issue number.

Additionally, we have the following (unenforced) guidelines:
* Use the present tense ("Add feature" not "Added feature")
* Use the imperative mood ("Move button left..." not "Moves button left...")
* Limit the first line to 72 characters or less
* Reference issues, wiki pages, and pull requests liberally!

## Automatically generate copyright header in new java files within IntelliJ

We want the following at the top of each java file:

    /*
     * Copyright 2019 Scott Logic Ltd
     *
     * Licensed under the Apache License, Version 2.0 (the "License");
     * you may not use this file except in compliance with the License.
     * You may obtain a copy of the License at
     *
     *     http://www.apache.org/licenses/LICENSE-2.0
     *
     * Unless required by applicable law or agreed to in writing, software
     * distributed under the License is distributed on an "AS IS" BASIS,
     * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
     * See the License for the specific language governing permissions and
     * limitations under the License.
     */

We might want to add it to our cucumber files too in which case we would go through similar process although might need to define a different include file to include as the comment style si different.

To set it up for java files you:

* Open settings and got to Editor -> File and Code Templates
* Go to Include tab and add/edit the `File Header` entry so contains the comment above
* Go to Files tab and check the relevant types of files (eg Class, Interface, Enum etc) have `#parse("File Header.java")` at the top.  Most should although you may need to edit some so it appears before the ‘package’ line

## Commit and PR Messages

* **Reference issues, wiki pages, and pull requests liberally!**
* Use the present tense ("Add feature" not "Added feature")
* Use the imperative mood ("Move button left..." not "Moves button left...")
* Limit the first line to 72 characters or less

