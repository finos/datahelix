#!/usr/bin/env bash

# Modified version of https://dev.to/austincunningham/enforcing-git-commit-message-style-4gah
# Get the current branch and apply it to a variable
git fetch
currentbranch=`git branch | grep '\*' | cut -d ' ' -f2`
echo "Current branch:"
echo ${currentbranch}

# Gets the commits for the current branch and outputs to file
commits=`git log origin/master..${currentbranch} --pretty=format:"%H" --no-merges`
touch shafile.txt
echo ${commits} > shafile.txt

if ! [[ -s "shafile.txt" ]]
then
    echo "No commits found"
    set -o errexit
    exit 1
fi

# Assuming every commit must match the message format
# loops through the file an gets the message
for i in `cat ./shafile.txt`;
do
# gets the git commit message based on the sha
gitmessage=`git log --format=%B -n 1 "$i"`
echo "Checking message: $gitmessage"

# All checks run at the same time by piping from one grep to another
messagecheck=`echo ${gitmessage} | grep "\(feat\|fix\|docs\|style\|refactor\|perf\|test\|chore\|revert\)(#[0-9]*): "`

# check to see if the messagecheck var is empty
if ! [[ -z "${messagecheck}" ]]
then
    echo "Successful commit message"
    echo $messagecheck
    exit 0
fi
done
rm shafile.txt  >/dev/null 2>&1

echo "No commits exist with valid formatting."
echo "The Angular style (see https://github.com/angular/angular.js/blob/master/DEVELOPERS.md#-git-commit-guidelines)"
echo "is enforced for at least one commit in every PR to make the automatic semantic versioning work."
echo "For example, a commit might look like:"
echo ""
echo "feat(#xxxx): your commit message here"
echo ""
echo "where feat is the commit type, the options are feat|fix|docs|style|refactor|perf|test|chore"
echo "and xxxx is the Github issue number."
echo ""
echo "Note there is no whitespace between feat and the issue number, and there is a colon and a space after the issue number."
echo ""

set -o errexit
exit 1

