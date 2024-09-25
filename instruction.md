## User-service

Docker command to start postgres
```bash
docker run --name user-service -p 5432:5432 -e POSTGRES_PASSWORD=postgres -d postgres
```


#### Before running application
ADD users to the table by commenting everything in SecurityConfiguration then signup from postman
ADD roles to the table
ADD(Map) role, user table id's in users_role
Run `addSampleRegisteredClient` testcase to add client




#### How to create and run multiple instances.
1. add this `server.port=${SERVER_PORT}` to application.properties
2. Got to `Run->Edit configurations`
3. Select `UserServiceApplication` under Spring Boot and click on `copy configuration` as many as you want server(instances).
4. Run all UserServiceApplication, UserServiceApplication(1), UserServiceApplication(2)...etc one by one.
5. Go to http://localhost:8761/ to see all the instances running
6. And `SERVER_PORT=8080`, `SERVER_PORT=8081`, `SERVER_PORT=8082`...etc inside Environment variables as many instances created


#### Steps to run the application through api-gateway
1. Run User service and service discovery
2. once services and service-discovery is up
3. Run api-gateway-springboot-mss
4. Test http://localhost:9000/api/users/all in browser or postman


#### Remove cached .idea
```bash
git rm -r --cached .idea
```

#### Health check of service
http://localhost:8081/actuator/health