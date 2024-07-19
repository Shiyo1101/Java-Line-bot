# ベースイメージとしてMavenを使用し、依存関係を事前にダウンロード
FROM maven:3.8.6-eclipse-temurin-17 AS build
WORKDIR /home/app

# 依存関係だけを事前にコピーしてダウンロード
COPY pom.xml .
RUN mvn dependency:go-offline -B

# アプリケーションソースをコピー
COPY src ./src

# パッケージング
RUN mvn clean package -Dmaven.test.skip=true

# ランタイムイメージを作成
FROM eclipse-temurin:17-alpine
WORKDIR /usr/local/lib
COPY --from=build /home/app/target/line-bot-0.0.1-SNAPSHOT.jar app.jar

# ポートを公開
EXPOSE 8080

# 健康チェックを追加（Render.comがサポートしているかどうかを確認）
HEALTHCHECK --interval=30s --timeout=10s --start-period=30s --retries=3 CMD curl -f http://localhost:8080/actuator/health || exit 1

# エントリーポイントを設定
ENTRYPOINT ["java", "-jar", "/usr/local/lib/app.jar"]
