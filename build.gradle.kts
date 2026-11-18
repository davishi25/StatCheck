import org.apache.commons.lang3.SystemUtils
import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import net.fabricmc.loom.task.RemapJarTask

plugins {
    idea
    java
    id("gg.essential.loom") version "0.10.0.+"
    id("dev.architectury.architectury-pack200") version "0.1.3"
    id("com.github.johnrengelman.shadow") version "8.1.1"
    kotlin("jvm") version "2.0.0"
}

//Constants:

val baseGroup: String by project
val mcVersion: String by project
val version: String by project
val modid: String by project
val transformerFile = file("src/main/resources/accesstransformer.cfg")

// Toolchains:
java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(8))
}

// Minecraft configuration:
loom {
    log4jConfigs.from(file("log4j2.xml"))
    runConfigs {
        "client" {
            if (SystemUtils.IS_OS_MAC_OSX) {
                // This argument causes a crash on macOS
                vmArgs.remove("-XstartOnFirstThread")
            }
        }
        remove(getByName("server"))
    }
    forge {
        pack200Provider.set(dev.architectury.pack200.java.Pack200Adapter())
    }
}

sourceSets.main {
    output.setResourcesDir(sourceSets.main.flatMap { it.java.classesDirectory })
    java.srcDir(layout.projectDirectory.dir("src/main/kotlin"))
    kotlin.destinationDirectory.set(java.destinationDirectory)
}

// Dependencies:

repositories {
    mavenCentral()
    maven("https://repo.spongepowered.org/maven/")
}

// Dependencies in this configuration are:
//  - available when compiling Kotlin/Java sources (via compileOnly),
//  - available to the development runtime (via runtimeOnly), and
//  - bundled only in the regular, non-dependent output JAR.
val bundledDependencies by configurations.creating {
    isCanBeConsumed = false
    isCanBeResolved = true
}

configurations.compileOnly.get().extendsFrom(bundledDependencies)
configurations.runtimeOnly.get().extendsFrom(bundledDependencies)

dependencies {
    minecraft("com.mojang:minecraft:1.8.9")
    mappings("de.oceanlabs.mcp:mcp_stable:22-1.8.9")
    forge("net.minecraftforge:forge:1.8.9-11.15.1.2318-1.8.9")

    bundledDependencies(kotlin("stdlib-jdk8"))
    bundledDependencies("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.9.0")
}

// Tasks:
tasks.compileJava {
    dependsOn(tasks.processResources)
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
}

tasks.withType<Jar>().configureEach {
    archiveBaseName.set(modid)
    archiveVersion.set(version)
    manifest.attributes.run {
        this["FMLCorePluginContainsFMLMod"] = "true"
        this["ForceLoadAsMod"] = "true"
    }
}

tasks.processResources {
    inputs.property("version", project.version)
    inputs.property("mcversion", mcVersion)
    inputs.property("modid", modid)
    inputs.property("basePackage", baseGroup)

    filesMatching(listOf("mcmod.info", "mixins.$modid.json")) {
        expand(inputs.properties)
    }

    rename("accesstransformer.cfg", "META-INF/${modid}_at.cfg")
}

val remapJar by tasks.named<RemapJarTask>("remapJar") {
    archiveBaseName.set(modid)
    archiveClassifier.set("dependent")
    destinationDirectory.set(layout.buildDirectory.dir("libs"))
    input.set(tasks.jar.get().archiveFile)
}

tasks.jar {
    archiveClassifier.set("dev")
    destinationDirectory.set(layout.buildDirectory.dir("intermediates"))
}

val bundledJar by tasks.registering(ShadowJar::class) {
    dependsOn(remapJar)

    archiveBaseName.set(modid)
    archiveVersion.set(version)
    archiveClassifier.set("")
    destinationDirectory.set(layout.buildDirectory.dir("libs"))

    from({
        zipTree(remapJar.archiveFile.get().asFile).matching {
            exclude("META-INF/MANIFEST.MF")
        }
    })
    configurations = listOf(bundledDependencies)

    minimize()
}

tasks.assemble {
    dependsOn(remapJar, bundledJar)
}
