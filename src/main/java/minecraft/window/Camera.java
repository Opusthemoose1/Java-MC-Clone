package minecraft.window;

import minecraft.math.IVector;
import minecraft.math.Vector;
import org.joml.Matrix4f;
import org.joml.Vector2d;
import org.joml.Vector3f;

import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static org.lwjgl.opengl.GL11.glViewport;

public class Camera implements CameraObserver {

    public enum CameraDirection {
        FORWARD,
        BACKWARD,
        LEFT,
        RIGHT
    }

    public static final float VELOCITY = 10f, MAX_PITCH = 89f;

    private final Matrix4f perspective;
    private final Matrix4f ortho;
    private final Matrix4f view;
    private  IVector position;
    private IVector front;
    private final IVector worldUp;
    private IVector up;

    private float yaw, pitch, fov;
    private float screenWidth, screenHeight;

    private double lastX, lastY;

    public Camera()  {
        this.perspective = new Matrix4f();
        this.ortho = new Matrix4f();
        this.view = new Matrix4f();

        this.position = new Vector(0.0f, 0.0f, 0.0f);
        this.front = new Vector(0.0f, 0.0f, 1.0f);
        this.worldUp = new Vector(0.0f, 1.0f, 0.0f);

        this.yaw = 0.0f;
        this.pitch = 0.0f;
        this.fov = 90.0f;

        this.lastX = 960;
        this.lastY = 540;

        screenWidth = 720;
        screenHeight = 480;

        updateCameraVectors();
    }

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
        IVector positionPlusFront = position.clone().add(front);
        return view.identity().lookAt(
                new Vector3f(position.getX(), position.getY(), position.getZ()),
                new Vector3f(positionPlusFront.getX(), positionPlusFront.getY(), positionPlusFront.getZ()),
                new Vector3f(up.getX(), up.getY(), up.getZ())
        );
    }

    public void processInput(CameraDirection dir, float deltaTime) {
        float velocity = VELOCITY * deltaTime;
        switch(dir) {
            case FORWARD -> this.position.add(this.front.clone().multiply(velocity));
            case BACKWARD -> this.position.subtract(this.front.clone().multiply(velocity));
            case LEFT -> this.position.subtract(getRightDirection().multiply(velocity));
            case RIGHT -> this.position.add(getRightDirection().multiply(velocity));
        }
    }

    private IVector getRightDirection() {
        return this.front.clone().cross(this.up).normalize();
    }

    public void mouseControl(Vector2d mousePos) {
        double xoffset = mousePos.x - lastX;
        double yoffset = lastY - mousePos.y;
        lastX = mousePos.x;
        lastY = mousePos.y;

        float sensitivity = 0.1f;
        xoffset *= sensitivity;
        yoffset *= sensitivity;

        yaw   += (float) xoffset;
        pitch += (float) yoffset;

        pitch = Math.clamp(pitch, -MAX_PITCH, MAX_PITCH);

        IVector direction = new Vector(
                (float)(cos(Math.toRadians(yaw)) * cos(Math.toRadians(pitch))),
                (float)sin(Math.toRadians(pitch)),
                (float)(sin(Math.toRadians(yaw)) * cos(Math.toRadians(pitch)))
        );
        this.front = direction.normalize();
    }

    private void updateCameraVectors() {
        front = new Vector(
                (float)(Math.cos(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch))),
                (float)(Math.sin(Math.toRadians(pitch))),
                (float)(Math.sin(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch)))
        ).normalize();

        // right = front × worldUp
        IVector right = front.clone().cross(worldUp).normalize();

        // up = right × front
        up = right.clone().cross(front).normalize();
    }

    public Matrix4f getProjectionMatrix() {
        return new Matrix4f(this.perspective);
    }

    public Matrix4f getOrtho() {
        return new Matrix4f(this.ortho);
    }

    public IVector getPosition() {
        return position.clone();
    }

    @Override
    public void onFramebufferResize(int width, int height) {
        glViewport(0, 0, width, height);
        screenWidth = width;
        screenHeight = height;
        updateProjectionMatrix();

    }

    public static class Builder
    {
        private IVector position; // World position
        private float yaw, pitch, fov;
        private float scrWidth, scrHeight;

        public Builder position(IVector pos) {
            this.position = pos;
            return this;
        }
        public Builder yaw(float yaw)
        {
            this.yaw = yaw;
            return this;
        }
        public Builder pitch(float pitch)
        {
            this.pitch = pitch;
            return this;
        }
        public Builder fov(float fov)
        {
            this.fov = fov;
            return this;
        }
        public Builder screenWidth(float width)
        {
            this.scrWidth = width;
            return this;
        }
        public Builder screenHeight(float height)
        {
            this.scrHeight = height;
            return this;
        }
        public Camera build()
        {
            Camera camera = new Camera();
            camera.position = position;
            camera.yaw = yaw;
            camera.pitch = pitch;
            camera.fov = fov;
            camera.screenWidth = scrWidth;
            camera.screenHeight = scrHeight;

            camera.updateProjectionMatrix();

            return camera;
        }

    }
}
