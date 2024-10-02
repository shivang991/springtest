
# Build Frontend

FROM node:21-alpine AS frontend

RUN apk add --no-cache libc6-compat
WORKDIR /app

COPY client/pnpm-lock.yaml client/package.json client/app.vue client/nuxt.config.ts ./
COPY client/pages pages
COPY client/assets assets
COPY client/public public

RUN corepack enable pnpm && pnpm i --frozen-lockfile
RUN pnpm generate


# Build application jar

FROM eclipse-temurin:21-jdk-jammy AS builder

WORKDIR /opt/app

COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN ./mvnw dependency:go-offline
COPY src/ src
COPY --from=frontend /app/.output/public/ src/main/resources/static
RUN ./mvnw clean install -DskipTests


# Serve the jar
FROM eclipse-temurin:21-jre-jammy

RUN addgroup springtest-grp; adduser  --ingroup springtest-grp --disabled-password user
USER user

WORKDIR /opt/app

EXPOSE 8080
COPY --from=builder /opt/app/target/*.jar /opt/app/*.jar
ENTRYPOINT [ "java", "-jar", "/opt/app/*.jar"]