cd /d .
del /s /q .\logs\carpark*.log
mvn  -P local clean war:exploded install -T 1C


