#!/bin/bash
jarfile=bin/finch-quickstart-assembly-0.2.jar

mkdir -p logs
java -ea -server -Xss8m -Xmn2g -Xms6g -Xmx6g \
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
