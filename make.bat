@ECHO OFF

REM Check if an argument is provided
IF "%~1"=="" GOTO HELP

REM Check the provided argument and execute corresponding target
IF /I "%~1"=="dev" GOTO DEV
IF /I "%~1"=="up" GOTO UP
IF /I "%~1"=="down" GOTO DOWN
IF /I "%~1"=="prune" GOTO PRUNE
IF /I "%~1"=="build" GOTO BUILD
IF /I "%~1"=="help" GOTO HELP

REM If an invalid argument is provided, show the help message
:HELP
ECHO Available targets:
ECHO   dev    : Run Docker Compose and Maven project
ECHO   up     : Start Docker services in the background
ECHO   down   : Stop and remove Docker containers, networks, volumes
ECHO   prune  : Remove unused Docker containers, networks, volumes, images
ECHO   build  : Clean and build the Maven project
ECHO   help   : Show this help message
GOTO :EOF

REM Targets
:DEV
CALL :UP
CALL :BUILD
CALL :MVN
GOTO :EOF

:UP
docker compose -f ./.docker/docker-compose.yml up -d
GOTO :EOF

:DOWN
docker compose -f ./.docker/docker-compose.yml down -v
GOTO :EOF

:PRUNE
docker system prune -a
GOTO :EOF

:BUILD
mvn clean install
GOTO :EOF

:MVN
mvn spring-boot:run
GOTO :EOF
