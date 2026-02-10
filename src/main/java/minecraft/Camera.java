package minecraft;

import org.joml.Matrix4f;
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
    private Matrix4f projection;
    private Matrix4f view;
    private Vector3f position;
    private Vector3f front;
    private Vector3f right;
    private Vector3f worldUp;
    private Vector3f up;

    private float yaw;
    private float pitch;
    Camera(Vector3f position)
    {
        this.projection = new Matrix4f();
        this.view = new Matrix4f();

        this.position = position;
        this.front = new Vector3f(0.0f, 0.0f, -1.0f);
        this.worldUp = new Vector3f(0.0f, 1.0f, 0.0f);

        this.yaw = -90.0f;
        this.pitch = 0.0f;

        updateCameraVectors();
    }
    public void updateProjectionMatrix(float fov, float width, float height)
    {
        this.projection.perspective((float)Math.toRadians(fov), width / height, 0.1f, 100.0f);
    }
    public Matrix4f getViewMatrix()
    {
        return view.lookAt(this.position, this.position.add(this.front), this.up);
    }
    private final Vector3f tmp = new Vector3f(); // Just for vector operations
    public void processInput(Camera_Direction dir, float deltaTime)
    {
        float velocity = 0.1f * deltaTime;
        switch(dir)
        {
            case FORWARD:
                this.position.add(tmp.set(this.front).mul(velocity));
                break;
            case BACKWARD:
                this.position.sub(tmp.set(this.front).mul(velocity));
                break;
            case LEFT:
                this.position.sub(tmp.set(this.right).mul(velocity));
                break;
            case RIGHT:
                this.position.add(tmp.set(this.right).mul(velocity));
                break;
        }
    }
    private void updateCameraVectors()
    {
        Vector3f temp_front = new Vector3f();
        temp_front.x = (float) (cos(Math.toRadians(this.yaw)) * cos(Math.toRadians(this.pitch)));
        temp_front.y = (float)sin(Math.toRadians(this.pitch));
        temp_front.z = (float) (sin(Math.toRadians(this.yaw)) * cos(Math.toRadians(this.pitch)));
        this.front = temp_front.normalize();
        Vector3f temp_right = new Vector3f();
        this.right = temp_right.cross(this.front, this.worldUp).normalize();
        this.up = up.cross(this.right, this.front).normalize();

    }

}
