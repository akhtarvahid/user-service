## User-service

Docker command to start postgres

> docker run --name user-service -p 5432:5432 -e POSTGRES_PASSWORD=postgres -d postgres




ADD users to the table by commenting everything in SecurityConfiguration then signup from postman
ADD roles to the table
ADD(Map) role, user table id's in users_role
Run addSampleRegisteredClient testcase to add client