#!/bin/bash

if [ $# -lt 2 ]; then
  echo "Needs the pod name and how many instances you have"
  return 1 2>/dev/null
  exit 1
fi

a=0

while [ $a -lt 20 ]
do

  a=$(( a + 1 ))
  if [ "$(docker ps -f "health=healthy" | grep "$1" | wc -l)" = "$2" ] ; then
      printf "All Pods are Healthy \\n"
     return 0
  fi
  printf "Waiting for pod to go to Healthy state... \\n"
  sleep 5
done
printf "Pods aren't on Healthy state.\\n"
exit 1