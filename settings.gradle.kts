pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}

rootProject.name = "ksvg"

buildscript {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
        maven( url = "https://mirrors.tencent.com/repository/maven/tencent_public" )
    }

    dependencies {
        classpath("com.android.tools.build:gradle:7.1.3")
    }
}
