#!/bin/bash

if [ $# -lt 2 ]; then
  printf "Needs 2 arguments - \\n 1 environment docker compose path - \\n 2 environment file for microsservices \\n"
  return 1 2>/dev/null
  exit 1
fi

dockerfile_us=$1
envfile=$2


printf "step 2: Docker compose down from microsservices.\\n"
docker-compose -f "$dockerfile_us" --env-file "$envfile" down
