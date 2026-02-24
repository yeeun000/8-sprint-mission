# 베이스 이미지
FROM amazoncorretto:17

# 작업 디렉토리
WORKDIR /app

# Gradle Wrapper와 설정 파일 복사
COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .

# 소스 코드 복사, jar만
COPY build/libs/discodeit-1.2-M8.jar app.jar

# 포트 노출
EXPOSE 80

# 환경 변수
ENV PROJECT_NAME=discodeit
ENV PROJECT_VERSION=1.2-M8
ENV JVM_OPTS=""
ENV SERVER_PORT=80

# 애플리케이션 실행 명령어
ENTRYPOINT ["sh", "-c", "java $JVM_OPTS -jar app.jar"]