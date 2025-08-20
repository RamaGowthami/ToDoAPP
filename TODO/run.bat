@echo off
echo Starting Spring Boot ToDo Application...
echo.
echo Make sure you have:
echo 1. Java 21 installed and JAVA_HOME set
echo 2. MySQL running with database 'todoapp' created
echo 3. Updated application.properties with your MySQL password
echo.
pause
./mvnw spring-boot:run
pause


