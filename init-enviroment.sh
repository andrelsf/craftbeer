#!/usr/bin/env sh
# 
# @Date 06/02/2021
# @author Andre Ferreira <andre.dev.linux@gmail.com>

JAR_FILE=$(pwd)/target/craft-beer-1.0.jar

if [ ! -f ${JAR_FILE} ];
then
    echo "Gerando o arquivo JAR"
    $(pwd)/mvnw clean package -Dmaven.test.skip=true
fi

CRAFTBEER_API=$(docker images craftbeer_api --format "{{.Repository}}")
CRAFTBEER_POSTGRESQL=$(docker images craftbeer_postgresql --format "{{.Repository}}")

if test -z ${CRAFTBEER_API} && test -z ${CRAFTBEER_POSTGRESQL};
then
    echo "Iniciando build das imagens"
    docker-compose build --no-cache
fi

DOCKER_POSTGRESQL=$(docker ps --format "{{.Names}}" | grep -i craftbeer_postgresql_1)

if [ -z ${DOCKER_POSTGRESQL} ];
then
    echo "Iniciando docker Database PostgreSQL"
    docker-compose up -d postgresql
fi

echo "Esperando Postgres..."

while ! nc -z 10.62.0.254 5432;
do
    sleep 1
done

echo "PostgreSQL iniciado"

###### DOCKER

DOCKER_API=$(docker ps --format "{{.Names}}" | grep -i craftbeer_api_1)

if [ -z ${DOCKER_API} ];
then
    echo "Iniciando CraftBeer API"
    docker-compose up -d api
fi

echo -n "Deseja iniciar os testes? S ou N: "
read resposta

if test ${resposta} = 'S' || test ${resposta} = 's';
then
    echo "Iniciando os testes"
    $(pwd)/mvnw test
fi

echo "\ncurl -X GET -H "Accept:application/json" -H "Content-Type:application/json;charset=utf-8" http://localhost:9000/api/v1/beers\n"