package minecraft;

import minecraft.window.rendering.ChunkRenderer;
import minecraft.chunk.location.Location;
import minecraft.entity.EntityManager;
import minecraft.entity.Player;
import minecraft.window.Camera;
import minecraft.window.Window;
import minecraft.window.input.Input;
import minecraft.window.text.TextRenderer;
import minecraft.window.texture.Shader;
import minecraft.window.texture.TextureAtlas;
import minecraft.window.texture.TextureMap;
import org.junit.jupiter.api.Test;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.glEnable;

public class FullGameTest {

    private static final Location INITIAL_CAMERA_POSITION = Location.createLocation(0f, 18f, 0f);
    private static final int DEFAULT_WIDTH = 1080, DEFAULT_HEIGHT = 720;
    private static final String DEFAULT_RESOURCE_PATH = "src/resources";

    @Test
    public void testFullGame() {
        //TODO: Initialize GL in a way so that the context can be preserved for making new TextRenderer, Shader, etc., so that these dependencies can be added to Window constructor
        //TODO: Turn window into a builder pattern
        Window window = new Window(DEFAULT_WIDTH, DEFAULT_HEIGHT);

//        Camera camera = new Camera.Builder()
//                .position(INITIAL_CAMERA_POSITION)
//                .yaw(0.0f)
//                .pitch(0.0f)
//                .fov(90.0f)
//                .screenWidth(window.getWidth())
//                .screenHeight(window.getHeight())
//                .build();

        // This line is critical for LWJGL's interoperation with GLFW's
        // OpenGL context, or any context that is managed externally.
        // LWJGL detects the context that is current in the current thread,
        // creates the GLCapabilities instance and makes the OpenGL
        // bindings available for use.
        GL.createCapabilities();

        WorldContext context = new WorldContext(new FlatWorldChunkLoader(), new EntityManager());
        Player player = new Player(Location.createLocation(0f, 20f, 0f), context);
        context.getEntityManager().addEntity(player);

        Camera camera = new Camera(INITIAL_CAMERA_POSITION, window.getWidth(), window.getHeight());
        window.setCamera(camera);
        camera.attach(player);

        Shader textShader = new Shader("src/resources/shaders/text.vert", "src/resources/shaders/text.frag");
        TextRenderer text = new TextRenderer("src/resources/textures/ascii.png", textShader );

        window.setTextRenderer(text);

        TextureMap blockTextureMap = new TextureMap(DEFAULT_RESOURCE_PATH + "/textures/blocks/");
        TextureAtlas textureAtlas = new TextureAtlas(blockTextureMap);

        Shader shader = new Shader("src/resources/shaders/basic.vert", "src/resources/shaders/basic.frag");
        window.attach(new ChunkRenderer(textureAtlas, camera, shader));

        // Turn on depth buffer
        glEnable(GL_DEPTH_TEST);

        Input input = new Input(window.getWindowHandle());
        input.attach(camera);
        window.setInput(input);

        Minecraft minecraft = new Minecraft(window, input, player);

        glfwInitHint(GLFW_PLATFORM, GLFW_PLATFORM_X11); //might need for XWayland to solve an exception on init
        minecraft.run();
    }
}
