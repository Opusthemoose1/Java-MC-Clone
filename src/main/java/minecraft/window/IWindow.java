package minecraft.window;

import minecraft.WorldContext;
import minecraft.window.input.IInputSource;

public interface IWindow {

    long getWindowHandle();

    void loop(WorldContext context);

    int getWidth();

    int getHeight();

    void setInput(IInputSource input);

    void free();

    boolean canContinueLoop();

    Camera getCamera();

    double getDeltaTime();
}
