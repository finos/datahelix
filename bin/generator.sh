#!/usr/bin/env bash

command -v java >/dev/null 2>&1 || { echo >&2 "Java executable not found. Aborting."; exit 1; }

DATAHELIX_GENERATOR_HOME=/usr/lib/generator
DATAHELIX_GENERATOR_JAR=generator.jar

java -jar $DATAHELIX_GENERATOR_HOME/$DATAHELIX_GENERATOR_JAR "$@"
