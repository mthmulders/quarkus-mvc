# Quarkus MVC Extension

[![CircleCI](https://circleci.com/gh/mthmulders/quarkus-mvc.svg?style=svg)](https://circleci.com/gh/mthmulders/quarkus-mvc)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=mthmulders_quarkus-mvc&metric=alert_status)](https://sonarcloud.io/dashboard?id=mthmulders_quarkus-mvc)

This is an experiment to see if [JSR-371 (Model-View-Controller)](https://www.mvc-spec.org/) could work in Quarkus.

It comes with an example application that shows how to use this Quarkus extension.

## Status
> First of all, this is **an experiment**.
> It doesn't work at all - yet.

First hurdle to take is the fact the Reference Implementation of JSR-371, [Krazo](https://github.com/eclipse-ee4j/krazo), uses a circular dependency.
This is not yet supported in ArC, the CDI-implementation used by Quarkus.
Because they've [shown interest](https://github.com/quarkusio/quarkus/issues/2990) in having support for that, I'm working on that first.
