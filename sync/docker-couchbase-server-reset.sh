#!/bin/bash
echo "create network bridge if it doesn't exist"
docker network create -d bridge workshop
echo "stopping cb-server image ..."
docker stop cb-server
echo "deleting cb-server image ..."
docker rm cb-server
echo "Running cb-server..."
docker run -d --name cb-server --network workshop -p 8091-8094:8091-8094 -p 11210:11210 priyacouch/couchbase-server-userprofile:7.0.0-dev
