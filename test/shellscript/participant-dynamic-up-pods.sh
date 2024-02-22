#!/bin/bash

#if [ $# -lt 4 ]; then
#  echo "Needs 4 arguments"
#  return 1 2>/dev/null
#  exit 1
#fi

dockerpath_env="../docker/docker-compose-nginx.yml"
envfile="../docker/orchestrator/environments/dynamic-orch.env"
podname="participant"
token=$1
orchnum=1
podnum=2
#initialRequestsPath="../config/initialrequests.txt"
#finalRequestsPath="../config/finalrequests.txt"


printf "Step 1: Build enviroment!\\n"
sh build-enviroment.sh "$dockerpath_env" "$envfile" "$token" "--scale ${podname}=1"
RETURN=$?

if [ $RETURN -eq 0 ];
then
  printf "The script build-enviroment.sh was executed successfuly\\n"
else
  sh destroy-enviroment.sh "$dockerpath_env" "$envfile"
  printf "The script assertHealty.sh was NOT executed successfuly and returned the code %s \\n" $RETURN
  exit $RETURN
fi

sleep 10

printf "Step 2: Assert system healty\\n"
sh assertHealthy.sh "$podname" "1"
RETURN=$?

if [ $RETURN -eq 0 ];
then
  printf "The script assertHealthy.sh was executed successfuly\\n"
else
  sh destroy-enviroment.sh "$dockerpath_env" "$envfile"
  printf "The script assertHealty.sh was NOT executed successfuly and returned the code %s \\n" $RETURN
  exit $RETURN
fi

printf "Step 3: Send Requests\\n"
sh sendRequests.sh "$orchnum" "3000" "70" & sh dynamic-up-scale.sh "$dockerpath_env" "$envfile" "$podname" "$podnum" & wait
RETURN=$?

if [ $RETURN -eq 0 ];
then
  printf "The script sendRequests.sh was executed successfuly\\n"
else
  sh destroy-enviroment.sh "$dockerpath_env" "$envfile"
  printf "The script sendRequests.sh was NOT executed successfuly and returned the code %s \\n" $RETURN
  exit $RETURN
fi

printf "Step 4: Collect Logs\\n"
sh collect-logs.sh "$podname" "$podnum"
RETURN=$?
if [ $RETURN -eq 0 ];
then
  printf "The script collect-logs.sh was executed successfuly\\n"
else
  sh destroy-enviroment.sh "$dockerpath_env" "$envfile"
  printf "The script collect-logs.sh was NOT executed successfuly and returned the code %s \\n" $RETURN
  exit $RETURN
fi

sh destroy-enviroment.sh "$dockerpath_env" "$envfile"
return 0