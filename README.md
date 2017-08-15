# stormZkCli

Storm objects are serialized before written to ZooKeeper which makes debugging a little bit difficult. 
This project provides a command line interface for storm developers to easily print out Storm objects from ZooKeeper.

## Prerequisites

* [Storm](https://github.com/apache/storm) built locally.
* ZooKeeper installed and launched.
* Storm launched

## Installation

* `git clone git@github.com:Ethanlm/stormZkCli.git`
* `mvn package`

## Running

* Change [storm.yaml](https://github.com/Ethanlm/stormZkCli/blob/master/src/main/resources/storm.yaml) accordingly
* Run `bin/stormZkCli.sh`
