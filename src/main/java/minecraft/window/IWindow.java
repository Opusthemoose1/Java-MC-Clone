package minecraft.window;

import minecraft.WorldContext;
import minecraft.window.input.IInputSource;
import minecraft.window.text.ITextRenderer;

public interface IWindow extends FrameRenderPublisher {

    long getWindowHandle();

    void loop(WorldContext context);

    int getWidth();

    int getHeight();

    void setInput(IInputSource input);

    void free();

    boolean canContinueLoop();

    Camera getCamera();

    void setCamera(Camera camera);

    void setTextRenderer(ITextRenderer textRenderer);

}
