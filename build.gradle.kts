val gemini_api_version: String by project
val jackson_version: String by project
val clikt_version: String by project
val slf4j_version: String by project
val logback_version: String by project
val klogging_version: String by project
val guava_version: String by project
val cucumber_version: String by project
val mockk_version: String by project

plugins {
    kotlin("jvm") version "1.9.24"
    application
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "org.aithana.platform.server"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.google.genai:google-genai:${gemini_api_version}")
    implementation("com.fasterxml.jackson.core:jackson-databind:${jackson_version}")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:${jackson_version}")
    implementation("com.github.ajalt.clikt:clikt:${clikt_version}")
    implementation("org.slf4j:slf4j-api:${slf4j_version}")
    implementation("io.github.oshai:kotlin-logging-jvm:${klogging_version}")
    implementation("com.google.guava:guava:${guava_version}")

    runtimeOnly("ch.qos.logback:logback-classic:${logback_version}")

    testImplementation(kotlin("test"))
    testImplementation("io.cucumber:cucumber-java:${cucumber_version}")
    testImplementation("io.cucumber:cucumber-junit:${cucumber_version}")
    testImplementation("io.mockk:mockk:${mockk_version}")
}

val cucumberRuntime by configurations.creating {
    extendsFrom(configurations.testImplementation.get())
}

tasks {
    test {
        useJUnitPlatform()
    }

    register<JavaExec>("acceptanceTest") {
        group = "verification"
        description = "Runs Cucumber acceptance tests"

        dependsOn("testClasses", "mainClasses")

        mainClass.set("io.cucumber.core.cli.Main")

        classpath = cucumberRuntime +
                sourceSets.main.get().output +
                sourceSets.test.get().output

        args(
            "--glue", "org.aithana.platform.server.steps",
            "--plugin", "pretty",
            "--plugin", "html:build/reports/cucumber/acceptance-test-report.html",
            "src/test/resources/features"
        )

        systemProperties(System.getProperties().mapKeys { it.key as String })

        doLast {
            val result = executionResult.get()
            if (result.exitValue != 0) {
                throw GradleException("Cucumber acceptance tests failed! Check the reports")
            }
        }
    }

    register<Copy>("installGitHooks") {
        from("$rootDir/scripts/git-hooks/") { // Assuming your hook scripts are in scripts/git-hooks
            include("pre-commit", "pre-push")
        }
        into("$rootDir/.git/hooks")
        fileMode = 0b0111101101 // rwxr-xr-x (executable permissions)
    }

    named<Task>("assemble").configure {
        dependsOn("installGitHooks")
    }
}

kotlin {
    jvmToolchain(17)
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

application {
    mainClass.set("org.aithana.platform.server.MainKt")
}