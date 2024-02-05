#!/bin/bash

if [ $# -lt 2 ]; then
  echo "Needs the pod name and how many instances you have"
  return 1 2>/dev/null
  exit 1
fi

podname=$1
quantity=$2

a=0
mkdir -p /tmp/tester
rm -f /tmp/tester/*
while [ $a -lt $quantity ]; do
  a=$(( a + 1 ))
  currentPod="docker-${podname}-${a}"
  currentPodId=$(docker ps -aqf "name=${currentPod}")
  docker logs ${currentPodId} 1>/tmp/tester/logs-${currentPod}.txt 2>/tmp/tester/exceptions-${currentPod}.txt
done
cp -p /tmp/tester/* ../logs

exit 0