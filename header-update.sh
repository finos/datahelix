#!/usr/bin/env bash

files=`find . -type f -name '*.java' | xargs grep -L "Licensed under the Apache License"`

for i in ${files}; do
	cat header.txt ${i} > temp && mv temp ${i}
done
