./gradlew build
docker buildx build --platform linux/arm64 --tag=tomvanbesien/raspberry-uploader:latest --push .
