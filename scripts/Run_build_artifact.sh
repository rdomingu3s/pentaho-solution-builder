#!/bin/sh

BASEDIR=`dirname $0`

. $BASEDIR/base/config
. $BASEDIR/base/functions.sh

######
# Run the build artifact 
# Version: 1
#
# Arg1: Start date	(yyyy-MM-dd)
# Arg2: End date	(yyyy-MM-dd)
#
######

# Check if we have at least 2 args
# check_args $# 2

cd $BASEDIR/base
./kitchen.sh -file="${KETTLE_HOME}/runtime/build_package/build_package.kjb"  -param:org=$1 -param:name=$2 -param:rev=$3