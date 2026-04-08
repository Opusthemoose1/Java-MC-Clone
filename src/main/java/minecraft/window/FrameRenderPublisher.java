package minecraft.window;

public interface FrameRenderPublisher {

    void attach(FrameRenderObserver observer);

    void detach(FrameRenderObserver observer);

}
