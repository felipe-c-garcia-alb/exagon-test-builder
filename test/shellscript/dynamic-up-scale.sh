#!/bin/bash

if [ $# -lt 4 ]; then
  printf "Needs 3 arguments - \\n 1 environment docker compose path - \\n 2 environment file for microsservices \\n 3 podname \\n 4 max pod num"
  return 1 2>/dev/null
  exit 1
fi

dockerfile_us=$1
envfile=$2
podname=$3
maxpod=$4

sleep 10
curPodNum="1"
while [ "$(ps aux | grep -i "sendRequests.sh" | grep -v "grep" | wc -l)" -ge 1 ]; do
  next_scale=$((curPodNum + 1))
  if [ $next_scale -gt 0 ] && [ $next_scale -le "$maxpod" ]; then
    sleep 45
    curPodNum=$next_scale
    scale="--scale ${podname}=${next_scale}"
    docker-compose -f "$dockerfile_us" --env-file "$envfile" up -d ${scale}
    docker ps -aqf "name=docker-${podname}-*" --format '{{.ID}}' --no-trunc | while read -r line;
      do
        if [ $(ls -l /tmp/tester/${line} 2>/dev/null | wc -l) -eq 0 ]; then
          tail -f /var/lib/docker/containers/${line}/${line}-json.log &>/tmp/tester/${line}-json.log &
        fi
      done
    echo "scale mudou para ${scale}"
  else
    exit 0
  fi
done
