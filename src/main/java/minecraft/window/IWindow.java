package minecraft.window;

import minecraft.WorldContext;
import minecraft.chunk.IChunkLoader;
import minecraft.window.input.IInput;

public interface IWindow {

    long getWindowHandle();

    void loop(WorldContext context);

    int getWidth();

    int getHeight();

    void setInput(IInput input);

    void free();

    boolean canContinueLoop();

    Camera getCamera();

    double getDeltaTime();
}
