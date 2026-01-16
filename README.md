[![Maven Central](https://img.shields.io/maven-central/v/io.github.creativedrewy/mozartwallpapers)](https://central.sonatype.com/artifact/io.github.creativedrewy/mozartwallpapers)

## Mozart

Compose beautiful Android Live Wallpapers

## Overview

Mozart is a library that allows you to create Android Live Wallpapers using Jetpack Compose. What sorts of wallpapers can you create? Well, anything you can dream up with Jetpack Compose:

![Screen Recording](assets/screenrecord.gif)

## Installation

```kotlin
dependencies {
    implementation("io.github.creativedrewy:mozartwallpapers:<version>")
}
```

For version catalog:

```toml
[libraries]
mozart = { module = "io.github.creativedrewy:mozartwallpapers", version = "mozart" }
```

## Usage

Create your Wallpaper Service class and implement the required method:

```kotlin
class MyComposeWallpaper: MozartWallpaperService() {

    override val wallpaperContents: @Composable ((OffsetValues) -> Unit)
        get() = { offsets ->
            Box(
                modifier = Modifier
            ) {
                // Your Jetpack composables go here!
            }
        }
}
```

Create `res/xml/wallpaper.xml`:

```xml
<?xml version="1.0" encoding="utf-8"?>
<wallpaper
    xmlns:android="http://schemas.android.com/apk/res/android"
    android:thumbnail="@drawable/ic_launcher_background"
    android:description="@string/app_name" />
```

And register the wallpaper in your manifest:

```xml
 <service
        android:name="com.myapp.package.MyComposeWallpaper"
        android:enabled="true"
        android:label="My Wallpaper"
        android:exported="true"
        android:permission="android.permission.BIND_WALLPAPER" >

        <intent-filter>
            <action android:name="android.service.wallpaper.WallpaperService" >
            </action>
        </intent-filter>

        <meta-data
            android:name="android.service.wallpaper"
            android:resource="@xml/wallpaper" >
        </meta-data>
    </service>
```

You can apply your wallpaper by firing the relevant Intent:

```kotlin
val myPaperIntent = Intent(WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER)
myPaperIntent.putExtra(
    WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT,
    ComponentName(this, MyComposeWallpaper::class.java)
)
```

## License

```
Copyright 2026 Andrew Watson

Licensed under the Apache License, Version 2.0
```
