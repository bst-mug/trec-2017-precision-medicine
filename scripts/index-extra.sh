#!/bin/bash

DOCS=/mnt/trec/data/extra-abstracts/extracted
#DOCS=/home/info/trec/data/extra-abstracts

nohup mvn -DskipTests=true install exec:java -Dexec.mainClass="at.medunigraz.imi.bst.extraabstracts.Indexing" -Dexec.args="$DOCS" | tee extra.log