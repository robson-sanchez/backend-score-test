#!/usr/bin/env bash

IMAGE_NAME=ifood-score

if [ "$#" -gt 0 ]; then
  IMAGE_NAME=ifood-score:$1
fi

echo "Stopping score-app docker container"
docker stop score-app

echo "Removing score-app docker container"
docker rm score-app

echo "Running score-app docker container"
docker run --name score-app -p 8080:80 -d $IMAGE_NAME