# Portfolio Analyzer (SpringBoot)

## Development

### Prerequisites

- Java >= 17
- Maven >= 3.9
- Docker

### Running the application

#### Windows

Quick Start

```shell
./make.bat dev
```

The [`make.bat`](make.bat) script can be used to run various commands.

```shell
 C:\path\to\project\portfolio-analyzer-backend> ./make.bat help
Available targets:
  dev    : Run Docker Compose and Maven project
  up     : Start Docker services in the background
  down   : Stop and remove Docker containers, networks, volumes
  prune  : Remove unused Docker containers, networks, volumes, images
  build  : Clean and build the Maven project
  help   : Show this help message
```

#### OSX/WSL/Linux (Unix-based)

Quick Start

```shell
make dev
```

Mac is able to run makefiles by default. For Windows, you can install [WSL](https://docs.microsoft.com/en-us/windows/wsl/install-win10) or [Cygwin](https://www.cygwin.com/) to run makefiles. Alternatively, you can use the [`make.bat`](make.bat) script.

```shell
~/path/to/project/portfolio-analyzer-backend
❯ make help
Available targets:
  dev    : Run Docker Compose and Maven project
  up     : Start Docker services in the background
  down   : Stop and remove Docker containers, networks, volumes
  prune  : Remove unused Docker containers, networks, volumes, images
  build  : Clean and build the Maven project
  help   : Show this help message
```

## Reference Documentation

For further reference, please consider the following sections:

- [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
- [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/3.1.4/maven-plugin/reference/html/)
- [Create an OCI image](https://docs.spring.io/spring-boot/docs/3.1.4/maven-plugin/reference/html/#build-image)
- [Spring Data JPA](https://docs.spring.io/spring-boot/docs/3.1.4/reference/htmlsingle/index.html#data.sql.jpa-and-spring-data)
- [Spring Web](https://docs.spring.io/spring-boot/docs/3.1.4/reference/htmlsingle/index.html#web)

## Guides

The following guides illustrate how to use some features concretely:

- [Accessing Data with JPA](https://spring.io/guides/gs/accessing-data-jpa/)
- [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
- [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
- [Building REST services with Spring](https://spring.io/guides/tutorials/rest/)
