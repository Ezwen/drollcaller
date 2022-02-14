# drollcaller

Drollcaller is a simple monitoring tool for online services.

- Periodically checks that a selected set of services are online.
- Compatible with various protocols (HTTP, SSH, Matrix, etc.).
- Sends notifications when a service is down (or when a service is back up) using several notification systems (Email, FreeSMS, etc.).
- Can be silenced daily on a chosen time period (eg. when daily maintenance is scheduled).


## Installation

No public builds are available at the current time.
Please refer to the _Development_ section to create your own builds.

## Usage

### Command-line

The command-line interface is minimalist:
```
Usage: java -jar drollcaller.jar [-hV] <configurationFilePath>
Monitor periodically a set of services.
      <configurationFilePath>
                  The YAML monitoring configuration file.
  -h, --help      Show this help message and exit.
  -V, --version   Print version information and exit.
```

The only required argument is the `<configurationFilePath>`, which is a YAML configuration file that fully dictates the behavior of drollcaller.
A sample configuration file is available [here](./examples/example_monitoring_configuration.yml)

### Docker

If using drollcaller as a Docker image, the configuration simply has to be mounted as a volume at the path `/monitoring_configuration.yml` in the Docker container.


Therefore drollcaller can be started with the following command (if using the image built using the instructions given in _Development_):
```
docker run -ti -v ./examples/example_monitoring_configuration.yml:/monitoring_configuration.yml drollcaller_dev
```

The same can be achieved using Docker Compose.
A sample compose file is provided [here](./examples/docker-compose.yml)

## Development

Drollcaller is developped in Kotlin, can be compiled with Maven, and can be packaged as a Docker image.

### Maven

Local maven build:

```
mvn package
```

This creates a file `target/drollcaller-1.0-SNAPSHOT-jar-with-dependencies.jar` which is a complete standalone executable of drollcaller.

### Docker image

Local Docker image build:

```
docker build -t drollcaller_dev .
```

This creates a local Docker image named `drollcaller_dev`.