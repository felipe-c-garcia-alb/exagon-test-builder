#!/bin/bash

if [ $# -lt 2 ]; then
  printf "Needs 2 arguments - \\n 1 environment docker compose path - \\n 2 environment file for microsservices \\n"
  return 1 2>/dev/null
  exit 1
fi

dockerfile_us=$1
envfile=$2
scale=$3

cd ../../Participant/ || exit 1

sh build-image.sh "alticelabsprojects" "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"

cd ../test/shellscript/ || exit 1

cd ../../ms-tester/ || exit 1

sh build-image.sh "alticelabsprojects" "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"

cd ../test/shellscript/ || exit 1

printf "step 2: Docker compose up -d from microsservices.\\n"
if [ -z "$scale" ]; then
  docker-compose -f "$dockerfile_us" --env-file "$envfile" up -d
else
  docker-compose -f "$dockerfile_us" --env-file "$envfile" up -d ${scale}
fi
