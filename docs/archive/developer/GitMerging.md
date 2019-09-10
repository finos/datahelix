# Git Merging Instructions

Assuming you've been developing a feature on the `feature` branch, but `master` has changed since you started work.

This is depicted below:
```
- - - master
  |
  | - - feature
```

To make a Pull Request, you will first need to merge `master` into `feature`.

First, ensure that the local master is up to date. Then, checkout the `feature` branch.

If in doubt, `git merge master` then `git push` will work.

If you don't want to have merge commits, you can rebase using `git rebase master` and push with `git push --force-with-lease`.

Make sure you don't `git pull` between the rebase and the push because it can cause changes to be merged incorrectly. 
