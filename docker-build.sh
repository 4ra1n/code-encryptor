#!/bin/bash

IMAGE_NAME="code-encryptor"
CONTAINER_NAME="code-encryptor-builder"
Y4_VERSION="0.4"

EXISTING_IMAGE=$(docker images -q $IMAGE_NAME:$Y4_VERSION)
if [ ! -z "$EXISTING_IMAGE" ]; then
    echo "image $IMAGE_NAME:$Y4_VERSION already exists. deleting..."
    docker rmi $EXISTING_IMAGE
fi

docker build -t $IMAGE_NAME:$Y4_VERSION .
CONTAINER_ID=$(docker run -d --name $CONTAINER_NAME $IMAGE_NAME:$Y4_VERSION)
echo "container is running with ID: $CONTAINER_ID"

echo "waiting for container to complete the task"
docker wait $CONTAINER_ID

docker cp $CONTAINER_ID:/app/build.zip .
echo "file copied to build.zip"

docker stop $CONTAINER_ID
docker rm $CONTAINER_ID

echo "container stopped and removed"

unzip build.zip
rm -rf build.zip
mv -f target/libdecrypter.so src/main/resources/
mv -f target/libencryptor.so src/main/resources/
rm -rf target
