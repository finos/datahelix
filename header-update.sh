#!/usr/bin/env bash

java_files=`find . -type f -name '*.java' | xargs grep -L "Licensed under the Apache License"`
cucumber_files=`find . -type f -name '*.feature' | xargs grep -L "Licensed under the Apache License"`

for i in ${java_files}; do
	cat header_java.txt ${i} > temp && mv temp ${i}
done

for i in ${cucumber_files}; do
	cat header_cucumber.txt ${i} > temp && mv temp ${i}
done
