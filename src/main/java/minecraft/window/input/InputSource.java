package minecraft.window.input;

import minecraft.window.WindowResizeObserver;
import org.joml.Vector2d;

import javax.print.DocFlavor;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.GLFW.*;

public class InputSource implements IInputSource {

    // A list of all possible keys
    private final boolean[] keys = new boolean[GLFW_KEY_LAST + 1];
    private boolean leftClick = false, rightClick = false;
    private double mouseX = 0, mouseY = 0;

    List<WindowResizeObserver> resizeObservers = new ArrayList<>();

    public InputSource(long windowHandle) {
        glfwSetKeyCallback(windowHandle, (w, key, scancode, action, mods) -> {
            if (key >= 0) {
                keys[key] = action != GLFW_RELEASE;
            }
            if ( key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE )
                glfwSetWindowShouldClose(windowHandle, true); // We will detect this in the rendering loop

        });
        glfwSetCursorPosCallback(windowHandle, (w, xpos, ypos) -> {
            mouseX = xpos;
            mouseY = ypos;
        });

        glfwSetFramebufferSizeCallback(windowHandle, (w, width, height) -> {
            for (WindowResizeObserver observer : resizeObservers) {
                observer.onFramebufferResize(width, height);
            }
        });

        glfwSetMouseButtonCallback(windowHandle, (window, button, action, mods) -> {
            if (button == GLFW_MOUSE_BUTTON_LEFT) leftClick = action == GLFW_PRESS;
            else if (button == GLFW_MOUSE_BUTTON_RIGHT) rightClick = action == GLFW_PRESS;
        });

        glfwSetInputMode(windowHandle, GLFW_CURSOR, GLFW_CURSOR_DISABLED);
    }

    public void poll() {
        glfwPollEvents();
    }

    public boolean isKeyDown(int key) {
        if (key == IInputSource.LEFT_CLICK_KEY) return leftClick;
        if (key == IInputSource.RIGHT_CLICK_KEY) return rightClick;
        return keys[key];
    }

    public double getMouseX() {
        return mouseX;
    }

    public double getMouseY() {
        return mouseY;
    }

    @Override
    public void attach(WindowResizeObserver observer) {
        resizeObservers.add(observer);
    }

    @Override
    public void detach(WindowResizeObserver observer) {
        if (!resizeObservers.contains(observer)) return;
        resizeObservers.remove(observer);

    }
}
