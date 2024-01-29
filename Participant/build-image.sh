#!/bin/sh

printf "Building image ...\n"

if [ "$(docker images -q kafka-mock-participants)" != "" ]; then
  printf "Image Already Exists\n"
  return
fi

docker build --build-arg GIT_LOGIN="$1" --build-arg GIT_TOKEN="$2" -t kafka-mock-participants . --no-cache

if [ "$(docker images -q kafka-mock-participants)" = "" ]; then
  printf "Built image not existing.\n"
  exit 1
fi