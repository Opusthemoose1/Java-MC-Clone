package minecraft;

import static org.lwjgl.glfw.GLFW.*;

public class Input {
    private static Input instance;
    // A list of all possible keys
    private static final boolean[] keys = new boolean[GLFW_KEY_LAST + 1];

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
                getInstance().keys[key] = action != GLFW_RELEASE;
            }
        });
    }

    public static boolean isKeyDown(int key) {
        return keys[key];
    }
}
