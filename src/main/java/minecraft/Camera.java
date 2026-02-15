package minecraft;

import org.joml.Matrix4f;
import org.joml.Vector2d;
import org.joml.Vector3f;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

enum Camera_Direction {
    FORWARD,
    BACKWARD,
    LEFT,
    RIGHT
}

public class Camera {
    private final Matrix4f perspective;
    private final Matrix4f ortho;
    private final Matrix4f view;
    private final Vector3f position;
    private Vector3f front;
    private final Vector3f worldUp;
    private Vector3f up;

    private float yaw;
    private float pitch;

    private Boolean firstMouse; // Detects if it's the first mouse movement
    private double lastX;
    private double lastY;
    Camera(Vector3f position)
    {
        this.perspective = new Matrix4f();
        this.ortho = new Matrix4f();
        this.view = new Matrix4f();

        this.position = position;
        this.front = new Vector3f(0.0f, 0.0f, -1.0f);
        this.worldUp = new Vector3f(0.0f, 1.0f, 0.0f);

        this.yaw = -90.0f;
        this.pitch = 0.0f;

        this.firstMouse = false;
        this.lastX = 960;
        this.lastY = 540;

        updateCameraVectors();
    }
    public void updateProjectionMatrix(float fov, float width, float height)
    {
        this.perspective.identity().perspective(
                (float) Math.toRadians(fov),
                width / height,
                0.1f,
                100.0f
        );
        this.ortho.identity().ortho(0, width, height, 0, -1, 1);
    }
    public Matrix4f getViewMatrix()
    {
        return view.identity().lookAt(
                position,
                tmp.set(position).add(front),
                up
        );
    }
    private final Vector3f tmp = new Vector3f(); // Just for vector operations
    public void processInput(Camera_Direction dir, float deltaTime)
    {
        float velocity = 10.0f * deltaTime;

        switch(dir)
        {
            case FORWARD:
                this.position.add(tmp.set(this.front).mul(velocity));
                break;
            case BACKWARD:
                this.position.sub(tmp.set(this.front).mul(velocity));
                break;
            case LEFT:
                tmp.set(this.front).cross(this.up).normalize().mul(velocity);
                this.position.sub(tmp);
                break;
            case RIGHT:
                tmp.set(this.front).cross(this.up).normalize().mul(velocity);
                this.position.add(tmp);
                break;

        }
    }
    public void mouseControl(Vector2d mousePos)
    {
        if (this.firstMouse)
        {
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

        yaw   += (float)xoffset;
        pitch += (float)yoffset;

        if(pitch > 89.0f)
            pitch = 89.0f;
        if(pitch < -89.0f)
            pitch = -89.0f;


        Vector3f direction = new Vector3f();
        direction.x = (float)(cos(Math.toRadians(yaw)) * cos(Math.toRadians(pitch)));
        direction.y = (float)sin(Math.toRadians(pitch));
        direction.z = (float)(sin(Math.toRadians(yaw)) * cos(Math.toRadians(pitch)));
        this.front = direction.normalize();
    }
    private void updateCameraVectors()
    {
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

    public Matrix4f getProjectionMatrix() {return this.perspective; }
    public Matrix4f getOrtho() {return this.ortho; }
    public Vector3f getPosition() {
        return this.position;
    }
}
