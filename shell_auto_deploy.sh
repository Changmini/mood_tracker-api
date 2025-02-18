#!/bin/bash

##### PROCESS KILL
processes=$(ps -ef | grep java | grep -v grep)
if [ -z "$processes" ]; then
    echo "Java process is not running"
else
    echo "$processes" | while read -r line; do
        pid=$(echo "$line" | awk '{print $2}')
        echo "Java process PID $pid shutdown..."
        kill -15 "$pid"
    done
    echo "Java process terminated"
fi

if ! command -v git &> /dev/null; then
    echo "git command is not installed.. please install git"
    exit 1
fi



##### GIT - API
echo "backend processing"
cd
cd mood_tracker-api
if [ ! -d ".git" ]; then
    echo "backend-file does not have a .git repository"
    exit 1
fi
echo "running 'git reset --hard'"
git reset --hard
echo "running 'git pull'"
git pull

FILENAME="src/main/resources/application.yml"
USERNAME=
PASSWORD=
sed -i "0,/^username:/! { /^username:/ { s/^username: */username: $USERNAME/; t } }" "$FILENAME"
sed -i "0,/^password:/! { /^password:/ { s/^password: */username: $PASSWORD/; t } }" "$FILENAME"
echo "DB-info modification completed"

chmod 777 ./gradlew
./gradlew build

processes=$(ps -ef | grep java | grep -v grep)
echo "$processes" | while read -r line; do
        pid=$(echo "$line" | awk '{print $2}')
        echo "Java process PID $pid shutdown..."
        kill -15 "$pid"

cd build/lib
nohup sudo java -jar mood_tracker-api-0.0.1-SNAPSHOT.jar > /dev/null 2>&1 &



echo "success"