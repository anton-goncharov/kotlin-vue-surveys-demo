import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.util.Properties
import kotlin.collections.*

plugins {
    id("org.springframework.boot") version "2.4.3"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    id("org.liquibase.gradle") version "2.0.4"
    id("com.palantir.docker") version "0.22.1"

    kotlin("jvm") version "1.4.30"
    kotlin("plugin.spring") version "1.4.30"
    kotlin("plugin.jpa") version "1.4.30"
    kotlin("plugin.allopen") version "1.4.30" // to add JPA lazy-fetching support
}

val imageName = "surveys-backend"
group = "com.antongoncharov.demo"
version = "1.0.0-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
    maven(url = "http://repo.spring.io/libs-release")
    maven(url = "http://repo.spring.io/libs-milestone")
    maven(url = "http://repo.spring.io/libs-snapshot")
}

dependencies {
    // embedded H2 database
    runtimeOnly("com.h2database:h2:1.4.200")
//    runtimeOnly("io.r2dbc:r2dbc-h2")

    // liquibase
    liquibaseRuntime("org.liquibase:liquibase-core")
    liquibaseRuntime("org.liquibase.ext:liquibase-hibernate5:3.8")
    liquibaseRuntime("com.h2database:h2:1.4.200")
    liquibaseRuntime("ch.qos.logback:logback-core:1.2.3")
    liquibaseRuntime("ch.qos.logback:logback-classic:1.2.3")
    liquibaseRuntime("javax.persistence:javax.persistence-api:2.2")
    liquibaseRuntime("org.springframework.boot:spring-boot-starter-data-jpa")
    liquibaseRuntime("org.springframework.boot:spring-boot-starter-security")
    liquibaseRuntime("org.jetbrains.kotlin:kotlin-noarg") // to use kotlin data classes in JPA
    liquibaseRuntime(files("build/classes/kotlin/main"))
    liquibaseRuntime(files("build/resources/main"))

    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-data-rest")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.boot:spring-boot-starter-rsocket")
    implementation("org.springframework.boot:spring-boot-starter-data-r2dbc")
//    implementation("org.springframework.data:spring-data-r2dbc:1.3.1")
    implementation("io.r2dbc:r2dbc-h2")
    implementation("io.r2dbc:r2dbc-spi:0.9.0.M1")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    // reactor/webflux with coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactive")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")

    implementation("org.liquibase:liquibase-core")
    implementation("io.jsonwebtoken:jjwt:0.9.1")

    compileOnly("org.springframework.boot:spring-boot-devtools")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("io.projectreactor:reactor-test")
    testImplementation("org.springframework.security:spring-security-test")
}

docker {
    val bootJar = tasks.getByName<org.springframework.boot.gradle.tasks.bundling.BootJar>("bootJar")
    dependsOn(bootJar)
    copySpec.from(bootJar.outputs.files.singleFile).into("build/libs")
    buildArgs(mapOf("DEPENDENCY" to "dependency"))
    setDockerfile(file("Dockerfile"))
    name = "${imageName}:${project.version}"
}

allOpen {   // to add JPA lazy-fetching support
    annotation("javax.persistence.Entity")
    annotation("javax.persistence.Embeddable")
    annotation("javax.persistence.MappedSuperclass")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "11"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

// Liquibase build configuration
val props = Properties()
props.load(file("src/main/resources/liquibase.properties").inputStream())

tasks.named("diff") {
    dependsOn("assemble")
}

tasks.named("diffChangeLog") {
    dependsOn("assemble")
}

liquibase {
    activities.register("main") {
        this.arguments = mapOf(
            "changeLogFile" to props.getProperty("liquibase.changelog.main"),
            "referenceUrl" to props.getProperty("liquibase.changelog.referenceUrl"),
            "url" to props.getProperty("spring.datasource.url"),
            "username" to props.getProperty("spring.datasource.username"),
            "password" to props.getProperty("spring.datasource.password"),
            "logLevel" to "info"
        )
    }
    runList = "main"
}

