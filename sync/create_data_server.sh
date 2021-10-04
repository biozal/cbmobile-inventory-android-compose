curl -v -X POST http://localhost:8091/pools/default/buckets \
-u Administrator:password \
-d name=projects \
-d ramQuotaMB=256

sleep 10s
curl -v http://localhost:8093/query/service \
  -u Administrator:password \
  -d 'statement=CREATE INDEX idx_projects_team on projects(team)'
      
curl -v http://localhost:8093/query/service \
  -u Administrator:password \
  -d 'statement=CREATE INDEX idx_projects_type on projects(type)'
      
curl -v http://localhost:8093/query/service \
  -u Administrator:password \
  -d 'statement=CREATE INDEX idx_projects_projectId on projects(projectId)'

docker cp sample-project-documents-server.json \
cb-server:/sample-project-documents-server.json

docker exec -it cb-server /opt/couchbase/bin/cbimport json --format list \
  -c http://localhost:8091 -u Administrator -p password \
  -d "file:///sample-project-documents-server.json" -b 'projects' -g %projectId%
