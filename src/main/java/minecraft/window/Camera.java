package minecraft.window;

import minecraft.chunk.location.Location;
import minecraft.chunk.location.YawPitchObserver;
import minecraft.chunk.location.YawPitchPublisher;
import minecraft.math.IVector;
import minecraft.math.Vector;
import org.joml.Matrix4f;
import org.joml.Vector2d;
import org.joml.Vector3f;

import java.util.HashSet;
import java.util.Set;

import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static org.lwjgl.opengl.GL11.glViewport;

public class Camera implements CameraObserver, YawPitchPublisher {


    public static final float MAX_PITCH = 89f;

    private final Matrix4f perspective;
    private final Matrix4f ortho;
    private final Matrix4f view;
    private Location location;
    private IVector front;
    private final IVector worldUp;
    private IVector up;

    private float fov;
    private float screenWidth, screenHeight;

    private final Set<YawPitchObserver> observers = new HashSet<>();

    private double lastX, lastY;

    public Camera(Location location, int screenWidth, int screenHeight)  {
        this.perspective = new Matrix4f();
        this.ortho = new Matrix4f();
        this.view = new Matrix4f();

        this.location = location.clone();
        this.front = new Vector(0.0f, 0.0f, 1.0f);
        this.worldUp = new Vector(0.0f, 1.0f, 0.0f);

        this.fov = 90.0f;

        this.lastX = screenWidth / 2.0;
        this.lastY = screenHeight / 2.0;

        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;

        updateCameraVectors();
        updateProjectionMatrix();
    }

    public float getScreenWidth() {return screenWidth; }
    public float getScreenHeight() {return screenHeight; }
    public void updateProjectionMatrix() {
        this.perspective.identity().perspective(
                (float) Math.toRadians(fov),
                screenWidth / screenHeight,
                0.1f,
                100.0f
        );
        this.ortho.identity().ortho(0, screenWidth, screenHeight, 0, -1, 1);
    }

    public Matrix4f getViewMatrix() {
        IVector positionPlusFront = location.toVector().add(front);
        return view.identity().lookAt(
                new Vector3f(location.getX(), location.getY(), location.getZ()),
                new Vector3f(positionPlusFront.getX(), positionPlusFront.getY(), positionPlusFront.getZ()),
                new Vector3f(up.getX(), up.getY(), up.getZ())
        );
    }

    public IVector getRightDirection() {
        return this.front.clone().cross(this.up).normalize();
    }

    public IVector getFront() {return front; }

    public void mouseControl(Vector2d mousePos) {
        double xoffset = mousePos.x - lastX;
        double yoffset = lastY - mousePos.y;
        lastX = mousePos.x;
        lastY = mousePos.y;

        float sensitivity = 0.1f;
        xoffset *= sensitivity;
        yoffset *= sensitivity;

//        yaw   += (float) xoffset;
//        pitch += (float) yoffset;
//
//        pitch = Math.clamp(pitch, -MAX_PITCH, MAX_PITCH);
        location.setYaw(location.getYaw() + (float) xoffset);
        location.setPitch(Math.clamp(location.getPitch() + (float) yoffset, -MAX_PITCH, MAX_PITCH));

        updateCameraVectors();
        notifyObservers();
    }

    private void notifyObservers() {
        for (YawPitchObserver observer : observers) {
            observer.updateYawAndPitch(location.getYaw(), location.getPitch());
        }
    }

    public void attach(YawPitchObserver observer) {
        observers.add(observer);
    }

    public void detach(YawPitchObserver observer) {
        observers.remove(observer);
    }

    public void updateCameraVectors() {
        front = location.getDirection();

        // right = front × worldUp
        IVector right = location.getRightDirection();

        // up = right × front
        up = right.clone().cross(front).normalize();
    }

    public Matrix4f getProjectionMatrix() {
        return new Matrix4f(this.perspective);
    }

    public Matrix4f getOrtho() {
        return new Matrix4f(this.ortho);
    }

    @Override
    public void onFramebufferResize(int width, int height) {
        glViewport(0, 0, width, height);
        screenWidth = width;
        screenHeight = height;
        updateProjectionMatrix();

    }

    public void setLocation(Location location) {
        this.location = location.clone();
    }
}
