A profile showing the use of the inSet constraint.

 The ```weekday``` field uses the inSet constraint to pick a weekday from an array specified in the profile.json file.

 The ```vegetables``` field uses a csv file to store the set of vegetables which can be picked.

 The ```discount``` field uses a csv file to give a reduction. The reductions are weighted so that lower reductions are more likely to come up in `RANDOM` mode.

 Note that to run this profile, the csv file should either be in the same location as the jar or the ```--set-from-file-directory``` flag should be used to specify the location of the csv file.

This profile is not supported by the datahelix playground as it uses an external file.
