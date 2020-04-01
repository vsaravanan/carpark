#!/usr/bin/env bash
####
project=carpark
git pull
rm -rf /opt/$project/logs/*
sudo systemctl stop $project
mvn  -P server clean install -T 1C
sudo systemctl restart $project; systemctl status $project
