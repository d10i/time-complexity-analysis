#!/usr/bin/env bash

cd dissertation
./build.sh
cd ..

cd agent-test
./build.sh
cd ..

./run.sh
