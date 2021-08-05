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
        url = uri("https://repo.glaremasters.me/repository/concuncan/")
    }

    maven {
        url = uri("https://repo.cloudnetservice.eu/repository/releases/")
    }

    maven {
        url = uri("https://repo.aikar.co/content/groups/aikar/")
    }

    maven {
        url = uri("https://repo.dmulloy2.net/repository/public/")
    }

    maven {
        url = uri("https://hub.spigotmc.org/nexus/content/groups/public/")
    }

    maven {
        url = uri("https://mvnrepository.com/artifact/com.github.azbh111/craftbukkit-1.8.8")
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

    // External depedencies
    compileOnlyApi("org.github.paperspigot:paperspigot-api:1.8.8-R0.1-SNAPSHOT")
    compileOnlyApi("com.comphenix.protocol:ProtocolLib:4.7.0")
    compileOnlyApi("com.grinderwolf:slimeworldmanager-api:2.2.1")
    compileOnlyApi("com.mojang:authlib:1.5.21")
    compileOnlyApi("de.dytanic.cloudnet:cloudnet-driver:$CN_VERSION")
    compileOnlyApi("de.dytanic.cloudnet:cloudnet-wrapper-jvm:$CN_VERSION")
    compileOnlyApi("de.dytanic.cloudnet:cloudnet-bridge:$CN_VERSION")
    implementation("co.aikar:taskchain-bukkit:3.7.2")
    compileOnlyApi("com.github.azbh111:craftbukkit-1.8.8:R")

    // Internal dependencies
    compileOnlyApi("net.trainingsoase:OaseAPI-Spigot:0.0.0-20210802.152932-14")
    compileOnlyApi("net.trainingsoase:Hopjes:1.0.0-20210801.171404-4")
    compileOnlyApi("net.trainingsoase:Oreo:1.0.0-20210802.151000-6")
    compileOnlyApi("net.trainingsoase:spectator:0.0.0-SNAPSHOT")
    implementation("com.github.juliarn:npc-lib:2.6-RELEASE")

    // Testing dependencies
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
    depend = listOf("OaseAPI","Hopjes", "Oreo", "CloudNet-Bridge", "SpectatorSystem")
    commands {
        register("start")
    }

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