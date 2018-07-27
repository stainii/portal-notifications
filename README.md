# portal-notifications
A module for my personal portal, which **listens to events from other modules, and turns them into notifications**.

These notifications can be delivered to the user by so called *notification-plugins*. 

With the front-end, it's possible to *subscribe to events*, and to *define how to transform these into notifications*.

## Config in the Spring cloud config server
This module needs to fetch config from the Spring cloud config server.

| setting key | description |
| ----------- | ----------------------------------------------- |
| url         | the base url where this application is deployed |

## Development server

Run `npm run start` for a dev server. Navigate to `http://localhost:4200/`. The app will automatically reload if you change any of the source files.

## Code scaffolding

Run `ng generate component component-name` to generate a new component. You can also use `ng generate directive|pipe|service|class|guard|interface|enum|module`.

## Build

Run `ng build` to build the project. The build artifacts will be stored in the `src/main/public/` directory. Use the `-prod` flag for a production build.

## Running unit tests

Run `ng test` to execute the unit tests via [Karma](https://karma-runner.github.io).

## Further help

To get more help on the Angular CLI use `ng help` or go check out the [Angular CLI README](https://github.com/angular/angular-cli/blob/master/README.md).
