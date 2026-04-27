package minecraft;

import minecraft.chunk.FlatWorldChunk;
import minecraft.chunk.FlatWorldChunkLoader;
import minecraft.chunk.HillWorldChunkLoader;
import minecraft.chunk.IChunkLoader;
import minecraft.command.*;
import minecraft.entity.*;
import minecraft.math.Vector;
import minecraft.timer.Timer;
import minecraft.window.IWindow;
import minecraft.window.input.IInputManager;
import minecraft.window.input.InputManager;
import minecraft.window.rendering.ChunkRenderer;
import minecraft.chunk.location.Location;
import minecraft.window.Camera;
import minecraft.window.Window;
import minecraft.window.input.InputSource;
import minecraft.window.rendering.EntityRenderer;
import minecraft.window.rendering.IMesh;
import minecraft.window.rendering.LoadOBJNoNormals;
import minecraft.window.text.ITextRenderer;
import minecraft.window.text.TextRenderer;
import minecraft.window.texture.IShader;
import minecraft.window.texture.Shader;
import minecraft.window.texture.TextureAtlas;
import minecraft.window.texture.TextureMap;
import org.junit.jupiter.api.Test;
import org.lwjgl.opengl.GL;

import java.util.Random;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.glEnable;

public class FullGameTest {

    private static final Location INITIAL_CAMERA_POSITION = Location.createLocation(0f, 18f, 0f);
    private static final int DEFAULT_WIDTH = 1080, DEFAULT_HEIGHT = 720;
    private static final String DEFAULT_RESOURCE_PATH = "src/resources";

    private static final Random random = new Random();

    @Test
    public void testFullGame() {
        //TODO: Initialize GL in a way so that the context can be preserved for making new TextRenderer, Shader, etc., so that these dependencies can be added to Window constructor
        //TODO: Turn window into a builder pattern
        IWindow window = new Window(DEFAULT_WIDTH, DEFAULT_HEIGHT);

        // This line is critical for LWJGL's interoperation with GLFW's
        // OpenGL context, or any context that is managed externally.
        // LWJGL detects the context that is current in the current thread,
        // creates the GLCapabilities instance and makes the OpenGL
        // bindings available for use.
        GL.createCapabilities();

        WorldContext context = new WorldContext(new HillWorldChunkLoader(), new EntityManager());
        Player player = new Player(Location.createLocation(0f, 40f, 0f), context);
        context.getEntityManager().addEntity(player);
        spawnEntities(new EntityFactory(context, new Timer()));

        Camera camera = new Camera(INITIAL_CAMERA_POSITION, window.getWidth(), window.getHeight());
        window.setCamera(camera);
        camera.attachYawPitchObserver(player);

        IShader textShader = new Shader("src/resources/shaders/text.vert", "src/resources/shaders/text.frag");
        ITextRenderer text = new TextRenderer("src/resources/textures/ascii.png", textShader );

        window.setTextRenderer(text);

        window.attach(loadChunkRenderer());
        window.attach(loadEntityRenderer());

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

    private void spawnEntities(EntityFactory factory) {
        int maxX = 100, maxZ = 100;
        int y = FlatWorldChunk.HEIGHT;
        double densityPerBlock = 0.001;
        int count = (int) (4 * maxX * maxZ * densityPerBlock);

        for (int i = 0; i < count; i++) {
            int x = random.nextInt(-maxX, maxX), z = random.nextInt(-maxZ, maxZ);
            factory.createEntity(selectRandomEntity(), Location.createLocation(x, y, z));
        }
    }

    private EntityType selectRandomEntity() {
        float demonProbability = 0.1f;
        float ogrePorbability = 0.35f;
        float spinner = random.nextFloat();
        if (spinner <= demonProbability) return EntityType.DEMON;
        if (spinner <= demonProbability + ogrePorbability) return EntityType.OGRE;
        return EntityType.CHICKEN;
    }

    private ChunkRenderer loadChunkRenderer() {
        TextureMap blockTextureMap = new TextureMap(DEFAULT_RESOURCE_PATH + "/textures/blocks/");
        TextureAtlas textureAtlas = new TextureAtlas(blockTextureMap);
        Shader shader = new Shader("src/resources/shaders/basic.vert", "src/resources/shaders/basic.frag");
        return new ChunkRenderer(textureAtlas, shader);
    }

    private EntityRenderer loadEntityRenderer() {
        String sphereMeshPath = "src/resources/models/sphere.obj";
        Shader entityShader = new Shader("src/resources/shaders/entity.vert", "src/resources/shaders/entity.frag");
        LoadOBJNoNormals objLoader = new LoadOBJNoNormals();

        IMesh ogreMesh = objLoader.loadFile(sphereMeshPath);
        ogreMesh.setShader(entityShader);
        ogreMesh.setColor(new Vector(0.2f, 0.8f, 0.1f));

        IMesh chickenMesh = objLoader.loadFile(sphereMeshPath);
        chickenMesh.setShader(entityShader);
        chickenMesh.setColor(new Vector(0.8f, 0.8f, 0.8f));

        IMesh demonMesh = objLoader.loadFile(sphereMeshPath);
        demonMesh.setShader(entityShader);
        demonMesh.setColor(new Vector(0.94f, 0.33f, 0.26f));

        EntityRenderer entityRenderer = new EntityRenderer();
        entityRenderer.setEntityMesh(EntityType.OGRE, ogreMesh);
        entityRenderer.setEntityMesh(EntityType.CHICKEN, chickenMesh);
        entityRenderer.setEntityMesh(EntityType.DEMON, demonMesh);
        return entityRenderer;
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
