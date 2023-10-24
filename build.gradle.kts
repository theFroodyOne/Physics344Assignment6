plugins{
    id("java")
    id("jacoco")
}

repositories{
    mavenCentral()
}

dependencies {
    testImplementation("junit:junit:4.13.1")
}
