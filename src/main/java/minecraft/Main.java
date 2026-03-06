package minecraft;

import minecraft.chunk.ChunkLoader;
import minecraft.chunk.ChunkRenderer;
import minecraft.window.Camera;
import minecraft.window.text.TextRenderer;
import minecraft.window.Window;
import minecraft.window.input.Input;
import minecraft.window.texture.Shader;
import minecraft.window.texture.TextureMap;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL;

import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.glEnable;

public class Main {

    private static final int DEFAULT_WIDTH = 1920, DEFAULT_HEIGHT = 1080;
    private static final String DEFAULT_RESOURCE_PATH = "src/resources";

    public static void main(String[] args) {



        TextureMap textureMap = new TextureMap(DEFAULT_RESOURCE_PATH + "/textures/");

        Camera camera = new Camera(new Vector3f(0.0f, 0.0f, 0.0f));
        camera.updateProjectionMatrix(90.0f, 1920.0f, 1080.0f);

        //TODO: Initialize GL in a way so that the context can be preserved for making new TextRenderer, Shader, etc., so that these dependencies can be added to Window constructor
        Window window = new Window(DEFAULT_WIDTH, DEFAULT_HEIGHT, textureMap, camera);

        // This line is critical for LWJGL's interoperation with GLFW's
        // OpenGL context, or any context that is managed externally.
        // LWJGL detects the context that is current in the current thread,
        // creates the GLCapabilities instance and makes the OpenGL
        // bindings available for use.
        GL.createCapabilities();

        Shader textShader = new Shader("src/resources/shaders/text.vert", "src/resources/shaders/text.frag");
        TextRenderer text = new TextRenderer("src/resources/textures/ascii.png", textShader );
        window.setTextRenderer(text);


        window.setChunkRenderer(new ChunkRenderer(textureMap,
                new Shader("src/resources/shaders/basic.vert",
                        "src/resources/shaders/basic.frag")));

        // Turn on depth buffer
        glEnable(GL_DEPTH_TEST);


        ChunkLoader chunkLoader = new ChunkLoader(8);

        window.setChunkLoader(chunkLoader);

        Input input = new Input(window.getWindowHandle());
        window.setInput(input);

        Minecraft minecraft = new Minecraft(window);
        minecraft.run();
    }
}
