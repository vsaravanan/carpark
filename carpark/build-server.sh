git pull
rm -rf /opt/carpark/logs/*
mvn  -P server clean install -T 1C
systemctl stop carpark; systemctl start carpark; systemctl status carpark


