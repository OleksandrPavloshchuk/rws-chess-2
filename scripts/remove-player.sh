#!/usr/bin/env bash

set -x

curl -v \
  -X DELETE \
  "http://localhost:8080/players/$1"