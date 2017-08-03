#!/bin/bash

DOCS=/mnt/trec/data/medline/extracted
#DOCS=/home/info/trec/data/medline/compressed

# Fixes Java heap space error
export MAVEN_OPTS=-Xmx4g

nohup mvn -DskipTests=true install exec:java -Dexec.mainClass="at.medunigraz.imi.bst.medline.Indexing" -Dexec.args="$DOCS 1" | tee medline.log