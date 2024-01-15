docker build -t knote-java .
docker run --name=mongo --rm --network=knote -d mongo
docker run --name=knote-java --rm --network=knote -p 8080:8080 -e SPRING_DATA_MONGODB_URI=mongodb://mongo:27017/dev -d knote-java



