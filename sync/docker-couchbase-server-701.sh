#!/bin/bash
echo "stopping cb-server 7.01 image ..."
docker stop cb-server
echo "deleting cb-server 7.01 image ..."
docker rm cb-server
echo "Running cb-server 7.01..."
docker run -d --name cb-server-701 --network workshop -p 8091-8094:8091-8094 -p 11210:11210 couchbase:enterprise-7.0.1
