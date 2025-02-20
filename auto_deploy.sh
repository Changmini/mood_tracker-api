#!/bin/bash
# # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # #
#
# Currently, the shell script must be executed with the API and VIEW projects existing in the path of the Linux User( /home/{user} )
#
# # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # #
read -p "DB username : " USERNAME
if [ -z "$USERNAME" ]; then
    echo "Nothing username"
fi
read -sp "DB password : " PASSWORD
if [ -z "$PASSWORD" ]; then
    echo "Nothing username"
fi

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
sed -i "s|^const API_HOST = \"localhost\"; *|//const API_HOST = \"localhost\";|" "$FILENAME"
sed -i "s|^//const API_HOST = \"3.38.99.65\"; *|const API_HOST = \"3.38.99.65\";|" "$FILENAME"
echo "API Origin modification completed"


##### Apply 'npm run build'
echo "npm run build...."
npm run build


##########################################################
########################## JAVA ##########################
##########################################################
##### Git Check
echo "backend processing"
cd
cd mood_tracker-api
if [ ! -d ".git" ]; then
    echo "backend-file does not have a .git repository"
    exit 1
fi


##### Apply 'gradlew clean'
./gradlew clean


##### Git PULL
echo "running 'git reset --hard'"
git reset --hard
echo "running 'git pull'"
git pull


##### Set Database info
FILENAME="src/main/resources/application.yml"
sed -i "s/usrename:/usrename: $USERNAME/g" "$FILENAME"
sed -i "s/password:/password: $PASSWORD/g" "$FILENAME"
echo "DB-info modification completed"


##### Apply 'chmod 777 gradlew'
chmod 777 ./gradlew
./gradlew build


##### Gradle process terminated
processes=$(ps -ef | grep java | grep -v grep)
echo "$processes" | while read -r line; do
        pid=$(echo "$line" | awk '{print $2}')
        echo "Java process PID $pid shutdown..."
        kill -15 "$pid"
done

##### Run jar
cd build/libs
nohup sudo java -jar mood_tracker-api-0.0.1-SNAPSHOT.jar > /dev/null 2>&1 &


echo "success"