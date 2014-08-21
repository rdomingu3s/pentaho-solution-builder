#!/bin/bash
. config
. functions

init
./pan.sh "${1+$@}" -level $KETTLE_LOG_LEVEL
