package minecraft.window;

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
    private  Vector3f position;
    private Vector3f front;
    private final Vector3f worldUp;
    private Vector3f up;

    private float yaw, pitch, fov;
    private float screenWidth, screenHeight;

    private Boolean firstMouse; // Detects if it's the first mouse movement
    private double lastX, lastY;

    public Camera()  {
        this.perspective = new Matrix4f();
        this.ortho = new Matrix4f();
        this.view = new Matrix4f();

        this.position = new Vector3f(0.0f, 0.0f, 0.0f);
        this.front = new Vector3f(0.0f, 0.0f, 1.0f);
        this.worldUp = new Vector3f(0.0f, 1.0f, 0.0f);

        this.yaw = 0.0f;
        this.pitch = 0.0f;
        this.fov = 90.0f;

        this.firstMouse = false;
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
        Vector3f tmp = new Vector3f();
        return view.identity().lookAt(
                position,
                tmp.set(position).add(front),
                up
        );
    }

    public void processInput(CameraDirection dir, float deltaTime) {
        float velocity = VELOCITY * deltaTime;
        switch(dir) {
            case FORWARD -> this.position.add(new Vector3f(this.front).mul(velocity));
            case BACKWARD -> this.position.sub(new Vector3f(this.front).mul(velocity));
            case LEFT -> this.position.sub(getRightDirection().mul(velocity));
            case RIGHT -> this.position.add(getRightDirection().mul(velocity));
        }
    }

    private Vector3f getRightDirection() {
        return new Vector3f(this.front).cross(this.up).normalize();
    }

    public void mouseControl(Vector2d mousePos) {
        if (this.firstMouse) {
            this.lastX = mousePos.x;
            this.lastY = mousePos.y;
            this.firstMouse = false;
        }
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

        Vector3f direction = new Vector3f();
        direction.x = (float)(cos(Math.toRadians(yaw)) * cos(Math.toRadians(pitch)));
        direction.y = (float)sin(Math.toRadians(pitch));
        direction.z = (float)(sin(Math.toRadians(yaw)) * cos(Math.toRadians(pitch)));
        this.front = direction.normalize();
    }

    private void updateCameraVectors() {
        front = new Vector3f(
                (float)(Math.cos(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch))),
                (float)(Math.sin(Math.toRadians(pitch))),
                (float)(Math.sin(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch)))
        ).normalize();

        // right = front × worldUp
        Vector3f right = new Vector3f(front).cross(worldUp).normalize();

        // up = right × front
        up = new Vector3f(right).cross(front).normalize();
    }

    public Matrix4f getProjectionMatrix() {
        return new Matrix4f(this.perspective);
    }

    public Matrix4f getOrtho() {
        return new Matrix4f(this.ortho);
    }

    public Vector3f getPosition() {
        return new Vector3f(this.position);
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
        private Vector3f position; // World position
        private float yaw, pitch, fov;
        private float scrWidth, scrHeight;

        public Builder position(Vector3f pos) {
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
