import org.jetbrains.kotlin.gradle.dsl.JvmTarget
        plugins {
            id("com.android.application")
            id("org.jetbrains.kotlin.android")
            id("com.google.devtools.ksp")
            id("androidx.room3")
        }
android {
    namespace = "id.antasari.p05.wargahub"
    compileSdk = 36
    defaultConfig {
        applicationId = "id.antasari.p05.wargahub"
        minSdk = 26
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    buildFeatures { viewBinding = true }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}
        kotlin { compilerOptions { jvmTarget.set(JvmTarget.JVM_17) } }
        room3 { schemaDirectory("$projectDir/schemas") }
        dependencies {
            implementation("androidx.core:core-ktx:1.17.0")
            implementation("androidx.appcompat:appcompat:1.7.1")
            implementation("androidx.activity:activity-ktx:1.11.0")
            implementation("androidx.recyclerview:recyclerview:1.4.0")
            implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.9.4")
            implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.9.4")
            implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.10.2")
            implementation("androidx.room3:room3-runtime:3.0.3")
            implementation("androidx.sqlite:sqlite-framework:2.7.1")
            ksp("androidx.room3:room3-compiler:3.0.3")
            testImplementation("junit:junit:4.13.2")
            androidTestImplementation("androidx.test.ext:junit:1.2.1")
            androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
        }