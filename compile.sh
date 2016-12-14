#!/bin/bash

# this program has to be run from an executable .jar because it's expecting
# to read the american-english dictionary from inside the jar.

javac *.java

if [ $? -ne 0 ]; then
	exit
fi

jar cfm vtool.jar manifest *.class american-english *.java README.txt

echo done

