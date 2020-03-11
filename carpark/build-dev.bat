cd /d .
del /s /q .\logs\carpark*.log
mvn  -P dev clean install -T 1C


