# portal-notifications
[![Build Status](https://server.stijnhooft.be/jenkins/buildStatus/icon?job=portal-notifications/master)](https://server.stijnhooft.be/jenkins/job/portal-notifications/job/master/)

A module for my personal portal, which **listens to events from other modules, and turns them into notifications**.

These notifications are published to **notification topic**. Listeners to this topic, like portal-email, can turn these messages into emails. 

With the front-end, it's possible to *subscribe to events*, and to *define how to transform these into notifications*.

## Config in the Spring cloud config server
This module needs to fetch config from the Spring cloud config server.

| setting key | description |
| ----------- | ----------------------------------------------- |
| url         | the base url where this application is deployed |

## Environment variables
| Name | Example value | Description | Required? |
| ---- | ------------- | ----------- | -------- |
| POSTGRES_PASSWORD | secret | Password to log in to the database | required
| JAVA_OPTS_NOTIFICATIONS | -Xmx400m -Xms400m | Java opts you want to pass to the JVM | optional

### Release
#### How to release
To release a module, this project makes use of the JGitflow plugin and the Dockerfile-maven-plugin.

1. Make sure all changes have been committed and pushed to Github.
1. Switch to the dev branch.
1. Make sure that the dev branch has at least all commits that were made to the master branch
1. Make sure that your Maven has been set up correctly (see below)
1. Run `mvn jgitflow:release-start -Pproduction`.
1. Run `mvn jgitflow:release-finish -Pproduction`.
1. In Github, mark the release as latest release.
1. Congratulations, you have released both a Maven and a Docker build!

More information about the JGitflow plugin can be found [here](https://gist.github.com/lemiorhan/97b4f827c08aed58a9d8).

##### Maven configuration
At the moment, releases are made on a local machine. No Jenkins job has been made (yet).
Therefore, make sure you have the following config in your Maven `settings.xml`;

````$xml
<servers>
    <server>
        <id>docker.io</id>
        <username>your_username</username>
        <password>*************</password>
    </server>
    <server>
        <id>portal-nexus-releases</id>
        <username>your_username</username>
        <password>*************</password>
    </server>
</servers>
````
* docker.io points to the Docker Hub.
* portal-nexus-releases points to my personal Nexus (see `<distributionManagement>` in the project's `pom.xml`)
