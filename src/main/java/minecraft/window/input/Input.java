package minecraft.window.input;

import minecraft.window.CameraObserver;
import org.joml.Vector2d;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.GLFW.*;

public class Input implements IInput {

    // A list of all possible keys
    private final boolean[] keys = new boolean[GLFW_KEY_LAST + 1];
    private final Vector2d mousePos;

    List<CameraObserver> resizeObservers = new ArrayList<>();

    public Input(long windowHandle) {
        glfwSetKeyCallback(windowHandle, (w, key, scancode, action, mods) -> {
            if (key >= 0) {
                keys[key] = action != GLFW_RELEASE;
            }
            if ( key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE )
                glfwSetWindowShouldClose(windowHandle, true); // We will detect this in the rendering loop

        });
        mousePos = new Vector2d(0, 0);
        glfwSetCursorPosCallback(windowHandle, (w, xpos, ypos) -> {
            mousePos.x = xpos;
            mousePos.y = ypos;
        });

        glfwSetFramebufferSizeCallback(windowHandle, (w, width, height) -> {
            for (CameraObserver observer : resizeObservers) {
                observer.onFramebufferResize(width, height);
            }
        });

        glfwSetInputMode(windowHandle, GLFW_CURSOR, GLFW_CURSOR_DISABLED);
    }

    public void poll() {
        glfwPollEvents();
    }

    public boolean isKeyDown(int key) {
        return keys[key];
    }

    public Vector2d getMousePos() {
        return new Vector2d(mousePos);
    }

    @Override
    public void attach(CameraObserver observer) {
        resizeObservers.add(observer);
    }

    @Override
    public void detach(CameraObserver observer) {
        if (!resizeObservers.contains(observer)) return;
        resizeObservers.remove(observer);

    }
}
