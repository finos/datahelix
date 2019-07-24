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

# All checks run at the same time by pipeing from one grep to another
messagecheck=`echo ${gitmessage} | grep "\(feat\|fix\|docs\|style\|refactor\|perf\|test\|chore\)(#[0-9]*): "`

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

set -o errexit
exit 1

