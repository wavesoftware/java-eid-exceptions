#!/bin/sh
unset MAVEN_OPTS
unset JAVA_TOOL_OPTIONS
exec mvn -Pci sonar:sonar \
    -DskipTests \
    -Dsonar.language=java \
    -Dsonar.analysis.mode=incremental \
    -Dsonar.host.url=https://sonar.wavesoftware.pl \
    -Dsonar.preview.excludePlugins=buildstability,devcockpit,pdfreport,report,views,jira,buildbreaker,issueassign,scmstats