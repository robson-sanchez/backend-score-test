#!/usr/bin/env bash

echo "Building score application"
mvn clean install

IMAGE_NAME=ifood-score

if [ "$#" -gt 0 ]; then
  IMAGE_NAME=ifood-score:$1
fi

echo "Building docker image"
echo "docker build -f ./docker/application/Dockerfile -t $IMAGE_NAME ."
docker build -f ./docker/application/Dockerfile -t $IMAGE_NAME .