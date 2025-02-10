plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    kotlin("plugin.serialization") version "2.0.21"
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

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material){}
    implementation(libs.glide){
        isTransitive = true
    }
    kapt(libs.glide.compiler)
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
    implementation(libs.dagger)
    implementation(libs.dagger.android)
    implementation(libs.dagger.android.support)
    implementation(libs.androidx.legacy.support.v4)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.fragment.ktx)
    kapt(libs.dagger.compiler)
    kapt(libs.dagger.android.processor)
    implementation(libs.jakewharton.timber)
    implementation(libs.kotlinx.serialization.json)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}