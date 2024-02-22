#!/bin/bash


#cat /tmp/tester/id.txt | while read -r line || [[ -n "$line" ]];
#do
#  cp -p /var/lib/docker/containers/${line}/${line}-json.log ../logs/${line}-json.log
#done
cp -p /tmp/tester/* ../logs
rm -f /tmp/tester/*
exit 0