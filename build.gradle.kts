import org.springframework.boot.gradle.tasks.bundling.BootBuildImage

plugins {
    id("de.cofinpro.spring-native.common-conventions")
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.dependency.management)
    alias(libs.plugins.graalvm.buildtools)
}

group = "de.cofinpro"
version = "0.3.3-SNAPSHOT"


dependencies {
    implementation(libs.spring.boot.web)
    implementation(libs.spring.boot.jpa)
    implementation(libs.spring.boot.validation)
    implementation(libs.spring.boot.oauth2.resource)
    implementation(libs.spring.boot.actuator)
    compileOnly(libs.lombok)
    developmentOnly(libs.spring.boot.devtools)
    runtimeOnly(libs.postgresql)
    annotationProcessor(libs.spring.boot.configuration.processor)
    annotationProcessor(libs.lombok)
    testImplementation(libs.spring.boot.test)
    testImplementation(libs.spring.security.test)
}

val dockerHubRepo = "wisskirchenj/"

tasks.named<BootBuildImage>("bootBuildImage") {
    builder.set("dashaun/builder:tiny")
    imageName.set(dockerHubRepo + rootProject.name + ":" + version)
    createdDate.set("now")
    environment.put("BP_NATIVE_IMAGE", "true")
}
