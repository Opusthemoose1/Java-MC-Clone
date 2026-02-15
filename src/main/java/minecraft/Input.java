package minecraft;

import org.joml.Vector2d;
import org.joml.Vector2f;
import org.joml.Vector3f;

import static org.lwjgl.glfw.GLFW.*;

public class Input {

    // A list of all possible keys
    private static final boolean[] keys = new boolean[GLFW_KEY_LAST + 1];

    private Vector2d mousePos;

    public Input(long windowHandle) {
        glfwSetKeyCallback(windowHandle, (w, key, scancode, action, mods) -> {
            if (key >= 0) {
                Input.keys[key] = action != GLFW_RELEASE;
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

    public static boolean isKeyDown(int key) {
        return keys[key];
    }

    public Vector2d getMousePos() {
        return new Vector2d(mousePos);
    }
}
