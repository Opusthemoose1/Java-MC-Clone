package minecraft.window;

import minecraft.chunk.location.Location;
import minecraft.chunk.location.LocationObserver;
import minecraft.chunk.location.YawPitchObserver;
import minecraft.chunk.location.YawPitchPublisher;
import minecraft.entity.Player;
import minecraft.math.IVector;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.util.HashSet;
import java.util.Set;

import static org.lwjgl.opengl.GL11.glViewport;

public class Camera implements WindowResizeObserver, YawPitchPublisher, LocationObserver {

    public static final float MAX_PITCH = 89f, SENSITIVITY = 0.1F, FOV = 80f, CAMERA_OFFSET_MULTIPLIER = -0.15f;

    private final Matrix4f perspective;
    private final Matrix4f ortho;
    private final Matrix4f view;
    private Location location;
    private IVector front;
    private IVector up;

    private float screenWidth, screenHeight;

    private final Set<YawPitchObserver> observers = new HashSet<>();

    private double lastX, lastY;

    public Camera(Location location, int screenWidth, int screenHeight) {
        this.perspective = new Matrix4f();
        this.ortho = new Matrix4f();
        this.view = new Matrix4f();

        this.location = location.clone();

        this.lastX = screenWidth / 2.0; //initially, assume moues to be at the center of the screen
        this.lastY = screenHeight / 2.0;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;

        updateCameraVectors();
        updateProjectionMatrix();
    }

    public void updateProjectionMatrix() {
        this.perspective.identity().perspective(
                (float) Math.toRadians(FOV),
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

    public void mouseControl(double mouseX, double mouseY) {
        double xOffset = mouseX - lastX;
        double yOffset = lastY - mouseY;
        if (xOffset == 0 && yOffset == 0) return;
        lastX = mouseX;
        lastY = mouseY;

        xOffset *= SENSITIVITY;
        yOffset *= SENSITIVITY;

        location.setYaw(location.getYaw() + (float) xOffset);
        location.setPitch(Math.clamp(location.getPitch() + (float) yOffset, -MAX_PITCH, MAX_PITCH));

        updateCameraVectors();
        notifyObservers();
    }

    private void notifyObservers() {
        for (YawPitchObserver observer : observers) {
            observer.updateYawAndPitch(location.getYaw(), location.getPitch());
        }
    }

    @Override
    public void attachYawPitchObserver(YawPitchObserver observer) {
        observers.add(observer);
    }

    @Override
    public void detachYawPitchObserver(YawPitchObserver observer) {
        observers.remove(observer);
    }

    public void updateCameraVectors() {
        front = location.getDirection();
        up = location.getUpDirection(); // up = right × front
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

    @Override
    public void updateLocation(Location location) {
        this.location = location.clone().add(0, Player.HEIGHT, 0).add(front.clone().multiply(CAMERA_OFFSET_MULTIPLIER));
    }

    public Location getLocation() {
        return location.clone();
    }
}
