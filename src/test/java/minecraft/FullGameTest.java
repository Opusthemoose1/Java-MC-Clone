package minecraft;

import minecraft.Minecraft;
import minecraft.WorldContext;
import minecraft.chunk.ChunkLoader;
import minecraft.chunk.ChunkRenderer;
import minecraft.entity.EntityManager;
import minecraft.math.IVector;
import minecraft.math.Vector;
import minecraft.window.Camera;
import minecraft.window.Window;
import minecraft.window.input.Input;
import minecraft.window.text.TextRenderer;
import minecraft.window.texture.Shader;
import minecraft.window.texture.TextureMap;
import org.junit.jupiter.api.Test;
import org.lwjgl.opengl.GL;

import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.glEnable;

public class FullGameTest {

    private static final IVector INITIAL_CAMERA_POSITION = new Vector(0f, 18f, 0f);
    private static final int DEFAULT_WIDTH = 1080, DEFAULT_HEIGHT = 720;
    private static final String DEFAULT_RESOURCE_PATH = "src/resources";

    @Test
    public void testFullGame() {
        //TODO: Initialize GL in a way so that the context can be preserved for making new TextRenderer, Shader, etc., so that these dependencies can be added to Window constructor
        //TODO: Turn window into a builder pattern
        Window window = new Window(DEFAULT_WIDTH, DEFAULT_HEIGHT);

        Camera camera = new Camera.Builder()
                .position(INITIAL_CAMERA_POSITION)
                .yaw(0.0f)
                .pitch(0.0f)
                .fov(90.0f)
                .screenWidth(window.getWidth())
                .screenHeight(window.getHeight())
                .build();

        window.setCamera(camera);
        // This line is critical for LWJGL's interoperation with GLFW's
        // OpenGL context, or any context that is managed externally.
        // LWJGL detects the context that is current in the current thread,
        // creates the GLCapabilities instance and makes the OpenGL
        // bindings available for use.
        GL.createCapabilities();
        TextureMap blockTextureMap = new TextureMap(DEFAULT_RESOURCE_PATH + "/textures/blocks/");


        Shader textShader = new Shader("src/resources/shaders/text.vert", "src/resources/shaders/text.frag");
        TextRenderer text = new TextRenderer("src/resources/textures/ascii.png", textShader );

        window.setTextRenderer(text);

        window.setChunkRenderer(new ChunkRenderer(blockTextureMap,
                new Shader("src/resources/shaders/basic.vert",
                        "src/resources/shaders/basic.frag")));


        // Turn on depth buffer
        glEnable(GL_DEPTH_TEST);

        Input input = new Input(window.getWindowHandle());
        input.attach(camera);
        window.setInput(input);

        WorldContext context = new WorldContext(new ChunkLoader(), new EntityManager());
        Minecraft minecraft = new Minecraft(window, input, context);
        minecraft.run();
    }
}
