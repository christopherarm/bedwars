plugins {
    java
    id("net.minecrell.plugin-yml.bukkit") version "0.4.0"
    id("com.github.johnrengelman.shadow") version "4.0.4"
    `maven-publish`
    `java-library`
}

group = "net.trainingsoase"
version = "1.0.0-SNAPSHOT"

val CN_VERSION = "3.4.0-RELEASE";

repositories {
    mavenCentral()

    maven {
        url = uri("https://libraries.minecraft.net/")
    }

    maven {
        url = uri("https://repo.cloudnetservice.eu/repository/releases/")
    }

    maven {
        url = uri("https://repo.aikar.co/content/groups/aikar/")
    }

    maven {
        url = uri("https://hub.spigotmc.org/nexus/content/groups/public/")
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
configurations.all {
    resolutionStrategy.cacheDynamicVersionsFor(0, TimeUnit.MILLISECONDS)
}

dependencies {

    compileOnlyApi("com.mojang:authlib:1.5.21")
    compileOnlyApi("net.trainingsoase:Hopjes:1.0.+")
    compileOnlyApi("org.github.paperspigot:paperspigot-api:1.8.8-R0.1-SNAPSHOT")
    compileOnlyApi("net.trainingsoase:OaseAPI-Spigot:0.0.+")
    compileOnlyApi("net.trainingsoase:Oreo:1.0.+")
    compileOnlyApi("co.aikar:taskchain-bukkit:3.7.2")

    compileOnlyApi("de.dytanic.cloudnet:cloudnet-driver:$CN_VERSION")
    compileOnlyApi("de.dytanic.cloudnet:cloudnet-wrapper-jvm:$CN_VERSION")
    compileOnlyApi("de.dytanic.cloudnet:cloudnet-bridge:$CN_VERSION")

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
    depend = listOf("OaseAPI","Hopjes", "Oreo", "CloudNet-Bridge")

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

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}
