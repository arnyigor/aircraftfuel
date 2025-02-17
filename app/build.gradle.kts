plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("plugin.serialization") version "2.1.10"
    kotlin("kapt")
}

android {
    namespace = "com.arny.aircraftrefueling"
    compileSdk = 35
    val vMajor = 1
    val vMinor = 2
    val vBuild = 0
    defaultConfig {
        applicationId = "com.arny.aircraftrefueling"
        minSdk = 21
        targetSdk = 35
        versionCode = vMajor * 100 + vMinor * 10 + vBuild
        val name = "$vMajor" + ".${vMinor}" + ".${vBuild}"
        versionName = "v$name($versionCode)"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    // Configure APK file name
    applicationVariants.all {
        val variant = this
        variant.outputs
            .map { it as com.android.build.gradle.internal.api.BaseVariantOutputImpl }
            .forEach { output ->
                val outputFileName = "Android-${variant.baseName}-${variant.versionName}-${variant.versionCode}" +
                        ".apk"
                output.outputFileName = outputFileName
            }
    }
    buildFeatures {
        viewBinding = true
        buildConfig = true
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {

    implementation(libs.kotlin.stdlib)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material){}
    implementation(libs.glide){
        isTransitive = true
    }
    implementation(libs.glide.okhttp3.integration) {
        exclude(group = "glide-parent")
    }
    implementation(libs.androidx.recyclerview)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.preference.ktx)
    implementation(libs.toasty)
    implementation(libs.androidx.drawerlayout)
    implementation(libs.navigation.fragment.ktx)
    implementation(libs.navigation.ui.ktx)
    implementation(libs.joda.time)
    implementation(libs.dagger)
    implementation(libs.dagger.android)
    implementation(libs.dagger.android.support)
    kapt(libs.dagger.android.processor)
    kapt(libs.dagger.compiler)
    implementation(libs.androidx.legacy.support.v4)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.fragment.ktx)
    implementation(libs.jakewharton.timber)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.material.dialogs.core)
    implementation(libs.material.dialogs.input)
    implementation(libs.material.dialogs.files)
    implementation(libs.material.dialogs.color)
    implementation(libs.material.dialogs.datetime)
    implementation(libs.material.dialogs.bottomsheets)
    implementation(libs.material.dialogs.lifecycle)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}