# Horus Client

A client-side Fabric visual dashboard and customizable HUD for Minecraft Java Edition **1.21.11**, with larger touch controls for Android launchers such as Mojo.

## Visual options

- Open the pause menu and tap **Horus Visuals**, or press **Right Shift**.
- Toggle the lightweight HUD, accent theme, and profile label.
- Enable a client-side custom sky tint and cycle through **Aurora**, **Sunset**, **Ocean**, and **Dream** palettes.
- The sky tint changes the sky color only; vanilla sun, moon, clouds, and weather rendering remain in place.
- These options are cosmetic and do not change server gameplay.

## Build

Build the mod on a computer with **JDK 21** and Gradle 8.14 or newer:

```sh
gradle build
```

The mod jar is created in `build/libs/`.

## Install in Mojo Launcher

1. In Mojo, create or select a **Minecraft Java 1.21.11** instance that has **Fabric Loader** installed.
2. Install a Fabric API version marked for Minecraft 1.21.11 in the same instance.
3. Copy the built Horus `.jar` and Fabric API `.jar` into that instance's `mods` folder.
4. Launch the Fabric instance, enter a world, open the pause menu, and tap **Horus Visuals**.

If Mojo does not offer Fabric for 1.21.11, the mod cannot run in that instance until the launcher supports that loader/version combination.
