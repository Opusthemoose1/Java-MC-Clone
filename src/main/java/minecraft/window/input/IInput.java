package minecraft.window.input;

import minecraft.window.CameraObserver;
import org.joml.Vector2d;

public interface IInput {

    void poll();

    boolean isKeyDown(int key);

    Vector2d getMousePos();

    void attach(CameraObserver observer);

    void detach(CameraObserver observer);

}
