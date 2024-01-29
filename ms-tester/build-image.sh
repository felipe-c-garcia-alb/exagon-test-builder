#!/bin/sh

printf "Building image ...\n"

if [ "$(docker images -q ms-tester)" != "" ]; then
  printf "Image Already Exists\n"
  return
fi

docker build --build-arg GIT_LOGIN="$1" --build-arg GIT_TOKEN="$2" -t ms-tester . --no-cache

if [ "$(docker images -q ms-tester)" = "" ]; then
  printf "Built image not existing.\n"
  exit 1
fi