# Screens

Screens are the different scenes we can see in the game. This game relies on three screens:

- [](SplashScreen.md)
- [](MainMenu.md)
- [](GameScreen.md)

The different screen types are kept track of in the Screens `enum`:

`````java
public enum Screens {
    MainMenu,
    Game,
    Splash,
}
`````

See [](Screens.md#screen-manager) for details about navigation.

When the client is connected to the server, its either on the Main Menu or Game Screen. Disconnecting brings it back to
the splash screen.

## Important Methods

The most important methods we used of the LibGDX `ScreenAdapter` are:

- render(float delta): Rendering method called every frame, delta is the elapsed time since the last rendering step
- resize(int width, int height): Called when the window is resized
- show(): Called when the screen is put up for display
- dispose(): Handles manual garbage collection (Some LibGDX objects aren't destroyed automatically)

## UI + Input

The screens all extends the LibGDX class ScreenAdapter for easy method overrides.
Rendering takes place using one or two viewports and orthographic cameras, both from LibGDX.
We use Scene2D for our UI with a adjusted default skin
from the [SkinComposer Tool](https://libgdx.com/wiki/tools/skin-composer).
All used graphics are combined in one `TextureAtlas` for efficient storage and access.

The input it handled using a `InputMultiplexer` to allow the registration of multiple input processors.
See [](Input.md).

More information about each screen can be found in their subtopic or [javadoc](https://b-team-organisation.github.io/Fantasy-Chess/java-docs/client/core/com/bteam/fantasychess_client/ui/package-summary.html).

## Screen Manager

The `Screen Manager` can be used to navigate between screens by calling the `navigateTo(Screens screen)` method.
This method capsules a call to `navigateToNonRunnable(Screens screen)` to increase readability, as not doing so would
require posting a Runnable to the LibGDX Thread:

````java
public void navigateTo(Screens screen) {
    Gdx.app.postRunnable(() -> navigateToNonRunnable(screen));
}
````