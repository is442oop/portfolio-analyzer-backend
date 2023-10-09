@ECHO off

REM Check if an argument is provided
IF "%~1"=="" GOTO HELP

REM Check the provided argument and execute corresponding target
IF /I "%~1"=="dev" GOTO DEV
IF /I "%~1"=="up" GOTO UP
IF /I "%~1"=="down" GOTO DOWN
IF /I "%~1"=="prune" GOTO PRUNE
IF /I "%~1"=="build" GOTO BUILD
IF /I "%~1"=="mvn" GOTO MVN
IF /I "%~1"=="help" GOTO HELP

:help
ECHO Available targets:
ECHO   dev    : Run Docker Compose and Maven project
ECHO   up     : Start Docker services in the background
ECHO   down   : Stop and remove Docker containers, networks, volumes
ECHO   prune  : Remove unused Docker containers, networks, volumes, images
ECHO   build  : Clean and build the Maven project
ECHO   mvn    : Run the Maven project
ECHO   help   : Show this help message
GOTO :EOF

:DEV
call :UP
call :BUILD
call :MVN
GOTO :EOF

:UP
CALL docker compose -f ./.docker/docker-compose.yml up -d
GOTO :EOF

:DOWN
CALL docker compose -f ./.docker/docker-compose.yml down -v
GOTO :EOF

:PRUNE
CALL docker system prune -a
GOTO :EOF

:BUILD
CALL ./scripts/mvnw.cmd clean install
GOTO :EOF

:MVN
CALL ./scripts/mvnw.cmd spring-boot:run
GOTO :EOF
