plugins {
    id("com.android.application")
    id("com.google.gms.google-services")

}

android {
    namespace = "com.task.mywallpaper"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.task.mywallpaper"
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8

        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {

    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    //fire base dependencies
    implementation(platform("com.google.firebase:firebase-bom:32.5.0"))
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-auth:22.2.0")

    //country code picker
    implementation("com.hbb20:ccp:2.4.7")
    implementation("io.michaelrocks:libphonenumber-android:8.13.25")

    //loader dependency
    implementation("com.tuyenmonkey:mkloader:1.4.0")

    implementation ("androidx.browser:browser:1.3.0")

    //design and recycleview

    implementation ("com.github.bumptech.glide:glide:4.16.0")
    //implementation ("com.github.bumptech.glide:compiler:4.16.0")
    implementation ("de.hdodenhof:circleimageview:3.1.0")

    //volley
    implementation ("com.android.volley:volley:1.2.1")

    //download wallpaper

    implementation ("com.artjimlop:altex-image-downloader:0.0.4")

    // https://mvnrepository.com/artifact/com.github.chrisbanes/PhotoView
    implementation ("com.github.chrisbanes:PhotoView:2.3.0")

    implementation ("br.com.simplepass:loading-button-android:1.12.1")








}