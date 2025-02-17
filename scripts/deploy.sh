#!/usr/bin/env bash

cd /home/ubuntu/app

EXIST_BLUE=$(docker-compose ps | grep "spring-blue" | grep Up)

if [ -z "$EXIST_BLUE" ]; then
    BEFORE_COLOR="green"
    AFTER_COLOR="blue"
    BEFORE_PORT=8081
    AFTER_PORT=8080
else
    BEFORE_COLOR="blue"
    AFTER_COLOR="green"
    BEFORE_PORT=8080
    AFTER_PORT=8081
fi

echo "===== 기존 이미지 및 컨테이너 정리 ====="
docker-compose stop spring-${AFTER_COLOR}
docker-compose rm -f spring-${AFTER_COLOR}

# 기존 이미지 삭제 (이미지가 없을 수도 있으므로 오류 무시)
docker rmi $(docker images -q app_spring-${AFTER_COLOR}) || true

echo "===== 새 이미지 빌드 ====="
docker-compose build --no-cache spring-${AFTER_COLOR}

echo "===== ${AFTER_COLOR} 서버 실행(port:${AFTER_PORT}) ====="
docker-compose up -d spring-${AFTER_COLOR}

# 서버 응답 체크
for cnt in {1..10}
do
    echo "===== 서버 응답 확인중(${cnt}/10) ====="
    UP=$(curl -s http://localhost:${AFTER_PORT}/api/actuator/health | jq -r .status)
    if [ "$UP" == "UP" ]; then
        break
    else
        sleep 10
    fi
done

if [ $cnt -eq 10 ]; then
    echo "===== 서버 실행 실패 ====="
    exit 1
fi

echo "===== Nginx 설정 변경 ====="
sudo sed -i "s/${BEFORE_PORT}/${AFTER_PORT}/" /etc/nginx/nginx.conf && sudo nginx -s reload

echo "===== ${BEFORE_COLOR} 서버 종료(port:${BEFORE_PORT}) ====="
docker-compose stop spring-${BEFORE_COLOR}
