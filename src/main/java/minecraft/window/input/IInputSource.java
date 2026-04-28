package minecraft.window.input;

import minecraft.window.WindowResizeObserver;
import org.joml.Vector2d;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_LAST;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_RIGHT;

public interface IInputSource {

    int LEFT_CLICK_KEY = GLFW_KEY_LAST + 1;
    int RIGHT_CLICK_KEY = GLFW_KEY_RIGHT + 1;

    void poll();

    boolean isKeyDown(int key);

    double getMouseX();

    double getMouseY();

    void attach(WindowResizeObserver observer);

    void detach(WindowResizeObserver observer);

}
