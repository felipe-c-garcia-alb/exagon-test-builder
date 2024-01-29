#!/bin/bash

#if [ $# -lt 4 ]; then
#  echo "Needs 4 arguments"
#  return 1 2>/dev/null
#  exit 1
#fi

dockerpath_env="../docker/docker-compose-orch.yml"
envfile="../docker/orchestrator/environments/.env"
podname="provisioning-orchestrator"
#initialRequestsPath="../config/initialrequests.txt"
#finalRequestsPath="../config/finalrequests.txt"


printf "Step 1: Build enviroment!\\n"
sh build-enviroment.sh "$dockerpath_env"  "$envfile"
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
sh sendRequests.sh
RETURN=$?
if [ $RETURN -eq 0 ];
then
  printf "The script sendRequests.sh was executed successfuly\\n"
else
  sh destroy-enviroment.sh "$dockerpath_env" "$envfile"
  printf "The script sendRequests.sh was NOT executed successfuly and returned the code %s \\n" $RETURN
  exit $RETURN
fi

sh destroy-enviroment.sh "$dockerpath_env" "$envfile"
return 0