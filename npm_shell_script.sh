#!/bin/bash

##### Process Kill -15 java
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


##########################################################
########################## NODE ##########################
##########################################################
##### Git Check
echo "frontend processing"
cd
cd mood_tracker-view
if [ ! -d ".git" ]; then
    echo "frontend-file does not have a .git repository"
    exit 1
fi


##### Git PULL
echo "running 'git reset --hard'"
git reset --hard
echo "running 'git pull'"
git pull


##### Change URL-Origin info
FILENAME="src/common.js"
sed -i "s|^const API_HOST = \"localhost\"; *|//const API API_HOST = \"localhost\";|" "$FILENAME"
sed -i "s|^//const API_HOST = \"3.38.99.65\"; *|const API API_HOST = \"3.38.99.65\";|" "$FILENAME"
echo "API Origin modification completed"


##### Apply 'npm run build'
echo "npm run build...."
npm run build
