# portal-notifications
[![Build Status](https://server.stijnhooft.be/jenkins/buildStatus/icon?job=portal-notifications/master)](https://server.stijnhooft.be/jenkins/job/portal-notifications/job/master/)

A module for my personal portal, which **listens to events from other modules, and turns them into notifications**.

These notifications can be delivered to the user by so called *notification-plugins*. 

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
#### Maven release
To release a module, this project makes use of the JGitflow plugin.
**Do use the Maven profile `-Pproduction`**.

More information can be found [here](https://gist.github.com/lemiorhan/97b4f827c08aed58a9d8).

At the moment, releases are made on a local machine. No Jenkins job has been made (yet).

#### Docker release
A Docker release is made, by running `mvn clean deploy -Pproduction` on the Maven release branch.
