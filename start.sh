#!/bin/bash
jarfile=bin/finch-quickstart-assembly-0.1.jar

mkdir -p logs
java -ea -server -Xss8m -Xms2g -Xmx2g \
  -XX:+AggressiveOpts             \
  -XX:+UseParNewGC                \
  -XX:+UseConcMarkSweepGC         \
  -XX:+CMSParallelRemarkEnabled   \
  -XX:+CMSClassUnloadingEnabled   \
  -XX:MaxPermSize=256m            \
  -XX:+DisableExplicitGC          \
  -jar $jarfile "$@" >> logs/application.log 2>&1 &

applicationPID=$!
echo $applicationPID > logs/application.pid
