package minecraft.window.input;

import org.joml.Vector2d;

import static org.lwjgl.glfw.GLFW.*;

public class Input implements IInput {

    // A list of all possible keys
    private final boolean[] keys = new boolean[GLFW_KEY_LAST + 1];

    private final Vector2d mousePos;

    public Input(long windowHandle) {
        glfwSetKeyCallback(windowHandle, (w, key, scancode, action, mods) -> {
            if (key >= 0) {
                keys[key] = action != GLFW_RELEASE;
            }
        });
        mousePos = new Vector2d(0, 0);
        glfwSetCursorPosCallback(windowHandle, (w, xpos, ypos) -> {
            mousePos.x = xpos;
            mousePos.y = ypos;
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
}
