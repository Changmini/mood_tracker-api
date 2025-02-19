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


echo "success"