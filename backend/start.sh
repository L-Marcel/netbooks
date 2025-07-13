#!/bin/sh
SOURCE_DATA_IN_REPO="./database/data" 
VOLUME_MOUNT_PATH="./database"

cp -r -n $SOURCE_DATA_IN_REPO/. $VOLUME_MOUNT_PATH/
echo "LS:"
ls
echo "LS ..:"
ls ..
echo "LS ./app:"
ls ./app
java -jar target/backend-0.0.1-SNAPSHOT.jar
