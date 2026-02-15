package minecraft.window.input;

import org.joml.Vector2d;

public interface IInput {

    public void poll();

    public boolean isKeyDown(int key);

    public Vector2d getMousePos();

}
