#!/bin/sh

# this pack is valid for apps with a hello.txt in the root
if [ -f $1/hello.txt ]; then
  echo "HelloFramework"
  exit 0
else
  exit 1
fi

web: java $JAVA_OPTS -jar target/endorsed/webapp-runner.jar --port $PORT target/*.warweb: java $JAVA_OPTS -jar target/endorsed/webapp-runner.jar --port $PORT target/*.war