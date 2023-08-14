import io.github.kobylynskyi.graphql.codegen.gradle.GraphQLCodegenGradleTask
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "3.1.2"
	id("io.spring.dependency-management") version "1.1.2"
	id ("io.github.kobylynskyi.graphql.codegen") version "5.8.0"
	kotlin("jvm") version "1.8.22"
	kotlin("plugin.spring") version "1.8.22"
}

group = "nl.aswad"
version = "0.0.1-SNAPSHOT"

java {
	sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
	mavenCentral()
}

// Automatically generate GraphQL code on project build:
sourceSets {
	getByName("main").java.srcDirs("$buildDir/generated")
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-webflux")
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("org.springframework.boot:spring-boot-starter-graphql")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("io.micrometer:context-propagation")
	implementation("io.micrometer:micrometer-tracing")
	implementation("io.micrometer:micrometer-registry-otlp")
	implementation("io.micrometer:micrometer-tracing-bridge-otel")
	implementation("io.micrometer:micrometer-registry-prometheus")
	implementation("io.opentelemetry:opentelemetry-exporter-otlp")
	implementation("io.projectreactor:reactor-core-micrometer")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
	implementation("javax.validation:validation-api:2.0.1.Final")
	implementation(project(":otel-propagator"))

	runtimeOnly(project(":otel-appender"))

	testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs += "-Xjsr305=strict"
		jvmTarget = "17"
		dependsOn("graphqlCodegen")
	}
}

// Add generated sources to your project source sets:
tasks.named<JavaCompile>("compileJava") {
	dependsOn("graphqlCodegen")
}

tasks.named<GraphQLCodegenGradleTask>("graphqlCodegen") {
	// all config options:
	// https://github.com/kobylynskyi/graphql-java-codegen/blob/main/docs/codegen-options.md
	graphqlSchemaPaths = listOf("$projectDir/src/main/resources/graphql/schema.graphqls")
	outputDir = File("$buildDir/generated")
	packageName = "nl.aswad.observability.dto"
	customTypesMapping = mutableMapOf(Pair("EpochMillis", "java.time.LocalDateTime"))
}

tasks.withType<Test> {
	useJUnitPlatform()
}
