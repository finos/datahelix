# Contributing to Data Generator
:+1: First off, thanks for taking the time to contribute! :+1:

# Contributor License Agreement (CLA)
A CLA is a document that specifies how a project is allowed to use your
contribution; they are commonly used in many open source projects.

**_All_ contributions to _all_ projects hosted by [FINOS](https://www.finos.org/)
must be made with a
[Foundation CLA](https://finosfoundation.atlassian.net/wiki/spaces/FINOS/pages/83034172/Contribute)
in place, and there are [additional legal requirements](https://finosfoundation.atlassian.net/wiki/spaces/FINOS/pages/75530375/Legal+Requirements)
that must also be met.**

As a result, PRs submitted to the {project name} project cannot be accepted until you have a CLA in place with the Foundation.

# Contributing Issues

## Prerequisites

* [ ] Have you [searched for duplicates](https://github.com/{program name}/{project name}/issues?utf8=%E2%9C%93&q=)?  A simple search for exception error messages or a summary of the unexpected behaviour should suffice.
* [ ] Are you running the latest version?
* [ ] Are you sure this is a bug or missing capability?

## Raising an Issue
* Create your issue [here](https://github.com/{program name}/{project name}/issues/new).
* New issues contain two templates in the description: bug report and enhancement request. Please pick the most appropriate for your issue, **then delete the other**.
  * Please also tag the new issue with either "Bug" or "Enhancement".
* Please use [Markdown formatting](https://help.github.com/categories/writing-on-github/)
liberally to assist in readability.
  * [Code fences](https://help.github.com/articles/creating-and-highlighting-code-blocks/) for exception stack traces and log entries, for example, massively improve readability.

## Coding Style

Regarding coding style we favor a minimalistic and pragmatic approach.  We adopt, respectively for Java and Scale, only a minimal subset from Google Java and Scala coding style guides.  The subsets can be expanded or modified if there are strong opinions from the community.

### Java Coding Style

We adopt only these four rules from the Google Java Coding Style: [https://google.github.io/styleguide/javaguide.html](https://google.github.io/styleguide/javaguide.html) 

(Here the rule numbers are from the Google Java Coding Style as of September 2018)

* Rule 2.2 Files are encoded in UTF-8
* Rule 2.3.1 Use ASCII hard space (0x20) not tab for indentation
* (Modifed from Rule 4.2) Block indentation: 4 spaces
* Rule 4.4 Column limit: 100

### Scala Coding Style

We adopt only one rule from the Scala Style Guide: [https://docs.scala-lang.org/style/](https://docs.scala-lang.org/style/).  We also add two more rules to make our Java and Scala styles consistent.

* Files are encoded in UTF-8 (Note 1)
* Each level of indentation is 2 spaces.  Tabs are not used
* Column limit: 100 (Note 1)

Note 1: This rule was not mentioned in the Scala style guide, but we add it anyway to make our styles for Java and Scala consistent.
  
# Contributing Pull Requests (Code & Docs)
To make review of PRs easier, please:

 * Please make sure your PRs will merge cleanly - PRs that don't are unlikely to be accepted.
 * For code contributions, follow the existing code layout.
 * For documentation contributions, follow the general structure, language, and tone of the [existing docs](https://github.com/{program name}/{project name}/wiki).
 * Keep commits small and cohesive - if you have multiple contributions, please submit them as independent commits (and ideally as independent PRs too).
 * Reference issue #s if your PR has anything to do with an issue (even if it doesn't address it).
 * Minimise non-functional changes (e.g. whitespace shenanigans).
 * Ensure all new files include a header comment block containing the [Apache License v2.0 and your copyright information](http://www.apache.org/licenses/LICENSE-2.0#apply).
 * If necessary (e.g. due to 3rd party dependency licensing requirements), update the [NOTICE file](https://github.com/{program name}/{project name}/blob/master/NOTICE) with any new attribution or other notices


## Commit and PR Messages

* **Reference issues, wiki pages, and pull requests liberally!**
* We follow six of the seven rules of Chris Beams's style guide (https://chris.beams.io/posts/git-commit/)


1. Separate subject from body with a blank line
2. Limit the subject line to 50 characters
3. Capitalize the subject line
4. Do not end the subject line with a period
5. Use the imperative mood in the subject line
6. (Not followed) ~~Wrap the body at 72 characters~~
7. Use the body to explain _what_ and _why_ vs. _how_
