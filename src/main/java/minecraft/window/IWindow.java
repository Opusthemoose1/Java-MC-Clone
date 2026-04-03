package minecraft.window;

import minecraft.chunk.IChunkLoader;
import minecraft.window.input.IInput;

public interface IWindow {

    long getWindowHandle();

    void loop();

    int getWidth();

    int getHeight();

    void setInput(IInput input);

    void free();

    boolean canContinueLoop();

    Camera getCamera();

    double getDeltaTime();
}
