plugins {
    java
    maven
    id("net.minecrell.plugin-yml.bukkit") version "0.4.0"
    id("com.github.johnrengelman.shadow") version "4.0.4"
    `maven-publish`
    `java-library`

}

group = "net.trainingsoase"
version = "1.0.0-SNAPSHOT"

repositories {
    mavenCentral()
    mavenLocal()
    maven {
        url = uri("https://papermc.io/repo/repository/maven-public/")
    }
    maven {
        url = uri("https://gitlab.madfix.de/api/v4/groups/64/-/packages/maven")
        name = "GitLab"

        if (System.getenv("CI_JOB_TOKEN") == null) {
            credentials(HttpHeaderCredentials::class) {
                name = "Private-Token"
                value = property("gitlabToken") as String
            }
        } else {
            credentials(HttpHeaderCredentials::class) {
                name = "Job-Token"
                value = System.getenv("CI_JOB_TOKEN")
            }
        }

        authentication {
            create<HttpHeaderAuthentication>("header")
        }
    }

}

dependencies {
    implementation("net.trainingsoase:Hopjes:1.0.0-SNAPSHOT") {
        exclude("net.trainingsoase","oase-data-impl")
    }
    implementation("org.github.paperspigot:paperspigot-api:1.8.8-R0.1-SNAPSHOT")
    implementation("net.trainingsoase:OaseAPI-Spigot:0.0.0-SNAPSHOT") {

    }

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = project.group.toString()
            artifactId = "bedwars" // Only lowercase!!!!!
            version = project.version.toString()

            from(components["java"])
        }
    }
    repositories {
        maven {
            url = uri("https://gitlab.madfix.de/api/v4/projects/176/packages/maven")
            name = "GitLab"
            credentials(HttpHeaderCredentials::class) {
                name = "Job-Token"
                value = System.getenv("CI_JOB_TOKEN")
            }
            authentication {
                create<HttpHeaderAuthentication>("header")
            }
        }
    }
}


bukkit {
    name = "Bedwars"
    main = "net.trainingsoase.bedwars.Bedwars"
    author = "TrainingsOase"

}
java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}
tasks {
    named<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar>("shadowJar") {
        // archiveBaseName.set("shadow")
        mergeServiceFiles()
    }
    build {
        dependsOn(shadowJar)
    }
}
