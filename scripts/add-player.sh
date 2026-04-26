#!/usr/bin/env bash

set -x

curl -X POST \
  -H "Content-Type: text/plain" \
  -d  "$1" \
  http://localhost:8080/players