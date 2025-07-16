# Elasticsearch Setup

This project includes a `docker-compose.yml` file to spin up a single-node Elasticsearch cluster.

## Prerequisites

- Docker installed
- Docker Compose installed

## Starting Elasticsearch

To start the Elasticsearch cluster, run the following command in the directory containing the `docker-compose.yml` file:

```bash
docker-compose up -d
```
This will launch a single-node Elasticsearch cluster accessible at http://localhost:9200. The -d flag runs the container in detached mode (in the background).

## Verifying Elasticsearch is Running

To confirm that Elasticsearch is running, execute this command:
```bash
curl http://localhost:9200
```
### You should see a JSON response similar to the following, indicating that Elasticsearch is operational:
```bash
name	"shubham-VivoBook-ASUSLaptop-K3502ZA-S3502ZA"
cluster_name	"elasticsearch"
cluster_uuid	"Px_XHWg5SLqtmeAbXy3XjA"
version	
number	"9.0.3"
build_flavor	"default"
build_type	"deb"
build_hash	"cc7302afc8499e83262ba2ceaa96451681f0609d"
build_date	"2025-06-18T22:09:56.772581489Z"
build_snapshot	false
lucene_version	"10.1.0"
minimum_wire_compatibility_version	"8.18.0"
minimum_index_compatibility_version	"8.0.0"
tagline	"You Know, for Search"
```
## Build & Run the Spring Boot Application
From the project root:

 **Build**
 ```
mvn clean package
```
 **Run**
```
java -jar target/Course-ElasticSearch-0.0.1-SNAPSHOT.jar
```
 **Or via Maven plugin**
 ```
mvn spring-boot:run
```
The application listens on port 8080 by default.

## Populate the Index with Sample Data
The app auto-loads sample data on startup via DataLoader. You should see startup logs indicating documents indexed.

## API Endpoints
**All search endpoints use GET /api/search with query parameters**

Full-Text Search
```
http://localhost:8080/api/search?q=math
```
Price Range
```
http://localhost:8080/api/search?minPrice=20&maxPrice=100
```
Category Search
```
http://localhost:8080/api/search?category=Math
```
Age & Date Filters
```
http://localhost:8080/api/search?minAge=10&maxAge=15&nextSessionDate=2025-09-01
```
Sort + Pagination
```
http://localhost:8080/api/search?sort=priceDesc&page=1&size=5
```
nextSessionDate
```
http://localhost:8080/api/search?nextSessionDate=2025-09-01
```
sort + type filter
```
http://localhost:8080/api/search?sort=priceAsc&type=COURSE
```
