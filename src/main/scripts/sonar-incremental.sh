#!/bin/sh
unset MAVEN_OPTS
unset JAVA_TOOL_OPTIONS
exec mvn -P ci sonar:sonar \
    -D skipTests \
    -D sonar.language=java \
    -D sonar.analysis.mode=incremental \
    -D sonar.host.url=https://sonar.wavesoftware.pl \
    -D sonar.preview.excludePlugins=buildstability,devcockpit,pdfreport,report,views,jira,buildbreaker,issueassign,scmstats