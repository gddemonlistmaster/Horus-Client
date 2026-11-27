# Horus Client

A client-side Fabric visual dashboard and customizable HUD for Minecraft Java Edition **1.21.11**, with larger touch controls for Android launchers such as Mojo.

## Mobile-friendly controls

- Open the pause menu and tap the large **Horus Visuals** button; a keyboard is not required.
- Tap the large **ON/OFF** controls to toggle the HUD, accent theme, and profile indicator.
- The **Right Shift** key binding remains available when a keyboard is connected.
- The mod is client-only and does not change server gameplay.

## Build

Build the mod on a computer with **JDK 21** and Gradle 8.14 or newer:

```sh
gradle build
```

The mod jar is created in `build/libs/`.

## Install in Mojo Launcher

1. In Mojo, create or select a **Minecraft Java 1.21.11** instance that has **Fabric Loader** installed. The launcher must support this Minecraft version and Fabric; available options can differ by Mojo release.
2. Install a Fabric API version marked for Minecraft 1.21.11 in the same instance.
3. Copy the built Horus `.jar` and Fabric API `.jar` into that instance's `mods` folder. Use Mojo's instance-management option to open the game directory if available.
4. Launch the Fabric instance, enter a world, open the pause menu, and tap **Horus Visuals**.

If Mojo does not offer Fabric for 1.21.11, the mod cannot run in that instance until the launcher supports that loader/version combination. Android performance and compatibility depend on the device, Java runtime, and Mojo version; mobile support is not guaranteed by the mod alone. Check Mojo's crash log if the game fails to launch.
