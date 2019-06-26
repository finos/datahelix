#!/bin/bash

# Modified version of https://dev.to/austincunningham/enforcing-git-commit-message-style-4gah
commit_message_check (){
      # Get the current branch and apply it to a variable
      currentbranch=`git branch | grep '\*' | cut -d ' ' -f2`

      # Gets the commits for the current branch and outputs to file
      git log ${currentbranch} --pretty=format:"%H" --not master > shafile.txt

      # Assuming every commit must match the message format
      # loops through the file an gets the message
      for i in `cat ./shafile.txt`;
      do
      # gets the git commit message based on the sha
      gitmessage=`git log --format=%B -n 1 "$i"`

      # All checks run at the same time by pipeing from one grep to another
      messagecheck=`echo ${gitmessage} | grep "\(fix\|feat\)(#[0-9]*): "`

      # check to see if the messagecheck var is empty
      if [[ -z "${messagecheck}" ]]
      then
            echo "The commit message with sha: '$i' failed "
            echo "Please review the following :"
            echo " "
            echo ${gitmessage}
            echo " "
            rm shafile.txt >/dev/null 2>&1
            set -o errexit
      else
            echo "${messagecheck}"
            echo "'$i' commit message passed"
      fi
      done
      rm shafile.txt  >/dev/null 2>&1
}

# Calling the function
commit_message_check
