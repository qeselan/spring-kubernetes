docker build -t knote-java .
docker run --name=mongo --rm --network=knote -d mongo
docker run --name=knote-java --rm --network=knote -p 8080:8080 -e SPRING_DATA_MONGODB_URI=mongodb://mongo:27017/dev -d
knote-java

--- Minio ---
docker run --name=minio --rm -p 9000:9000 -e MINIO_ACCESS_KEY=mykey -e MINIO_SECRET_KEY=mysecret -d minio/minio server
/data
java -Dminio.access.key=mykey -Dminio.secret.key=mysecret -jar target/knote-java-0.0.1-SNAPSHOT.jar



