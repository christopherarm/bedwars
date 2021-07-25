plugins {
    java
    id("net.minecrell.plugin-yml.bukkit") version "0.4.0"
}

group = "net.trainingsoase"
version = "1.0.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
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
