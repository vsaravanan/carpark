git pull
rm -rf /opt/carpark/logs/*
mvn  -P server clean war:exploded install -T 1C


