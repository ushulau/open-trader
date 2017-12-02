#!/usr/bin/env bash
################################################
# Example of build and deployment script for   #
# open-trader into  AWS VM                     #
################################################
HOST_IP="IP address of your host"
cd ~/work/open-trader/
mvn clean install
echo "build is complete"
scp ~/work/open-trader/target/open-trader.jar ubuntu@$HOST_IP:~/
ssh ubuntu@HOST_IP << 'ENDSSH'
echo "Stopping open trader client!"
for pid in $(sudo ps -ef | grep "java" | awk '{print $2}'); do sudo kill -9 $pid; done
nohup sudo java -jar open-trader.jar &
ENDSSH
