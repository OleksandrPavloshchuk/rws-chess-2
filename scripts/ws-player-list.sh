#!/usr/bin/env bash

set -x

websocat "ws://localhost:8080/ws/players/$1"
