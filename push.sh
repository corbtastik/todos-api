#!/bin/bash
# set var(s) accordingly
cf push \
--var app.name=todos-api \
--var app.artifact=target/todos-api-1.0.0.SNAP.jar \
--var app.route=corbs-todos-api.cfapps.io \
--var app.memory=1G \
--var env-key-1=EUREKA_CLIENT_ENABLED \
--var env-val-1=false \
--var env-key-2=SPRING_CLOUD_CONFIG_ENABLED \
--var env-val-2=false \
--var env-key-3=TODOS_API_LIMIT \
--var env-val-3=128
