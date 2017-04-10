buildscript {

    repositories {
        gradleScriptKotlin()
    }

    dependencies {
        classpath(kotlinModule("gradle-plugin"))
    }
}

plugins {
    application
}

apply {
    plugin("kotlin")
}

application {
    mainClassName = "samples.HelloWorldKt"
}

repositories {
    gradleScriptKotlin()
}

dependencies {
    compile(kotlinModule("stdlib"))
    compile("com.fasterxml.jackson.core:jackson-databind:2.8.8")

    testCompile("junit:junit:4.12")
    testCompile("org.assertj:assertj-core:3.5.2")
}

defaultTasks = listOf("build")
