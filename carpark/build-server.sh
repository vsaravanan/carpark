#!/usr/bin/env bash
####
project=carpark
git checkout .
git pull
rm -rf /opt/$project/logs/*
sudo systemctl stop $project
mvn  -P server clean install -T 1C
sudo systemctl restart $project; systemctl status $project
chmod 755 build-server.sh
