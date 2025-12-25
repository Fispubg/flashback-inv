# Flashback Inventory Addon (Fabric 1.21.8)
Companion Fabric mod for Flashback that records open container contents (chests, shulkers, barrels, ender chest, hoppers, droppers, player inventory screens and other ScreenHandler-backed containers)
into Flashback snapshots so POV replay shows the correct items in open menus.

## What you get in this ZIP
- Fabric mod project source (client-only)
- Mixin that injects into Flashback's snapshot hook and writes an NBT payload with container slot data
- Replay payload handler that decodes NBT and applies inventory/ender chest to the replay player (helper)

## Important
- This ZIP **does not contain a compiled .jar**. The build environment here cannot guarantee a Java toolchain or Gradle execution for compiling a Fabric mod.
- To produce the final jar you must run `gradlew build` locally (Java 17 required).
- Place your Flashback jar into `libs/` and, if desired, add it to `build.gradle` as a compileOnly dependency to resolve types at compile time.
- If Flashback changes the hook method/class name between versions, update `MixinSnapshotHook`'s @Mixin target accordingly (TODO comments included).

## Build steps
1. Install Java 17 and Gradle (or use the provided Gradle wrapper if you add it).
2. Place Flashback jar into `libs/` and update `build.gradle` if you want compile-time referencing.
3. Run `gradlew build` or `gradle build`.
4. The mod jar will be available at `build/libs/flashback-inventory-addon-0.1.0.jar`.

## If you want me to produce a compiled jar
- Provide the exact Flashback jar filename (the jar you use) and I will update mixin targets in the source to match that Flashback version.
- I still cannot compile a .jar in this environment reliably; I can provide a GitHub Actions workflow file you can run to produce consistent builds in CI (or I can produce the workflow and instructions).
