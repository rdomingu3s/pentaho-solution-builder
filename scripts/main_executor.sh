#!/bin/sh

BASEDIR=`dirname $0`

. $BASEDIR/base/config
. $BASEDIR/base/functions.sh

######
# Run the load dependencies and run the target defined
# Version: 1
#
# Arg1: Start date	(yyyy-MM-dd)
# Arg2: End date	(yyyy-MM-dd)
#
######

# Check if we have at least 2 args
# check_args $# 2

cd $BASEDIR/base
./kitchen.sh -file="${KETTLE_HOME}/runtime/executor/main_executor.kjb" -param:plugin=$1 -param:target=$2 $3