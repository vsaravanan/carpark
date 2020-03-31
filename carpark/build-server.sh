#!/usr/bin/env bash
####
git pull
rm -rf /opt/carpark/logs/*
mvn  -P server clean install -T 1C


