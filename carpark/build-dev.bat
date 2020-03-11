cd /d .
del /s /q .\logs\carpark*.log
mvn  -P local clean install -T 1C


