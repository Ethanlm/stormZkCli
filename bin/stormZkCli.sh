#!/usr/bin/env bash

PRG="${0}"
STORM_ZK_BIN_DIR=`dirname ${PRG}`
export STORM_ZK_BASE_DIR=`cd ${STORM_ZK_BIN_DIR}/..;pwd`
export STORM_ZK_CONF_DIR="${STORM_ZK_CONF_DIR:-$STORM_ZK_BASE_DIR/conf}"
export STORM_ZK_CONF_FILE="${STORM_ZK_CONF_FILE:-$STORM_ZK_BASE_DIR/conf/storm.yaml}"
export STORM_ZK_LIB_DIR="${STORM_ZK_LIB_DIR:-$STORM_ZK_BASE_DIR/lib}"


echo "loading jars"

cp=""
FILES=${STORM_ZK_LIB_DIR}/*.jar

for jar_file in $FILES
do
   cp=${cp}:${jar_file}
done


echo "starting server"

jvmOpts="-Dstorm.conf.file="${STORM_ZK_CONF_FILE}

java -cp $cp $jvmOpts storm.zookeeper.StormZkClient