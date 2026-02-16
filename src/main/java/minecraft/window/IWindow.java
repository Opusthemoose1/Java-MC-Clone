package minecraft.window;

import minecraft.chunk.IChunkLoader;
import minecraft.window.input.IInput;

public interface IWindow {

    public long getWindowHandle();

    public void loop();

    public int getWidth();

    public int getHeight();

    public void setInput(IInput input);

    public IChunkLoader getChunkLoader();

    public void free();

    public boolean canContinueLoop();

}
