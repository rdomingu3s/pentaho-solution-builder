#!/bin/sh

BASEDIR=`dirname $0`

. $BASEDIR/base/config
. $BASEDIR/base/functions.sh

######
# Run the build artifact 
# Version: 1
#
# Arg1: Plugin name (jar name)
#
######

# Check if we have at least 2 args
# check_args $# 1

cd $BASEDIR/base
./kitchen.sh -file="${KETTLE_HOME}/runtime/executor/install_plugin.kjb"  -param:plugin=$1