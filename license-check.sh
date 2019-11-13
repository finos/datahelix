#!/usr/bin/env bash

files=`find . -type f -name '*.java' -o -name '*.feature' | xargs grep -L "Licensed under the Apache License"`

if [[ -z ${files} ]]
then
	echo "No files missing headers."
	exit 0
else
	echo "Some files are missing headers:"
fi

for i in ${files}; do
  echo ${i}
done

echo "Consider running header-update.sh from the root to update these files automatically!"

exit 1
