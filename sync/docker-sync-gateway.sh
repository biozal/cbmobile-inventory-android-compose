#!/bin/bash
echo "stopping sync-gateway  ..."
docker stop sync-gateway
echo "deleting sync-gateway  ..."
docker rm sync-gateway
echo "Running sync-gateway 2.8..."
docker run -p 4984-4985:4984-4985 --network workshop --name sync-gateway -d -v `pwd`/sync-gateway-config-inventory-demo.json:/etc/sync_gateway/sync_gateway.json couchbase/sync-gateway:2.8.2-enterprise -adminInterface :4985 /etc/sync_gateway/sync_gateway.json

