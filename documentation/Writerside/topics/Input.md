# Input

`Author: Lukas Walker`

This topic talks about the different `InputAdapters` used in the game.

We currently use:

1. [](MapInputAdapter.md) for inputs on the game screen
2. [](FullscreenInputListener.md) for toggling fullscreen.

The `stage` objects used to orchestrate the UI are also processing inputs and have to be set as input processors
in the screens.

To add multiple input processors on the same screen, we use `InputMultiplexers` that send input events to the 
input processors added to them in the order they were added. If a processor returns `true`, he marks the event as
processed and it won't be handed to the next processor.