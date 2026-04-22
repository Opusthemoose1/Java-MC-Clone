package minecraft;

import minecraft.chunk.FlatWorldChunkLoader;
import minecraft.command.*;
import minecraft.entity.AttackingEntity;
import minecraft.entity.Ogre;
import minecraft.timer.Timer;
import minecraft.window.input.IInputManager;
import minecraft.window.input.InputManager;
import minecraft.window.rendering.ChunkRenderer;
import minecraft.chunk.location.Location;
import minecraft.entity.EntityManager;
import minecraft.entity.Player;
import minecraft.window.Camera;
import minecraft.window.Window;
import minecraft.window.input.InputSource;
import minecraft.window.rendering.EntityRenderer;
import minecraft.window.rendering.IMesh;
import minecraft.window.rendering.LoadOBJNoNormals;
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

        // This line is critical for LWJGL's interoperation with GLFW's
        // OpenGL context, or any context that is managed externally.
        // LWJGL detects the context that is current in the current thread,
        // creates the GLCapabilities instance and makes the OpenGL
        // bindings available for use.
        GL.createCapabilities();

        WorldContext context = new WorldContext(new FlatWorldChunkLoader(), new EntityManager());
        Player player = new Player(Location.createLocation(0f, 40f, 0f), context);
        Ogre ogre = new Ogre(Location.createLocation(0f, 30f, 0f), context, new Timer());
        context.getEntityManager().addEntity(player);
        context.getEntityManager().addEntity(ogre);



        Camera camera = new Camera(INITIAL_CAMERA_POSITION, window.getWidth(), window.getHeight());
        window.setCamera(camera);
        camera.attachYawPitchObserver(player);

        Shader textShader = new Shader("src/resources/shaders/text.vert", "src/resources/shaders/text.frag");
        TextRenderer text = new TextRenderer("src/resources/textures/ascii.png", textShader );

        window.setTextRenderer(text);

        TextureMap blockTextureMap = new TextureMap(DEFAULT_RESOURCE_PATH + "/textures/blocks/");
        TextureAtlas textureAtlas = new TextureAtlas(blockTextureMap);

        Shader shader = new Shader("src/resources/shaders/basic.vert", "src/resources/shaders/basic.frag");
        window.attach(new ChunkRenderer(textureAtlas, shader));

        LoadOBJNoNormals objLoader = new LoadOBJNoNormals();
        IMesh mesh = objLoader.loadFile("src/resources/models/sphere.obj");
        Shader entityShader = new Shader("src/resources/shaders/entity.vert", "src/resources/shaders/entity.frag");
        mesh.setShader(entityShader);


        EntityRenderer entityRenderer = new EntityRenderer();
        entityRenderer.addEntityMesh(ogre, mesh);
        window.attach(entityRenderer);


        // Turn on depth buffer
        glEnable(GL_DEPTH_TEST);

        InputSource input = new InputSource(window.getWindowHandle());
        input.attach(camera);
        window.setInput(input);
        InputManager inputManager = new InputManager(input);
        bindKeys(inputManager);

        Minecraft minecraft = new Minecraft(window, inputManager, player, new Timer());

        glfwInitHint(GLFW_PLATFORM, GLFW_PLATFORM_X11); //might need for XWayland to solve an exception on init
        minecraft.run();
    }

    private void bindKeys(IInputManager inputManager) {
        CommandFactory factory = new CommandFactory();
        inputManager.bindDownKey(GLFW_KEY_W, factory.newMoveForwardsCommand());
        inputManager.bindDownKey(GLFW_KEY_S, factory.newMoveBackwardsCommand());
        inputManager.bindDownKey(GLFW_KEY_A, factory.newMoveLeftCommand());
        inputManager.bindDownKey(GLFW_KEY_D, factory.newMoveRightCommand());
        inputManager.bindDownKey(GLFW_KEY_SPACE, factory.newJumpCommand());
        inputManager.bindDownKey(GLFW_KEY_LEFT_CONTROL, factory.newSprintStartCommand());
        inputManager.bindUpKey(GLFW_KEY_LEFT_CONTROL, factory.newSprintStopCommand());
        inputManager.bindDownKey(GLFW_KEY_R, factory.newBreakBlockCommand());
    }
}
