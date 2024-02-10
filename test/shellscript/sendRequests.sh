#!/bin/bash

sleep 5

if [ $# -lt 3 ]; then
  docker run -i --rm --network=host ms-tester "1" "1000" "50"
else
  docker run -i --rm --network=host ms-tester "$1" "$2" "$3"
fi

