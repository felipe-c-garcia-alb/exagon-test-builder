#!/bin/bash

sleep 5

if [ -z "$1" ]; then
  docker run -it --rm --network=host ms-tester "1"
else
  docker run -it --rm --network=host ms-tester "$1"
fi