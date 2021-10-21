echo "stopping cb-server image ..."
docker stop cb-server
echo "deleting cb-server image ..."
docker rm cb-server
echo "Running cb-server..."
docker run -d --name cb-server-701 --network workshop -p 8091-8094:8091-8094 -p 11210:11210 couchbase:enterprise-7.0.1