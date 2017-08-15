#!/bin/bash

export STORM_ZK_CLI_HOME=`pwd`/$0/../../
#echo $STORM_ZK_CLI_HOME

if [ -f $STORM_ZK_CLI_HOME/target/stormZkCli-1.0-SNAPSHOT.jar ]; then
  java -cp $STORM_ZK_CLI_HOME/target/stormZkCli-1.0-SNAPSHOT.jar storm.zookeeper.StormZkClient
else
   echo "jar file not found"
fi