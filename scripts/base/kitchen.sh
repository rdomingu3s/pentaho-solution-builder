#!/bin/bash
. config
. functions

init
./kitchen.sh "${1+$@}" -level $KETTLE_LOG_LEVEL 
