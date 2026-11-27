# Horus Client

A client-side Fabric mod starter for a polished visual dashboard and a calm, minimal HUD. It targets Minecraft Java Edition 1.21.1 and is designed to work with Fabric Loader and Fabric API, including compatible Android Java launchers such as Mojo Launcher.

## Features

- Custom in-game dashboard, opened with **Right Shift** on keyboard.
- **Horus Visuals** button in the pause menu for touch-friendly access on phones and tablets.
- Aurora HUD panel with a violet/teal accent toggle.
- Optional visual-profile status indicator.
- Client-side only; it does not change server gameplay or require server installation.

## Build

Requirements: JDK 21 and Gradle (or add the Gradle wrapper).

```sh
gradle build
```

The built mod jar will be in `build/libs/`. Install Fabric Loader and Fabric API for Minecraft 1.21.1 in the launcher, then put the jar in that instance's `mods` folder. On Android, use a Mojo Launcher instance configured for the same Minecraft and Fabric versions. Performance depends on the device; this starter intentionally avoids shaders and heavyweight rendering dependencies.
