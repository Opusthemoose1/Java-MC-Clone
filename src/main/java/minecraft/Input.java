package minecraft;

import org.joml.Vector2d;
import org.joml.Vector2f;
import org.joml.Vector3f;

import static org.lwjgl.glfw.GLFW.*;

public class Input {
    private static Input instance;
    // A list of all possible keys
    private static final boolean[] keys = new boolean[GLFW_KEY_LAST + 1];
    private static Vector2d mousePos;

    public static synchronized Input getInstance() {
        if (instance == null) {
            instance = new Input();
        }
        return instance;
    }

    private Input() {}

    public static void poll() {
        glfwPollEvents();
    }

    public static void init(long window_handler)
    {

        glfwSetKeyCallback(window_handler, (w, key, scancode, action, mods) -> {
            if (key >= 0) {
                Input.keys[key] = action != GLFW_RELEASE;
            }
        });
        mousePos = new Vector2d(0, 0);
        glfwSetCursorPosCallback(window_handler, (w, xpos, ypos) -> {
            Input.mousePos.x = xpos;
            Input.mousePos.y = ypos;
        });

        glfwSetInputMode(window_handler, GLFW_CURSOR, GLFW_CURSOR_DISABLED);
    }

    public static boolean isKeyDown(int key) {
        return keys[key];
    }
    public static Vector2d getMousePos()
    {
        return Input.mousePos;
    }
}
