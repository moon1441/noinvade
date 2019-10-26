#!/bin/bash

SERVER_PORT=8080

MYSQL_HOST=192.168.50.8
MYSQL_PORT=3306
MYSQL_SCHEMA=nilm
MYSQL_USER=admin
MYSQL_PASSWD=admin

INFLUX_HOST=192.168.50.8
INFLUX_PORT=8086
INFLUX_DB_NAME=nilm

BASE_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null 2>&1 && pwd )"
JAR_FILE="${BASE_DIR}/noinvade-0.0.1.jar"
JAVA_HOME="${BASE_DIR}/jdk-11.0.2"
JAVA_PKG_NAME="${BASE_DIR}/openjdk-11.0.2_linux-x64_bin.tar.gz"

echo "Application directory: ${BASE_DIR}"

if [[ ! -e ${JAVA_HOME} ]] && [[ -e ${JAVA_PKG_NAME} ]]; then
    echo "Unpacking OpenJDK..."
    tar -C ${BASE_DIR} -xzf ${JAVA_PKG_NAME}
fi

${JAVA_HOME}/bin/java -jar ${JAR_FILE} \
    -Dserver.port=${SERVER_PORT} \
    -DinfluxUrl="http://${INFLUX_HOST}:${INFLUX_PORT}" \
    -DinfluxDBName=nilm \
    -DmysqlUrl="jdbc:mysql://${MYSQL_HOST}:${MYSQL_PORT}/${MYSQL_SCHEMA}" \
    -DmysqlUser=${MYSQL_USER} \
    -DmysqlPsw=${MYSQL_PASSWD}