import com.diffplug.gradle.spotless.SpotlessExtension
import com.diffplug.spotless.LineEnding.UNIX
import org.gradle.api.JavaVersion.VERSION_17
import org.gradle.api.attributes.TestSuiteType.UNIT_TEST
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `jvm-test-suite`
    id("com.diffplug.spotless") version "6.23.3"
    kotlin("jvm") version "1.9.21"
}

group = "com.michalkucera.hexagonal"
version = "1.0.0"
val javaVersion = VERSION_17

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation(kotlin("reflect"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.1")

    implementation(platform("org.jmolecules:jmolecules-bom:2023.1.0"))
    implementation("org.jmolecules:jmolecules-ddd")
    implementation("org.jmolecules:kmolecules-ddd")
    implementation("org.jmolecules:jmolecules-events")
    implementation("org.jmolecules:jmolecules-hexagonal-architecture")
}

testing {
    suites {
        getByName<JvmTestSuite>("test") {
            testType.set("common")
            useJUnitJupiter()
            sources {
                kotlin {
                    setSrcDirs(listOf("src/test/common/kotlin"))
                }
                resources {
                    setSrcDirs(listOf("src/test/common/resources"))
                }
            }
            dependencies {
                // We can replace direct dependency on main's runtimeClasspath with implementation(project())
                // once https://github.com/gradle/gradle/issues/25269 is resolved
                implementation(sourceSets.main.get().runtimeClasspath)
                implementation("org.junit.jupiter:junit-jupiter-api")
                implementation("org.junit.jupiter:junit-jupiter-params")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test")
                runtimeOnly("org.junit.jupiter:junit-jupiter-engine")
                runtimeOnly("org.junit.platform:junit-platform-engine")
            }
        }

        register<JvmTestSuite>("architectureTest") {
            testType.set("architecture-test")
            useJUnitJupiter()
            sources {
                kotlin {
                    setSrcDirs(listOf("src/test/architectureTest/kotlin"))
                }
                resources {
                    setSrcDirs(listOf("src/test/architectureTest/resources"))
                }
            }
            dependencies {
                // We can replace direct dependency on main's runtimeClasspath with implementation(project())
                // once https://github.com/gradle/gradle/issues/25269 is resolved
                implementation(sourceSets.main.get().runtimeClasspath)
                implementation("com.tngtech.archunit:archunit-junit5-api:1.2.1")
                implementation("com.tngtech.archunit:archunit-junit5-engine:1.2.1")

                implementation(platform("org.jmolecules:jmolecules-bom:2023.1.0"))
                implementation("org.jmolecules.integrations:jmolecules-archunit")
            }
        }

        register<JvmTestSuite>("unitTest") {
            testType.set(UNIT_TEST)
            useJUnitJupiter()
            sources {
                kotlin {
                    setSrcDirs(listOf("src/test/unitTest/kotlin"))
                }
                resources {
                    setSrcDirs(listOf("src/test/unitTest/resources"))
                }
            }
            dependencies {
                implementation(sourceSets.test.get().runtimeClasspath)
                implementation(sourceSets.test.get().output)
                implementation("com.willowtreeapps.assertk:assertk:0.28.0")
            }
        }
    }
}
tasks.named("check") {
    dependsOn(
        testing.suites.named("unitTest")
    )
}

configure<JavaPluginExtension> {
    sourceCompatibility = javaVersion
    targetCompatibility = javaVersion
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = javaVersion.toString()
    }
}

tasks.getByName<Jar>("jar") {
    enabled = false
}

configure<SpotlessExtension> {
    encoding("UTF-8")
    lineEndings = UNIX
    kotlin {
        ktlint("1.0.1")
        targetExclude("build/generated/**/*")
    }

    kotlinGradle {
        ktlint("1.0.1")
    }
}
