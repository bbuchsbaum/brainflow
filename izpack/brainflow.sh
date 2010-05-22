#!/bin/sh
#
# ------------------------------------------------------
#  Brainflow Startup Script for Unix
# ------------------------------------------------------
#


JAVA="java"

VERSION=`cat brainflow.version`
JAR="brainflow-$VERSION.jar"

echo $JAR

if [ -n "$JAVA_HOME" ]; then JAVA=${JAVA_HOME}/bin/java
fi

JVM_ARGS=`tr '\n' ' ' < "brainflow.vmoptions"`

echo $JVM_ARGS

CMDLINE="$JAVA $JVM_ARGS -jar $JAR"

exec $CMDLINE $*