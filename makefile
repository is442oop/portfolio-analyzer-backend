.PHONY: dev up down prune build mvn

help:
	@echo "Available targets:"
	@echo "  dev    : Run Docker Compose and Maven project"
	@echo "  up     : Start Docker services in the background"
	@echo "  down   : Stop and remove Docker containers, networks, volumes"
	@echo "  prune  : Remove unused Docker containers, networks, volumes, images"
	@echo "  build  : Clean and build the Maven project"
	@echo "  mvn    : Run the Maven project"
	@echo "  help   : Show this help message"

dev: up build mvn

up:
	docker compose -f ./.docker/docker-compose.yml up -d

down:
	docker compose -f ./.docker/docker-compose.yml down -v

prune:
	docker system prune -a

build:
	mvn clean install

mvn:
	mvn spring-boot:run
