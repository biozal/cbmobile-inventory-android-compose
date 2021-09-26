#!/bin/bash
echo "Running cb-server..."
docker run -d --name cb-server --network workshop -p 8091-8094:8091-8094 -p 11210:11210 priyacouch/couchbase-server-userprofile:dev
