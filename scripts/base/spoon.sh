#!/bin/bash
. config
. functions

init
./spoon.sh "${1+$@}"
