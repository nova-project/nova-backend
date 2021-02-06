<h1 align="center">
    Nova Framework
</h1>
<p align="center">
    <a style="text-decoration:none" href="https://github.com/getnova/framework/releases">
        <img alt="Releases" src="https://img.shields.io/github/v/tag/getnova/framework?label=latest%20version&style=flat-square">
    </a>
    <a style="text-decoration:none" href="https://github.com/getnova/framework/actions">
        <img alt="Build" src="https://img.shields.io/github/workflow/status/getnova/framework/CI?label=CI&style=flat-square">
    </a>
    <a style="text-decoration:none" href="https://hub.docker.com/r/getnova/nova-framework">
        <img alt="DockerHub" src="https://img.shields.io/docker/pulls/getnova/nova-framework?style=flat-square">
    </a>
</p>
<p align="center">
    This is the framework of the Nova Project.
</p>

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing
purposes. See deployment for notes on how to deploy the project on a live system.

### Prerequisites

What things you need to install the software and how to install them

* A Java IDE ([IntelliJ IDEA](https://www.jetbrains.com/idea/))
* [JDK 14](https://adoptopenjdk.net/index.html) or higher
* [Git](https://git-scm.com/)

### Installing

A step by step series of examples that tell you how to get a development env running

```sh
git clone https://github.com/getnova/framework.git
```

Then you can open it with you IDE and start contributing.

### Environment

| Name                  | Default Value                 | Description                                                                                                                                                         |
|-----------------------|-------------------------------|---------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `DEBUG`               | `false`                       | This sets the min `LOG_LEVEL` to `INFO` and adds some additional debugging features.                                                                                |
| `LOG_LEVEL`           | `WARN`                        | Defines the log level: `ALL`, `TRACE`, `DEBUG`, `INFO`, `WARN`, `ERROR`, `FATAL`, `OFF`                                                                             |
| `REST_API_PAT`        | `api`                         | The web path where the rest api is accessible.                                                                                                                      |
| `WEBSOCKET_API_PATH`  | `ws`                          | The web path where the websocket api is accessible.                                                                                                                 |
| `CDN_DATA_DIR`        | `data`                        | The file system path where the files for the cdn is stored.                                                                                                         |
| `SQL_SERVER_LOCATION` | `postgresql://localhost:5432` | The uri where the database server is accessible. `<database>://<host>:<port>` (Currently only postgresql is supported; you can add you if you compile it yourself.) |
| `SQL_SERVER_DATABASE` | `nova`                        | The database where the tables should be created.                                                                                                                    |
| `SQL_SERVER_USERNAME` | `nova`                        | The database user witch should be used to accessed the database.                                                                                                    |
| `SQL_SERVER_PASSWORD` | `nova`                        | The database password witch should be used to accessed the database.                                                                                                |
| `SQL_SHOW_STATEMENTS` | `false`                       | `true`: all executed sql statements will be logged; `false`: no statements will be logged                                                                           |

## Built With

* [Gradle](https://gradle.org/) - The build tool
* [JUnit](https://junit.org/) - The test tool

## License

| Licenses                                                                                                              |
|-----------------------------------------------------------------------------------------------------------------------|
| From 08.08.2020 [AGPL v3](LICENSE)                                                                                    |
| Upto 07.08.2020 [MIT](https://github.com/getnova/framework/blob/9988969fdfdf69540b3cb54a04cd70b21457f1fc/LICENSE)  |

# Third Party Licenses

[THIRD-PARTY-NOTICES](THIRD-PARTY-NOTICES)
