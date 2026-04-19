package minecraft.window.input;

import minecraft.window.WindowResizeObserver;
import org.joml.Vector2d;

public interface IInputSource {

    void poll();

    boolean isKeyDown(int key);

    double getMouseX();

    double getMouseY();

    void attach(WindowResizeObserver observer);

    void detach(WindowResizeObserver observer);

}
