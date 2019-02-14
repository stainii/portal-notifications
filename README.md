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
| JAVA_OPTS_PORTAL_NOTIFICATIONS | -Xmx400m -Xms400m | Java opts you want to pass to the JVM | optional
