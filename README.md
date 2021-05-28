# Surveys (Kotlin+Vue.js Fullstack Demo Application) 

Surveys is a demo application based on **Kotlin + Vue.js** development stack.
I've implemented it to have a bootstrap repository for my future pet project.
In addition, it briefly showcases state-of-the-art stack for creating production-ready fullstack applications.  

## What's Inside

It showcases the following concepts:

1. Kotlin + Spring backend API development
2. Rapid CRUD bootstrapping using Spring Data REST
3. Reactive streams with Spring WebFlux on Kotlin Coroutines 
4. Live UI updates through RSocket using the reactive streams
5. JWT-based Authentication
6. Database migrations
7. One-click build & deploy 

### Backend Tech Stack

- Kotlin 1.4.x

- Spring Boot 2.4
  
- Spring Data REST

- Spring Security (JWT-based authentication)

- Spring WebFlux

- Hibernate JPA

- H2 Database

- Liquibase (database migrations)

- Gradle Build 

#### Frontend Tech Stack

- Vue.js 2 + Vuex + Vue-Router

- Bootstrap 4

- RSocket

## Running Locally

```
docker-compose up 
```

### Backend

In container
```
docker ...  
```

Locally during development
```
gradle run
 ```

### Frontend

In container
```
docker ...  
```

Locally during development
```shell
yarn serve
```

## License

MIT