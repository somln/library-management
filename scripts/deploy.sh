#!/usr/bin/env bash

cd /home/ubuntu/app

EXIST_BLUE=$(docker-compose ps | grep "spring-blue" | grep Up)

if [ -z "$EXIST_BLUE" ]; then
    docker-compose up -d spring-blue
    BEFORE_COLOR="green"
    AFTER_COLOR="blue"
    BEFORE_PORT=8081
    AFTER_PORT=8080
else
    docker-compose up -d spring-green
    BEFORE_COLOR="blue"
    AFTER_COLOR="green"
    BEFORE_PORT=8080
    AFTER_PORT=8081
fi

echo "===== ${AFTER_COLOR} server up(port:${AFTER_PORT}) ====="


for cnt in {1..10}
do
    echo "===== 서버 응답 확인중(${cnt}/10) =====";
    UP=$(curl -s http://localhost:${AFTER_PORT}/api/actuator/health)
    if [ -z "${UP}" ]
        then
            sleep 10
            continue
        else
            break
    fi
done

if [ $cnt -eq 10 ]
then
    echo "===== 서버 실행 실패 ====="
    exit 1
fi

echo "===== Nginx 설정 변경 ====="
docker exec -it nginx /bin/bash -c "sed -i 's/${BEFORE_PORT}/${AFTER_PORT}/' /etc/nginx/nginx.conf && nginx -s reload"

echo "$BEFORE_COLOR server down(port:${BEFORE_PORT})"
docker-compose stop spring-${BEFORE_COLOR}