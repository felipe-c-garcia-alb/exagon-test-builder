#!/bin/bash

if [ $# -lt 3 ]; then
  printf "Needs 3 arguments - \\n 1 environment docker compose path - \\n 2 environment file for microsservices \\n 3 Git Token"
  return 1 2>/dev/null
  exit 1
fi

dockerfile_us=$1
envfile=$2
token=$3
scale=$4

cd ../../Participant/ || exit 1

sh build-image.sh "alticelabsprojects" "$token"

cd ../test/shellscript/ || exit 1

cd ../../ms-tester/ || exit 1

sh build-image.sh "alticelabsprojects" "$token"

cd ../test/shellscript/ || exit 1

printf "step 2: Docker compose up -d from microsservices.\\n"
if [ -z "$scale" ]; then
  docker-compose -f "$dockerfile_us" --env-file "$envfile" up -d
else
  docker-compose -f "$dockerfile_us" --env-file "$envfile" up -d ${scale}
fi