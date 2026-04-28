package minecraft.input;

import minecraft.window.WindowResizeObserver;
import minecraft.window.input.IInputSource;
import org.joml.Vector2d;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;


public class TestInputSource implements IInputSource {

    private final Set<Integer> downKeys = new HashSet<>();
    private final Vector2d mousePosition = new Vector2d();

    private boolean leftClick = false, rigthClick = false;

    public TestInputSource(Integer... keysDown) {
        downKeys.addAll(Arrays.asList(keysDown));
    }

    @Override
    public void poll() {

    }

    @Override
    public boolean isKeyDown(int key) {
        return downKeys.contains(key);
    }

    public void setMousePosition(double x, double y) {
        mousePosition.x = x;
        mousePosition.y = y;
    }

    public double getMouseX() {
        return 0;
    }

    public double getMouseY() {
        return 0;
    }

    @Override
    public void attach(WindowResizeObserver observer) {

    }

    @Override
    public void detach(WindowResizeObserver observer) {

    }
}
