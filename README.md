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

````groovy
repositories {
  maven { url 'https://raw.githubusercontent.com/getnova/maven/main' }
}

dependencies {
  // use only the modules that you need
  implementation 'net.getnova.framework:nova-api:<version>'
  implementation 'net.getnova.framework:nova-api-rest:<version>'
  implementation 'net.getnova.framework:nova-api-ws:<version>'
  implementation 'net.getnova.framework:nova-cdn:<version>'
  implementation 'net.getnova.framework:nova-core:<version>'
  implementation 'net.getnova.framework:nova-data-jpa:<version>'
  implementation 'net.getnova.framework:nova-web:<version>'
}
````

## Contributing

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

Then you can open it with you IDE and start contributing. |

## Built With

* [Gradle](https://gradle.org/) - The build tool
* [JUnit](https://junit.org/) - The test tool

## License

| Licenses                                                                                                              |
|-----------------------------------------------------------------------------------------------------------------------|
| From 08.08.2020 [AGPL v3](LICENSE)                                                                                    |
| Upto 07.08.2020 [MIT](https://github.com/getnova/framework/blob/9988969fdfdf69540b3cb54a04cd70b21457f1fc/LICENSE)  |

## Third Party Licenses

[THIRD-PARTY-NOTICES](THIRD-PARTY-NOTICES)
