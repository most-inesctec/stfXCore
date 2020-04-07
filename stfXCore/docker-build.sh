#!/bin/sh
./mvnw package && java -jar target/sftx-core.jar
docker build -t stfx-core . --no-cache --force-rm

