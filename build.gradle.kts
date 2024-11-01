plugins {
    id("java")
}

group = "org.football"
version = "21"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.projectlombok:lombok:1.18.34")
    annotationProcessor("org.projectlombok:lombok:1.18.34")
    testImplementation("org.spockframework:spock-core:2.4-M4-groovy-4.0")
}

tasks.jar {
    manifest.attributes["Main-Class"] = "org.football.Main"
}