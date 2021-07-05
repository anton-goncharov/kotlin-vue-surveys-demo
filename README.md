# Surveys (Kotlin+Vue.js Fullstack Demo Application) 

## Summary

The Surveys is a demo application based on **Kotlin + Vue.js** development stack. It's packed with various frequently used features (see the details below) implemented in the basic shape. Mainly I've developed it to have a boilerplate repository for future pet projects 🏗. 

This repository will evolve as I have time to add new features and optimize existing functionality.

The idea of application is to provide a platform for conducting surveys and having simple real-time analytics for running polls. There're 2 kinds of actors: coordinators creating surveys, and respondents.

- Surveys list

- Composing a survey

- Respondent's survey view

- Reviewing real-time survey responses

## Design

It showcases the following concepts:

#### Kotlin + Spring backend API development

I've started using Kotlin on new backend projects 2 years ago, and it's my first repository to have boilerplate code for full-stack apps with Kotlin. Here I'm using Kotlin in pretty much the same Spring setup as I would use Java.      

#### Rapid CRUD bootstrapping using Spring Data REST

It's arguable whether Spring Data REST is a good fit for a production-ready project. On the other hand, it allows to start quick having full-blown API implementation with sorting, paging and filtering for given entities.

#### Database migrations with Liquibase

I used Hibernate JPA auto schema creation during the development. After I finalized the first version of the schema, I generated the Liquibase changelog from JPA entities and disabled ddl-auto by Hibernate.

To populate schema with some initial data (predefined users)

#### Reactive flow for live data streaming

After following the [official guide on Spring Webflux + RSocket](https://spring.io/guides/tutorials/spring-webflux-kotlin-rsocket/) I got an idea to implement some real-time streaming functionality to try Reactive Streams in such application setup. 

The survey stats page is implemented using rsocket.js, it reads the survey response stream data from a Kotlin Flow that is populated with data from R2dbc H2 reactive-ready driver. 

#### Building with Gradle

As a sidenote, I've never used Gradle before, always preferring Maven as a tool that makes more sense to me. This was an interesting challenge to create something new with Gradle, especially writing the build script with Kotlin DSL since there're not many ready-to-use examples.

> Some features are still in development 👷‍♂️. Don't consider the current implementation to be polished at this stage. The nearest TODOs:
> - User management CRUD table
> - User registration 
> - User profile editing
> - Filtering survey list
> - Sample integration tests for the rsocket interactions

### Backend Stack

- Kotlin 1.4
- Spring Boot 2.4.3
- Spring Data REST
- Spring Security (JWT-based authentication)
- Spring WebFlux
- Hibernate JPA
- H2 Database
- Liquibase (database migrations)
- Gradle Build 

#### Frontend Stack

- Vue.js 2
- Vuex  
- Vue-router
- Vue-image-upload
- Bootstrap 4
- RSocket
- Apexcharts

## Launch

### Backend

Running in a command line
```shell
./gradlew bootRun
```

Running in a container
```shell
# build an image of 'surveys-backend'
./gradlew docker

# run a container
docker run -p 8081:8081 -p 8082:8082 surveys-backend:1.0.0-SNAPSHOT
```

### Frontend 

Running in a command line with `yarn` 
```shell
cd client
yarn serve
```

It launches the web application at http://localhost:8080

There're 2 predefined users:
```
m.lin@surveys.com / !F85BVekCVM3uDKyty (role: coordinator)
a.mcgill@surveys.com / 12345!abc (role: respondent)
```

## License

MIT