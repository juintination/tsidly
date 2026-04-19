# Build stage
FROM eclipse-temurin:21-jdk AS builder

# 작업 디렉토리
WORKDIR /build

# Gradle 설정 및 wrapper 복사
COPY gradlew ./
COPY gradlew.bat ./
COPY gradle/ gradle/
COPY build.gradle.kts settings.gradle.kts ./

RUN chmod +x gradlew

# Gradle dependencies 실행
RUN ./gradlew dependencies --no-daemon || true

# 소스 코드 복사
COPY src/ src/

# 실행 JAR 생성
RUN ./gradlew bootJar -x test --no-daemon


# Runtime stage
FROM eclipse-temurin:21-jre

WORKDIR /app

# 빌드 결과 복사
COPY --from=builder /build/build/libs/*.jar app.jar

# 애플리케이션 실행
ENTRYPOINT ["java", "-jar", "app.jar"]
